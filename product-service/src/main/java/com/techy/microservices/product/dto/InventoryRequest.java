package com.techy.microservices.product.dto;

public class InventoryRequest {
    private String skuCode;
    private int quantity;

    public InventoryRequest() {
    }

    public InventoryRequest(String skuCode, int quantity) {
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
