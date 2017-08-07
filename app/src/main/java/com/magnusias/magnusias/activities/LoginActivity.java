package com.magnusias.magnusias.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.magnusias.magnusias.R;
import com.magnusias.magnusias.models.User;
import com.magnusias.magnusias.utils.AppController;
import com.magnusias.magnusias.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Button mLoginButton;
    private EditText mUsernameOrEmailEditText, mPasswordEditText;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginButton = (Button) findViewById(R.id.login_button);
        TextView mForgotPasswordTextView = (TextView) findViewById(R.id.forgot_password_text);
        TextView mRegisterTextView = (TextView) findViewById(R.id.register_text);
        mUsernameOrEmailEditText = (EditText) findViewById(R.id.username_email_edit_text);
        mPasswordEditText = (EditText) findViewById(R.id.password_edit_text);
        pd = new ProgressDialog(this, ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.setMessage("Please Wait...");

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usernameOrEmail = mUsernameOrEmailEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString();
                //Toast.makeText(getApplicationContext(), "Username or email = " + usernameOrEmail + "\nPassword = " + password, Toast.LENGTH_SHORT).show();
                if (!TextUtils.isEmpty(usernameOrEmail) && !TextUtils.isEmpty(password)) {
                    loginUser(usernameOrEmail, password);
                } else if (TextUtils.isEmpty(usernameOrEmail)) {
                    mUsernameOrEmailEditText.setText("");
                    mUsernameOrEmailEditText.setHint("Username or email must not be empty");
                    mUsernameOrEmailEditText.setHintTextColor(Color.RED);
                } else {
                    mPasswordEditText.setHint("Password must not be empty");
                    mPasswordEditText.setHintTextColor(Color.RED);
                }

            }
        });

        mRegisterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        mForgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
            }
        });

    }

    private void loginUser(final String usernameOrEmail, final String password) {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Authenticating. Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        getUser(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        if (error instanceof NetworkError)
                            Toast.makeText(LoginActivity.this, "Network error, try again", Toast.LENGTH_SHORT).show();
                        else if (error instanceof ServerError)
                            Toast.makeText(getApplicationContext(), "Server error, try again", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_email", usernameOrEmail);
                map.put("password", password);
                return map;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        AppController.getInstance().addToRequestQueue(stringRequest, Constants.TAG_STRING_REQ);

    }

    private void getUser(String jsonString) {
        String code;

        if (!TextUtils.isEmpty(jsonString)) {
            try {
                JSONObject baseJsonResponse = new JSONObject(jsonString);
                code = baseJsonResponse.getString("code");

                switch (code) {
                    case "1":

                        JSONObject userJSON = baseJsonResponse.getJSONObject("user");
                        String firstname, lastname, username, email, contact;
                        firstname = userJSON.getString("firstname");
                        lastname = userJSON.getString("lastname");
                        username = userJSON.getString("username");
                        email = userJSON.getString("email");
                        contact = userJSON.getString("contact");
                        int id = userJSON.getInt("id");
                        User user = new User(firstname, lastname, username, email, contact);
                        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean("is_logged_in", true);
                        editor.putInt(Constants.USER_ID, id);
                        editor.putString("firstname", user.getFirstName());
                        editor.putString("lastname", user.getLastName());
                        editor.putString("username", user.getUsername());
                        editor.putString("email", user.getEmail());
                        editor.putString("contact", user.getContact());
                        editor.apply();
                        Intent myIntent = new Intent(this, MainActivity.class);
                        startActivity(myIntent);

                        break;
                    case "0":
                        Toast.makeText(this, "Can not login at the moment, try later", Toast.LENGTH_SHORT).show();
                        break;
                    case "2":
                        Toast.makeText(this, "Incorrect username/email or/and password", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(this, "Unexpected error occured. Try again", Toast.LENGTH_SHORT).show();
                        break;
                }

            } catch (JSONException e) {
                Log.e("QueryUtils", "Problem parsing the JSON results", e);
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // do something on back.
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}

