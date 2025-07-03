package com.evolutionnext;


import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

@Testcontainers
public class HealthEndpointE2ETest {
    private static final Logger logger = LoggerFactory.getLogger(HealthEndpointE2ETest.class);

    private static final String COMPOSE_FILE = "docker-compose.yaml";
    private static final String ENV_FILE = ".env";

    private static final Map<String, String> env;

    static {
        InputStream inputStream = HealthEndpointE2ETest.class.getClassLoader().getResourceAsStream(ENV_FILE);
        if (inputStream == null) {
            throw new RuntimeException("Could not find " + ENV_FILE);
        }
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            env = properties
                .entrySet()
                .stream()
                .map(entry ->
                    Map.entry(entry.getKey().toString(), entry.getValue().toString()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            logger.info("Loaded env {}", env);
        } catch (Exception e) {
            throw new RuntimeException("Could not load " + ENV_FILE, e);
        }
    }

    @Container
    public static ComposeContainer composeContainer = new ComposeContainer(
        getComposeFile())
        .withEnv(env)
        .waitingFor("full-application", Wait.forHttp("/health").forStatusCode(200).forPort(8080))
        .withExposedService("full-application", 8080);

    private static File getComposeFile() {
        try {
            URL resource = HealthEndpointE2ETest.class.getClassLoader().getResource(HealthEndpointE2ETest.COMPOSE_FILE);
            if (resource == null) {
                throw new RuntimeException("Could not find " + HealthEndpointE2ETest.COMPOSE_FILE);
            }
            return Paths.get(resource.toURI()).toFile();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testHealthEndpoint() {
        int mappedPort = composeContainer.getServicePort("full-application", 8080);
        logger.debug("Mapped port is {}", mappedPort);

        given()
            .contentType(ContentType.JSON)
            .when()
            .get(String.format("http://localhost:%d/health", mappedPort))
            .then()
            .statusCode(200);
    }
}
