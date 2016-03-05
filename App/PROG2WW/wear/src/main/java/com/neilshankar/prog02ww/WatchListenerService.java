package com.neilshankar.prog02ww;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;


public class WatchListenerService extends WearableListenerService {
    // In PhoneToWatchService, we passed in a path, either "/FRED" or "/LEXY"
    // These paths serve to differentiate different phone-to-watch messages
    private static final String FRED_FEED = "/Fred";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
//        if( messageEvent.getPath().equalsIgnoreCase( FRED_FEED ) ) {
//            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
//            Intent intent = new Intent(this, MainActivity.class );
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            //you need to add this flag since you're starting a new activity from a service
//            intent.putExtra("CAT_NAME", "Fred");
//            startActivity(intent); // start watch MainActivity
//        } else {
//            super.onMessageReceived( messageEvent );
//        }

        Log.d("T", "in WatchListenerService, got: " + messageEvent.getPath());
        String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
        Intent intent = new Intent(this, EnterLocation.class );
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //you need to add this flag since you're starting a new activity from a service
        intent.putExtra("CAT_NAME", "Fred");
        startActivity(intent); // start watch MainActivity

    }
}