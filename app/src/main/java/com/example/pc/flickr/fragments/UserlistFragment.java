package com.example.pc.flickr.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pc.flickr.R;
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
    public UserListAdapter userListAdapter;
    public RecyclerView recyclerView;
    public ArrayList<WishListModel> arrayList;
    public DatabaseReference databaseReference;

    public UserlistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_userlist, container, false);

        arrayList = new ArrayList<>();
        type = this.getArguments().getString("type");

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.user_toolbar);
        toolbar.setTitle(type);
        FirebaseCurd firebaseCurd = new FirebaseCurd();
        switch (type){
            case "WatchList":
                databaseReference = firebaseCurd.getmWatchListReference();
                break;
            case "WishList":
                databaseReference = firebaseCurd.getmWishListReference();
                break;
        }


        recyclerView = (RecyclerView) rootView.findViewById(R.id.user_listview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    WishListModel wishListModel = postSnapshot.getValue(WishListModel.class);
                    arrayList.add(wishListModel);
                }
                userListAdapter = new UserListAdapter(arrayList);
                recyclerView.setAdapter(userListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        return rootView;
    }

    private class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.userListViewHolder> {
        private ArrayList<WishListModel> userListArrayList;

        class userListViewHolder extends RecyclerView.ViewHolder {
            ImageView userListImageView;
            TextView userListNameTextView;
            TextView userListRatingTextView;
            public userListViewHolder(View itemView) {
                super(itemView);
                userListNameTextView = (TextView) itemView.findViewById(R.id.user_listview_item_name);
                userListImageView = (ImageView) itemView.findViewById(R.id.user_listview_item_poster);
                userListRatingTextView = (TextView) itemView.findViewById(R.id.user_listview_item_rating_value);
            }
        }

        public UserListAdapter(ArrayList<WishListModel> arrayList) {
            this.userListArrayList = arrayList;
        }

        @Override
        public userListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_listview_item, parent, false);
            return new userListViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(userListViewHolder holder, int position) {
            WishListModel WishListModel = userListArrayList.get(position);
            holder.userListNameTextView.setText(WishListModel.getItemName());
            holder.userListRatingTextView.setText(WishListModel.getItemRating());
            Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500" + WishListModel.getImgUrl()).into(holder.userListImageView);
        }

        @Override
        public int getItemCount() {
            return userListArrayList.size();
        }
    }

}
