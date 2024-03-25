package com.example.scarlet.Data;

public class Feedback {
    private String customerId;
    private String customerName;
    private String content;



    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Feedback() {
    }

    public Feedback(String customerId, String customerName, String content) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.content = content;
    }
}
