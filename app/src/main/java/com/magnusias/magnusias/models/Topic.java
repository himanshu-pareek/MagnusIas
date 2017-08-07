package com.magnusias.magnusias.models;

import android.widget.Toast;

public class Topic {
    private int id, subjectId, catSubId;
    private String title;
    private String description;
    private String createdOn;
    private boolean isActive;
    private String imageUrl;
    private String createdBy;

    public Topic (int id, int subjectId, int catSubId, String title, String desc, String co, boolean ia, String imageUrl, String createdBy) {
        this.id = id;
        this.subjectId = subjectId;
        this.catSubId = catSubId;
        this.title = title;
        this.description = desc;
        this.createdOn = co;
        this.isActive = ia;
        this.imageUrl = imageUrl;
        this.createdBy = createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public int getId() {
        return id;
    }

    public int getCatSubId() {
        return catSubId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }
    public boolean getIsActive() {
        return isActive;
    }
}
