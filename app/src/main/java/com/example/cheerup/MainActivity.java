package com.example.cheerup;
//Dashboard

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    TextView ref,note,con;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initCompo();
        onclick();
    }

    private void initCompo() {
        ref = (TextView)findViewById(R.id.refresh);
        note = (TextView)findViewById(R.id.notes);
        con = (TextView)findViewById(R.id.contact);

    }

    private void onclick() {
        ref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Clicked Refresh Mood ",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, ListDisplay.class);
                startActivity(i);
            }
        });

        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Clicked Notes !",Toast.LENGTH_SHORT).show();
                Intent notesi = new Intent(MainActivity.this, NotesListDisplay.class);
                startActivity(notesi);
            }
        });
        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Clicked Contact Us !",Toast.LENGTH_SHORT).show();
                Intent contacti = new Intent(MainActivity.this, ContactUs.class);
                startActivity(contacti);
            }
        });


    }
}