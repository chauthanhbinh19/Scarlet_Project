package com.example.scarlet.Data;

import java.util.Date;

public class Offer {
    private String name;
    private String code;
    private String description;
    private int point;
    private int image;
    private Date startDate;
    private Date endDate;
    private String key;
    private Date exchangeDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getExchangeDate() {
        return exchangeDate;
    }

    public void setExchangeDate(Date exchangeDate) {
        this.exchangeDate = exchangeDate;
    }

    public Offer() {
    }

    public Offer(String name, String description, int point, int image, Date startDate, Date endDate) {
        this.name = name;
        this.description = description;
        this.point = point;
        this.image = image;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Offer(String name, String code, String description, int point, int image, String key) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.point = point;
        this.image = image;
        this.key = key;
    }

    public Offer(String name, String code, String description, int point, int image, Date startDate, Date endDate) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.point = point;
        this.image = image;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Offer(String name, String description, int point, int image, Date exchangeDate) {
        this.name = name;
        this.description = description;
        this.point = point;
        this.image = image;
        this.exchangeDate=exchangeDate;
    }

    public Offer(String name, String description, int point, int image, Date startDate, Date endDate, String key) {
        this.name = name;
        this.description = description;
        this.point = point;
        this.image = image;
        this.startDate = startDate;
        this.endDate = endDate;
        this.key = key;
    }

    public Offer(String name, String code, String description, int point, int image, Date startDate, Date endDate, String key) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.point = point;
        this.image = image;
        this.startDate = startDate;
        this.endDate = endDate;
        this.key = key;
    }
}
