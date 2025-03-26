package com.techy.microservices.order.service;

import com.techy.microservices.order.client.InventoryClient;
import com.techy.microservices.order.dto.OrderRequest;
import com.techy.microservices.order.dto.OrderResponse;
import com.techy.microservices.order.dto.UserDetails;
import com.techy.microservices.order.event.OrderPlacedEvent;
import com.techy.microservices.order.models.Order;
import com.techy.microservices.order.repos.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    // Constructor Injection
    public OrderService(OrderRepository orderRepository, InventoryClient inventoryClient, KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.inventoryClient = inventoryClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        log.info("Received order request: {}", orderRequest);

        if (orderRequest == null) {
            log.error("OrderRequest is null");
            throw new IllegalArgumentException("OrderRequest cannot be null");
        }

        boolean isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());
        log.info("Checking stock for SKU: {} - Available: {}", orderRequest.skuCode(), isProductInStock);

        if (isProductInStock) {
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setSkuCode(orderRequest.skuCode());
            order.setPrice(orderRequest.price());
            order.setQuantity(orderRequest.quantity());

            Order savedOrder = orderRepository.save(order);
            log.info("Order successfully created with Order ID: {}, Order Number: {}", savedOrder.getId(), savedOrder.getOrderNumber());

            // Create Kafka event with null-safe user details
            OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();
            orderPlacedEvent.setOrderNumber(order.getOrderNumber());

            UserDetails userDetails = orderRequest.userDetails();
            if (userDetails != null) {
                orderPlacedEvent.setEmail(userDetails.email() != null ? userDetails.email() : "");
                orderPlacedEvent.setFirstName(userDetails.firstName() != null ? userDetails.firstName() : "");
                orderPlacedEvent.setLastName(userDetails.lastName() != null ? userDetails.lastName() : "");
            } else {
                log.warn("UserDetails is null in the OrderRequest");
                orderPlacedEvent.setEmail("");
                orderPlacedEvent.setFirstName("");
                orderPlacedEvent.setLastName("");
            }

            log.info("Publishing OrderPlacedEvent to Kafka: {}", orderPlacedEvent);

            kafkaTemplate.send("order-placed", orderPlacedEvent);
            log.info("OrderPlacedEvent published successfully");

            return new OrderResponse(
                    savedOrder.getId(),
                    savedOrder.getOrderNumber(),
                    savedOrder.getSkuCode(),
                    savedOrder.getPrice(),
                    savedOrder.getQuantity()
            );
        } else {
            log.warn("Product out of stock for SKU: {}", orderRequest.skuCode());
            throw new RuntimeException("Product is not in stock for SKU: " + orderRequest.skuCode());
        }
    }
}
