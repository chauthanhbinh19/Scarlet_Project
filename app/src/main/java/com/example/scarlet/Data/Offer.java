package com.example.scarlet.Data;

import java.util.Date;

public class Offer {
    private String name;
    private String description;
    private int point;
    private int image;
    private Date startDate;
    private Date endDate;

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

    public Offer(String name, String description, int point, int image) {
        this.name = name;
        this.description = description;
        this.point = point;
        this.image = image;
    }
}
