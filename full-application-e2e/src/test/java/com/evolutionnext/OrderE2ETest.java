package com.evolutionnext;


import io.restassured.http.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@Testcontainers
public class OrderE2ETest {
    private static final Logger logger = LoggerFactory.getLogger(OrderE2ETest.class);

    private static final String COMPOSE_FILE = "docker-compose.yaml";
    private static final String ENV_FILE = ".env";

    private static final Map<String, String> env;

    static {
        InputStream inputStream = OrderE2ETest.class.getClassLoader().getResourceAsStream(ENV_FILE);
        if (inputStream == null) {
            throw new RuntimeException("Could not find " + ENV_FILE);
        }
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            env = properties
                .entrySet()
                .stream()
                .map(entry -> Map.entry(entry.getKey().toString(), entry.getValue().toString()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } catch (Exception e) {
            throw new RuntimeException("Could not load " + ENV_FILE, e);
        }
    }

    @Container
    public static ComposeContainer composeContainer = new ComposeContainer(
        getComposeFile())
        .withEnv(env)
        .withExposedService("web", 8080);


    private static String getComposeFile() {
        URL resource = OrderE2ETest.class.getClassLoader().getResource(OrderE2ETest.COMPOSE_FILE);
        if (resource == null) {
            throw new RuntimeException("Could not find " + OrderE2ETest.COMPOSE_FILE);
        }
        return resource.getFile();
    }

    @Test
    public void testInitializeOrder() {
        String customerId = "d9e8f7c6-b5a4-4321-8765-fedcba987654";
        String requestBody = "{\"customerId\": \"" + customerId + "\"}";
        int mappedPort = composeContainer.getServicePort("full-application", 8080);
        logger.debug("Mapped port is {}", mappedPort);

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post(String.format("http://localhost:%d/order", mappedPort))
            .then()
            .statusCode(201)
            .body("orderId.id", notNullValue());
    }
}
