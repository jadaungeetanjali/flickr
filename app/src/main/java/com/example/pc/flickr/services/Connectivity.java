package com.example.pc.flickr.services;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
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
    public void checkNetworkConnection(){
        AlertDialog.Builder builder =new AlertDialog.Builder(mActivity);
        builder.setTitle("No internet Connection");
        builder.setMessage("Please turn on internet connection to continue");
        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mActivity.finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                mActivity.finish();

            }
        });

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
