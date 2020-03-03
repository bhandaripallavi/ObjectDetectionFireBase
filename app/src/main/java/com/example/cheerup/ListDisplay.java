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

public class ListDisplay extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;

    private static String url = "https://letscheerup.000webhostapp.com//wp-content/themes/admiral/app_api/getpostlist.php";

    ArrayList<HashMap<String, String>> categorylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_display);
        Intent intent1 = getIntent();
        categorylist = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        new Categoty().execute();

    }
    private class Categoty extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ListDisplay.this);
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

                    JSONArray option = jsonObj.getJSONArray("Categories");

                    for (int i = 0; i < option.length(); i++) {
                        JSONObject c = option.getJSONObject(i);
                        String id = c.getString("term_id");
                        String cat = c.getString("name");
                        HashMap<String, String> option1 = new HashMap<>();

                        if(!cat.equals("Uncategorised")){
                            option1.put("term_id",id);
                            option1.put("name", cat);
                        }

                        categorylist.add(option1);
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
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    ListDisplay.this, categorylist,
                    R.layout.list_item, new String[]{"term_id", "name"}, new int[]{R.id.title_lv,R.id.date_lv
                    });

            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView cat1 = (TextView) view.findViewById(R.id.title_lv);
                    TextView cat2 = (TextView) view.findViewById(R.id.date_lv);


                    String cat_sendid = cat1.getText().toString();
                    String cat_send = cat2.getText().toString();

                    Intent i = new Intent(ListDisplay.this, SingleActivity.class);
                    i.putExtra("term_id1", cat_sendid);
                    i.putExtra("name1", cat_send);

                    startActivity(i);
                }
            });
        }}}



