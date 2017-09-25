package com.example.pc.flickr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.flickr.fragments.HorizontalListFragment;
import com.example.pc.flickr.fragments.UserlistFragment;
import com.example.pc.flickr.models.NavigationModel;
import com.example.pc.flickr.models.WishListModel;
import com.example.pc.flickr.services.FetchApiService;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private static final int RC_SIGN_IN =1;
    public Fragment currentFragment;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] mDrawerStringList,mDrawerIconList;
    private DrawerLayout mDrawerLayout;
    private RecyclerView mDrawerList;
    private NavigationListAdapter navigationListAdapter;
    private CharSequence mTitle,mDrawerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        Intent intent = new Intent(this,FetchApiService.class);
        startService(intent);
        mFirebaseAuth = FirebaseAuth.getInstance();

        // Navigation Drawer .............................
        mDrawerStringList = getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerIconList= getResources().getStringArray(R.array.navigation_drawer_icons_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (RecyclerView) findViewById(R.id.main_left_drawer);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mDrawerList.setLayoutManager(mLayoutManager);
        mDrawerList.setItemAnimator(new DefaultItemAnimator());
        ArrayList<NavigationModel> navigationModels = new ArrayList<>();
        for (int i = 0; i <mDrawerStringList.length;i++){
            int resourceId = this.getResources().
                    getIdentifier(mDrawerIconList[i], "string", this.getPackageName());
            NavigationModel navigationModel = new NavigationModel(resourceId,mDrawerStringList[i]);
            navigationModels.add(navigationModel);
        }
        navigationListAdapter = new NavigationListAdapter(navigationModels);
        mDrawerList.setAdapter(navigationListAdapter);
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        // Navigation Drawer till here......................

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
            case R.id.user_menu_option:
                Intent intent = new Intent(this,UserActivity.class);
                startActivity(intent);
                return true;
            case R.id.user_sign_out:
                AuthUI.getInstance().signOut(this);
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
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
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
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("user_id", user_id);
        editor.putString("user_email", user_email);
        editor.putString("user_name", user_name);
        editor.commit();
        editor.apply();
    }

    private class NavigationListAdapter extends RecyclerView.Adapter<NavigationListAdapter.navigationListViewHolder> {
        private ArrayList<NavigationModel> navigationListArrayList;

        class navigationListViewHolder extends RecyclerView.ViewHolder {
            ImageView navigationListImageView;
            TextView navigationListTextView;

            public navigationListViewHolder(View itemView) {
                super(itemView);
                navigationListTextView = (TextView) itemView.findViewById(R.id.navigation_textView);
                navigationListImageView = (ImageView) itemView.findViewById(R.id.navigation_imageViewIcon);
            }
        }

        public NavigationListAdapter(ArrayList<NavigationModel> arrayList) {
            this.navigationListArrayList = arrayList;
        }

        @Override
        public navigationListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.navigation_list_item, parent, false);
            return new navigationListViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(navigationListViewHolder holder, int position) {
            NavigationModel navigationModel = navigationListArrayList.get(position);
            holder.navigationListTextView.setText(navigationModel.getName());
            holder.navigationListImageView.setImageResource(navigationModel.getIcon());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String[] type ={"WatchList","Wishlist","Rating","Favorite"};

                }
            });
        }

        @Override
        public int getItemCount() {
            return navigationListArrayList.size();
        }
    }
}
