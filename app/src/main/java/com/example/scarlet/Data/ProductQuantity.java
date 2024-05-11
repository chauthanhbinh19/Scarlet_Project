package com.example.scarlet.Data;

public class ProductQuantity {
    private String  productId;
    private int quantity;
    private int discount;
    private String size;

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

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public ProductQuantity() {
    }

    public ProductQuantity(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public ProductQuantity(String productId, int quantity, int discount, String size) {
        this.productId = productId;
        this.quantity = quantity;
        this.discount = discount;
        this.size = size;
    }
}
