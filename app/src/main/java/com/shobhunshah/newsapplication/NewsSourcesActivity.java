package com.shobhunshah.newsapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsSourcesActivity extends AppCompatActivity
{
    private ProgressDialog pd;
    private ListView lv ;
    private static String url = "https://newsapi.org/v1/sources?language=en";
    ArrayList<String> sourceList;
    private ConnectivityManager connectivityManager ;
    private SwipeRefreshLayout refreshLayout ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_sources);

        sourceList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);

        // Check network connection
        connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null)
        {
            Toast.makeText(this,"Check Internet Connection", Toast.LENGTH_SHORT).show();
        }
        else if (networkInfo.isConnected())
        {
            final GetContacts GetContacts = new GetContacts();
            GetContacts.execute();
        }

        // Set refresh layout
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_source_refreshing_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                ConnectivityManager manager = (ConnectivityManager) NewsSourcesActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo1 = manager.getActiveNetworkInfo();
                if (networkInfo1 == null)
                {
                    Toast.makeText(NewsSourcesActivity.this,"Check Internet Connection", Toast.LENGTH_SHORT).show();
                    refreshLayout.setRefreshing(false);
                }
                else if (networkInfo1.isConnected())
                {
                    GetContacts gc= new GetContacts();
                    gc.execute();
                }
            }
        });

    }
    private class GetContacts extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd = new ProgressDialog(NewsSourcesActivity.this);
            pd.setMessage("Please wait......");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);

            if (jsonStr != null)
            {
                try
                {
                    JSONObject jasonObject = new JSONObject(jsonStr);
                    JSONArray jsonArray = jasonObject.getJSONArray("sources");

                    for (int i=0 ; i<jsonArray.length() ; i++)
                    {
                        JSONObject c = jsonArray.getJSONObject(i);
                        String name = c.getString("name");
                        sourceList.add(name);
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            if (pd.isShowing())
                pd.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ArrayAdapter adapter = new ArrayAdapter(NewsSourcesActivity.this,
                    R.layout.list_single_news_sources,
                    R.id.name,sourceList);
            lv.setAdapter(adapter);
            refreshLayout.setRefreshing(false);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {
                    String name = adapterView.getItemAtPosition(i).toString();
                    Toast.makeText(NewsSourcesActivity.this,name, Toast.LENGTH_SHORT).show();

                    //Moves to Next Activity
                    Intent intentNewsItem = new Intent(NewsSourcesActivity.this,NewsItemActivity.class);
                    intentNewsItem.putExtra("SOURCENAME",name);
                    startActivity(intentNewsItem);
                }
            });

        }
    }
}
