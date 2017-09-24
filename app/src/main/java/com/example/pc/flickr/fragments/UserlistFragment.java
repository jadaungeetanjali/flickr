package com.example.pc.flickr.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc.flickr.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserlistFragment extends Fragment {
    public String type;

    public UserlistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_userlist, container, false);
        type = this.getArguments().getString("type");
        Log.i("type",type);
        return rootView;
    }

}
