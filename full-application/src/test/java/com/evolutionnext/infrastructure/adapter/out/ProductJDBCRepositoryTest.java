package com.evolutionnext.infrastructure.adapter.out;

import com.evolutionnext.arbitraries.ProductArbitrarySupplier;
import com.evolutionnext.domain.aggregates.product.Product;
import net.jqwik.api.*;
import net.jqwik.api.Tuple.Tuple2;
import net.jqwik.api.arbitraries.ListArbitrary;
import net.jqwik.api.lifecycle.BeforeTry;
import net.jqwik.testcontainers.Container;
import net.jqwik.testcontainers.Testcontainers;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class ProductJDBCRepositoryTest {

    private static final Logger logger = LoggerFactory.getLogger(ProductJDBCRepositoryTest.class);
    private DataSource dataSource;
    private final ProductJDBCRepository productJDBCRepository = new ProductJDBCRepository();

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.2")
        .withDatabaseName("orders")
        .withUsername("postgres")
        .withPassword("postgres")
        .withInitScript("init.sql");


    @BeforeTry
    void setupDataSource() {
        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setURL(postgres.getJdbcUrl());
        pgSimpleDataSource.setUser(postgres.getUsername());
        pgSimpleDataSource.setPassword(postgres.getPassword());
        dataSource = pgSimpleDataSource;
    }


    @Property
    void saveAndLoadCustomer(@ForAll(supplier = ProductArbitrarySupplier.class) Product product) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            logger.debug("Testing for product {}", product);
            ScopedValue.where(ConnectionScoped.CONNECTION, connection)
                .run(() -> {
                    productJDBCRepository.save(product);
                    Optional<Product> loadedCustomer = productJDBCRepository.load(product.id());
                    assertThat(loadedCustomer).isNotEmpty().contains(product);
                });
            logger.debug("Completed Testing for product {}", product);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Provide
    public Arbitrary<Tuple2<List<Product>, Product>> productListWithSelectionArbitrary() {
        ProductArbitrarySupplier productArbitrarySupplier = new ProductArbitrarySupplier();
        ListArbitrary<Product> productListArbitrary = productArbitrarySupplier.get().list().ofMinSize(1).ofMaxSize(10);
        Random random = new Random();
        return productListArbitrary.flatMap(productList -> {
            Product selectedProduct = productList.get(random.nextInt(productList.size()));
            return Arbitraries.of(Tuple.of(productList, selectedProduct));
        });
    }

    @Property
    void deleteProduct(@ForAll("productListWithSelectionArbitrary")
                Tuple2<List<Product>, Product> productEntry) {
        try (Connection connection = dataSource.getConnection()) {
            ScopedValue.where(ConnectionScoped.CONNECTION, connection)
                .run(() -> {
                    List<Product> productList = productEntry.get1();
                    productList.forEach(productJDBCRepository::save);
                    productJDBCRepository.delete(productEntry.get2().id());
                    Optional<Product> load = productJDBCRepository.load(productEntry.get2().id());
                    assertThat(load).isEmpty();
                });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Property
    void deleteAllProducts(@ForAll List<@From(supplier = ProductArbitrarySupplier.class) Product> productList) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            ScopedValue.where(ConnectionScoped.CONNECTION, connection)
                .run(() -> {
                    productList.forEach(productJDBCRepository::save);
                    productJDBCRepository.deleteAll();
                    List<Product> result = productJDBCRepository.findAll();
                    assertThat(result).hasSize(0);
                });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
