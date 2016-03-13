// shoutout to http://bit.ly/1RVK3Fx

package com.neilshankar.prog02ww;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnApplyWindowInsetsListener;
import android.view.WindowInsets;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONObject;

import java.io.InputStream;

public class MainActivity extends Activity {

    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
        pager.setOnApplyWindowInsetsListener(new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                pager.onApplyWindowInsets(insets);
                return insets;
            }
        });

        Intent it = getIntent();
        String name0 = it.getStringExtra("name0");
        String name1 = it.getStringExtra("name1");
        String name2 = it.getStringExtra("name2");
        String title0 = it.getStringExtra("title0");
        String title1 = it.getStringExtra("title1");
        String title2 = it.getStringExtra("title2");
        String geojson = it.getStringExtra("geojson");

        SampleGridPagerAdapter sgpa = new SampleGridPagerAdapter(this, getFragmentManager());
        sgpa.fill(name0, name1, name2, title0, title1, title2);
        pager.setAdapter(sgpa);

        DotsPageIndicator dotsPageIndicator = (DotsPageIndicator) findViewById(R.id.page_indicator);
        dotsPageIndicator.setPager(pager);

        vote(geojson);
        Log.d("0000000000000000", "geojson: " + geojson);





        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

    // read in vote json file
    private void vote(String geojson) {

        // administrative_area_level_2
//        int i_admin = geojson.indexOf("\"administrative_area_level_2\":", 0);
//        int i_shortname = geojson.indexOf("\"short_name\":", 0);


        try {
            JSONObject jo = new JSONObject(geojson);
            String fo = jo.getString("results");
            Log.d("0000000000000000", "results: " + fo);
            Log.d("0000000000000000", "ac: " + new JSONObject(fo).getString("address_components"));

//            InputStream stream = getAssets().open("vote.json");
//            int size = stream.available();
//            byte[] buffer = new byte[size];
//            stream.read(buffer);
//            stream.close();
//            String jsonString = new String(buffer, "UTF-8");


            // String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&key=AIzaSyCb7REm9Yhi2lusfMLNxU3CP-c-Ori8jnk";

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("0000000000000000", "can't convert geojson to json object");
        }
    }

    // shoutout to http://bit.ly/1nmLEsN
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
//                int rand = (int)(100 * Math.random());
//                int rand2 = 100-rand;
                if ((TextView)findViewById(R.id.obama) != null) {
                    ((TextView)findViewById(R.id.obama)).setText("60%");
                    ((TextView)findViewById(R.id.romney)).setText("40%");
                } else {
                    Log.d("0000000000000000", "obama romney null");
                }

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















