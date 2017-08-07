package com.magnusias.magnusias.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.magnusias.magnusias.models.CatSubject;
import com.magnusias.magnusias.models.Subject;
import com.magnusias.magnusias.utils.AppController;
import com.magnusias.magnusias.utils.Constants;
import com.magnusias.magnusias.utils.QueryUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubjectListActivity extends AppCompatActivity {

    private ArrayAdapter<Subject> mSubjects;
    private int mCatSubId;
    private String mUsername;
    private int mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list);

        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
        mCatSubId = settings.getInt(Constants.CAT_SUB_ID_STRING, -1);
        mUserId = settings.getInt(Constants.USER_ID, -1);

        if (mUserId == -1) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        if (mCatSubId == -1) {
            finish();
        }

        ListView subjectList = (ListView)findViewById(R.id.list_subject);
        mSubjects = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        subjectList.setAdapter(mSubjects);

        loadDataIntoAdapter();

        subjectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Subject subject = mSubjects.getItem(position);
                SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt(Constants.SUB_ID_STRING, subject.getId());
                editor.apply();
                startActivity(new Intent(getApplicationContext(), TopicListActivity.class));
            }
        });

    }

    private void loadDataIntoAdapter() {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.FETCH_SUBJECT_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        mSubjects.clear();
                        ArrayList<Subject> subjects = QueryUtils.getSubjectListFromJSON(response);
                        mSubjects.addAll(subjects);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        if (error instanceof NetworkError)
                            Toast.makeText(SubjectListActivity.this, "Network error, try again", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", String.valueOf(mUserId));
                map.put("cat_sub_id", String.valueOf(mCatSubId));
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
