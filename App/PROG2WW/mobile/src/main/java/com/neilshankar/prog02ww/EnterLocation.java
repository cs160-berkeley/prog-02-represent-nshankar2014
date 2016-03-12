package com.neilshankar.prog02ww;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
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

                String lat;
                Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLastLocation != null) {
                    lat = ""+mLastLocation.getLatitude();
                }


                String stringUrl = "http://congress.api.sunlightfoundation.com/legislators/locate?zip=94704&apikey=d5c6792487144ad397965ccbe5cb713d";
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    new DownloadWebpageTask().execute(stringUrl);
                } else {
                    ((EditText) findViewById(R.id.zip_input)).setText("000000");
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


    // shoutout to http://bit.ly/1XlKtX1
    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.d("0000000000000000", "async results: " + result);
        }
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    private String downloadUrl(String myurl) throws IOException {

        InputStream is = null;
        int len = 500; // Only display the first 500 characters of the retrieved web page content.

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("0000000000000000", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            Log.d("0000000000000000", "contentAsString: " + contentAsString);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

}