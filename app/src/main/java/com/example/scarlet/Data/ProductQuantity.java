package com.example.scarlet.Data;

public class ProductQuantity {
    private String  productId;
    private int quantity;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ProductQuantity() {
    }

    public ProductQuantity(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
