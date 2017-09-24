package com.example.pc.flickr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.pc.flickr.fragments.UserlistFragment;

public class UserActivity extends AppCompatActivity {
    public Button favoriteButton, ratingButton, watchListButton, wishListButton, profileButton;
    public LinearLayout buttonLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        final UserlistFragment userlistFragment = new UserlistFragment();
        buttonLinearLayout = (LinearLayout) findViewById(R.id.user_activity_button);
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
                buttonLinearLayout.setVisibility(View.GONE);
                userlistFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.user_activity,userlistFragment).commit();

            }
        });

        ratingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("type","rating");
                buttonLinearLayout.setVisibility(View.GONE);
                userlistFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.user_activity,userlistFragment).commit();
            }
        });

        watchListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("type","watchlist");
                buttonLinearLayout.setVisibility(View.GONE);
                userlistFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.user_activity,userlistFragment).commit();
            }
        });

        wishListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("type","wishlist");
                buttonLinearLayout.setVisibility(View.GONE);
                userlistFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.user_activity,userlistFragment).commit();
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("type","profile");
                buttonLinearLayout.setVisibility(View.GONE);
                userlistFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.user_activity,userlistFragment).commit();
            }
        });


    }
}
