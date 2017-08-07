package com.magnusias.magnusias.models;

public class Chapter {
    private int id, topicId, subId, catSubId;
    private String name;
    private String createdOn;
    private String createdBy;
    private boolean isActive;
    private String description;

    public Chapter (int id, int ti, int si, int ci, String name, String co, String cb, boolean ia, String desc) {
        this.id = id;
        this.topicId = ti;
        this.subId = si;
        this.catSubId = ci;
        this.name = name;
        this.createdOn = co;
        this.createdBy = cb;
        this.isActive = ia;
        this.description = desc;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public int getCatSubId() {
        return catSubId;
    }

    public int getId() {
        return id;
    }

    public int getSubId() {
        return subId;
    }

    public int getTopicId() {
        return topicId;
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

    public String getDescription() {
        return description;
    }
}
