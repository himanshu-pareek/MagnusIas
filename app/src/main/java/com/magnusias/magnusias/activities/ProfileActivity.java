package com.magnusias.magnusias.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.magnusias.magnusias.R;
import com.magnusias.magnusias.utils.Constants;

public class ProfileActivity extends AppCompatActivity {

    private TextView mWelcomeMessage, mFirstName, mLastName, mUsernameTextView, mEmail, mContact;
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);

        boolean isLoggedIn = settings.getBoolean("is_logged_in", false);
        if (!isLoggedIn) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        mUsername = settings.getString("username", "");

        if (TextUtils.isEmpty(mUsername)) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        String fn = settings.getString("firstname", "");
        String ln = settings.getString("lastname", "");
        String contact = settings.getString("contact", "");
        String email = settings.getString("email", "");

        mWelcomeMessage = (TextView) findViewById(R.id.welcome_message);
        mFirstName = (TextView) findViewById(R.id.firstname_text_view);
        mLastName = (TextView) findViewById(R.id.lastname_text_view);
        mUsernameTextView = (TextView) findViewById(R.id.username_text_view);
        mEmail = (TextView) findViewById(R.id.email_id_text_view);
        mContact = (TextView) findViewById(R.id.contact_text_view);

        mWelcomeMessage.setText("Welcome, " + fn + " " + ln);
        mFirstName.setText(fn);
        mLastName.setText(ln);
        mUsernameTextView.setText(mUsername);
        mEmail.setText(email);
        mContact.setText(contact);

    }
}
