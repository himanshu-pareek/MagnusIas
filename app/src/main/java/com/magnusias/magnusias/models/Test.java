package com.magnusias.magnusias.models;

public class Test {
    private int id, chapterId, topicId, subjectId, catSubId;
    private String name;
    private String testUrl;
    // private String imageUrl;
    private boolean isActive;

    public Test (int id, int chapterId, int topicId, int subjectId, int catSubId, String name, String testUrl,
                 /* String imageUrl ,*/ boolean isActive) {
        this.id = id;
        this.name = name;
        this.testUrl = testUrl;
        // this.imageUrl = imageUrl;
        this.isActive = isActive;
        this.chapterId = chapterId;
        this.topicId = topicId;
        this.subjectId = subjectId;
        this.catSubId = catSubId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public int getId() {
        return id;
    }

    public int getChapterId() {
        return chapterId;
    }

    public int getCatSubId() {
        return catSubId;
    }

    public int getTopicId() {
        return topicId;
    }

//    public String getImageUrl() {
//        return imageUrl;
//    }

    public String getName() {
        return name;
    }

    public String getTestUrl() {
        return testUrl;
    }

    public boolean getIsActive() {
        return isActive;
    }
}
