package com.magnusias.magnusias.models;

public class CatSubject {
    private int id;
    private String category, imageUrl;
    public CatSubject (int id, String category, String imageUrl) {
        this.id = id;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }
}
