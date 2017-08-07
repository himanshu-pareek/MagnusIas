package com.magnusias.magnusias.models;

public class Subject {
    private int id, catId;
    private String name;
    private boolean isActive;
    private String createdOn;

    public Subject (int id, int catId, String name, boolean isActive, String createdOn) {
        this.id = id;
        this.catId = catId;
        this.name = name;
        this.isActive = isActive;
        this.createdOn = createdOn;
    }

    public int getId() {
        return id;
    }

    public int getCatId() {
        return catId;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getName() {
        return name;
    }

    public boolean getIsActive() {
        return isActive;
    }

    @Override
    public String toString() {
        return name;
    }
}
