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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;


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
            Intent it = new Intent(EnterLocation.this, RepList.class);
            Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);

            // get location
            if (getApplicationContext().checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == PackageManager.PERMISSION_GRANTED) {
                double lat = 0;
                double lon = 0;
                Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLastLocation != null) {
                    lat = mLastLocation.getLatitude();
                    lon = mLastLocation.getLongitude();
                } else {
                    lat = 500;
                    lon = 500;
                }
                it.putExtra("zip", "");
                it.putExtra("lat", ""+lat);
                it.putExtra("lon", ""+lon);
                sendIntent.putExtra("zip", "");
                sendIntent.putExtra("lat", ""+lat);
                sendIntent.putExtra("lon", ""+lon);
            } else {
                // fine location permission disabled... force 94704
                it.putExtra("zip", "94704");
                it.putExtra("lat", "");
                it.putExtra("lon", "");
                sendIntent.putExtra("zip", "94704");
                sendIntent.putExtra("lat", "");
                sendIntent.putExtra("lon", "");
            }

            EnterLocation.this.startActivity(it);
            startService(sendIntent);
            }
        });

        Button enter = (Button)findViewById(R.id.enter);
        enter.setClickable(true);
        enter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View clicked) {
                Intent it = new Intent(EnterLocation.this, RepList.class);
                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                String zip = "" + ((EditText) findViewById(R.id.zip_input)).getText();
                it.putExtra("zip", zip);
                it.putExtra("lat", "");
                it.putExtra("lon", "");
                sendIntent.putExtra("zip", zip);
                sendIntent.putExtra("lat", "");
                sendIntent.putExtra("lon", "");
                EnterLocation.this.startActivity(it);
                startService(sendIntent);
            }
        });
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















