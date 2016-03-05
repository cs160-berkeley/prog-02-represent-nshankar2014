package com.neilshankar.prog02ww;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.GridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RepList extends Activity {

    private TextView mTextView;
    private Button mFeedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rep_list);

//        final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
//        pager.setAdapter(new SampleGridPagerAdapter(this, getFragmentManager()));

        Intent intent = getIntent();
        final String zip = intent.getStringExtra("zip");

        if (zip == "94704") {
            ((TextView)findViewById(R.id.name1)).setText("Brad Rigo (R)");
            ((TextView)findViewById(R.id.name2)).setText("Pat Toomey (R)");
            ((TextView)findViewById(R.id.name3)).setText("Chad McGraw (D)");
        } else if (zip == "19403") {
            ((TextView) findViewById(R.id.name1)).setText("Hunter Wayans (D)");
            ((TextView) findViewById(R.id.name2)).setText("Rain Bust (R)");
            ((TextView) findViewById(R.id.name3)).setText("Cooper Lighthook (D)");
        } else {
            ((TextView)findViewById(R.id.name1)).setText("Buster White (D)");
            ((TextView)findViewById(R.id.name2)).setText("Nate Cream (R)");
            ((TextView)findViewById(R.id.name3)).setText("Andrew Carmichael (D)");
        }

        ((Button)findViewById(R.id.vote)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View clicked) {
                Intent it = new Intent(RepList.this, Vote.class);
                it.putExtra("zip", it.getStringExtra(zip));
                RepList.this.startActivity(it);
            }
        });
    }

}

