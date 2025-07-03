package com.evolutionnext.infrastructure.adapter.out;

import com.evolutionnext.arbitraries.CustomerArbitrarySupplier;
import com.evolutionnext.domain.aggregates.customer.Customer;
import net.jqwik.api.*;
import net.jqwik.api.Tuple.Tuple2;
import net.jqwik.api.arbitraries.ListArbitrary;
import net.jqwik.api.lifecycle.BeforeTry;
import net.jqwik.testcontainers.Container;
import net.jqwik.testcontainers.Testcontainers;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class CustomerJDBCRepositoryTest {

    private static final Logger logger = LoggerFactory.getLogger(CustomerJDBCRepositoryTest.class);
    private DataSource dataSource;
    private final CustomerJDBCRepository customerJDBCRepository = new CustomerJDBCRepository();


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
    void saveAndLoadCustomer(@ForAll(supplier = CustomerArbitrarySupplier.class) Customer customer) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            logger.debug("Testing for customer {}", customer);
            ScopedValue.where(ConnectionScoped.CONNECTION, connection)
                .run(() -> {
                    customerJDBCRepository.save(customer);
                    Integer result = customerJDBCRepository
                        .load(customer.id())
                        .map(c1 -> c1.creditLimit().compareTo(customer.creditLimit()))
                        .orElse(Integer.MAX_VALUE);
                    assertThat(result).isZero();
                });
            logger.debug("Completed Testing for customer {}", customer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Provide
    public Arbitrary<Tuple2<List<Customer>, Customer>> customerListWithSelectionArbitrary() {
        CustomerArbitrarySupplier customerArbitrarySupplier = new CustomerArbitrarySupplier();
        ListArbitrary<Customer> customerListArbitrary = customerArbitrarySupplier.get().list().ofMinSize(1).ofMaxSize(10);
        Random random = new Random();
        return customerListArbitrary.flatMap(customerList -> {
            Customer selectedCustomer = customerList.get(random.nextInt(customerList.size()));
            return Arbitraries.of(Tuple.of(customerList, selectedCustomer));
        });
    }

    @Property
    void deleteCustomer(@ForAll("customerListWithSelectionArbitrary")
                        Tuple2<List<Customer>, Customer> customerEntry) {
        try (Connection connection = dataSource.getConnection()) {
            ScopedValue.where(ConnectionScoped.CONNECTION, connection)
                .run(() -> {
                    List<Customer> customerList = customerEntry.get1();
                    customerList.forEach(customerJDBCRepository::save);
                    customerJDBCRepository.delete(customerEntry.get2().id());
                    Optional<Customer> load = customerJDBCRepository.load(customerEntry.get2().id());
                    assertThat(load).isEmpty();
                });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Property
    void deleteAllCustomers(@ForAll List<@From(supplier = CustomerArbitrarySupplier.class) Customer> customerList) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            ScopedValue.where(ConnectionScoped.CONNECTION, connection)
                .run(() -> {
                    customerList.forEach(customerJDBCRepository::save);
                    customerJDBCRepository.deleteAll();
                    List<Customer> result = customerJDBCRepository.findAll();
                    assertThat(result).hasSize(0);
                });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
