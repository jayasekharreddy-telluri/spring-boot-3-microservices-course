package com.techy.microservices.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techy.microservices.order.dto.OrderRequest;
import com.techy.microservices.order.dto.OrderResponse;
import com.techy.microservices.order.stub.InventoryClientStud;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.containers.MySQLContainer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@AutoConfigureWireMock(port = 0)
public class OrderControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ MySQL TestContainer setup
    @Container
    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.3.0"))
            .withDatabaseName("test_db")
            .withUsername("test_user")
            .withPassword("test_pass");

    @Test
    void shouldCreateOrder() throws Exception {

        InventoryClientStud.stubInventoryCall("iphone_15",100);

        // ✅ Arrange: Create test order request
        OrderRequest orderRequest = new OrderRequest("ORD123", "SKU567", new BigDecimal("199.99"), 2);
/*
        // ✅ Act & Assert: Call API & verify response
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderNumber").value("ORD123"))
                .andExpect(jsonPath("$.skuCode").value("SKU567"))
                .andExpect(jsonPath("$.price").value(199.99))
                .andExpect(jsonPath("$.quantity").value(2));*/
    }
}
