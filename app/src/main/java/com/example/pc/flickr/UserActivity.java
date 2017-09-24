package com.example.pc.flickr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.user_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                bundle.putString("type","Favorite");
                buttonLinearLayout.setVisibility(View.GONE);
                userlistFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.user_fragment_container,userlistFragment).commit();

            }
        });

        ratingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("type","Rating");
                buttonLinearLayout.setVisibility(View.GONE);
                userlistFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.user_fragment_container,userlistFragment).commit();
            }
        });

        watchListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("type","WatchList");
                buttonLinearLayout.setVisibility(View.GONE);
                userlistFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.user_fragment_container,userlistFragment).commit();
            }
        });

        wishListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("type","WishList");
                buttonLinearLayout.setVisibility(View.GONE);
                userlistFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.user_fragment_container,userlistFragment).commit();
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("type","profile");
                buttonLinearLayout.setVisibility(View.GONE);
                userlistFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.user_fragment_container,userlistFragment).commit();
            }
        });
    }
}
