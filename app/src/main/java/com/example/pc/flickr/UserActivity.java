package com.example.pc.flickr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pc.flickr.fragments.UserlistFragment;

public class UserActivity extends AppCompatActivity {
    public Button favoriteButton, ratingButton, watchListButton, wishListButton, profileButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        favoriteButton = (Button) findViewById(R.id.user_favorite_list);
        ratingButton = (Button) findViewById(R.id.user_rating_list);
        watchListButton = (Button) findViewById(R.id.user_watchList_list);
        wishListButton = (Button) findViewById(R.id.user_wishList_list);
        profileButton = (Button) findViewById(R.id.user_profile_list);
        final Bundle bundle = new Bundle();
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("type","favorite");
            }
        });

        ratingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("type","rating");
            }
        });

        watchListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("type","watchlist");
            }
        });

        wishListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("type","wishlist");
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("type","profile");
            }
        });
        UserlistFragment userlistFragment = new UserlistFragment();
        userlistFragment.setArguments(bundle);
    }
}
