package com.example.pc.flickr.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.flickr.R;
import com.example.pc.flickr.fragments.HorizontalListFragment;
import com.example.pc.flickr.models.UserModel;
import com.example.pc.flickr.services.FetchApiService;
import com.example.pc.flickr.services.FirebaseCurd;
import com.example.pc.flickr.util.activities.ActivityConfig;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private static final int RC_SIGN_IN = 1;
    public Fragment currentFragment;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private View navHeader;
    private TextView navHeaderName, navHeaderEmail;
    private CharSequence mTitle, mDrawerTitle;
    private ImageView navHeaderImg;
    private int navItemIndex;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting up Toolbar for app
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        Intent intent = new Intent(this, FetchApiService.class);
        startService(intent);

        //Getting firebase auth instance
        mFirebaseAuth = FirebaseAuth.getInstance();

        // Navigation Drawer .............................
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        navHeaderName = (TextView) navHeader.findViewById(R.id.main_drawer_name);
        navHeaderEmail = (TextView) navHeader.findViewById(R.id.main_drawer_email);
        navHeaderImg = (ImageView) navHeader.findViewById(R.id.main_drawer_avatar);
        setUpNavigationView();
        // Navigation Drawer till here......................

        Bundle moviesBundle = new Bundle();
        String[] movieHeading = {ActivityConfig.NOW_PLAYING, ActivityConfig.POPULAR, ActivityConfig.TOP_RATED, ActivityConfig.UPCOMING};
        moviesBundle.putString(ActivityConfig.TYPE, ActivityConfig.MOVIES);
        moviesBundle.putStringArray(ActivityConfig.URL_HEADING, movieHeading);
        final HorizontalListFragment moviesFragment = new HorizontalListFragment();
        moviesFragment.setArguments(moviesBundle);

        Bundle tvBundle = new Bundle();
        String[] tvHeading = {ActivityConfig.TOP_RATED, ActivityConfig.POPULAR, ActivityConfig.TOP_RATED, ActivityConfig.ON_THE_AIR};
        tvBundle.putString(ActivityConfig.TYPE, ActivityConfig.TV_SHOWS);
        tvBundle.putStringArray(ActivityConfig.URL_HEADING, tvHeading);
        final HorizontalListFragment tvFragment = new HorizontalListFragment();
        tvFragment.setArguments(tvBundle);

        Bundle celebsBundle = new Bundle();
        String[] celebsHeading = {ActivityConfig.POPULAR};
        celebsBundle.putString(ActivityConfig.TYPE, ActivityConfig.CELEBRITY);
        celebsBundle.putStringArray(ActivityConfig.URL_HEADING, celebsHeading);
        final HorizontalListFragment celebsFragment = new HorizontalListFragment();
        celebsFragment.setArguments(celebsBundle);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tablayout);
        tabLayout.addTab(tabLayout.newTab().setText(ActivityConfig.MOVIES.toUpperCase()));
        tabLayout.addTab(tabLayout.newTab().setText(ActivityConfig.TV_SHOWS.toUpperCase()));
        tabLayout.addTab(tabLayout.newTab().setText(ActivityConfig.CELEBRITY.toUpperCase()));

        fragmentTranstion(moviesFragment);
        currentFragment = moviesFragment;
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    //Movies Fragment will be added
                    fragmentTranstion(moviesFragment);
                    currentFragment = moviesFragment;
                } else if (tab.getPosition() == 1) {
                    //Tv Fragment will be added
                    fragmentTranstion(tvFragment);
                    currentFragment = tvFragment;
                } else {
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
                if (firebaseUser != null) {
                    onSignedInInitialize(firebaseUser.getDisplayName(), firebaseUser.getUid(), firebaseUser.getEmail(), firebaseUser.getPhotoUrl().toString());
                    addUser(firebaseUser.getUid(),firebaseUser.getDisplayName(),firebaseUser.getEmail(),firebaseUser.getPhotoUrl().toString());
                } else {
                    //onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(
                                            Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .setTheme(R.style.RedTheme)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

        loadNavHeader();

    }


    private void fragmentTranstion(Fragment fragment) {
        FrameLayout frameLayout =  (FrameLayout) findViewById(R.id.main_fragment_container);
        frameLayout.removeAllViews();
        frameLayout.removeAllViewsInLayout();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "User Signed in!!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Sign in canceled!!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    public void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    public void onSignedInInitialize(String user_name, String user_id, String user_email, String user_image) {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("user_id", user_id);
        editor.putString("user_email", user_email);
        editor.putString("user_name", user_name);
        editor.putString("user_image",user_image);
        Log.e("user image",user_image);
        editor.apply();
    }
    public void addUser(final String uid, final String user_name,final String email,final String imgUrl){
        final FirebaseCurd firebaseCurd = new FirebaseCurd(MainActivity.this);
        DatabaseReference mUserReference = firebaseCurd.getmUsersReference().child(uid);
        mUserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                    if (userModel == null){
                        UserModel addUserModel = new UserModel(uid,user_name,email,imgUrl);
                        firebaseCurd.addUserModel(addUserModel);
                    }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
    private void loadNavHeader() {
        SharedPreferences sharedPref = this.getSharedPreferences("MyPref", 0);
        String user_name = sharedPref.getString("user_name", null);
        String user_email = sharedPref.getString("user_email", null);
        String user_image = sharedPref.getString("user_image",null);
        navHeaderName.setText(user_name);
        navHeaderEmail.setText(user_email);
        Picasso.with(this).load(user_image).fit()
                .into(navHeaderImg);
    }

    private Bundle getBundle() {
        Bundle bundle = new Bundle();
        switch (navItemIndex) {
            case 0:
                bundle.putString(ActivityConfig.TYPE, ActivityConfig.WISHLIST);
                return bundle;
            case 1:
                bundle.putString(ActivityConfig.TYPE, ActivityConfig.WATCHLIST);
                return bundle;
            case 2:
                bundle.putString(ActivityConfig.TYPE, ActivityConfig.FAVORITE);
                return bundle;
            case 3:
                bundle.putString(ActivityConfig.TYPE, ActivityConfig.RATING);
                return bundle;
        }
        return bundle;
    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_wishlist:
                        navItemIndex = 0;
                        break;
                    case R.id.nav_watchlist:
                        navItemIndex = 1;
                        break;
                    case R.id.nav_favorite:
                        navItemIndex = 2;
                        break;
                    case R.id.nav_rating:
                        navItemIndex = 3;
                        break;
                    case R.id.nav_friends:
                        Intent intent = new Intent(MainActivity.this,FriendsActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.nav_about_sign_out:
                        AuthUI.getInstance().signOut(MainActivity.this);
                        mDrawerLayout.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);
                Bundle bundle = getBundle();
                Intent intent = new Intent(MainActivity.this,UserActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            }
        });
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
