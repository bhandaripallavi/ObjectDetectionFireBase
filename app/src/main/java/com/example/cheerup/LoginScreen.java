package com.example.cheerup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginScreen extends AppCompatActivity {
    Button b2;
    EditText edemail, edphone,edpswd,edname;
    TextView tx1;
    int counter = 3;
    WebView wb1;
    String url,email;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    public static final String Phone = "phoneKey";
    public static final String Email = "emailKey";
    public static final String Password = "pswdKey";

    SharedPreferences sharedpreferences;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        prefs = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        email = prefs.getString("emailKey", "");

        if(!email.equals(""))
        {
            Toast.makeText(LoginScreen.this,"You are already a member ! ",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(LoginScreen.this, MainActivity.class);
            startActivity(i);
        }
        initCompo();
        listners();

    }
    private void listners() {

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameurl = edname.getText().toString();
                String phoneurl = edphone.getText().toString();
                String emailurl = edemail.getText().toString();
                String passwordurl = edpswd.getText().toString();
                url = "https://letscheerup.000webhostapp.com/wp-content/themes/admiral/app_api/ins_retrive.php?uname='"+nameurl+"'&umobile='"+phoneurl+"'&uemail='"+emailurl+"'&upassword='"+passwordurl+"'";
                wb1.loadUrl(url);
                SharedPreferences.Editor editor = sharedpreferences.edit();

                String n  = edname.getText().toString();
                String ph  = edphone.getText().toString();
                String e  = edemail.getText().toString();
                String psw  = edpswd.getText().toString();

                editor.putString(Name, n);
                editor.putString(Phone, ph);
                editor.putString(Email, e);
                editor.putString(Password, psw);
                editor.commit();
                Toast.makeText(LoginScreen.this,"Thanks",Toast.LENGTH_LONG).show();
                Intent i = new Intent(LoginScreen.this, MainActivity.class);
                startActivity(i);
            }
        });


    }


    public void initCompo() {

        edname = (EditText) findViewById(R.id.ed_title);
        edemail = (EditText) findViewById(R.id.ed_email);
        edphone = (EditText) findViewById(R.id.ed_phone);
        edpswd = (EditText) findViewById(R.id.ed_pswd);
        wb1= (WebView) findViewById(R.id.wb);
        b2 = (Button) findViewById(R.id.button_cancel);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        wb1.setWebViewClient(new MyWebViewClient());
        wb1.getSettings().setBuiltInZoomControls(false);
        wb1.getSettings().setSupportZoom(false);
        wb1.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wb1.getSettings().setAllowFileAccess(true);
        wb1.getSettings().setDomStorageEnabled(true);

    }

}
