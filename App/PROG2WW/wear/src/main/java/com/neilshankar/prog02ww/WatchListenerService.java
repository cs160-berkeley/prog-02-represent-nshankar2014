package com.neilshankar.prog02ww;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;


public class WatchListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        if (messageEvent.getPath().equals("/data")) {
            String data = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Intent intent = new Intent(this, MainActivity.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // parse message and put intents
            int i_pipe1 = data.indexOf("|", 0);
            int i_pipe2 = data.indexOf("|", i_pipe1+1);
            int i_pipe3 = data.indexOf("|", i_pipe2+1);
            int i_pipe4 = data.indexOf("|", i_pipe3+1);
            int i_pipe5 = data.indexOf("|", i_pipe4+1);
            int i_pipe6 = data.indexOf("|", i_pipe5+1);

            String name0 = data.substring(0, i_pipe1);
            String name1 = data.substring(i_pipe1+1, i_pipe2);
            String name2 = data.substring(i_pipe2+1, i_pipe3);
            String title0 = data.substring(i_pipe3+1, i_pipe4);
            String title1 = data.substring(i_pipe4+1, i_pipe5);
            String title2 = data.substring(i_pipe5+1, i_pipe6);
            String geojson = data.substring(i_pipe6+1, data.length());

            intent.putExtra("name0", name0);
            intent.putExtra("name1", name1);
            intent.putExtra("name2", name2);
            intent.putExtra("title0", title0);
            intent.putExtra("title1", title1);
            intent.putExtra("title2", title2);
            intent.putExtra("geojson", geojson);

            Log.d("0000000000000000", "name0: " + name0);
            Log.d("0000000000000000", "name1: " + name1);
            Log.d("0000000000000000", "name2: " + name2);
            Log.d("0000000000000000", "title0: " + title0);
            Log.d("0000000000000000", "title1: " + title1);
            Log.d("0000000000000000", "title2: " + title2);

            startActivity(intent); // start watch activity

        } else {
            super.onMessageReceived(messageEvent);
        }

    }
}