package com.magnusias.magnusias.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.magnusias.magnusias.utils.AppController;
import com.magnusias.magnusias.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private Spinner mSpinner;
    private EditText mFirstNameEditText, mLastNameEditTExt, mEmailEditText, mContactEditText;
    private Button mRegisterButton;
    private boolean isLoggedIn;

    private ProgressDialog pd;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mSpinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.come_across_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);

        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
        isLoggedIn = settings.getBoolean("is_logged_in", false);

        if (isLoggedIn) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        mFirstNameEditText = (EditText) findViewById(R.id.first_name_edit_text);
        mLastNameEditTExt = (EditText) findViewById(R.id.last_name_edit_text);
        mEmailEditText = (EditText) findViewById(R.id.email_edit_text);
        mContactEditText = (EditText) findViewById(R.id.contact_edit_text);
        mRegisterButton = (Button) findViewById(R.id.register_button);
        TextView mLoginTextView = (TextView) findViewById(R.id.login_text);
        pd = new ProgressDialog(this, ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.setMessage("Please Wait...");

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            String firstName, lastName, email, contact, refer;
            firstName = mFirstNameEditText.getText().toString().trim();
            lastName = mLastNameEditTExt.getText().toString().trim();
            email = mEmailEditText.getText().toString().trim();
            contact = mContactEditText.getText().toString().trim();
            refer = mSpinner.getSelectedItem().toString();

            if (!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(contact)) {
                if (hasOnlyAlphaAndWSpaces(firstName) && hasOnlyAlphaAndWSpaces(lastName) && isValidEmail(email) && isValidContact(contact)) {

                    registerUser(firstName, lastName, email, contact, refer);

                } else if (!hasOnlyAlphaAndWSpaces(firstName)) {
                    setErrorMessage(mFirstNameEditText, "Is this really your first name ?");
                } else if (!hasOnlyAlphaAndWSpaces(lastName)) {
                    setErrorMessage(mLastNameEditTExt, "Is this really your last name ?");
                } else if (!isValidEmail(email)) {
                    setErrorMessage(mEmailEditText, "Unsupported email format");
                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected error occured", Toast.LENGTH_SHORT).show();
                }
            } else {
                //check which is empty
                if (TextUtils.isEmpty(firstName)) {
                    setErrorMessage(mFirstNameEditText, "Please provide your name");
                } else if (TextUtils.isEmpty(lastName)) {
                    setErrorMessage(mLastNameEditTExt, "Please chose a username");
                } else if (TextUtils.isEmpty(email)) {
                    setErrorMessage(mEmailEditText, "Enter your email address here");
                } else if (TextUtils.isEmpty(contact)) {
                    setErrorMessage(mContactEditText, "Enter your contact number here");
                }
            }

            }
        });

        mLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start the login activity
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

    private void registerUser(final String firstName, final String lastName, final String email, final String contact, final String refer) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Registering. Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        getResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        if (error instanceof NetworkError)
                            Toast.makeText(RegisterActivity.this, "Network error, try again", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("firstname", firstName);
                map.put("lastname", lastName);
                map.put("email", email);
                map.put("contact", contact);
                map.put("refer", refer);
                return map;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        AppController.getInstance().addToRequestQueue(stringRequest, Constants.TAG_STRING_REQ);

    }

    private void getResponse(String s) {
        try {
            JSONObject mainObj = new JSONObject(s);
            int result = Integer.parseInt(mainObj.getString("code"));
            switch (result) {
                case 0:
                    Toast.makeText(this, "Error registering user. Try again", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(this, "You have been registered successfully. Check you email for further instructions.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                    break;
                case 2:
                    Toast.makeText(this, "This email is already registered. Try another.", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(this, "Unexpected error occured. Try again.", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Toast.makeText(this, "Unexpected error.Try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setErrorMessage(EditText errorEditText, String errorMessage) {
        errorEditText.setText("");
        errorEditText.setHint(errorMessage);
        errorEditText.setHintTextColor(Color.RED);
    }

    private boolean hasOnlyAlphaAndWSpaces(String s) {
        Pattern p = Pattern.compile("^[ A-z]+$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    private boolean isValidEmail(String s) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(s);
        return matcher.find();
    }

    private boolean isValidContact(String s) {
        return s.matches("[0-9]+") && s.length() == 10;
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
        isLoggedIn = settings.getBoolean("is_logged_in", false);

        if (isLoggedIn) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // do something on back.
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
