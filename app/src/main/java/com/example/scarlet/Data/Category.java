package com.example.scarlet.Data;

public class Category {
    private String id;
    private String name_category;
    private String img;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName_category() {
        return name_category;
    }

    public void setName_category(String name_category) {
        this.name_category = name_category;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Category() {
    }

    public Category(String id, String name_category, String img) {
        this.id = id;
        this.name_category = name_category;
        this.img = img;
    }
}
