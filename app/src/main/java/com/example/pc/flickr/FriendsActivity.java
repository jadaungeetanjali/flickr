package com.example.pc.flickr;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class FriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.friends_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("MOVIES"));
        tabLayout.addTab(tabLayout.newTab().setText("TV"));
        tabLayout.addTab(tabLayout.newTab().setText("CELEBS"));

        //fragmentTranstion(moviesFragment);
        //currentFragment = moviesFragment;
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    //Movies Fragment will be added
                    //fragmentTranstion(moviesFragment);
                    //currentFragment = moviesFragment;
                } else if (tab.getPosition() == 1) {
                    //Tv Fragment will be added
                    //fragmentTranstion(tvFragment);
                    //currentFragment = tvFragment;
                } else {
                    //Celebs Fragment will be added
                    //currentFragment = celebsFragment;
                    //fragmentTranstion(celebsFragment);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
