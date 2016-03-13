// shoutout to http://bit.ly/1RVK3Fx

package com.neilshankar.prog02ww;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridViewPager;
import android.view.View;
import android.view.View.OnApplyWindowInsetsListener;
import android.view.WindowInsets;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
        pager.setOnApplyWindowInsetsListener(new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                pager.onApplyWindowInsets(insets);
                return insets;
            }
        });

        Intent it = getIntent();
        String name0 = it.getStringExtra("name0");
        String name1 = it.getStringExtra("name1");
        String name2 = it.getStringExtra("name2");
        String title0 = it.getStringExtra("title0");
        String title1 = it.getStringExtra("title1");
        String title2 = it.getStringExtra("title2");

        SampleGridPagerAdapter sgpa = new SampleGridPagerAdapter(this, getFragmentManager());
        sgpa.fill(name0, name1, name2, title0, title1, title2);
        pager.setAdapter(sgpa);

        DotsPageIndicator dotsPageIndicator = (DotsPageIndicator) findViewById(R.id.page_indicator);
        dotsPageIndicator.setPager(pager);
    }

}





















