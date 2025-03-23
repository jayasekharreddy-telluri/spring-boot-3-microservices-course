package com.techy.microservices.inventory_service.repo;

import com.techy.microservices.inventory_service.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory,Long> {
   // boolean existsBySkucodeAndQuantityGreaterThanEqual(String skuCode, Integer quantity);
    boolean existsBySkuCodeAndQuantityGreaterThanEqual(String skuCode, Integer quantity);
    Optional<Inventory> findBySkuCode(String skuCode);

}
