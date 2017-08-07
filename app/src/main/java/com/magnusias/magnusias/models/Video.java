package com.magnusias.magnusias.models;

public class Video {
    private int id, chapterId, topId, subjectId, catSubId;
    private String title;
    private String videoUrl;
    private String isVisible;
    private String imageUrl;

    public Video (int id, int chapterId, int topId, int subjectId, int catSubId,
                  String title, String videoUrl, String isVisible, String imageUrl) {
        this.id = id;
        this.chapterId = chapterId;
        this.topId = topId;
        this.subjectId = subjectId;
        this.catSubId = catSubId;
        this.title = title;
        this.videoUrl = videoUrl;
        this.isVisible = isVisible;
        this.imageUrl = imageUrl;
    }

    public int getCatSubId() {
        return catSubId;
    }

    public int getChapterId() {
        return chapterId;
    }

    public int getId() {
        return id;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public int getTopId() {
        return topId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getIsVisible() {
        return isVisible;
    }

    public String getTitle() {
        return title;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}
