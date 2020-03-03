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

public class SingleActivity extends AppCompatActivity {

    String url;
    ArrayList<HashMap<String, String>> subcategorylist;
    private String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_display2);
        Intent i = getIntent();
        String getId = i.getStringExtra("term_id1");
        String getName = i.getStringExtra("name1");

        String getMainCatId = getId;

        Toast.makeText(SingleActivity.this, "ID : " + getId, Toast.LENGTH_SHORT).show();
        url = "https://letscheerup.000webhostapp.com//wp-content/themes/admiral/app_api/getpostlist.php?cat_id=" + getMainCatId;

        subcategorylist = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        new Subcategory().execute();

    }

    private class Subcategory extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SingleActivity.this);
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

                    JSONArray option = jsonObj.getJSONArray("SubCategories");

                    for (int i = 0; i < option.length(); i++) {
                        JSONObject c = option.getJSONObject(i);

                        String subcat = c.getString("name");
                        String idsubcat = c.getString("term_id");


                        HashMap<String, String> option1 = new HashMap<>();

                        option1.put("name", subcat);
                        option1.put("term_id", idsubcat);


                        subcategorylist.add(option1);
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
                    SingleActivity.this, subcategorylist,
                    R.layout.list_item, new String[]{"name", "term_id"}, new int[]{R.id.date_lv, R.id.title_lv});

            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView subcatid = (TextView) view.findViewById(R.id.title_lv);
                    TextView subcatname = (TextView) view.findViewById(R.id.date_lv);

                    String sedsubcatid = subcatid.getText().toString();

                    Intent i = new Intent(SingleActivity.this, TitleDisplay.class);
                    i.putExtra("subcategoryid", sedsubcatid);
                    startActivity(i);
                }
            });
        }
    }
}



