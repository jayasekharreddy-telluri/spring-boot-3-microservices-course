package com.techy.microservices.order.service;

import com.techy.microservices.order.client.InventoryClient;
import com.techy.microservices.order.dto.OrderRequest;
import com.techy.microservices.order.dto.OrderResponse;
import com.techy.microservices.order.models.Order;
import com.techy.microservices.order.repos.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final InventoryClient inventoryClient;

    // Constructor Injection
    public OrderService(OrderRepository orderRepository, InventoryClient inventoryClient) {
        this.orderRepository = orderRepository;
        this.inventoryClient = inventoryClient;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {

        var isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(),orderRequest.quantity());

        if (isProductInStock){

            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
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
        }else{

            throw new RuntimeException("product is not in stock with skucode +"+ orderRequest.skuCode());
        }


    }
}
