package com.techy.microservices.order.Controllers;

import com.techy.microservices.order.dto.OrderRequest;
import com.techy.microservices.order.dto.OrderResponse;
import com.techy.microservices.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 🔹 POST: Create an Order
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        LOGGER.info("Received request to create order: {}", orderRequest);

        OrderResponse orderResponse = orderService.createOrder(orderRequest);

        LOGGER.info("Order created successfully for orderNumber: {}", orderResponse.orderNumber());

        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }
}
