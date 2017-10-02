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
    ArrayList<String> urlList = new ArrayList<>();
    public Connectivity( Activity activity){
        this.mActivity = activity;
    }
    public boolean internetConnectivity(){
        ConnectivityManager connectivityManager = (ConnectivityManager)  mActivity.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if ((networkInfo != null) && networkInfo.isConnected()) {
            return true;
        }
        else{
            return false;
        }

    }

}
