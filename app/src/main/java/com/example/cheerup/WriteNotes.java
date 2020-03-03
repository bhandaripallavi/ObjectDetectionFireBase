package com.example.cheerup;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URLEncoder;

public class WriteNotes extends AppCompatActivity {
    EditText edTitle, edContent;
    WebView wb1;
    Button butS, butC;
    TextView tv_notes, date;
    String url, email, password;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_notes);

        initCompo();
        listners();

        prefs = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        password = prefs.getString("pswdKey", "");
        email = prefs.getString("emailKey", "");
    }


    private void listners() {

        butS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String titleurl = edTitle.getText().toString();

                String contenturl = edContent.getText().toString();

                // url = "https://letscheerup.000webhostapp.com/wp-content/themes/admiral/app_api/ins_notes.php?password='" + password + "'&title='" + titleurl + "'&content='" + contenturl + "'&email='" + email + "'";

                url = "https://letscheerup.000webhostapp.com/wp-content/themes/admiral/app_api/ins_notes.php";
                try {
                    String postData = "password=" + URLEncoder.encode(password, "UTF-8") +
                            "&title=" + URLEncoder.encode(titleurl, "UTF-8") +
                            "&content=" + URLEncoder.encode(contenturl, "UTF-8")+
                            "&email=" + URLEncoder.encode(email, "UTF-8");
                    wb1.postUrl(url, postData.getBytes());
                } catch (IOException e) {

                    Toast.makeText(WriteNotes.this, "IO e : " + e, Toast.LENGTH_LONG).show();
                }
            }
        });
        butC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    public void initCompo() {

        edContent = (EditText) findViewById(R.id.ed_content);
        date = (TextView) findViewById(R.id.date_tv);
        edTitle = (EditText) findViewById(R.id.ed_title);
        wb1 = (WebView) findViewById(R.id.wb);
        butC = (Button) findViewById(R.id.button_cancel);
        butS = (Button) findViewById(R.id.button_save1);
        tv_notes = (TextView) findViewById(R.id.tvnotes);

        wb1.setWebViewClient(new MyWebViewClient());
        wb1.getSettings().setBuiltInZoomControls(false);
        wb1.getSettings().setSupportZoom(false);
        wb1.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wb1.getSettings().setAllowFileAccess(true);
        wb1.getSettings().setDomStorageEnabled(true);

    }

}


