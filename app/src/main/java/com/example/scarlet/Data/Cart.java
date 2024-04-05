package com.example.scarlet.Data;

import java.util.List;

public class Cart {
    private String customerId;
    private List<ProductQuantity> productQuantityList;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }


    public List<ProductQuantity> getProductQuantityList() {
        return productQuantityList;
    }

    public void setProductQuantityList(List<ProductQuantity> productQuantityList) {
        this.productQuantityList = productQuantityList;
    }

    public Cart() {
    }

    public Cart(String customerId, List<ProductQuantity> productQuantityList) {
        this.customerId = customerId;
        this.productQuantityList = productQuantityList;
    }
}
