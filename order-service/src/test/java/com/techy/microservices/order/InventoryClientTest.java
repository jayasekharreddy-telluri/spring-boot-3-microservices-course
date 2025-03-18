package com.techy.microservices.order;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.techy.microservices.order.client.InventoryClient;
import com.techy.microservices.order.dto.OrderRequest;
import com.techy.microservices.order.repos.OrderRepository;
import com.techy.microservices.order.service.OrderService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class InventoryClientTest {

    private static WireMockServer wireMockServer;

    @Mock
    private InventoryClient inventoryClient;  // Use @Mock instead of @MockBean

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;  // Inject Mocks manually

    @BeforeAll
    static void startWireMock() {
        wireMockServer = new WireMockServer(8082); // Start WireMock on port 8082
        wireMockServer.start();
        configureFor("localhost", 8082);
    }

    @AfterAll
    static void stopWireMock() {
        wireMockServer.stop();
    }

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);  // Initialize mocks manually
    }

    @Test
    void testCreateOrder_WhenProductIsInStock() {
        // Mock API response with WireMock
        wireMockServer.stubFor(get(urlPathEqualTo("/api/inventory/check"))
                .withQueryParam("skuCode", equalTo("iphone_15"))
                .withQueryParam("quantity", equalTo("1001"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("true")));


    }
}
