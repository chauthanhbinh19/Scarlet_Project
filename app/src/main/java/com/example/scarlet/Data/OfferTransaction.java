package com.example.scarlet.Data;

import java.util.Date;

public class OfferTransaction {
    private String customerId;
    private String offerId;
    private String offerCode;
    private Date exchangeDate;
    private int point;

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getOfferCode() {
        return offerCode;
    }

    public void setOfferCode(String offerCode) {
        this.offerCode = offerCode;
    }

    public Date getExchangeDate() {
        return exchangeDate;
    }

    public void setExchangeDate(Date exchangeDate) {
        this.exchangeDate = exchangeDate;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public OfferTransaction() {
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public OfferTransaction(String customerId, String offerId, String offerCode, Date exchangeDate, int point) {
        this.customerId = customerId;
        this.offerId = offerId;
        this.offerCode = offerCode;
        this.exchangeDate = exchangeDate;
        this.point = point;
    }
}
