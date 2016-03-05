package com.neilshankar.prog02ww;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.GridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RepList extends Activity {

    private TextView mTextView;
    private Button mFeedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rep_list);

//        final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
//        pager.setAdapter(new SampleGridPagerAdapter(this, getFragmentManager()));

        Intent intent = getIntent();
        final String zip = intent.getStringExtra("zip");

        if (zip.equals("94704")) {
            ((Button)findViewById(R.id.name1)).setText("Brad Rigo (R)");
            ((Button)findViewById(R.id.name2)).setText("Pat Toomey (R)");
            ((Button)findViewById(R.id.name3)).setText("Chad McGraw (D)");
        } else if (zip.equals("19403")) {
            ((Button) findViewById(R.id.name1)).setText("Hunter Wayans (D)");
            ((Button) findViewById(R.id.name2)).setText("Rain Bust (R)");
            ((Button) findViewById(R.id.name3)).setText("Cooper Lighthook (D)");
        } else {
            ((Button)findViewById(R.id.name1)).setText("Buster White (D)");
            ((Button)findViewById(R.id.name2)).setText("Nate Cream (R)");
            ((Button)findViewById(R.id.name3)).setText("Andrew Carmichael (D)");
        }

        ((Button)findViewById(R.id.vote)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View clicked) {
                Intent it = new Intent(RepList.this, Vote.class);
                it.putExtra("zip", zip);
                RepList.this.startActivity(it);
            }
        });

        findViewById(R.id.name1).setClickable(true);
        findViewById(R.id.name2).setClickable(true);
        findViewById(R.id.name3).setClickable(true);

        ((Button)findViewById(R.id.name1)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View clicked) {
                Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
                sendIntent.putExtra("face", "face1");
                startService(sendIntent);
            }
        });
        ((Button)findViewById(R.id.name1)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View clicked) {
                Intent sendIntent2 = new Intent(getBaseContext(), WatchToPhoneService.class);
                sendIntent2.putExtra("face", "face2");
                startService(sendIntent2);
            }
        });
        ((Button)findViewById(R.id.name1)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View clicked) {
                Intent sendIntent3 = new Intent(getBaseContext(), WatchToPhoneService.class);
                sendIntent3.putExtra("face", "face3");
                startService(sendIntent3);
            }
        });


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

    // shoutout to http://bit.ly/1nmLEsN
    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter
            if (mAccel > 100 && Math.abs(delta) > 20) {
//                Intent it = new Intent(RepList.this, RepList.class);
//                it.putExtra("zip", "10000");
//                RepList.this.startActivity(it);

                ((TextView)findViewById(R.id.name1)).setText("Kramer Bucks (D)");
                ((TextView)findViewById(R.id.name2)).setText("Nancy Pack (R)");
                ((TextView)findViewById(R.id.name3)).setText("Kyle Pillow (D)");
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }


}

