package com.example.cheerup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.Toast;


import java.util.HashMap;


public class PostDisplay extends AppCompatActivity {
    String url,text,content;
    WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_display);

        Intent i = getIntent();
        String getpostId = i.getStringExtra("pid");
        String postidselector = getpostId;

        url = "https://letscheerup.000webhostapp.com//wp-content/themes/admiral/app_api/getpostcontent.php?post_id=" + postidselector;
        Toast.makeText(PostDisplay.this, "ID is : " + postidselector, Toast.LENGTH_SHORT).show();

        initcompo();
        setData();


    }

    void initcompo() {


        webView = (WebView) findViewById(R.id.webview_content);

    }


    void setData() {

        webView.getSettings().setJavaScriptEnabled(true);
//        text = "<html><body style=\"text-align:justify\">" + content.replace("\n", "<p style='text-align: justify;'>") + "</body></Html>";
//        webView.loadData(text.replace("<img ","<img style='width:100% !important; height: auto !important;'"), "text/html; charset=utf-8", "utf-8");
        webView.loadUrl(url);


    }


}
