package com.example.scarlet.Data;

public class Product {
    private String key;
    private String name;
    private String description;
    private String categoryId;
    private String categoryName;
    private double price;
    private int point;
    private String img;
    private String icon;
    private int quantity;
    private double total;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Product() {
    }

    public Product(String name, String description, String categoryId, String categoryName, double price, int point, String img) {
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.price = price;
        this.point = point;
        this.img = img;
    }
    public Product(String name, String description, String categoryId, String categoryName, double price, int point, String img,String key) {
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.price = price;
        this.point = point;
        this.img = img;
        this.key=key;
    }
    public Product(String name, double price, String img,String icon,String key) {
        this.name = name;
        this.price = price;
        this.img=img;
        this.icon=icon;
        this.key=key;
    }
    public Product(String name, double price, String img,String icon,String key,int point) {
        this.name = name;
        this.price = price;
        this.img=img;
        this.icon=icon;
        this.key=key;
        this.point=point;
    }
    public Product(String name, double price, String img,String icon,String key,int quantity, double total) {
        this.name = name;
        this.price = price;
        this.img=img;
        this.icon=icon;
        this.key=key;
        this.quantity=quantity;
        this.total=total;
    }
    public Product(String name,double price,int quantity,double total, String categoryName){
        this.name = name;
        this.price = price;
        this.quantity=quantity;
        this.total=total;
        this.categoryName=categoryName;
    }

    public Product(String key, String name) {
        this.key = key;
        this.name = name;
    }
}
