package com.example.pc.flickr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.pc.flickr.fragments.UserlistFragment;
import com.firebase.ui.auth.AuthUI;

public class UserActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.user_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        UserlistFragment userlistFragment = new UserlistFragment();
        Bundle bundle = this.getIntent().getExtras();
        userlistFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.user_fragment_container,userlistFragment).commit();

    }
}
