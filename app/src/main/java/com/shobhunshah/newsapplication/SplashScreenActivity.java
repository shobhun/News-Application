package com.shobhunshah.newsapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity
{
    private TextView splashtextview ;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splashtextview = (TextView) findViewById(R.id.splashTextView);

        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Days_Later.ttf");
        splashtextview.setTypeface(type);

        getSupportActionBar().hide();

        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    sleep(3000);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    Intent secondIntent = new Intent(SplashScreenActivity.this,NewsSourcesActivity.class);
                    startActivity(secondIntent);
                    finish();
                }
            }
        };
        thread.start();
    }
}
