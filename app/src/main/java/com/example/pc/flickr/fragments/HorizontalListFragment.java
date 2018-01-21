package com.example.pc.flickr.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc.flickr.adapters.MainAdapters;
import com.example.pc.flickr.R;


import java.util.ArrayList;
import java.util.Arrays;

public class HorizontalListFragment extends Fragment {
    public MainAdapters.MainParentAdapter mAdapter;
    String type;
    RecyclerView recyclerView;

    public HorizontalListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_horizontal_list, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_main);
        ArrayList<String> arrayList = new ArrayList<String>();
        String urlHeading[] = this.getArguments().getStringArray("urlHeading");
        ArrayList<String> urlHeadingList = new ArrayList<>(Arrays.asList(urlHeading));
        type = this.getArguments().getString("type");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MainAdapters.MainParentAdapter(urlHeadingList,getContext(),type);
        recyclerView.setAdapter(mAdapter);
        return rootView;
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.v("lifecycle","onPaused called");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.v("lifecycle","onStop called");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView.setAdapter(null);
        recyclerView.setLayoutManager(null);
        recyclerView.removeAllViewsInLayout();
        recyclerView.getRecycledViewPool().clear();
        Log.v("lifecycle","onDestroy called");
    }

    // Called at the end of the full lifetime.
    @Override
    public void onDestroy(){
        super.onDestroy();

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
