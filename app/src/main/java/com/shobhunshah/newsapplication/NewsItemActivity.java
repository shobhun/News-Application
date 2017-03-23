package com.shobhunshah.newsapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.shobhunshah.newsapplication.NewsItems.ItemDisplayedOnCard;
import com.shobhunshah.newsapplication.mRecyclerView.MyAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsItemActivity extends AppCompatActivity
{
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    private String sourceName ;
    private SwipeRefreshLayout refreshLayout ;
    private ConnectivityManager connectivityManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_item);

        sourceName = getIntent().getStringExtra("SOURCENAME");
        getSupportActionBar().setTitle(sourceName);

        sourceName = sourceName.replaceAll(" ", "-");
        sourceName = sourceName.replace("/", "-");
        sourceName = sourceName.replace("(", "");
        sourceName = sourceName.replace(")", "");
        sourceName = sourceName.replace("--", "-");

        // checks network connection
        connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null)
        {
            Toast.makeText(this,"Check Internet Connection", Toast.LENGTH_SHORT).show();
        }
        else if (networkInfo.isConnected())
        {
            //Make call to AsyncTask
            new AsyncLogin().execute();
        }

        // handles refresh layout
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_item_refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                ConnectivityManager manager = (ConnectivityManager) NewsItemActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo1 = manager.getActiveNetworkInfo();
                if (networkInfo1 == null)
                {
                    Toast.makeText(NewsItemActivity.this,"Check Internet Connection", Toast.LENGTH_SHORT).show();
                    refreshLayout.setRefreshing(false);
                }
                else if (networkInfo1.isConnected())
                {
                    //Make call to AsyncTask
                    new AsyncLogin().execute();
                }
            }
        });
    }

    private class AsyncLogin extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(NewsItemActivity.this);
        String url = null;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params)
        {
            url= "https://newsapi.org/v1/articles?source=" +
            sourceName.toLowerCase() +
            "&apiKey=4f4d46cd6fcd4de0b6cd1316a1bdf080";

            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String result)
        {

            //this method will be running on UI thread

            pdLoading.dismiss();
            List<ItemDisplayedOnCard> data = new ArrayList<>();

            pdLoading.dismiss();
            try
            {

                JSONObject jsonObject = new JSONObject(result);
                JSONArray jArray = jsonObject.getJSONArray("articles");
                // Extract data from json and store into ArrayList as class objects
                for (int i = 0; i < jArray.length(); i++)
                {
                    JSONObject json_data = jArray.getJSONObject(i);
                    ItemDisplayedOnCard itemOnCard = new ItemDisplayedOnCard();
                    itemOnCard.author = json_data.getString("author");
                    itemOnCard.title = json_data.getString("title");
                    itemOnCard.description = json_data.getString("description");
                    itemOnCard.url = json_data.getString("url");
                    itemOnCard.urlToImage = json_data.getString("urlToImage");
                    data.add(itemOnCard);
                }

                // Setup and Handover data to recyclerview
                recyclerView = (RecyclerView) findViewById(R.id.recyclerViewNewsItem);
                myAdapter = new MyAdapter(NewsItemActivity.this, data);
                recyclerView.setAdapter(myAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(NewsItemActivity.this));
                refreshLayout.setRefreshing(false);

            } catch (JSONException e)
            {
                Toast.makeText(NewsItemActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }
    }
}