package com.evolutionnext.infrastructure.adapter.out;

import com.evolutionnext.arbitraries.CustomerArbitrarySupplier;
import com.evolutionnext.domain.aggregates.customer.Customer;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.lifecycle.BeforeContainer;
import net.jqwik.api.lifecycle.BeforeProperty;
import net.jqwik.api.lifecycle.BeforeTry;
import net.jqwik.testcontainers.Container;
import net.jqwik.testcontainers.Testcontainers;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class CustomerJDBCRepositoryTest {
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
            System.out.printf("Testing for customer %s%n", customer);
            ScopedValue.where(ConnectionScoped.CONNECTION, connection)
                .run(() -> {
                    customerJDBCRepository.save(customer);
                    Customer loadedCustomer = customerJDBCRepository.load(customer.id());
                    assertThat(customer).isEqualTo(loadedCustomer);
                });
            System.out.printf("Completed Testing for customer %s%n", customer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
