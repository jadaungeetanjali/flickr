package com.example.pc.flickr;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        Intent intent = new Intent(this,FetchApiService.class);
        startService(intent);

        Bundle moviesBundle = new Bundle();
        String[] movieHeading = {"Now Playing", "Popular", "Top Rated", "Upcoming"};
        moviesBundle.putString("type","movies");
        moviesBundle.putStringArray("urlHeading", movieHeading);
        final HorizontalListFragment moviesFragment =new HorizontalListFragment();
        moviesFragment.setArguments(moviesBundle);



        Bundle tvBundle = new Bundle();
        String[] tvHeading = {"Airing Today", "Popular", "Top Rated", "On The Air"};
        tvBundle.putString("type","tv");
        tvBundle.putStringArray("urlHeading", tvHeading);
        final HorizontalListFragment tvFragment =new HorizontalListFragment();
        tvFragment.setArguments(tvBundle);

        Bundle celebsBundle = new Bundle();
        String[] celebsHeading = {"Popular"};
        celebsBundle.putString("type","celebs");
        celebsBundle.putStringArray("urlHeading", celebsHeading);
        final HorizontalListFragment celebsFragment =new HorizontalListFragment();
        celebsFragment.setArguments(celebsBundle);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("MOVIES"));
        tabLayout.addTab(tabLayout.newTab().setText("TV"));
        tabLayout.addTab(tabLayout.newTab().setText("CELEBS"));

        fragmentTranstion(moviesFragment);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    //Movies Fragment will be added
                    fragmentTranstion(moviesFragment);
                }

                else if (tab.getPosition() == 1){
                    //Tv Fragment will be added
                    fragmentTranstion(tvFragment);
                }

                else {
                    //Celebs Fragment will be added
                    fragmentTranstion(celebsFragment);
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

    private void fragmentTranstion(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,fragment).commit();

    }
}
