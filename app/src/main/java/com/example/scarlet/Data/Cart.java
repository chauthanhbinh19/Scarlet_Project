package com.example.scarlet.Data;

import java.util.List;

public class Cart {
    private List<Product> listProduct;
    private double total;

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Cart(List<Product> listProduct, double total) {
        this.listProduct = listProduct;
        this.total = total;
    }
}
