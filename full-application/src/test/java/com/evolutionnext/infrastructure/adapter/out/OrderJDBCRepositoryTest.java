package com.evolutionnext.infrastructure.adapter.out;


import com.evolutionnext.arbitraries.CustomerArbitrarySupplier;
import com.evolutionnext.domain.aggregates.customer.Customer;
import com.evolutionnext.domain.aggregates.order.Order;
import com.evolutionnext.domain.aggregates.order.OrderId;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.lifecycle.AfterTry;
import net.jqwik.api.lifecycle.BeforeTry;
import net.jqwik.testcontainers.Container;
import net.jqwik.testcontainers.Testcontainers;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class OrderJDBCRepositoryTest {

    private static final Logger logger = LoggerFactory.getLogger(OrderJDBCRepositoryTest.class);
    private DataSource dataSource;
    private final CustomerJDBCRepository customerJDBCRepository = new CustomerJDBCRepository();
    private final OrderJDBCRepository orderJDBCRepository = new OrderJDBCRepository();
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

    @AfterTry
    public void reset() {
        try (Connection connection = dataSource.getConnection()) {
            logger.debug("Resetting Data");
            ScopedValue.where(ConnectionScoped.CONNECTION, connection)
                .run(() -> {
                    orderJDBCRepository.deleteAll();
                    customerJDBCRepository.deleteAll();
                    productJDBCRepository.deleteAll();
                });
            logger.debug("Resetting Data Complete");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Property
    void testInitializeOrder(@ForAll(supplier = CustomerArbitrarySupplier.class) Customer customer) {
        try (Connection connection = dataSource.getConnection()) {
            logger.debug("Testing for customer {}", customer);
            ScopedValue.where(ConnectionScoped.CONNECTION, connection)
                .run(() -> {
                    customerJDBCRepository.save(customer);
                    OrderId orderId = new OrderId(UUID.randomUUID());
                    Order order = Order.of(orderId, customer.id());
                    orderJDBCRepository.save(order);
                    assertThat(orderJDBCRepository.load(orderId)).contains(order);
                });
            logger.debug("Completed Testing for customer {}", customer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
