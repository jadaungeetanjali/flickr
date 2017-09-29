package com.example.pc.flickr;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.pc.flickr.fragments.FriendListFragment;

public class FriendListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.friends_list_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.friends_list_tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("WISHLIST"));
        tabLayout.addTab(tabLayout.newTab().setText("WATCHLIST"));
        tabLayout.addTab(tabLayout.newTab().setText("FAVORITE"));

        Bundle bundle = this.getIntent().getExtras();
        Bundle watchBundle = new Bundle();
        Bundle wishBundle = new Bundle();
        Bundle favoriteBundle = new Bundle();
        final FriendListFragment watchListFragment = new FriendListFragment();
        watchBundle.putString("id",bundle.getString("id"));
        watchBundle.putString("type","WatchList");
        watchListFragment.setArguments(watchBundle);

        final FriendListFragment wishListFragment = new FriendListFragment();
        wishBundle.putString("type","WishList");
        wishBundle.putString("id",bundle.getString("id"));
        wishListFragment.setArguments(wishBundle);

        final FriendListFragment favoriteFragment = new FriendListFragment();
        favoriteBundle.putString("type","Favorite");
        favoriteBundle.putString("id",bundle.getString("id"));
        favoriteFragment.setArguments(favoriteBundle);

        fragmentTranstion(wishListFragment);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    fragmentTranstion(wishListFragment);

                } else if (tab.getPosition() == 1) {
                    fragmentTranstion(watchListFragment);

                } else {
                    fragmentTranstion(favoriteFragment);
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
        getSupportFragmentManager().beginTransaction().replace(R.id.friends_list_fragment_container, fragment).commit();

    }
}