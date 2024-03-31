package com.example.scarlet.Data;

import java.util.Date;
import java.util.List;

public class Deal {
    private String name;
    private String code;
    private List<String> productId;
    private List<String> categoryId;
    private int discount;
    private Date expiryDate;
    private String description;
    private String deliveryMethod;
    private int deliveryIcon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getProductId() {
        return productId;
    }

    public void setProductId(List<String> productId) {
        this.productId = productId;
    }

    public List<String> getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(List<String> categoryId) {
        this.categoryId = categoryId;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public Deal() {
    }

    public int getDeliveryIcon() {
        return deliveryIcon;
    }

    public void setDeliveryIcon(int deliveryIcon) {
        this.deliveryIcon = deliveryIcon;
    }

    public Deal(String name, String code, List<String> productId, List<String> categoryId, int discount, Date expiryDate, String description, String deliveryMethod, int deliveryIcon) {
        this.name = name;
        this.code = code;
        this.productId = productId;
        this.categoryId = categoryId;
        this.discount = discount;
        this.expiryDate = expiryDate;
        this.description = description;
        this.deliveryMethod = deliveryMethod;
        this.deliveryIcon = deliveryIcon;
    }

    public Deal(String name, int discount, Date expiryDate, String deliveryMethod, int deliveryIcon) {
        this.name = name;
        this.discount = discount;
        this.expiryDate = expiryDate;
        this.deliveryMethod = deliveryMethod;
        this.deliveryIcon = deliveryIcon;
    }
}
