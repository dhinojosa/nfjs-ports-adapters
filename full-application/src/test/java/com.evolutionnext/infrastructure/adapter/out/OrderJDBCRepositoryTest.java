package com.evolutionnext.infrastructure.adapter.out;

import com.evolutionnext.domain.aggregates.order.Order;
import net.datafaker.Faker;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Testcontainers
public class OrderJDBCRepositoryTest {
    private static Faker faker;
    private static DataSource dataSource;
    private Connection connection;

    private OrderJDBCRepository orderJDBCRepository = new OrderJDBCRepository();
    private CustomerJDBCRepository customerJDBCRepository = new CustomerJDBCRepository();
    private ProductJDBCRepository productJDBCRepository = new ProductJDBCRepository();

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.2")
        .withDatabaseName("orders")
        .withUsername("test")
        .withPassword("test")
        .withInitScript("schema.sql");

    @BeforeAll
    static void beforeAll() throws SQLException {
        postgres.start();
        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setURL(postgres.getJdbcUrl());
        pgSimpleDataSource.setUser(postgres.getUsername());
        pgSimpleDataSource.setPassword(postgres.getPassword());
        dataSource = pgSimpleDataSource;
        faker = new Faker();
    }

    @BeforeEach
    public void beforeEach() throws SQLException {
        orderJDBCRepository = new OrderJDBCRepository();
    }

    @Property
    void saveAndLoadOrder(@ForAll Order order) {

    }
}
