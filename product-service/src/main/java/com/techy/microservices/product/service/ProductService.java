package com.techy.microservices.product.service;

import com.techy.microservices.product.dto.InventoryRequest;
import com.techy.microservices.product.dto.ProductRequest;
import com.techy.microservices.product.dto.ProductResponse;
import com.techy.microservices.product.model.Product;
import com.techy.microservices.product.repos.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final RestTemplate restTemplate;

    // Injecting Inventory Service URL from properties file
    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;

    public ProductService(ProductRepository productRepository, RestTemplate restTemplate) {
        this.productRepository = productRepository;
        this.restTemplate = restTemplate;
    }

    public ProductResponse createProduct(ProductRequest productRequest) {
        log.info("Creating product: {}", productRequest);

        Product product = new Product();
        product.setSkuCode(productRequest.skuCode());
        product.setName(productRequest.name());
        product.setDescription(productRequest.description());
        product.setPrice(productRequest.price());

        Product savedProduct = productRepository.save(product);
        log.info("Product saved successfully with ID: {}", savedProduct.getId());

        // ✅ Call Inventory Service after saving product
        saveToInventory(savedProduct.getSkuCode());

        return new ProductResponse(
                savedProduct.getId(),
                savedProduct.getSkuCode(),
                savedProduct.getName(),
                savedProduct.getDescription(),
                savedProduct.getPrice()
        );
    }

    public List<ProductResponse> getAllProducts() {
        log.info("Fetching all products");

        return productRepository.findAll().stream()
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getSkuCode(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice()
                ))
                .collect(Collectors.toList());
    }

    // ✅ Helper method to save product to Inventory Service
    private void saveToInventory(String skuCode) {
        var inventoryRequest = new InventoryRequest(skuCode, 10); // Default quantity = 10

        log.info("Sending request to Inventory Service [{}] to add SKU: {}", inventoryServiceUrl, skuCode);
        restTemplate.postForObject(inventoryServiceUrl, inventoryRequest, Void.class);
        log.info("Inventory updated successfully for SKU: {}", skuCode);
    }
}
