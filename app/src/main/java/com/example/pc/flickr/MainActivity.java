package com.example.pc.flickr;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.pc.flickr.fragments.HorizontalListFragment;
import com.example.pc.flickr.services.FetchApiService;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private static final int RC_SIGN_IN =1;
    public Fragment currentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        Intent intent = new Intent(this,FetchApiService.class);
        startService(intent);

        //Firebase auth
        mFirebaseAuth = FirebaseAuth.getInstance();



        //Firebase auth till here

        Bundle moviesBundle = new Bundle();
        String[] movieHeading = {"Now Playing", "Popular", "Top Rated", "Upcoming"};
        moviesBundle.putString("type","movies");
        moviesBundle.putStringArray("urlHeading", movieHeading);
        final HorizontalListFragment moviesFragment =new HorizontalListFragment();
        moviesFragment.setArguments(moviesBundle);

        Bundle tvBundle = new Bundle();
        String[] tvHeading = {"Airing Today", "Popular", "Top Rated", "On The Air"};
        tvBundle.putString("type","tv");
        tvBundle.putStringArray("urlHeading", tvHeading);
        final HorizontalListFragment tvFragment =new HorizontalListFragment();
        tvFragment.setArguments(tvBundle);

        Bundle celebsBundle = new Bundle();
        String[] celebsHeading = {"Popular"};
        celebsBundle.putString("type","celebs");
        celebsBundle.putStringArray("urlHeading", celebsHeading);
        final HorizontalListFragment celebsFragment =new HorizontalListFragment();
        celebsFragment.setArguments(celebsBundle);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("MOVIES"));
        tabLayout.addTab(tabLayout.newTab().setText("TV"));
        tabLayout.addTab(tabLayout.newTab().setText("CELEBS"));

        fragmentTranstion(moviesFragment);
        currentFragment = moviesFragment;
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    //Movies Fragment will be added
                    fragmentTranstion(moviesFragment);
                    currentFragment = moviesFragment;
                }

                else if (tab.getPosition() == 1){
                    //Tv Fragment will be added
                    fragmentTranstion(tvFragment);
                    currentFragment = tvFragment;
                }

                else {
                    //Celebs Fragment will be added
                    currentFragment = celebsFragment;
                    fragmentTranstion(celebsFragment);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null){
                    onSignedInInitialize(firebaseUser.getDisplayName(),firebaseUser.getUid(),firebaseUser.getEmail());
                }
                else {
                    //onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(
                                            Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }


    private void fragmentTranstion(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,fragment).commit();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.user_menu_option:
                Intent intent = new Intent(this,UserActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == RC_SIGN_IN){
            if (resultCode == RESULT_OK){
                Toast.makeText(this, "User Signed in!!", Toast.LENGTH_SHORT).show();
            }else if (resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Sign in canceled!!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }


    @Override
    public void onPause(){
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }
    @Override
    public void onResume(){
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    public void onSignedInInitialize(String user_name,String user_id,String user_email){

    }
}
