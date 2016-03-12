package com.neilshankar.prog02ww;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EnterLocation extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_location);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Wearable.API)  // used for data layer API
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        Button use_my_location = (Button)findViewById(R.id.use_my_location);
        use_my_location.setClickable(true);
        use_my_location.setOnClickListener(new View.OnClickListener() {
            public void onClick(View clicked) {

                // get location
                if (getApplicationContext().checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == PackageManager.PERMISSION_GRANTED) {
                    Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    if (mLastLocation != null) {
                        String x = ""+mLastLocation.getLatitude();
                        ((EditText) findViewById(R.id.zip_input)).setText(x);
                    }

                    try {
                        Log.d("0000000000000000", "http get results " + getInternetData());
                    } catch (Exception e) {
                        Log.d("0000000000000000", "http get results: exception");
                    }


                } else {
                    // fine location permission disabled... force 94704
                    Intent it = new Intent(EnterLocation.this, RepList.class);
                    it.putExtra("zip", "94704");
                    EnterLocation.this.startActivity(it);

                    Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                    sendIntent.putExtra("zip", "94704");
                    startService(sendIntent);
                }

            }
        });

        Button enter = (Button)findViewById(R.id.enter);
        enter.setClickable(true);
        enter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View clicked) {
                Intent it = new Intent(EnterLocation.this, RepList.class);
                it.putExtra("zip", "" + ((EditText) findViewById(R.id.zip_input)).getText());
                EnterLocation.this.startActivity(it);

                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                sendIntent.putExtra("zip", "" + ((EditText) findViewById(R.id.zip_input)).getText());
                startService(sendIntent);
            }
        });
    }

    public String getInternetData() throws Exception {

        URL url = new URL("http://congress.api.sunlightfoundation.com/legislators/locate?zip=94704&apikey=d5c6792487144ad397965ccbe5cb713d");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        BufferedReader in = null;
        String data = "";

        try {
            // InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            // readStream(in);

            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuffer sb = new StringBuffer("");
            String l = "";
            String nl = System.getProperty("line.separator");
            while ((l = in.readLine()) != null) {
                sb.append(l + nl);
            }
            in.close();
            urlConnection.disconnect();
            data = sb.toString();
            Log.d("0000000000000000", "data: " + data);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
//            urlConnection.disconnect();
//            if (in != null) {
//                try {
//                    in.close();
//                    return data;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else {
//                Log.d("0000000000000000", "in == null");
//            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {}

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connResult) {}

}
