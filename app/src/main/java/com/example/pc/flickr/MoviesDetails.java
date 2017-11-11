package com.example.pc.flickr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.pc.flickr.fragments.CelebsFragment;
import com.example.pc.flickr.fragments.MoviesFragment;
import com.example.pc.flickr.util.activities.ActivityConfig;


// MoviesDetails activity to display the detail of particular movie
public class MoviesDetails extends AppCompatActivity {
    private String type, id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_movie_layout);
        Bundle bundle = this.getIntent().getExtras();

        ActivityConfig activityConfig = new ActivityConfig();

        type = bundle.getString(activityConfig.TYPE);
        if ((type.equals(activityConfig.MOVIES)) || (type.equals(ActivityConfig.TV_SHOWS))  ) {
            MoviesFragment moviesFragment = new MoviesFragment();
            moviesFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.detail_fragment_container, moviesFragment).commit();
        }


        if((type.equals(ActivityConfig.CELEBRITY))){
            CelebsFragment celebsFragment = new CelebsFragment();
            celebsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.detail_fragment_container, celebsFragment).commit();
        }


    }
}

