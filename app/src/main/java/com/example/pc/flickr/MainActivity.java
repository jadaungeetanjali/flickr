package com.example.pc.flickr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        Intent intent = new Intent(this, FetchApiService.class);
        startService(intent);
        mFirebaseAuth = FirebaseAuth.getInstance();

        // Navigation Drawer .............................
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        navHeaderName = (TextView) navHeader.findViewById(R.id.main_drawer_name);
        navHeaderEmail = (TextView) navHeader.findViewById(R.id.main_drawer_email);
        navHeaderImg = (ImageView) navHeader.findViewById(R.id.main_drawer_avatar);
        //activityTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
        //loadNavHeader();
        setUpNavigationView();

        // Navigation Drawer till here......................

        Bundle moviesBundle = new Bundle();
        String[] movieHeading = {"Now Playing", "Popular", "Top Rated", "Upcoming"};
        moviesBundle.putString("type", "movies");
        moviesBundle.putStringArray("urlHeading", movieHeading);
        final HorizontalListFragment moviesFragment = new HorizontalListFragment();
        moviesFragment.setArguments(moviesBundle);

        Bundle tvBundle = new Bundle();
        String[] tvHeading = {"Airing Today", "Popular", "Top Rated", "On The Air"};
        tvBundle.putString("type", "tv");
        tvBundle.putStringArray("urlHeading", tvHeading);
        final HorizontalListFragment tvFragment = new HorizontalListFragment();
        tvFragment.setArguments(tvBundle);

        Bundle celebsBundle = new Bundle();
        String[] celebsHeading = {"Popular"};
        celebsBundle.putString("type", "celebs");
        celebsBundle.putStringArray("urlHeading", celebsHeading);
        final HorizontalListFragment celebsFragment = new HorizontalListFragment();
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
                    onSignedInInitialize(firebaseUser.getDisplayName(), firebaseUser.getUid(), firebaseUser.getEmail());
                } else {
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
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, fragment).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle Drawer Selection
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.user_sign_out:
                AuthUI.getInstance().signOut(this);
            default:
                return super.onOptionsItemSelected(item);
        }
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
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    public void onSignedInInitialize(String user_name, String user_id, String user_email) {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("user_id", user_id);
        editor.putString("user_email", user_email);
        editor.putString("user_name", user_name);
        editor.commit();
        editor.apply();
    }

    private void loadNavHeader() {
        SharedPreferences sharedPref = this.getSharedPreferences("MyPref", 0);
        String user_name = sharedPref.getString("user_name", null);
        String user_email = sharedPref.getString("user_email", null);
        navHeaderName.setText(user_name);
        navHeaderEmail.setText(user_email);
    }

    private Bundle getBundle() {
        Bundle bundle = new Bundle();
        switch (navItemIndex) {
            case 0:
                bundle.putString("type", "WishList");
                return bundle;
            case 1:
                bundle.putString("type", "WatchList");
                return bundle;
            case 2:
                bundle.putString("type", "Favorite");
                return bundle;
            case 3:
                bundle.putString("type", "Rating");
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
                    case R.id.nav_about_sign_out:
                        AuthUI.getInstance().signOut(MainActivity.this);
                        mDrawerLayout.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
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
}
