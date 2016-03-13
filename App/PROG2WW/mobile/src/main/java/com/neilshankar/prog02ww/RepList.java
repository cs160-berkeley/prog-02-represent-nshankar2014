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

import java.io.BufferedReader;
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
            stringUrl += "latitude=" + lat + "&longitude=" + lon;
        } else {
            stringUrl += "zip=" + zip;
        }
        stringUrl += "&apikey=d5c6792487144ad397965ccbe5cb713d";
        Log.d("0000000000000000", "stringUrl: " + stringUrl);

        // make the GET request
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute(stringUrl); // this is the magic line that makes the request happen
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

    // shoutout to http://bit.ly/1XlKtX1
    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            // Log.d("0000000000000000", "The response is: " + conn.getResponseCode());

            // read in the HTTP response
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
            Log.d("0000000000000000", "contentAsString: " + stringBuilder.toString());
            return stringBuilder.toString(); // return as string
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    // fill layout with json info
    private void fillLayout(String s) {
        int countIndex = s.indexOf("\"count\":");
        int commaIndex = s.indexOf(",", countIndex);
        int count = Integer.parseInt(s.substring(countIndex + 8, commaIndex));
        Log.d("0000000000000000", "count:" + count);

        // parse results string for certain keywords
        int i_fname = -1;
        int i_surname = -1;
        int i_party = -1;
        int i_title = -1;
        int i_email = -1;
        int i_website = -1;
        String[] fname = new String[3];
        String[] surname = new String[3];
        String[] party = new String[3];
        String[] title = new String[3];
        String[] email = new String[3];
        String[] website = new String[3];

        for (int i = 0; i < 3; i += 1) {
            i_fname = s.indexOf("\"first_name\":", i_fname + 1);
            i_surname = s.indexOf("\"last_name\":", i_surname + 1);
            i_party = s.indexOf("\"party\":", i_party + 1);
            i_title = s.indexOf("\"title\":", i_title + 1);
            i_email = s.indexOf("\"oc_email\":", i_email + 1);
            i_website = s.indexOf("\"website\":", i_website + 1);

            fname[i] = stripQuotes(s.substring(i_fname + 13, s.indexOf(",", i_fname)));
            surname[i] = stripQuotes(s.substring(i_surname + 12, s.indexOf(",", i_surname)));
            party[i] = stripQuotes(s.substring(i_party + 8, s.indexOf(",", i_party)));
            title[i] = stripQuotes(s.substring(i_title + 8, s.indexOf(",", i_title)));
            email[i] = stripQuotes(s.substring(i_email + 11, s.indexOf(",", i_email)));
            website[i] = stripQuotes(s.substring(i_website + 10, s.indexOf(",", i_website)));

            if (title[i].equals("Rep")) {
                title[i] = "House of Representatives";
            } else {
                title[i] = "Senate";
            }
        }

        // set text of xml elements in the UI
        ((TextView)findViewById(R.id.name0)).setText(fname[0] + " " + surname[0] + " (" + party[0] + ")");
        ((TextView)findViewById(R.id.name1)).setText(fname[1] + " " + surname[1] + " (" + party[1] + ")");
        ((TextView)findViewById(R.id.name2)).setText(fname[2] + " " + surname[2] + " (" + party[2] + ")");
        ((TextView)findViewById(R.id.title0)).setText(title[0]);
        ((TextView)findViewById(R.id.title1)).setText(title[1]);
        ((TextView)findViewById(R.id.title2)).setText(title[2]);
        ((TextView)findViewById(R.id.email0)).setText("Email: " + email[0]);
        ((TextView)findViewById(R.id.email1)).setText("Email: " + email[1]);
        ((TextView)findViewById(R.id.email2)).setText("Email: " + email[2]);
        ((TextView)findViewById(R.id.website0)).setText("Website: " + website[0]);
        ((TextView)findViewById(R.id.website1)).setText("Website: " + website[1]);
        ((TextView)findViewById(R.id.website2)).setText("Website: " + website[2]);


//        if (zip.equals("94704")) {
//            ((ImageButton)findViewById(R.id.img0)).setImageResource(R.drawable.face1);
//            ((ImageButton)findViewById(R.id.img1)).setImageResource(R.drawable.face2);
//            ((ImageButton)findViewById(R.id.img2)).setImageResource(R.drawable.face3);
//            ((LinearLayout)findViewById(R.id.card0)).setTag("face1");
//            ((LinearLayout)findViewById(R.id.card1)).setTag("face2");
//            ((LinearLayout)findViewById(R.id.card2)).setTag("face3");
//        }
    }

    // given "ham" return ham (without surrounding quotes)
    private String stripQuotes(String s) {
        if (s.substring(0,1).equals("\"") && s.substring(s.length()-1, s.length()).equals("\"")) {
            return s.substring(1, s.length()-1);
        } else {
            return s;
        }
    }


}










