package com.magnusias.magnusias.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.magnusias.magnusias.adapters.TopicAdapter;
import com.magnusias.magnusias.models.CatSubject;
import com.magnusias.magnusias.models.Subject;
import com.magnusias.magnusias.models.Topic;
import com.magnusias.magnusias.utils.AppController;
import com.magnusias.magnusias.utils.Constants;
import com.magnusias.magnusias.utils.QueryUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TopicListActivity extends AppCompatActivity {

    private TopicAdapter mTopics;
    private String mUsername;
    private int mSubId, mCatSubId;
    private int mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_list);

        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
        mSubId = settings.getInt(Constants.SUB_ID_STRING, -1);
        mCatSubId = settings.getInt(Constants.CAT_SUB_ID_STRING, -1);
        mUserId = settings.getInt(Constants.USER_ID, -1);

        if (mUserId == -1)
            startActivity(new Intent(this, LoginActivity.class));

        if (mSubId == -1 || mCatSubId == -1) {
            finish();
        }

        ListView topicList = (ListView) findViewById(R.id.list_topic);
        mTopics = new TopicAdapter(this, new ArrayList<Topic>());

        topicList.setAdapter(mTopics);

        loadDateIntoAdapter();

        topicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Topic topic = mTopics.getItem(position);
                SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt(Constants.TOPIC_ID_STRING, topic.getId());
                editor.apply();
                startActivity(new Intent(getApplicationContext(), ChapterListActivity.class));
            }
        });

    }

    private void loadDateIntoAdapter() {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.FETCH_TOPIC_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        mTopics.clear();
                        ArrayList<Topic> topics = QueryUtils.getTopicListFromJSON(response);
                        mTopics.addAll(topics);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        if (error instanceof NetworkError)
                            Toast.makeText(TopicListActivity.this, "Network error, try again", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", String.valueOf(mUserId));
                map.put("cat_sub_id", String.valueOf(mCatSubId));
                map.put("sub_id", String.valueOf(mSubId));
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



//        String[] imageUrls = {
//                "http://magnusias.com/Objective/sectionImage?imgpath=thumbqs-image27Feb17170604qs-imagephysicalGeography",
//                "http://magnusias.com/Objective/sectionImage?imgpath=thumbqs-image27Feb17170554qs-imagegeography",
//        };
//        String[] titles = {
//                "Physical Geography",
//                "Indian Geography"
//        };
//        String[] descs = {
//                "Understanding of the concept of Physical Geography is vital to conceptualise the concept of not only Geo-Morphology, Climatology, Oceanography but also to appreciate the Economic and Development issues at the international level, which is much sought after in the Civil Services question papers. Even to understand the Geo-political importance of any nation, resource mapping of that particular country needs the background of Geography. We have already seen while studying History, how much it was important to study the Geographical Map. The classes of Physical Geography have been meticulously made. Hope you will enjoy learning.",
//                "Indian Geography covers the most salient and fundamental aspect related to General Studies paper as a whole. The knowledge of topography, climate, river systems of Indian Continent pave the way for analysing the important aspect of culture and history of India as well. Though there are more than 7 questions in PT from this section, its importance is more pronounced for understanding the base line of History, Culture, Economy and as whole of the whole GS Paper. It is, in fact a hinge on which the GS Paper revolves. For a civil services aspirant, I recommend the reference book- D.R Khullar on which major part of SAVE classes are based. But, I had taken reference from so many other sources like The Economic Survey, Wikipedia, Maps of India. Students are advised to actively listen the SAVE classes first, then to proceed to D.R Khullar's Book and after that students should practice with the Question Bank available in the Market."
//        };
//        for (int i = 0; i < imageUrls.length; i++) {
//            Topic topic = new Topic(i, mSubId, mCatSubId, titles[i], descs[i], "145236542", true, imageUrls[i], "Himanshu Pareek");
//            mTopics.add(topic);
//        }
    }
}
