package com.techy.microservices.inventory_service.repo;

import com.techy.microservices.inventory_service.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory,Long> {
   // boolean existsBySkucodeAndQuantityGreaterThanEqual(String skuCode, Integer quantity);
    boolean existsBySkuCodeAndQuantityGreaterThanEqual(String skuCode, Integer quantity);

}
