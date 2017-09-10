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
        Button movieButton = (Button) findViewById(R.id.movie);
        movieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MoviesDetails.class);
                startActivity(intent);
            }
        });
        Button tvShowsButton = (Button) findViewById(R.id.tv_shows);
        tvShowsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TvShowsDetails.class);
                startActivity(intent);
            }
        });
        Button celebritiesButton = (Button) findViewById(R.id.celebrities);
        celebritiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Celebrities.class);
                startActivity(intent);
            }
        });
    }
}
