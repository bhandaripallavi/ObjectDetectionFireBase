package com.example.cheerup;

//Send user id to display notes of that id uder id created by pswd n mail

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.cheerup.LoginScreen.MyPREFERENCES;

public class NotesListDisplay extends AppCompatActivity {
    WebView wb1;
    String email, password;
    SharedPreferences prefs;
    String TAG = MainActivity.class.getSimpleName();
    ProgressDialog pDialog;
    ListView lv;
    String url = "https://letscheerup.000webhostapp.com/wp-content/themes/admiral/app_api/notes_list_display.php";
    ArrayList<HashMap<String, String>> titlelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        prefs = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        password = prefs.getString("pswdKey", "");
        email = prefs.getString("emailKey", "");



        titlelist = new ArrayList<>();
        new Title().execute();
        initcompo();
        urlcalling1();
        floatingbutton();

    }

    public void urlcalling1() {

        try {
            String postData = "password=" + URLEncoder.encode(password, "UTF-8") +
                    "&email=" + URLEncoder.encode(email, "UTF-8");
            wb1.postUrl(url, postData.getBytes());
        } catch (IOException e) {

            Toast.makeText(NotesListDisplay.this, "IO e : " + e, Toast.LENGTH_LONG).show();
        }
    }


    public void initcompo() {

        wb1 = (WebView) findViewById(R.id.webview1);
        lv = (ListView) findViewById(R.id.listview1);

        wb1.setWebViewClient(new MyWebViewClient());
        wb1.getSettings().setBuiltInZoomControls(false);
        wb1.getSettings().setSupportZoom(false);
        wb1.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wb1.getSettings().setAllowFileAccess(true);
        wb1.getSettings().setDomStorageEnabled(true);
    }

    public void floatingbutton() {
        FloatingActionButton fab = findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent i = new Intent(NotesListDisplay.this, WriteNotes.class);
                startActivity(i);
                SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                String restoredText = prefs.getString("text", null);
                if (restoredText != null) {
                    String password = prefs.getString("Password", "No password found ");
                    String email = prefs.getString("Email", "No email id found ");

                }
            }
        });
    }

    private class Title extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NotesListDisplay.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray option = jsonObj.getJSONArray("Notes");

                    for (int i = 0; i < option.length(); i++) {

                        JSONObject c = option.getJSONObject(i);
                        String title1 = c.getString("title");
                        String date1 = c.getString("date_time");
                        String content1 = c.getString("content");
                        String id1 = c.getString("id");

                        HashMap<String, String> option1 = new HashMap<>();
                        option1.put("title", title1);
                        option1.put("date", date1);
                        option1.put("content", content1);
                        option1.put("id", id1);
                        titlelist.add(option1);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            ListAdapter adapter = new SimpleAdapter(
                    NotesListDisplay.this, titlelist,
                    R.layout.notes_item, new String[]{"title", "date", "content", "id"}, new int[]{R.id.title_lv, R.id.date_lv, R.id.content_lv, R.id.id_lv
            });

            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView id_1 = (TextView) view.findViewById(R.id.id_lv);


                    String send_id = id_1.getText().toString();


                    Intent i = new Intent(NotesListDisplay.this, NotesView.class);
                    i.putExtra("id", send_id);


                    startActivity(i);
                }
            });
        }
    }
}



