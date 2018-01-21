package com.example.pc.flickr.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.pc.flickr.R;
import com.example.pc.flickr.fragments.CelebsFragment;
import com.example.pc.flickr.fragments.FindFragment;
import com.example.pc.flickr.fragments.FriendsFragment;
import com.example.pc.flickr.fragments.RequestFragment;

public class FriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.friends_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.friends_tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("FIND"));
        tabLayout.addTab(tabLayout.newTab().setText("FRIENDS"));
        tabLayout.addTab(tabLayout.newTab().setText("REQUESTS"));

        final FriendsFragment friendsFragment = new FriendsFragment();


        final RequestFragment requestFragment = new RequestFragment();


       final FindFragment findFragment = new FindFragment();

        fragmentTranstion(findFragment);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    fragmentTranstion(findFragment);

                } else if (tab.getPosition() == 1) {
                    fragmentTranstion(friendsFragment);

                } else {
                    fragmentTranstion(requestFragment);
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    private void fragmentTranstion(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.friends_fragment_container, fragment).commit();

    }
}
