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
import com.magnusias.magnusias.adapters.TestAdapter;
import com.magnusias.magnusias.adapters.VideoAdapter;
import com.magnusias.magnusias.models.Test;
import com.magnusias.magnusias.models.Video;
import com.magnusias.magnusias.utils.AppController;
import com.magnusias.magnusias.utils.Constants;
import com.magnusias.magnusias.utils.QueryUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestListActivity extends AppCompatActivity {

    private TestAdapter mTests;
    private int mCatSubId, mSubjectId, mTopicId, mChapterId;
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_list);

        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
        mCatSubId = settings.getInt(Constants.CAT_SUB_ID_STRING, -1);
        mSubjectId = settings.getInt(Constants.SUB_ID_STRING, -1);
        mTopicId = settings.getInt(Constants.TOPIC_ID_STRING, -1);
        mChapterId = settings.getInt(Constants.CHAPTER_ID_STRING, -1);

        if (mSubjectId == -1 || mCatSubId == -1 || mTopicId == -1 || mChapterId == -1) {
            finish();
        }

        ListView testList = (ListView) findViewById(R.id.list_test);
        mTests = new TestAdapter(this, new ArrayList<Test>());

        testList.setAdapter(mTests);

        loadVideoList();

        testList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Test test = mTests.getItem(position);
                if (test.getIsActive()) {
                    Intent i = new Intent(getApplicationContext(), TestWebViewActivity.class);
                    i.putExtra(Constants.TEST_URL_STRING, test.getTestUrl());
                    startActivity(i);
                } else {
                    Intent i = new Intent(getApplicationContext(), TestWebViewActivity.class);
                    i.putExtra(Constants.TEST_URL_STRING, Constants.SUBSCRIPTION_URL);
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
                Constants.FETCH_TEST_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Tests", response);
                        pDialog.hide();
                        mTests.clear();
                        ArrayList<Test> tests = QueryUtils.getTestListFromJSON(response);
                        if (tests != null)
                            mTests.addAll(tests);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        if (error instanceof NetworkError)
                            Toast.makeText(TestListActivity.this, "Network error, try again", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(TestListActivity.this, error.getClass().getName(), Toast.LENGTH_SHORT).show();
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
    }
}
