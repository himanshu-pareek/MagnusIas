package com.magnusias.magnusias.utils;

import com.magnusias.magnusias.models.CatSubject;
import com.magnusias.magnusias.models.Chapter;
import com.magnusias.magnusias.models.Subject;
import com.magnusias.magnusias.models.Test;
import com.magnusias.magnusias.models.Topic;
import com.magnusias.magnusias.models.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QueryUtils {


    public static ArrayList<CatSubject> getCatSubListFromJSON(String s) {
        try {
            JSONObject mainObj = new JSONObject(s);
            int result = mainObj.getInt("result");
            if (result == 0)
                return null;
            JSONArray catArray = mainObj.getJSONArray("cat_subjects");
            ArrayList<CatSubject> cats = new ArrayList<>();
            for (int i = 0; i < catArray.length(); i++) {
                JSONObject c = catArray.getJSONObject(i);
                CatSubject catSubject = new CatSubject(
                        c.getInt("id"),
                        c.getString("category"),
                        c.getString("image_url")
                );
                cats.add(catSubject);
            }
            return cats;
        } catch (JSONException e) {
            return null;
        }
    }

    public static ArrayList<Subject> getSubjectListFromJSON(String s) {
        try {
            JSONObject mainObj = new JSONObject(s);
            int result = mainObj.getInt("result");
            if (result == 0)
                return null;
            JSONArray subArray = mainObj.getJSONArray("subjects");
            ArrayList<Subject> subs = new ArrayList<>();
            for (int i = 0; i < subArray.length(); i++) {
                JSONObject c = subArray.getJSONObject(i);
                Subject subject = new Subject(
                        c.getInt("id"),
                        Integer.parseInt(c.getString("cat_sub_id")),
                        c.getString("name"),
                        c.getString("is_active").equals("yes"),
                        c.getString("created_on")
                );
                subs.add(subject);
            }
            return subs;
        } catch (JSONException e) {
            return null;
        }
    }

    public static ArrayList<Topic> getTopicListFromJSON(String s) {
        try {
            JSONObject mainObj = new JSONObject(s);
            int result = mainObj.getInt("result");
            if (result == 0)
                return null;
            JSONArray topicArray = mainObj.getJSONArray("topics");
            ArrayList<Topic> topics = new ArrayList<>();
            for (int i = 0; i < topicArray.length(); i++) {
                JSONObject c = topicArray.getJSONObject(i);
                Topic topic = new Topic(
                        c.getInt("id"),
                        Integer.parseInt(c.getString("subject_id")),
                        Integer.parseInt(c.getString("cat_sub_id")),
                        c.getString("title"),
                        c.getString("desc"),
                        c.getString("created_on"),
                        c.getString("is_active").equals("yes"),
                        c.getString("image_url"),
                        c.getString("created_by")
                );
                topics.add(topic);
            }
            return topics;
        } catch (JSONException e) {
            return null;
        }
    }

    public static ArrayList<Chapter> getChapterListFromJSON(String s) {
        try {
            JSONObject mainObj = new JSONObject(s);
            int result = mainObj.getInt("result");
            if (result == 0)
                return null;
            JSONArray chapterArray = mainObj.getJSONArray("chapters");
            ArrayList<Chapter> chapters = new ArrayList<>();
            for (int i = 0; i < chapterArray.length(); i++) {
                JSONObject c = chapterArray.getJSONObject(i);
                Chapter chapter = new Chapter(
                        c.getInt("id"),
                        Integer.parseInt(c.getString("topic_id")),
                        Integer.parseInt(c.getString("subject_id")),
                        Integer.parseInt(c.getString("cat_sub_id")),
                        c.getString("name"),
                        c.getString("created_on"),
                        c.getString("created_by"),
                        c.getString("is_active").equals("yes"),
                        c.getString("desc")
                );
                chapters.add(chapter);
            }
            return chapters;
        } catch (JSONException e) {
            return null;
        }
    }

    public static ArrayList<Video> getVideoListFromJSON(String s) {
        try {
            JSONObject mainObj = new JSONObject(s);
            int result = mainObj.getInt("result");
            if (result == 0)
                return null;
            JSONArray videoArray = mainObj.getJSONArray("videos");
            ArrayList<Video> videos = new ArrayList<>();
            for (int i = 0; i < videoArray.length(); i++) {
                JSONObject v = videoArray.getJSONObject(i);
                Video video = new Video(
                        v.getInt("id"),
                        Integer.parseInt(v.getString("chapter_id")),
                        Integer.parseInt(v.getString("topic_id")),
                        Integer.parseInt(v.getString("subject_id")),
                        Integer.parseInt(v.getString("cat_sub_id")),
                        v.getString("title"),
                        v.getString("video_url"),
                        v.getString("is_visible"),
                        v.getString("image_url")
                );
                videos.add(video);
            }
            return videos;
        } catch (JSONException e) {
            return null;
        }
    }

    public static ArrayList<Test> getTestListFromJSON(String s) {
        try {
            JSONObject mainObj = new JSONObject(s);
            int result = mainObj.getInt("result");
            if (result == 0)
                return null;
            JSONArray testArray = mainObj.getJSONArray("tests");
            ArrayList<Test> tests = new ArrayList<>();
            for (int i = 0; i < testArray.length(); i++) {
                JSONObject v = testArray.getJSONObject(i);
                Test test = new Test(
                        v.getInt("id"),
                        Integer.parseInt(v.getString("chapter_id")),
                        Integer.parseInt(v.getString("topic_id")),
                        Integer.parseInt(v.getString("subject_id")),
                        Integer.parseInt(v.getString("cat_sub_id")),
                        v.getString("title"),
                        v.getString("test_url"),
                        v.getString("is_visible").equals("yes")
                );
                tests.add(test);
            }
            return tests;
        } catch (JSONException e) {
            return null;
        }
    }

}
