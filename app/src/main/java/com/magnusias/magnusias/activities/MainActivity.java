package com.magnusias.magnusias.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.magnusias.magnusias.R;
import com.magnusias.magnusias.SharedPrefManager;
import com.magnusias.magnusias.adapters.CatSubjectAdapter;
import com.magnusias.magnusias.models.CatSubject;
import com.magnusias.magnusias.utils.AppController;
import com.magnusias.magnusias.utils.Constants;
import com.magnusias.magnusias.utils.QueryUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean isLoggedIn;
    private CatSubjectAdapter mCatSubjectAdapter;
    private Button mAboutSaveButton;
    private String mEmail;
    private int mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
        isLoggedIn = settings.getBoolean("is_logged_in", false);
        mUserId = settings.getInt(Constants.USER_ID, -1);

        Log.d("Logged In", isLoggedIn ? "Yes" : "No");

        if (!isLoggedIn) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        mEmail = settings.getString("email", "");



        if (!isLoggedIn || mUserId == -1) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAboutSaveButton = (Button) findViewById(R.id.about_save_button);
        mAboutSaveButton.setVisibility(View.GONE);

        loadAboutSave();

        ListView catSubList = (ListView) findViewById(R.id.list_cat_subject);
        mCatSubjectAdapter = new CatSubjectAdapter(this, new ArrayList<CatSubject>());

        catSubList.setAdapter(mCatSubjectAdapter);

        addDataToAdapter();

        catSubList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CatSubject c = mCatSubjectAdapter.getItem(position);
                SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt(Constants.CAT_SUB_ID_STRING, c.getId());
                editor.apply();
                startActivity(new Intent(getApplicationContext(), SubjectListActivity.class));
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        TextView useremailTextView = (TextView) hView.findViewById(R.id.user_email_text_view);
        useremailTextView.setText(mEmail);
        navigationView.setNavigationItemSelectedListener(this);


        sendTokenToServer();
    }

    // Registering receiver on activity resume
    @Override
    protected void onResume() {
        super.onResume();
    }


    //Unregistering receiver on activity paused
    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "onPause");
    }

    private void loadAboutSave() {
        mAboutSaveButton.setVisibility(View.VISIBLE);
    }

    private void addDataToAdapter() {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.FETCH_CAT_SUB_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        mCatSubjectAdapter.clear();
                        ArrayList<CatSubject> cats = QueryUtils.getCatSubListFromJSON(response);
                        mCatSubjectAdapter.addAll(cats);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        if (error instanceof NetworkError)
                            Toast.makeText(MainActivity.this, "Network error, try again", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", String.valueOf(mUserId));
                return map;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        // stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest, Constants.TAG_STRING_REQ);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_our_founder) {
            Intent i = new Intent(this, AboutUsWebViewActivity.class);
            i.putExtra(Constants.ABOUT_US_URL_STRING, Constants.OUT_FOUNDER_URL);
            startActivity(i);
        } else if (id == R.id.nav_method_of_teaching) {
            Intent i = new Intent(this, AboutUsWebViewActivity.class);
            i.putExtra(Constants.ABOUT_US_URL_STRING, Constants.METHOD_OF_TEACHING_URL);
            startActivity(i);
        } else if (id == R.id.nav_mission_and_vision) {
            Intent i = new Intent(this, AboutUsWebViewActivity.class);
            i.putExtra(Constants.ABOUT_US_URL_STRING, Constants.MISSION_AND_VISION_URL);
            startActivity(i);
        } else if (id == R.id.nav_faq) {
            Intent i = new Intent(this, AboutUsWebViewActivity.class);
            i.putExtra(Constants.ABOUT_US_URL_STRING, Constants.FAQ_URL);
            startActivity(i);
        } else if (id == R.id.nav_contact_us) {
            Intent i = new Intent(this, AboutUsWebViewActivity.class);
            i.putExtra(Constants.ABOUT_US_URL_STRING, Constants.CONTACT_US_URL);
            startActivity(i);
        } else if (id == R.id.nav_pricing) {
            Intent i = new Intent(this, AboutUsWebViewActivity.class);
            i.putExtra(Constants.ABOUT_US_URL_STRING, Constants.PRICING_URL);
            startActivity(i);
        } else if (id == R.id.nav_share) {
            // share the app
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "MagnusIAS");
                String sAux = "\nLet me recommend you this awesome and useful application.\nGive it a try.\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=com.magnusias.magnusias \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "Choose one of the following to share this app"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.nav_logout) {
            // logout the user
            SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.clear();
            editor.apply();
            startActivity(new Intent(this, LoginActivity.class));
        } else if (id == R.id.nav_profile) {
            // start profile activity
            startActivity(new Intent(this, ProfileActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //storing token to mysql server
    private void sendTokenToServer() {
        final String token = SharedPrefManager.getInstance(this).getDeviceToken();
        // final String email = editTextEmail.getText().toString();

        if (token == null) {
            //Toast.makeText(this, "Token not generated", Toast.LENGTH_LONG).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REGISTER_DEVICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            //Toast.makeText(MainActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", mEmail);
                params.put("token", token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isLoggedIn) {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
