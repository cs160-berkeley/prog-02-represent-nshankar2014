package com.neilshankar.prog02ww;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class RepDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rep_detail);

        final Intent it = getIntent();
        final String face = it.getStringExtra("face");

        if (face.equals("face1")) {
            ((ImageView)findViewById(R.id.bigface)).setImageResource(R.drawable.face1);
            ((TextView)findViewById(R.id.name)).setText("Brad Rigo (R)");
        } else if (face.equals("face2")) {
            ((ImageView)findViewById(R.id.bigface)).setImageResource(R.drawable.face2);
            ((TextView)findViewById(R.id.name)).setText("Pat Toomey (R)");
        } else if (face.equals("face3")) {
            ((ImageView)findViewById(R.id.bigface)).setImageResource(R.drawable.face3);
            ((TextView)findViewById(R.id.name)).setText("Chad McGraw (D)");
        } else if (face.equals("face4")) {
            ((ImageView)findViewById(R.id.bigface)).setImageResource(R.drawable.face4);
            ((TextView)findViewById(R.id.name)).setText("Hunter Wayans (D)");
        } else if (face.equals("face5")) {
            ((ImageView)findViewById(R.id.bigface)).setImageResource(R.drawable.face5);
            ((TextView)findViewById(R.id.name)).setText("Rain Bust (R)");
        } else if (face.equals("face6")) {
            ((ImageView)findViewById(R.id.bigface)).setImageResource(R.drawable.face6);
            ((TextView)findViewById(R.id.name)).setText("Cooper Lighthook (D)");
        } else if (face.equals("face7")) {
            ((ImageView)findViewById(R.id.bigface)).setImageResource(R.drawable.face7);
            ((TextView)findViewById(R.id.name)).setText("Buster White (D)");
        } else if (face.equals("face8")) {
            ((ImageView)findViewById(R.id.bigface)).setImageResource(R.drawable.face8);
            ((TextView)findViewById(R.id.name)).setText("Nate Cream (R)");
        } else if (face.equals("face9")) {
            ((ImageView)findViewById(R.id.bigface)).setImageResource(R.drawable.face9);
            ((TextView)findViewById(R.id.name)).setText("Andrew Carmichael (D)");
        }

    }
}
