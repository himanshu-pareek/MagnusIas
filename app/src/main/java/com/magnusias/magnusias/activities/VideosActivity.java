package com.magnusias.magnusias.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.magnusias.magnusias.R;
import com.magnusias.magnusias.adapters.VideoAdapter;
import com.magnusias.magnusias.models.Chapter;
import com.magnusias.magnusias.models.Video;
import com.magnusias.magnusias.utils.AppController;
import com.magnusias.magnusias.utils.Constants;
import com.magnusias.magnusias.utils.QueryUtils;
import com.vimeo.networking.Configuration;
import com.vimeo.networking.VimeoClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VideosActivity extends AppCompatActivity {

    private VimeoClient mVimeoClient;
    private VideoAdapter mVideos;

    private int mUserId;
    private int mCatSubId, mSubjectId, mTopicId, mChapterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
        mCatSubId = settings.getInt(Constants.CAT_SUB_ID_STRING, -1);
        mSubjectId = settings.getInt(Constants.SUB_ID_STRING, -1);
        mTopicId = settings.getInt(Constants.TOPIC_ID_STRING, -1);
        mChapterId = settings.getInt(Constants.CHAPTER_ID_STRING, -1);
        mUserId = settings.getInt(Constants.USER_ID, -1);

        if (mUserId == -1)
            startActivity(new Intent(this, LoginActivity.class));

        if (mSubjectId == -1 || mCatSubId == -1 || mTopicId == -1 || mChapterId == -1) {
            finish();
        }

//        String accessToken = getString(R.string.access_token);
//        Configuration.Builder builder = new Configuration.Builder(accessToken);
//
//        VimeoClient.initialize(builder.build());
//        mVimeoClient = VimeoClient.getInstance();

        ListView videoList = (ListView) findViewById(R.id.list_videos);
        mVideos = new VideoAdapter(this, new ArrayList<Video>());

        videoList.setAdapter(mVideos);

        loadVideoList();

        videoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Video video = mVideos.getItem(position);
                if (video.getIsVisible().equals("yes")) {
                    Intent i = new Intent(getApplicationContext(), VideoWebViewActivity.class);
                    i.putExtra(Constants.VIDEO_URL_STRING, video.getVideoUrl());
                    startActivity(i);
                } else {
                    Intent i = new Intent(getApplicationContext(), VideoWebViewActivity.class);
                    i.putExtra(Constants.VIDEO_URL_STRING, Constants.SUBSCRIPTION_URL);
                    startActivity(i);
                }
            }
        });

    }

    private void loadVideoList() {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.FETCH_VIDEO_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Videos", response);
                        pDialog.hide();
                        mVideos.clear();
                        ArrayList<Video> videos = QueryUtils.getVideoListFromJSON(response);
                        if (videos != null)
                            mVideos.addAll(videos);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        if (error instanceof NetworkError)
                            Toast.makeText(VideosActivity.this, "Network error, try again", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(VideosActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", String.valueOf(mUserId));
                map.put("cat_sub_id", String.valueOf(mCatSubId));
                map.put("sub_id", String.valueOf(mSubjectId));
                map.put("topic_id", String.valueOf(mTopicId));
                map.put("chapter_id", String.valueOf(mChapterId));
                return map;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest, Constants.TAG_STRING_REQ);

//        String[] titles = {
//                "title 1",
//                "title 2",
//                "title 3",
//                "title 4",
//                "title 5",
//                "title 6",
//                "title 7"
//        };
//
//        String[] videoUrls = {
//                "https://vimeo.com/219466905/128e87d4d0",
//                "",
//                "",
//                "",
//                "",
//                "",
//                ""
//        };
//
//        String[] imageUrls = {
//                "http://magnusias.com/Objective/ThumbImage?imgpath=188qs-image27qs-imagethumbqs-image04Oct16075155qs-image301-Geography",
//                "http://www.magnusias.com/Images/subscribenow.jpg",
//                "http://www.magnusias.com/Images/subscribenow.jpg",
//                "http://www.magnusias.com/Images/subscribenow.jpg",
//                "http://www.magnusias.com/Images/subscribenow.jpg",
//                "http://www.magnusias.com/Images/subscribenow.jpg",
//                "http://www.magnusias.com/Images/subscribenow.jpg"
//        };
//
//        String[] isVisibles = {
//                "yes", "no", "no", "no", "no", "no", "no"
//        };
//
//        for (int i = 0; i < titles.length; i++) {
//            Video video = new Video(i + 230, mChapterId, mTopicId, mSubjectId, mCatSubId, titles[i], videoUrls[i], isVisibles[i], imageUrls[i]);
//            mVideos.add(video);
//        }

    }
}
