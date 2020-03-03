package com.example.cheerup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TitleDisplay extends AppCompatActivity {
    String url;
    ArrayList<HashMap<String, String>> titlelist;
    private String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_display);
        Intent i = getIntent();
        String getId = i.getStringExtra("subcategoryid");
        String getSubCatId = getId;
        Toast.makeText(TitleDisplay.this, "ID : " + getId, Toast.LENGTH_SHORT).show();
        url = "https://letscheerup.000webhostapp.com//wp-content/themes/admiral/app_api/gettitle.php?subcat_id=" + getSubCatId;
        titlelist = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        new TitleDisplay.Title().execute();
    }

    private class Title extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TitleDisplay.this);
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

                    JSONArray option = jsonObj.getJSONArray("Posts");

                    for (int i = 0; i < option.length(); i++) {
                        JSONObject c = option.getJSONObject(i);

                        String p_id = c.getString("ID");
                        String p_tit = c.getString("post_title");
                        String p_date = c.getString("post_date");


                        HashMap<String, String> title1 = new HashMap<>();

                        title1.put("ID", p_id);
                        title1.put("post_title", p_tit);
                        title1.put("post_date", p_date);


                        titlelist.add(title1);
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
                    TitleDisplay.this, titlelist,
                    R.layout.title_item, new String[]{"ID", "post_title", "post_date"}, new int[]{R.id.idpost, R.id.title, R.id.date});

            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView postid = (TextView) view.findViewById(R.id.idpost);
                    TextView posttitle = (TextView) view.findViewById(R.id.title);
                    TextView postdate = (TextView) view.findViewById(R.id.date);



                    String post_send_id = postid.getText().toString();
                    String post_send_title = posttitle.getText().toString();
                    String post_send_date = postdate.getText().toString();


                    Intent i = new Intent(TitleDisplay.this, PostDisplay.class);

                    i.putExtra("pid", post_send_id);
                    i.putExtra("ptitle", post_send_title);
                    i.putExtra("pdate", post_send_date);
                    startActivity(i);
                }
            });
        }
    }
}
