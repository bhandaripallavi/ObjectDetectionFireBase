package com.example.cheerup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class NotesView extends AppCompatActivity {
    WebView edit_wb1;
    TextView id_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_view);
        initcompo();
        Intent i = getIntent();
        String getId = i.getStringExtra("id");
        String UpdatePostId = getId;
        edit_wb1.loadUrl("http://letscheerup.000webhostapp.com/wp-content/themes/admiral/app_api/update_notes.php?note_id="+ UpdatePostId);


    }


    private void initcompo() {
        edit_wb1 = (WebView)findViewById(R.id.edit_wb);
        id_tv = (TextView) findViewById(R.id.id_tv);
    }

}
