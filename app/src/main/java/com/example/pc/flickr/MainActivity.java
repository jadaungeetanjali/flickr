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
        // using setOnclickListener on button click
        Button movieButton = (Button) findViewById(R.id.movie);
        movieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //using intents to move from MainActivity to MoviesDetails on button click
                Intent intent = new Intent(MainActivity.this, MoviesDetails.class);
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
