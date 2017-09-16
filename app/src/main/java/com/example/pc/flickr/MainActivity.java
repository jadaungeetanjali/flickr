package com.example.pc.flickr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Bundle moviesBundle = new Bundle();
        String[] moviesUrls = {
                "https://api.themoviedb.org/3/movie/400?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US",  //detail_movie url
                "https://api.themoviedb.org/3/movie/400/credits?api_key=fe56cdee4dfea0c18403e0965acfa23b",        //cast url
                "https://api.themoviedb.org/3/movie/400/reviews?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US&page=1", //reviews
        };
        String[] movieHeading = {"detailMovie", "cast", "reviews"};
        moviesBundle.putStringArray("urls", moviesUrls);
        moviesBundle.putStringArray("urlHeading", movieHeading);
        // using setOnclickListener on button click
        Button movieButton = (Button) findViewById(R.id.movie);
        movieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //using intents to move from MainActivity to MoviesDetails on button click
                Intent intent = new Intent(MainActivity.this, MoviesDetails.class);
                intent.putExtras(moviesBundle);
                startActivity(intent);
            }
        });

        Button tvShowsButton = (Button) findViewById(R.id.tv_shows);
        tvShowsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //using intents to move from MainActivity to TvShowsDetails on button click
                Intent intent = new Intent(MainActivity.this, TvShowsDetails.class);
                startActivity(intent);
            }
        });
        Button celebritiesButton = (Button) findViewById(R.id.celebrities);
        celebritiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //using intents to move from MainActivity to Celebrities on button click
                Intent intent = new Intent(MainActivity.this, Celebrities.class);
                startActivity(intent);
            }
        });
    }
}
