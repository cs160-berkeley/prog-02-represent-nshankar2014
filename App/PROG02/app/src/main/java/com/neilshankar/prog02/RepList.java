package com.neilshankar.prog02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

public class RepList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rep_list);

        Intent it = getIntent();
        final int zip = Integer.parseInt(it.getStringExtra("zip"));

        if (zip == 94704) {
            ((ImageButton)findViewById(R.id.img1)).setImageResource(R.drawable.face1);
            ((ImageButton)findViewById(R.id.img2)).setImageResource(R.drawable.face2);
            ((ImageButton)findViewById(R.id.img3)).setImageResource(R.drawable.face3);
            ((TextView)findViewById(R.id.name1)).setText("Brad Rigo (R)");
            ((TextView)findViewById(R.id.name2)).setText("Pat Toomey (R)");
            ((TextView)findViewById(R.id.name3)).setText("Chad McGraw (D)");
        } else if (zip == 19403) {
            ((ImageButton) findViewById(R.id.img1)).setImageResource(R.drawable.face4);
            ((ImageButton) findViewById(R.id.img2)).setImageResource(R.drawable.face5);
            ((ImageButton) findViewById(R.id.img3)).setImageResource(R.drawable.face6);
            ((TextView) findViewById(R.id.name1)).setText("Hunter Wayans (D)");
            ((TextView) findViewById(R.id.name2)).setText("Rain Bust (R)");
            ((TextView) findViewById(R.id.name3)).setText("Cooper Lighthook (D)");
        } else {
            ((ImageButton)findViewById(R.id.img1)).setImageResource(R.drawable.face7);
            ((ImageButton)findViewById(R.id.img2)).setImageResource(R.drawable.face8);
            ((ImageButton)findViewById(R.id.img3)).setImageResource(R.drawable.face9);
            ((TextView)findViewById(R.id.name1)).setText("Buster White (D)");
            ((TextView)findViewById(R.id.name2)).setText("Nate Cream (R)");
            ((TextView)findViewById(R.id.name3)).setText("Andrew Carmichael (D)");
        }
    }


}
