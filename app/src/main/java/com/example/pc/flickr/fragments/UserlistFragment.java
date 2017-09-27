package com.example.pc.flickr.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pc.flickr.MoviesDetails;
import com.example.pc.flickr.R;
import com.example.pc.flickr.models.FavoriteModel;
import com.example.pc.flickr.models.WishListModel;
import com.example.pc.flickr.services.FirebaseCurd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserlistFragment extends Fragment {
    public String type;
    public WishListAdapter wishListAdapter;
    public FavoriteListAdapter favoriteListAdapter;
    public RecyclerView recyclerView;
    public ArrayList<WishListModel> arrayList;
    public ArrayList<FavoriteModel> favoriteList;
    public DatabaseReference databaseReference;
    private ProgressBar progressBar;

    public UserlistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_userlist, container, false);

        arrayList = new ArrayList<>();
        favoriteList = new ArrayList<>();
        type = this.getArguments().getString("type");

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.user_toolbar);
        toolbar.setTitle(type);
        FirebaseCurd firebaseCurd = new FirebaseCurd(getActivity());
        switch (type){
            case "WatchList":
                databaseReference = firebaseCurd.getmWatchListReference();
                break;
            case "WishList":
                databaseReference = firebaseCurd.getmWishListReference();
                break;
            case "Favorite":
                databaseReference = firebaseCurd.getmFavoriteReference();
                break;
        }


        recyclerView = (RecyclerView) rootView.findViewById(R.id.user_listview);
        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        //recyclerView.setLayoutManager(mLayoutManager);
        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        progressBar = (ProgressBar) rootView.findViewById(R.id.user_listview_progressBar);
        if (type.equals("WatchList")||type.equals("WishList")) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        WishListModel wishListModel = postSnapshot.getValue(WishListModel.class);
                        arrayList.add(wishListModel);
                    }
                    wishListAdapter = new WishListAdapter(arrayList);
                    recyclerView.setAdapter(wishListAdapter);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }
        else if (type.equals("Favorite")){
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.v("output",dataSnapshot.toString());
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Log.v("output2",postSnapshot.toString());
                        FavoriteModel favoriteModel = postSnapshot.getValue(FavoriteModel.class);
                        if (favoriteModel !=null)
                            favoriteList.add(favoriteModel);
                    }
                    favoriteListAdapter = new FavoriteListAdapter(favoriteList);
                    recyclerView.setAdapter(favoriteListAdapter);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }
        return rootView;
    }

    private class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.wishListViewHolder> {
        private ArrayList<WishListModel> userListArrayList;

        class wishListViewHolder extends RecyclerView.ViewHolder {
            ImageView userListImageView;
            TextView userListNameTextView;
            TextView userListRatingTextView;
            ProgressBar userListImageViewProgressBar;
            public wishListViewHolder(View itemView) {
                super(itemView);
                userListNameTextView = (TextView) itemView.findViewById(R.id.user_listview_item_name);
                userListImageView = (ImageView) itemView.findViewById(R.id.user_listview_item_poster);
                userListRatingTextView = (TextView) itemView.findViewById(R.id.user_listview_item_rating_value);
                userListImageViewProgressBar = (ProgressBar) itemView.findViewById(R.id.user_listview_item_poster_progressBar);
            }
        }

        public WishListAdapter(ArrayList<WishListModel> arrayList) {
            this.userListArrayList = arrayList;
        }

        @Override
        public wishListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_listview_item, parent, false);
            return new wishListViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final wishListViewHolder holder, int position) {
            final WishListModel wishListModel = userListArrayList.get(position);
            holder.userListNameTextView.setText(wishListModel.getItemName());
            holder.userListRatingTextView.setText(wishListModel.getItemRating());
            Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500" + wishListModel.getImgUrl())
                    .into(holder.userListImageView,new com.squareup.picasso.Callback() {

                        @Override
                        public void onSuccess() {
                            holder.userListImageViewProgressBar.setVisibility(View.GONE);
                            holder.userListImageView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError() {

                        }
                    });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(),MoviesDetails.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("type",wishListModel.getItemType());
                    mBundle.putString("id",wishListModel.getItemId());
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return userListArrayList.size();
        }
    }

    private class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.favoriteListViewHolder> {
        private ArrayList<FavoriteModel> userListArrayList;

        class favoriteListViewHolder extends RecyclerView.ViewHolder {
            ImageView userListImageView;
            TextView userListNameTextView;
            ProgressBar userListImageViewProgressBar;
            public favoriteListViewHolder(View itemView) {
                super(itemView);
                userListNameTextView = (TextView) itemView.findViewById(R.id.user_listview_item_name);
                userListImageView = (ImageView) itemView.findViewById(R.id.user_listview_item_poster);
                userListImageViewProgressBar = (ProgressBar) itemView.findViewById(R.id.user_listview_item_poster_progressBar);
            }
        }

        public FavoriteListAdapter(ArrayList<FavoriteModel> arrayList) {
            this.userListArrayList = arrayList;
        }

        @Override
        public favoriteListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_listview_item, parent, false);
            return new favoriteListViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final favoriteListViewHolder holder, int position) {
            final FavoriteModel favoriteModel = userListArrayList.get(position);
            holder.userListNameTextView.setText(favoriteModel.getItemName());
            Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500" + favoriteModel.getImgUrl()).
                    into(holder.userListImageView,new com.squareup.picasso.Callback() {

                        @Override
                        public void onSuccess() {
                            holder.userListImageViewProgressBar.setVisibility(View.GONE);
                            holder.userListImageView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError() {

                        }
                    });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(),MoviesDetails.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("type",favoriteModel.getItemType());
                    mBundle.putString("id",favoriteModel.getItemId());
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return userListArrayList.size();
        }
    }

}
