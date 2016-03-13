package com.neilshankar.prog02ww;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;
import android.widget.TextView;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

public class Splash extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "T3sbPn8vWYp7W9sfymvV6q8AM";
    private static final String TWITTER_SECRET = "A6NKwvFq1JaFSU8DofO3iXDLEUXOqwgCsWesMIa0J4j5csdc5d";

    /* code help from http://bit.ly/1PEsjPa */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_splash);

        ((TextView)findViewById(R.id.splashTitle)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/didot.ttf"));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Splash.this.startActivity(new Intent(Splash.this, EnterLocation.class));
                Splash.this.finish();
            }
        }, 5000);

    }

}
