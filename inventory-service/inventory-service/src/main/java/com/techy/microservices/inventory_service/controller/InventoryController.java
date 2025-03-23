package com.techy.microservices.inventory_service.controller;

import com.techy.microservices.inventory_service.dto.InventoryUpdateRequest;
import com.techy.microservices.inventory_service.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // ✅ Check if a product is in stock
    @GetMapping
    public ResponseEntity<Boolean> isInStock(@RequestParam String skuCode, @RequestParam int quantity) {
        boolean inStock = inventoryService.isInstock(skuCode, quantity);
        return ResponseEntity.ok(inStock);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> updateInventory(@RequestBody InventoryUpdateRequest request) {
        boolean updated = inventoryService.updateStock(request.getSkuCode(), request.getQuantity());

        Map<String, String> response = new HashMap<>();
        if (updated) {
            response.put("message", "Inventory updated successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Failed to update inventory for SKU: " + request.getSkuCode());
            return ResponseEntity.badRequest().body(response);
        }
    }


}
