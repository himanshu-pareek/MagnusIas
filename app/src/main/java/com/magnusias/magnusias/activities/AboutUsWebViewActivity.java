package com.magnusias.magnusias.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.magnusias.magnusias.R;
import com.magnusias.magnusias.utils.Constants;

public class AboutUsWebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_about_us_web_view );
        // Makes Progress bar Visible
        getWindow().setFeatureInt( Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);

        final WebView webview = (WebView) findViewById(R.id.about_us_web_view);
        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                //Make the bar disappear after URL is loaded, and changes string to Loading...
                setTitle("Loading...");
                setProgress(progress * 100); //Make the bar disappear after URL is loaded

                // Return the app name after finish loading
                if(progress == 100)
                    setTitle(R.string.app_name);
            }
        });

        Intent i = getIntent();
        final String url = i.getStringExtra(Constants.ABOUT_US_URL_STRING);

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String newurl) {
                if (url == newurl) {
                    webview.loadUrl(newurl);
                }
                return true;
            }
        });

        webview.loadUrl(url);

        //setContentView(R.layout.activity_about_us_web_view);

    }
}
