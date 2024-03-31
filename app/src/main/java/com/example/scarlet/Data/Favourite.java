package com.example.scarlet.Data;

public class Favourite {
    private String key;
    private String customerId;
    private String productId;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Favourite() {
    }

    public Favourite(String customerId, String productId) {
        this.customerId = customerId;
        this.productId = productId;
    }
}
