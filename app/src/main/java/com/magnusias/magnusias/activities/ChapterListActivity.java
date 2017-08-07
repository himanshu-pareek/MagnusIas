package com.magnusias.magnusias.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
import com.magnusias.magnusias.models.Chapter;
import com.magnusias.magnusias.utils.AppController;
import com.magnusias.magnusias.utils.Constants;
import com.magnusias.magnusias.utils.QueryUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChapterListActivity extends AppCompatActivity implements View.OnClickListener {

    private ChapterAdapter mChapters;
    private int mCatSubId, mSubId, mTopicId;
    private String mUsername;
    private int mUserId;
    private Chapter mChapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_list);

        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
        mSubId = settings.getInt(Constants.SUB_ID_STRING, -1);
        mCatSubId = settings.getInt(Constants.CAT_SUB_ID_STRING, -1);
        mTopicId = settings.getInt(Constants.TOPIC_ID_STRING, -1);
        mUserId = settings.getInt(Constants.USER_ID, -1);

        if (mUserId == -1)
            startActivity(new Intent(this, LoginActivity.class));

        if (mSubId == -1 || mCatSubId == -1 || mTopicId == -1) {
            finish();
        }

        ListView chapterList = (ListView) findViewById(R.id.list_chapter);
        mChapters = new ChapterAdapter(this, new ArrayList<Chapter>());

        chapterList.setAdapter(mChapters);

        loadDateIntoAdapter();

//        chapterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                mChapter = mChapters.getItem(position);
//                onClick(null);
//            }
//        });

    }

    private void loadDateIntoAdapter() {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.FETCH_CHAPTER_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        ArrayList<Chapter> chapters = QueryUtils.getChapterListFromJSON(response);
                        mChapters.addAll(chapters);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        if (error instanceof NetworkError)
                            Toast.makeText(ChapterListActivity.this, "Network error, try again", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", String.valueOf(mUserId));
                map.put("cat_sub_id", String.valueOf(mCatSubId));
                map.put("sub_id", String.valueOf(mSubId));
                map.put("topic_id", String.valueOf(mTopicId));
                return map;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                1500,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest, Constants.TAG_STRING_REQ);



//        String[] names = {
//                "Chapter-1-Overview of Syllabus of Geography ",
//                "Chapter-2- The Earth and the Universe",
//                "Chapter-3- Geomorphology",
//                "Chapter-4- Land Forms and its Types",
//                "Chapter-5- Oceanography",
//                "Chapter-6- Climatology",
//                "Chapter-7- Climatic Regions of the world"
//        };
//        String[] descs = {
//                "Geography must be studied along with 'Ecology and Environment'. We find the in last 5-6 years 20 to 25 questions have been asked directly from this area. In this chapter, you will get some insight of the questions asked in Geography.",
//                "This is one of the important chapter from which questions are usually asked in PT and Mains as well. Concept of Origin of Earth, concept of International Date Line,Equinox,Longitude and Latitude etc are hot favourite topic in the PCS examination. ",
//                "Geomorphology is the most important chapter of Physical Geography. This is the reason, that maximum number of SAVE classes are on the topics related to Geomorphology. The basic concept of continental drift, earthquakes, formation of volcanoes, mountains etc. are the hot favourite topics for CS( Mains) and (PT) as well. Besides, in GS Paper(III) also, these concepts are being asked in applied forms in topics like Disaster Management. ",
//                "Land Forms are varied. It is developed as a result of diverse climatic conditions and factors like land forms made by running water, by glaciation, by action of wind, by weathering effect ans so on. Study of different types of land forms i.e plateau, plain, desert etc are very interesting. It helps us to understand the Human and Economic Geography as well. Questions have been asked in all the previous years from this important chapter. ",
//                "Reliefs in oceans and ocean currents shape the climate on earth and affect the geo-morphological features on the globe, as ocean covers three fourth of its surface. Considering this aspect, UPSC lays emphasis to test the student's awareness about the basics of oceanography.In this chapter, students will get a vivid description of oceanography from UPSC General Studies point of view. ",
//                "Climatology is the most important chapter of Physical Geography, since all the aspects related to climate and weather like insolation, geographical location of a place, orography, wind direction, moisture, revolution of earth around its axis etc are being thoroughly discussed in this chapter. Hence, it is observed that most of the questions are being asked from this chapter. ",
//                "Understanding of climatic regions of the world is important from Civil Services exam point of view. It helps the students to amalgamate the concepts, one has learnt under different topics of Physical Geography. It also enables the student for globetrotting and to acquaint himself/herself to a particular climatic condition of a region/biome, which will prove to be a foundation of understanding of World Geography and Human and Economic Geography. Students are advised to refer some of the classes/documentary available at You tube. For example-- Documentary on Equatorial Forest, Yasuni tribe of Ecuador, Depletion of forest reserves in Indonesia will help the student to understand the important aspect of Equatorial region and its flora and fauna. Similarly, life in Sahara or problems of shifting cultivation or cattle rearing in temperate grassland gives an insight if the peculiarities of a particular climatic regions are well comprehended by the students. "
//        };
//        for (int i = 0; i < names.length; i++) {
//            Chapter chapter = new Chapter(i, mTopicId, mSubId, mCatSubId, names[i], "12365263", "Himanshu Pareek", true, descs[i]);
//            mChapters.add(chapter);
//        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_list_button:
                Toast.makeText(ChapterListActivity.this, "Video List\n" + mChapter.getName() + "\n" + mChapter.getDescription(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.test_series_button:
                Toast.makeText(ChapterListActivity.this, "Test List\n" + mChapter.getName() + "\n" + mChapter.getDescription(), Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(ChapterListActivity.this, "Nothing\n" + mChapter.getName() + "\n" + mChapter.getDescription(), Toast.LENGTH_SHORT).show();
        }
    }

    private void viewVideoList(Chapter chapter) {
        // Toast.makeText(this, "video\n" + chapter.getName() + "\n" + chapter.getDescription(), Toast.LENGTH_LONG).show();
        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(Constants.CHAPTER_ID_STRING, chapter.getId());
        editor.apply();
        startActivity(new Intent(this, VideosActivity.class));
    }

    private void viewTestList(Chapter chapter) {
        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(Constants.CHAPTER_ID_STRING, chapter.getId());
        editor.apply();
        startActivity(new Intent(this, TestListActivity.class));
    }

    private class ChapterAdapter extends ArrayAdapter<Chapter> {

        public ChapterAdapter (Context context, ArrayList<Chapter> resource) {
            super(context, 0, resource);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View listItemView = convertView;
            if (listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(
                        R.layout.layout_chapter_item, parent, false
                );
            }

            final Chapter chapter = getItem(position);

            TextView textView = (TextView) listItemView.findViewById(R.id.chapter_title_text_view);
            TextView descView = (TextView) listItemView.findViewById(R.id.chapter_desc_text_view);
            Button videoListButton = (Button) listItemView.findViewById(R.id.video_list_button);
            Button testListButton = (Button) listItemView.findViewById(R.id.test_series_button);

            textView.setText(chapter.getName());
            descView.setText(chapter.getDescription());

            videoListButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(ChapterListActivity.this, "Video" + chapter.getName(), Toast.LENGTH_SHORT).show();
                    viewVideoList(chapter);
                }
            });

            testListButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewTestList(chapter);
                }
            });

            return listItemView;
        }

    }


}
