package com.neilshankar.prog02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;

public class Splash extends AppCompatActivity {
    /* code help from http://bit.ly/1PEsjPa */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Splash.this.startActivity(new Intent(Splash.this, EnterLocation.class));
                Splash.this.finish();
            }
        }, 1000);

    }

}
