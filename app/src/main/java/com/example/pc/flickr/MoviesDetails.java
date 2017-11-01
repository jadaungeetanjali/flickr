package com.example.pc.flickr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.pc.flickr.fragments.CelebsFragment;
import com.example.pc.flickr.fragments.MoviesFragment;



// MoviesDetails activity to display the detail of particular movie
public class MoviesDetails extends AppCompatActivity {
    private String type, id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_movie_layout);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = this.getIntent().getExtras();
        type = bundle.getString("type");
        if ((type.equals("movies")) || (type.equals("tv"))  ) {
            MoviesFragment moviesFragment = new MoviesFragment();
            moviesFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.detail_fragment_container, moviesFragment).commit();
        }
        if((type.equals("celebs"))){
            CelebsFragment celebsFragment = new CelebsFragment();
            celebsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.detail_fragment_container, celebsFragment).commit();
        }
    }
    // CastAdapter class to populate data in castRecyclerView
}

