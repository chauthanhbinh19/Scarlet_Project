package com.example.scarlet.Data;

import java.util.List;

public class Cart {
    private String customerId;
    private List<String> productId;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<String> getProductId() {
        return productId;
    }

    public void setProductId(List<String> productId) {
        this.productId = productId;
    }

    public Cart() {
    }

    public Cart(String customerId, List<String> productId) {
        this.customerId = customerId;
        this.productId = productId;
    }
}
