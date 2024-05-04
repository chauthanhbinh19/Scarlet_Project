package com.example.scarlet.Data;

import java.util.Date;
import java.util.List;

public class DealTransaction {
    private String code;
    private String customerId;
    private String orderId;
    private String key;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public DealTransaction() {
    }

    public DealTransaction(String code, String customerId, String orderId, String key) {
        this.code = code;
        this.customerId = customerId;
        this.orderId = orderId;
        this.key = key;
    }

    public DealTransaction(String code, String customerId, String orderId) {
        this.code = code;
        this.customerId = customerId;
        this.orderId = orderId;
    }
}
