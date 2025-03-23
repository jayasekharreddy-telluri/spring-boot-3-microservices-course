package com.techy.microservices.inventory_service.service;

import com.techy.microservices.inventory_service.model.Inventory;
import com.techy.microservices.inventory_service.repo.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    // ✅ Check if stock is available
    public boolean isInstock(String skuCode, int quantity) {
        return inventoryRepository.findBySkuCode(skuCode)
                .map(inventory -> inventory.getQuantity() >= quantity)
                .orElse(false);
    }

    // ✅ Update stock when a product is added or modified
    @Transactional
    public boolean updateStock(String skuCode, int quantity) {
        // If quantity is 0, set it to default (10)
        int finalQuantity = (quantity == 0) ? 10 : quantity;

        Optional<Inventory> inventoryOpt = inventoryRepository.findBySkuCode(skuCode);

        if (inventoryOpt.isPresent()) {
            Inventory inventory = inventoryOpt.get();
            inventory.setQuantity(inventory.getQuantity() + finalQuantity);
            inventoryRepository.save(inventory);
        } else {
            // Create new inventory entry if SKU does not exist
            Inventory newInventory = new Inventory(skuCode, finalQuantity);
            inventoryRepository.save(newInventory);
        }
        return true;
    }
}
