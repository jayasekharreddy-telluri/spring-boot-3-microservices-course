package com.techy.microservices.inventory_service.controller;

import com.techy.microservices.inventory_service.model.Inventory;
import com.techy.microservices.inventory_service.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<Boolean> isInStock(@RequestParam String skuCode, @RequestParam int quantity) {
        boolean inStock = inventoryService.isInstock(skuCode, quantity);
        return ResponseEntity.ok(inStock);
    }

}
