package com.neilshankar.prog02ww;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Vote extends WearableActivity {

    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);

    private BoxInsetLayout mContainerView;
    private TextView mTextView;
    private TextView mClockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        Intent intent = getIntent();
        final String zip = intent.getStringExtra("zip");

        if (zip == "94704") {
            ((TextView)findViewById(R.id.obama)).setText("Obama: 30%");
            ((TextView)findViewById(R.id.romney)).setText("Romney: 70%");
        } else if (zip == "19403") {
            ((TextView)findViewById(R.id.obama)).setText("Obama: 40%");
            ((TextView)findViewById(R.id.romney)).setText("Romney: 60%");
        } else {
            ((TextView)findViewById(R.id.obama)).setText("Obama: 50%");
            ((TextView)findViewById(R.id.romney)).setText("Romney: 50%");
        }
    }

}
