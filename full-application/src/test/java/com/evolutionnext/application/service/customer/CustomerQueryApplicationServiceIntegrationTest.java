package com.evolutionnext.application.service.customer;


import com.evolutionnext.application.port.out.CustomerRepository;
import com.evolutionnext.application.port.out.OrderRepository;
import com.evolutionnext.application.port.out.ProductRepository;
import com.evolutionnext.application.results.customer.query.CustomerFound;
import com.evolutionnext.application.results.customer.query.CustomerQueryResult;
import com.evolutionnext.domain.aggregates.customer.CustomerId;
import com.evolutionnext.domain.aggregates.order.OrderId;
import com.evolutionnext.domain.aggregates.product.Product;
import com.evolutionnext.domain.aggregates.product.ProductId;
import com.evolutionnext.infrastructure.adapter.out.*;
import com.evolutionnext.domain.aggregates.customer.Customer;
import com.evolutionnext.domain.aggregates.order.Order;
import net.datafaker.Faker;
import net.jqwik.api.*;
import net.jqwik.api.arbitraries.ListArbitrary;
import net.jqwik.api.constraints.Positive;
import net.jqwik.api.lifecycle.BeforeTry;
import net.jqwik.api.Tuple;
import net.jqwik.testcontainers.Container;
import net.jqwik.testcontainers.Testcontainers;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class CustomerQueryApplicationServiceIntegrationTest {
    private static final Faker faker = new Faker();

    private static final Logger logger = LoggerFactory.getLogger(CustomerQueryApplicationServiceIntegrationTest.class);
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

    @Provide
    public Arbitrary<Product> getProduct() {
        Arbitrary<String> productNameArbitrary = Arbitraries.ofSuppliers(() -> faker.commerce().productName());
        Arbitrary<BigDecimal> priceArbitrary = Arbitraries.bigDecimals().between(BigDecimal.valueOf(3), BigDecimal.valueOf(300));
        Arbitrary<UUID> uuid = Arbitraries.create(UUID::randomUUID);
        return Combinators.combine(productNameArbitrary, priceArbitrary, uuid).as((name, price, id) ->
            new Product(new ProductId(id), name, price));
    }

    @Property
    public void testProductProperty(@ForAll("getProduct") Product product) {
        System.out.printf("Product: %s ", product);
        assertThat(product.id()).isNotNull();
    }

    @Provide
    public Arbitrary<Tuple.Tuple3<List<Product>, Customer, List<Order>>> getData() {
        ListArbitrary<Product> productListArbitrary = getProduct().list().ofMaxSize(100);
        Arbitrary<Customer> customerArbitrary = Arbitraries.just(new Customer(new CustomerId(UUID.randomUUID()), faker.name().fullName(), BigDecimal.valueOf(10000)));
        Arbitrary<List<Order>> orderArbitraryList = customerArbitrary.flatMap(customer ->
            Arbitraries.create(UUID::randomUUID).map(uuid -> Order.of(new OrderId(uuid), customer.id()))).list().ofMinSize(0).ofMaxSize(20);
        return Combinators.combine(productListArbitrary, customerArbitrary, orderArbitraryList).as(Tuple::of);
    }

    @Property
    public void testEnsureAllOrdersAreFromCustomer(@ForAll("getData") Tuple.Tuple3<List<Product>, Customer, List<Order>> data) throws SQLException {
        System.out.println("Data: " + data);
        Customer customer = data.get2();
        List<Order> orderList = data.get3();
        orderList.forEach(order -> assertThat(order.getCustomerId()).isEqualTo(customer.id()));
    }



    @Property
    public void testFindByIdAndVerifyCount(@ForAll("getData") Tuple.Tuple3<List<Product>, Customer, List<Order>> data) throws SQLException {
        List<Order> orderList = data.get3();
        int expectedCount = orderList.size();

        CustomerRepository customerRepository = new CustomerJDBCRepository();
        OrderRepository orderRepository = new OrderJDBCRepository();
        ProductRepository productRepository = new ProductJDBCRepository();
        Customer customer = data.get2();

        try (Connection connection = dataSource.getConnection()) {
            ScopedValue.where(ConnectionScoped.CONNECTION, connection)
                .run(() -> {
                    data.get1().forEach(productRepository::save);
                    customerRepository.save(customer);
                    orderList.forEach(orderRepository::save);

                    CustomerQueryApplicationService customerQueryApplicationService =
                        new CustomerQueryApplicationService(customerRepository, orderRepository);
                    CustomerQueryResult customerQueryResult = customerQueryApplicationService.findById(customer.id());
                    assertThat(customerQueryResult).isInstanceOf(CustomerFound.class);
                    CustomerFound customerFound = (CustomerFound) customerQueryResult;
                    assertThat(customerFound.customerData().numberOfOrders()).isEqualTo(expectedCount);

                    orderRepository.deleteAll();
                    customerRepository.deleteAll();
                    productRepository.deleteAll();
                });
        }
    }
}
