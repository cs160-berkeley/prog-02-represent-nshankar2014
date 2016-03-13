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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

public class MainActivity extends Activity {

    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity
    private int numCounties;
    private JSONObject votejson;
    private String countystate;
    private int obama;
    private int romney;
    private SampleGridPagerAdapter sgpa;

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

        vote(geojson);

        sgpa = new SampleGridPagerAdapter(this, getFragmentManager());
        sgpa.fill(name0, name1, name2, title0, title1, title2, countystate, obama, romney);
        pager.setAdapter(sgpa);

        DotsPageIndicator dotsPageIndicator = (DotsPageIndicator) findViewById(R.id.page_indicator);
        dotsPageIndicator.setPager(pager);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

    // read in vote json file
    private void vote(String geojson) {
        countystate = "";

        try {
            // retrieve county and state from geojson
            JSONObject jo = new JSONObject(geojson);
            JSONArray results = jo.getJSONArray("results");
            JSONObject firstDict = results.getJSONObject(0);
            JSONArray addrComp = firstDict.getJSONArray("address_components");
            for (int i = 0; i < addrComp.length(); i += 1) {
                JSONObject inner = addrComp.getJSONObject(i);
                String longname = inner.getString("long_name");
                String shortname = inner.getString("short_name");
                JSONArray types = inner.getJSONArray("types");
                if (types.get(0).equals("administrative_area_level_2")) {
                    Log.d("0000000000000000", "county: " + longname);
                    countystate += longname + ", ";
                } else if (types.get(0).equals("administrative_area_level_1")) {
                    Log.d("0000000000000000", "state: " + shortname);
                    countystate += shortname;
                    Log.d("0000000000000000", "countystate: " + countystate);
                }
            }

            // lookup obama romney stats from vote.json
            InputStream stream = getAssets().open("vote.json");
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String jsonString = new String(buffer, "UTF-8");

            votejson = new JSONObject(jsonString);
            numCounties = votejson.length();
            JSONObject or = votejson.getJSONObject(countystate);
            romney = or.getInt("romney");
            obama = or.getInt("obama");
            Log.d("0000000000000000", "obama: " + obama);
            Log.d("0000000000000000", "romney: " + romney);

        } catch (Exception e) {
            e.printStackTrace();
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

                // choose random county
                JSONArray counties = votejson.names();
                int rand = (int)(counties.length() * Math.random());
                String randCounty = "Berkeley County, WV";
                romney = 0;
                obama = 0;
                try {
                    randCounty = counties.getString(rand);
                    JSONObject or = votejson.getJSONObject(randCounty);
                    romney = or.getInt("romney");
                    obama = or.getInt("obama");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                sgpa.newVote(randCounty, obama, romney);

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















