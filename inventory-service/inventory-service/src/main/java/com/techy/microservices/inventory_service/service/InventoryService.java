package com.techy.microservices.inventory_service.service;

import com.techy.microservices.inventory_service.repo.InventoryRepository;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public boolean isInstock(String skuCode,Integer quantity){

        return inventoryRepository.existsBySkuCodeAndQuantityGreaterThanEqual(skuCode,quantity);
    }
}
