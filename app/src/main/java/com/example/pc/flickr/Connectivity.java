package com.example.pc.flickr;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.pc.flickr.fragments.CelebsFragment;
import com.example.pc.flickr.fragments.MoviesFragment;

import java.util.ArrayList;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by PC on 9/30/2017.
 */

public class Connectivity {
    Activity mActivity;
    Context mContext;
    ArrayList<String> urlList = new ArrayList<>();
    public Connectivity(ArrayList<String> urlList, Activity activity, Context context){
      this.urlList = urlList;
        this.mActivity = activity;
        this.mContext = context;
    }

    public boolean celebConnectivity(){
        ConnectivityManager connectivityManager = (ConnectivityManager)  mActivity.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if ((networkInfo != null) && networkInfo.isConnected()) {
            CelebsFragment.FetchTask fetchCelebsData = new CelebsFragment(). new FetchTask();
            fetchCelebsData.execute(urlList.get(0), urlList.get(1), urlList.get(2));
            return true;
        }
        else{
            Toast.makeText(mContext, "Please Connect to internet...", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    public boolean detailConnectivity(){
        ConnectivityManager connectivityManager = (ConnectivityManager)  mActivity.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if ((networkInfo != null) && networkInfo.isConnected()) {
           /* internet_connectivity.setVisibility(View.GONE);
            ScrollView scrollView = (ScrollView) rootView.findViewById(R.id.detail_movie_scrollView);
            scrollView.setVisibility(View.VISIBLE);  */
            MoviesFragment.FetchTask callMovieData = new MoviesFragment().new FetchTask();
            callMovieData.execute(urlList.get(0), urlList.get(1), urlList.get(2), urlList.get(3),urlList.get(4));
            return true;
        }
        else{
            Toast.makeText(mContext, "Please Connect to internet...", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

}
