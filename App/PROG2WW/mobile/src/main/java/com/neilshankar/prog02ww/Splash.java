package com.neilshankar.prog02ww;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;
import android.widget.TextView;

public class Splash extends AppCompatActivity {
    /* code help from http://bit.ly/1PEsjPa */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ((TextView)findViewById(R.id.splashTitle)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/didot.ttf"));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Splash.this.startActivity(new Intent(Splash.this, EnterLocation.class));
                Splash.this.finish();
            }
        }, 500);

    }

}
