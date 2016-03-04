package com.neilshankar.prog02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
            ((LinearLayout)findViewById(R.id.card1)).setTag("face1");
            ((LinearLayout)findViewById(R.id.card2)).setTag("face2");
            ((LinearLayout)findViewById(R.id.card3)).setTag("face3");
            ((TextView)findViewById(R.id.name1)).setText("Brad Rigo (R)");
            ((TextView)findViewById(R.id.name2)).setText("Pat Toomey (R)");
            ((TextView)findViewById(R.id.name3)).setText("Chad McGraw (D)");
        } else if (zip == 19403) {
            ((ImageButton) findViewById(R.id.img1)).setImageResource(R.drawable.face4);
            ((ImageButton) findViewById(R.id.img2)).setImageResource(R.drawable.face5);
            ((ImageButton) findViewById(R.id.img3)).setImageResource(R.drawable.face6);
            ((LinearLayout)findViewById(R.id.card1)).setTag("face4");
            ((LinearLayout)findViewById(R.id.card2)).setTag("face5");
            ((LinearLayout)findViewById(R.id.card3)).setTag("face6");
            ((TextView) findViewById(R.id.name1)).setText("Hunter Wayans (D)");
            ((TextView) findViewById(R.id.name2)).setText("Rain Bust (R)");
            ((TextView) findViewById(R.id.name3)).setText("Cooper Lighthook (D)");
        } else {
            ((ImageButton)findViewById(R.id.img1)).setImageResource(R.drawable.face7);
            ((ImageButton)findViewById(R.id.img2)).setImageResource(R.drawable.face8);
            ((ImageButton)findViewById(R.id.img3)).setImageResource(R.drawable.face9);
            ((LinearLayout)findViewById(R.id.card1)).setTag("face7");
            ((LinearLayout)findViewById(R.id.card2)).setTag("face8");
            ((LinearLayout)findViewById(R.id.card3)).setTag("face9");
            ((TextView)findViewById(R.id.name1)).setText("Buster White (D)");
            ((TextView)findViewById(R.id.name2)).setText("Nate Cream (R)");
            ((TextView)findViewById(R.id.name3)).setText("Andrew Carmichael (D)");
        }

        // attach a click listener to each ImageButton and Button
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


}
