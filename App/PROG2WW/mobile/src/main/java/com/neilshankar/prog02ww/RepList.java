package com.neilshankar.prog02ww;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class RepList extends AppCompatActivity {

    private Intent it;
    private String zip;
    private String lat;
    private String lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rep_list);

        it = getIntent();
        zip = it.getStringExtra("zip");
        lat = it.getStringExtra("lat");
        lon = it.getStringExtra("lon");
        Log.d("0000000000000000", "zip: " + zip);
        Log.d("0000000000000000", "lat: " + lat);
        Log.d("0000000000000000", "lon: " + lon);

        attachClickListeners();
        fetchData();
    }

    // attach a click listener to each ImageButton and Button
    private void attachClickListeners() {
        LinearLayout full = (LinearLayout) findViewById(R.id.full);
        for (int i = 0; i < full.getChildCount(); i += 1) {
            View v = full.getChildAt(i);
            if (v instanceof LinearLayout) {
                LinearLayout card = (LinearLayout) v;
                final String tag = "" + card.getTag();
                for (int j = 0; j < card.getChildCount(); j += 1) {
                    View a = card.getChildAt(j);
                    if (a instanceof ImageButton) {
                        final ImageButton ib = (ImageButton) a;
                        ib.setClickable(true);
                        ib.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View clicked) {
                                Intent it = new Intent(RepList.this, RepDetail.class);
                                it.putExtra("face", tag);
                                it.putExtra("zip", it.getStringExtra("zip"));
                                RepList.this.startActivity(it);
                            }
                        });
                    } else if (a instanceof LinearLayout) {
                        LinearLayout inner = (LinearLayout) a;
                        for (int x = 0; x < inner.getChildCount(); x += 1) {
                            View cat = inner.getChildAt(x);
                            if (cat instanceof Button) {
                                final Button b = (Button) cat;
                                b.setClickable(true);
                                b.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View clicked) {
                                        Intent it = new Intent(RepList.this, RepDetail.class);
                                        it.putExtra("face", tag);
                                        it.putExtra("zip", it.getStringExtra("zip"));
                                        RepList.this.startActivity(it);
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }
    }

    // construct HTTP GET query with either (zip) or (lat and lon)
    private void fetchData() {
        String stringUrl = "http://congress.api.sunlightfoundation.com/legislators/locate?";
        if (zip.equals("")) {
            stringUrl += "zip=" + zip;
        } else {
            stringUrl += "latitude=" + lat + "&longitude=" + lon;
        }
        stringUrl += "&apikey=d5c6792487144ad397965ccbe5cb713d";
        Log.d("0000000000000000", "stringUrl: " + stringUrl);

        // make the GET request
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute(stringUrl);
        } else {
            Log.d("0000000000000000", "network contectivity issues");
        }
    }

    // shoutout to http://bit.ly/1XlKtX1
    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string.
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

        // onPostExecute calls fillLayout(), which fills the UI with the json info
        @Override
        protected void onPostExecute(String result) {
            fillLayout(result);
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
            conn.connect();
            //Log.d("0000000000000000", "The response is: " + conn.getResponseCode());

            is = conn.getInputStream();
            String contentAsString = readIt(is, len);
            Log.d("0000000000000000", "contentAsString: " + contentAsString);
            return contentAsString;

        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException {
        Reader reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    // fill layout with json info
    private void fillLayout(String result) {
        if (zip.equals("94704")) {
            ((ImageButton)findViewById(R.id.img1)).setImageResource(R.drawable.face1);
            ((ImageButton)findViewById(R.id.img2)).setImageResource(R.drawable.face2);
            ((ImageButton)findViewById(R.id.img3)).setImageResource(R.drawable.face3);
            ((LinearLayout)findViewById(R.id.card1)).setTag("face1");
            ((LinearLayout)findViewById(R.id.card2)).setTag("face2");
            ((LinearLayout)findViewById(R.id.card3)).setTag("face3");
            ((TextView)findViewById(R.id.name1)).setText("Brad Rigo (R)");
            ((TextView)findViewById(R.id.name2)).setText("Pat Toomey (R)");
            ((TextView)findViewById(R.id.name3)).setText("Chad McGraw (D)");
        }
    }

}
