package com.magnusias.magnusias.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.magnusias.magnusias.R;
import com.magnusias.magnusias.utils.AppController;
import com.magnusias.magnusias.utils.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText mEmailEditText;
    private Button mSendEmailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mEmailEditText = (EditText) findViewById(R.id.forgot_password_email_edit_text);
        mSendEmailButton = (Button) findViewById(R.id.send_email_button);

        mSendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailEditText.getText().toString().trim();
                if (isValidEmail(email)) {
                    sendEmail(email);
                } else {
                    Toast.makeText(getApplicationContext(), "Email is not valid", Toast.LENGTH_SHORT).show();
                }
            }
        });

        (findViewById(R.id.fp_login_text_view)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

    private void sendEmail(final String email) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Processing. Please wait...");
        pd.setCancelable(false);
        pd.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.FORGOT_PASS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.hide();
                        if (response.equals("1")) {
                            Toast.makeText(ForgotPasswordActivity.this, "Email has been sent to the given email address. Check you inbox for further instructions.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        } else {
                            Toast.makeText(ForgotPasswordActivity.this, "Error sending email. Try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ForgotPasswordActivity.this, "An error occured, try again", Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("email", email);
                return map;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private boolean isValidEmail(String s) {
        if (TextUtils.isEmpty(s)) return false;
        Matcher matcher = Constants.VALID_EMAIL_ADDRESS_REGEX .matcher(s);
        return matcher.find();
    }
}
