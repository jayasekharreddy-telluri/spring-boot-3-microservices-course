package com.techy.microservices.order.service;

import com.techy.microservices.order.dto.OrderRequest;
import com.techy.microservices.order.dto.OrderResponse;
import com.techy.microservices.order.models.Order;
import com.techy.microservices.order.repos.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    // Constructor Injection
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(orderRequest.orderNumber());
        order.setSkuCode(orderRequest.skuCode());
        order.setPrice(orderRequest.price());
        order.setQuantity(orderRequest.quantity());

        Order savedOrder = orderRepository.save(order); // Save the order in the database
        // Return response DTO
        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getOrderNumber(),
                savedOrder.getSkuCode(),
                savedOrder.getPrice(),
                savedOrder.getQuantity()
        );
    }
}
