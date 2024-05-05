package com.example.scarlet.Data;

import java.util.Date;

public class Review {
    private String customerId;
    private String productId;
    private int rating;
    private String comment;
    private Date date;
    private String customerName;
    private String customerImage;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerImage() {
        return customerImage;
    }

    public void setCustomerImage(String customerImage) {
        this.customerImage = customerImage;
    }

    public Review() {
    }

    public Review(String customerId, String productId, int rating, String comment, Date date) {
        this.customerId = customerId;
        this.productId = productId;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
    }

    public Review(String customerId, String productId, int rating, String comment, Date date, String customerName, String customerImage) {
        this.customerId = customerId;
        this.productId = productId;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
        this.customerName = customerName;
        this.customerImage = customerImage;
    }
}
