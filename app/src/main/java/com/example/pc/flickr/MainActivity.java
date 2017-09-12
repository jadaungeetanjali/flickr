package com.example.pc.flickr;

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


        final FrameLayout fragmentContainer = (FrameLayout) findViewById(R.id.main_fragment_container);

        Bundle moviesBundle = new Bundle();
        String[] moviesUrls = {
                "https://api.themoviedb.org/3/movie/now_playing?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US&page=1",
                "https://api.themoviedb.org/3/movie/popular?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US&page=1",
                "https://api.themoviedb.org/3/movie/top_rated?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US&page=1",
                "https://api.themoviedb.org/3/movie/upcoming?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US&page=1"
        };
        String[] movieHeading = {"Now Playing", "Popular", "Top Rated", "Upcoming"};
        moviesBundle.putString("type","movies");
        moviesBundle.putStringArray("urls", moviesUrls);
        moviesBundle.putStringArray("urlHeading", movieHeading);
        final HorizontalListFragment moviesFragment =new HorizontalListFragment();
        moviesFragment.setArguments(moviesBundle);



        Bundle tvBundle = new Bundle();
        String[] tvUrls = {
                "https://api.themoviedb.org/3/tv/popular?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US&page=1",
                "https://api.themoviedb.org/3/tv/airing_today?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US&page=1",
                "https://api.themoviedb.org/3/tv/top_rated?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US&page=1",
                "https://api.themoviedb.org/3/tv/on_the_air?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US&page=1"
        };
        String[] tvHeading = {"Airing Today", "Popular", "Top Rated", "On The Air"};
        tvBundle.putString("type","tv");
        tvBundle.putStringArray("urls", tvUrls);
        tvBundle.putStringArray("urlHeading", tvHeading);
        final HorizontalListFragment tvFragment =new HorizontalListFragment();
        tvFragment.setArguments(tvBundle);

        Bundle celebsBundle = new Bundle();
        String[] celebsUrls = {
                "https://api.themoviedb.org/3/person/popular?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US&page=1"
        };
        String[] celebsHeading = {"Popular"};
        celebsBundle.putString("type","celebs");
        celebsBundle.putStringArray("urls", celebsUrls);
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
