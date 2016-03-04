package com.neilshankar.prog02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EnterLocation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_location);

        Button use_my_location = (Button)findViewById(R.id.use_my_location);
        use_my_location.setClickable(true);
        use_my_location.setOnClickListener(new View.OnClickListener() {
            public void onClick(View clicked) {
                Intent it = new Intent(EnterLocation.this, RepList.class);
                it.putExtra("zip", "94704");
                EnterLocation.this.startActivity(it);
            }
        });

        Button enter = (Button)findViewById(R.id.enter);
        enter.setClickable(true);
        enter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View clicked) {
                Intent it = new Intent(EnterLocation.this, RepList.class);
                it.putExtra("zip", "" + ((EditText) findViewById(R.id.zip_input)).getText());
                EnterLocation.this.startActivity(it);
            }
        });
    }

}
