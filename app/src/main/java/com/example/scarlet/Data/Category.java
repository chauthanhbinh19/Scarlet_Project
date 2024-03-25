package com.example.scarlet.Data;

public class Category {
    private String key;
    private String name_category;
    private int img;

    public String getName_category() {
        return name_category;
    }

    public void setName_category(String name_category) {
        this.name_category = name_category;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Category() {
    }

    public Category(String name_category, int img) {
        this.name_category = name_category;
        this.img = img;
    }
    public Category(String name_category,int img,String key){
        this.name_category=name_category;
        this.img=img;
        this.key=key;
    }
}
