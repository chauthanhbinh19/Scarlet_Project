package com.example.scarlet.Data;

public class Review {
    private String customerId;
    private int rating;
    private String comment;

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

    public Review() {
    }

    public Review(String customerId, int rating, String comment) {
        this.customerId = customerId;
        this.rating = rating;
        this.comment = comment;
    }
}
