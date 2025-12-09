package com.evolutionnext;

import com.evolutionnext.port.in.customer.ForClientCustomerCommandPort;
import com.evolutionnext.port.in.order.ForClientOrderCommandPort;
import com.evolutionnext.port.out.customer.CustomerRepository;
import com.evolutionnext.port.out.order.OrderRepository;
import com.evolutionnext.port.out.product.ProductRepository;
import com.evolutionnext.application.service.customer.CustomerCommandApplicationService;
import com.evolutionnext.application.service.customer.CustomerQueryApplicationService;
import com.evolutionnext.application.service.order.OrderCommandApplicationService;
import com.evolutionnext.infrastructure.adapter.in.HealthHandler;
import com.evolutionnext.infrastructure.adapter.in.HomeHandler;
import com.evolutionnext.infrastructure.adapter.in.MyWebServer;
import com.evolutionnext.infrastructure.adapter.in.OrderHandler;
import com.evolutionnext.infrastructure.adapter.out.CustomerJDBCRepository;
import com.evolutionnext.infrastructure.adapter.out.JDBCTransactional;
import com.evolutionnext.infrastructure.adapter.out.OrderJDBCRepository;
import com.evolutionnext.infrastructure.adapter.out.ProductJDBCRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Runner {
    private static final Logger logger = LoggerFactory.getLogger(Runner.class);


    public static void main(String[] args) throws IOException {
        String databaseHostEnv = System.getenv("DATABASE_HOST");
        String databaseUsernameEnv = System.getenv("DATABASE_USERNAME");
        String databasePasswordEnv = System.getenv("DATABASE_PASSWORD");
        String databasePortEnv = System.getenv("DATABASE_PORT");
        String databaseNameEnv = System.getenv("DATABASE_NAME");

        String databaseHost = databaseHostEnv == null ? "localhost" : databaseHostEnv;
        String databaseUsername = databaseUsernameEnv == null ? "postgres" : databaseUsernameEnv;
        String databasePassword = databasePasswordEnv == null ? "postgres" : databasePasswordEnv;
        String databaseName = databaseNameEnv == null ? "orders" : databaseNameEnv;
        int databasePort = databasePortEnv == null ? 5432 : Integer.parseInt(databasePortEnv);

        String jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s", databaseHost, databasePort, databaseName);
        logger.info("Connecting to database at {} with username {} and port {}", jdbcUrl, databaseUsername, databasePort);
        MyWebServer myWebServer = createWebServer(jdbcUrl, databaseUsername, databasePassword, databasePort);
        logger.info("Starting Server");
        myWebServer.start();
    }

    private static MyWebServer createWebServer(String databaseUrl, String databaseUsername, String databasePassword, int portNumber) throws IOException {
        PGSimpleDataSource dataSource = createDataSource(databaseUrl, databaseUsername, databasePassword, portNumber);
        JDBCTransactional jdbcTransactional = new JDBCTransactional(dataSource);

        CustomerRepository customerRepository = new CustomerJDBCRepository();
        OrderRepository orderRepository = new OrderJDBCRepository();
        ProductRepository productRepository = new ProductJDBCRepository();

        ForClientOrderCommandPort forClientOrderCommandPort = new OrderCommandApplicationService(orderRepository,
            customerRepository, jdbcTransactional);
        CustomerQueryApplicationService customerQueryApplicationService =
            new CustomerQueryApplicationService(customerRepository, orderRepository);
        ForClientCustomerCommandPort forClientCustomerCommandPort =
            new CustomerCommandApplicationService(customerRepository, jdbcTransactional);

        ObjectMapper objectMapper = new ObjectMapper();
        OrderHandler orderHandler = new OrderHandler(forClientOrderCommandPort, objectMapper);
        HealthHandler healthHandler = new HealthHandler();
        HomeHandler homeHandler = new HomeHandler();
        return new MyWebServer(orderHandler, healthHandler, homeHandler);
    }

    private static PGSimpleDataSource createDataSource(String databaseUrl, String databaseUsername, String databasePassword, int portNumber) {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL(databaseUrl);
        dataSource.setUser(databaseUsername);
        dataSource.setPassword(databasePassword);
        return dataSource;
    }
}
