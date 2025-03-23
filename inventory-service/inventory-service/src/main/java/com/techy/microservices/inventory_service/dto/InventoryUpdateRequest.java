package com.techy.microservices.inventory_service.dto;


public class InventoryUpdateRequest {
    private String skuCode;
    private int quantity;

    public InventoryUpdateRequest() {}

    public InventoryUpdateRequest(String skuCode, int quantity) {
        this.skuCode = skuCode;
        this.quantity = quantity;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
