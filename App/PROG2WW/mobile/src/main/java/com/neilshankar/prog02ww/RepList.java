package com.neilshankar.prog02ww;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class RepList extends AppCompatActivity {

    private Intent it;
    private String zip;
    private String lat;
    private String lon;
    private String geojson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rep_list);

        ((TextView)findViewById(R.id.maintext)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/didot.ttf"));

        it = getIntent();
        zip = it.getStringExtra("zip");
        lat = it.getStringExtra("lat");
        lon = it.getStringExtra("lon");

        attachClickListeners();
        fetchData();
        twitterConnect();
    }

    // attach a click listener to each ImageButton and Button
    private void attachClickListeners() {
        ImageButton img0 = (ImageButton)findViewById(R.id.img0);
        img0.setClickable(true);
        img0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View clicked) {
                Intent it = new Intent(RepList.this, RepDetail.class);
                it.putExtra("face", "face1");
                it.putExtra("zip", it.getStringExtra("zip"));
                RepList.this.startActivity(it);
            }
        });
        Button seemore0 = (Button)findViewById(R.id.seemore0);
        seemore0.setClickable(true);
        seemore0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View clicked) {
                Intent it = new Intent(RepList.this, RepDetail.class);
                it.putExtra("face", "face1");
                it.putExtra("zip", it.getStringExtra("zip"));
                RepList.this.startActivity(it);
            }
        });
    }

    // construct HTTP GET query with either (zip) or (lat and lon)
    // also construct geolocate url for vote data
    private void fetchData() {
        String repUrl = "http://congress.api.sunlightfoundation.com/legislators/locate?";
        String voteUrl = "https://maps.googleapis.com/maps/api/geocode/json?";
        if (zip.equals("")) {
            repUrl += "latitude=" + lat + "&longitude=" + lon;
            voteUrl += "latlng=" + lat + "," + lon;
        } else {
            repUrl += "zip=" + zip;
            voteUrl += "latlng=37.8687,-122.25";
        }
        repUrl += "&apikey=d5c6792487144ad397965ccbe5cb713d";
        voteUrl += "&key=AIzaSyCb7REm9Yhi2lusfMLNxU3CP-c-Ori8jnk";

        // make the GET request
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new GetVoteData().execute(voteUrl);
            new GetRepData().execute(repUrl); // this is the magic line that makes the request happen
        } else {
            Log.d("0000000000000000", "network contectivity issues");
        }

        // sample vote url: "https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&key=AIzaSyCb7REm9Yhi2lusfMLNxU3CP-c-Ori8jnk";
    }

    // shoutout to http://bit.ly/1XlKtX1
    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string.
    private class GetRepData extends AsyncTask<String, Void, String> {
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

    // for reverse geolocating
    private class GetVoteData extends AsyncTask<String, Void, String> {
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
            geojson = result;
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
        int i_termend = -1;
        String[] fname = new String[3];
        String[] surname = new String[3];
        String[] party = new String[3];
        String[] title = new String[3];
        String[] email = new String[3];
        String[] website = new String[3];
        String[] termend = new String[3];

        for (int i = 0; i < 3; i += 1) {
            i_fname = s.indexOf("\"first_name\":", i_fname + 1);
            i_surname = s.indexOf("\"last_name\":", i_surname + 1);
            i_party = s.indexOf("\"party\":", i_party + 1);
            i_title = s.indexOf("\"title\":", i_title + 1);
            i_email = s.indexOf("\"oc_email\":", i_email + 1);
            i_website = s.indexOf("\"website\":", i_website + 1);
            i_termend = s.indexOf("\"term_end\":", i_termend + 1);

            fname[i] = stripQuotes(s.substring(i_fname + 13, s.indexOf(",", i_fname)));
            surname[i] = stripQuotes(s.substring(i_surname + 12, s.indexOf(",", i_surname)));
            party[i] = stripQuotes(s.substring(i_party + 8, s.indexOf(",", i_party)));
            title[i] = stripQuotes(s.substring(i_title + 8, s.indexOf(",", i_title)));
            email[i] = stripQuotes(s.substring(i_email + 11, s.indexOf(",", i_email)));
            website[i] = stripQuotes(s.substring(i_website + 10, s.indexOf(",", i_website)));
            //termend[i] = stripQuotes(s.substring(i_termend + 11, s.indexOf(",", i_termend)));

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

        // send data to watch
        Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
        sendIntent.putExtra("name0", fname[0] + " " + surname[0] + " (" + party[0] + ")");
        sendIntent.putExtra("name1", fname[1] + " " + surname[1] + " (" + party[1] + ")");
        sendIntent.putExtra("name2", fname[2] + " " + surname[2] + " (" + party[2] + ")");
        sendIntent.putExtra("title0", title[0]);
        sendIntent.putExtra("title1", title[1]);
        sendIntent.putExtra("title2", title[2]);
        sendIntent.putExtra("geojson", geojson);
        startService(sendIntent);

//        if (zip.equals("94704")) {
//            ((ImageButton)findViewById(R.id.img0)).setImageResource(R.drawable.face1);
//            ((ImageButton)findViewById(R.id.img1)).setImageResource(R.drawable.face2);
//            ((ImageButton)findViewById(R.id.img2)).setImageResource(R.drawable.face3);
//        }
    }

    // given "ham" return ham (without surrounding quotes)
    private String stripQuotes(String s) {
        if (s.substring(0,1).equals("\"") && s.substring(s.length()-1, s.length()).equals("\"")) {
            return s.substring(1, s.length()-1);
        } else if (s.substring(0,1).equals("\"") && s.substring(s.length()-1, s.length()).equals("}")) {
            return s.substring(1, s.length()-2);
        }
        else {
            return s;
        }
    }

    private void twitterConnect() {
//        final LinearLayout myLayout = (LinearLayout) findViewById(R.id.linlay0);
//
//        final List<Long> tweetIds = Arrays.asList(510908133917487104L);
//        TweetUtils.loadTweets(tweetIds, new Callback<Tweet>() {
//            @Override
//            public void success(Result<Tweet> result) {
//                for (Tweet tweet : result.data) {
//                    myLayout.addView(new TweetView(RepList.this, tweet));
//                }
//            }
//
//            @Override
//            public void failure(TwitterException exception) {
//                // Toast.makeText(...).show();
//            }
//        });

//        final UserTimeline userTimeline = new UserTimeline.Builder()
//                .screenName("senatorboxer")
//                .maxItemsPerRequest(1)
//                .build();
//

        // load 3 tweets (hardcoded)
        final ViewGroup tweet0w = (ViewGroup)findViewById(R.id.tweetwrapper0);
        final ViewGroup tweet1w = (ViewGroup)findViewById(R.id.tweetwrapper1);
        final ViewGroup tweet2w = (ViewGroup)findViewById(R.id.tweetwrapper2);

        long tweet0 = 708420496245678080L;
        long tweet1 = 707328209700913152L;
        long tweet2 = 708793207757398016L;

        TweetUtils.loadTweet(tweet0, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                TweetView tweetView = new TweetView(RepList.this, result.data);
                tweet0w.addView(tweetView);
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Load Tweet failure", exception);
            }
        });
        TweetUtils.loadTweet(tweet1, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                TweetView tweetView = new TweetView(RepList.this, result.data);
                tweet1w.addView(tweetView);
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Load Tweet failure", exception);
            }
        });
        TweetUtils.loadTweet(tweet2, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                TweetView tweetView = new TweetView(RepList.this, result.data);
                tweet2w.addView(tweetView);
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Load Tweet failure", exception);
            }
        });

//        TweetFragment t = new TweetFragment();
//        FrameLayout frame = new FrameLayout(this);
//        frame.setId(R.id.tweetwrapper0);
//        setContentView(frame, new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//
//        Fragment newFragment = new TweetFragment();
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.add(R.id.tweetwrapper0, newFragment).commit();


    }




}










