package com.magnusias.magnusias;

        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Bundle;
 import android.view.View;
 import android.support.v7.app.AppCompatActivity;
 import android.widget.TextView;
        import android.widget.Button;

        import com.magnusias.magnusias.activities.MainActivity;

public class NotifyMessage extends AppCompatActivity implements View.OnClickListener {
    private String title,messaged;
    private TextView txtmsg,txtmsg2;
    private Button b2;
    private static final String MY_PREFS_NAME = "Reg";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify);
        b2=(Button) findViewById(R.id.b2);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.magnus_ias);
        SharedPreferences prefs2 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        messaged = (prefs2).getString("content", "");
        title = (prefs2).getString("head", "");
        // Locate the TextView
        txtmsg = (TextView) findViewById(R.id.message);

        // Set the data into TextView
        txtmsg.setText(messaged+"");
        txtmsg2 = (TextView) findViewById(R.id.message2);

        // Set the data into TextView
        txtmsg2.setText(title+"");
        b2.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.b2:
                Intent intent = new Intent(NotifyMessage.this, MainActivity.class);
                startActivity(intent);
                break;

        }
}}