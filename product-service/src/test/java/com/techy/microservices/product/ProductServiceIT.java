package com.techy.microservices.product;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Import(TestcontainersConfig.class) // Use Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceIT {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @DynamicPropertySource
    static void setMongoDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri",
                () -> "mongodb://" + TestcontainersConfig.mongoDBContainer().getHost()
                        + ":" + TestcontainersConfig.mongoDBContainer().getFirstMappedPort()
                        + "/test-db");
    }

    @Test
    void shouldCreateAndRetrieveProduct() {
        String productJson = """
            {
                "name": "Test Product",
                "description": "This is a test product",
                "price": 99.99
            }
        """;

        // Create product
        given()
                .contentType(ContentType.JSON)
                .body(productJson)
                .when()
                .post("/api/product")
                .then()
                .statusCode(201)
                .body("name", equalTo("Test Product"));

        // Retrieve products
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/product")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].name", equalTo("Test Product"));
    }
}
