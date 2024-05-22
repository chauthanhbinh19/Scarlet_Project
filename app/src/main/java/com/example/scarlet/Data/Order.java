package com.example.scarlet.Data;

import java.util.Date;
import java.util.List;

public class Order {
    private String userId;
    private String orderStatus;
    private Payment paymentMethod;
    private Address shippingAddress;
    private Date orderDate;
    private double total;
    private double tip;
    private String deliveryStatus;
    private double deliveryFee;
    private List<Product> productList;
    private String key;
    private boolean isConfirmed;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Payment getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Payment paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public double getTip() {
        return tip;
    }

    public void setTip(double tip) {
        this.tip = tip;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public Order() {
    }

    public Order(String userId, String orderStatus, Payment paymentMethod, Address shippingAddress, Date orderDate, double total, double tip, String deliveryStatus, double deliveryFee, List<Product> productList) {
        this.userId = userId;
        this.orderStatus = orderStatus;
        this.paymentMethod = paymentMethod;
        this.shippingAddress = shippingAddress;
        this.orderDate = orderDate;
        this.total = total;
        this.tip = tip;
        this.deliveryStatus = deliveryStatus;
        this.deliveryFee = deliveryFee;
        this.productList = productList;
    }
    public Order(String userId, String orderStatus, Payment paymentMethod, Address shippingAddress, Date orderDate, double total, double tip, String deliveryStatus, double deliveryFee, List<Product> productList, boolean isConfirmed) {
        this.userId = userId;
        this.orderStatus = orderStatus;
        this.paymentMethod = paymentMethod;
        this.shippingAddress = shippingAddress;
        this.orderDate = orderDate;
        this.total = total;
        this.tip = tip;
        this.deliveryStatus = deliveryStatus;
        this.deliveryFee = deliveryFee;
        this.productList = productList;
        this.isConfirmed=isConfirmed;
    }

    public Order(String userId, String orderStatus, Payment paymentMethod, Address shippingAddress, Date orderDate, double total, double tip, String deliveryStatus, double deliveryFee, List<Product> productList, String key) {
        this.userId = userId;
        this.orderStatus = orderStatus;
        this.paymentMethod = paymentMethod;
        this.shippingAddress = shippingAddress;
        this.orderDate = orderDate;
        this.total = total;
        this.tip = tip;
        this.deliveryStatus = deliveryStatus;
        this.deliveryFee = deliveryFee;
        this.productList = productList;
        this.key = key;
    }

    public Order(String orderStatus, Date orderDate, double total, List<Product> productList, String key, boolean isConfirmed) {
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.total = total;
        this.productList = productList;
        this.key = key;
        this.isConfirmed=isConfirmed;
    }

    public Order(String orderStatus, Date orderDate, double total, String key) {
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.total = total;
        this.key = key;
    }
}
