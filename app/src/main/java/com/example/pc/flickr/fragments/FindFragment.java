package com.example.pc.flickr.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pc.flickr.FriendListActivity;
import com.example.pc.flickr.R;
import com.example.pc.flickr.models.UserModel;
import com.example.pc.flickr.services.FirebaseCurd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<UserModel> findArrayList;
    private FindAdapter findAdapter;

    public FindFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_find, container, false);


        recyclerView = (RecyclerView) rootView.findViewById(R.id.find_fragment_recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        findArrayList = new ArrayList<>();

        FirebaseCurd firebaseCurd = new FirebaseCurd(getActivity());
        DatabaseReference usersReference = firebaseCurd.getmUsersReference();
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UserModel userModel = postSnapshot.getValue(UserModel.class);
                    findArrayList.add(userModel);
                }
                findAdapter = new FindAdapter(findArrayList);
                recyclerView.setAdapter(findAdapter);
                //progressBar.setVisibility(View.GONE);
                //recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        return rootView;
    }

    private class FindAdapter extends RecyclerView.Adapter<FindAdapter.findViewHolder> {
        private ArrayList<UserModel> arrayList;

        class findViewHolder extends RecyclerView.ViewHolder {
            ImageView friendImageView,addFriendImageView;
            TextView friendNameTextView, friendEmailTextView;

            //ProgressBar userListImageViewProgressBar;
            public findViewHolder(View itemView) {
                super(itemView);
                friendNameTextView = (TextView) itemView.findViewById(R.id.find_listItem_name_textView);
                friendEmailTextView = (TextView) itemView.findViewById(R.id.find_listItem_email_textView);
                addFriendImageView = (ImageView) itemView.findViewById(R.id.find_listItem_remove_imageView);
                //userListImageViewProgressBar = (ProgressBar) itemView.findViewById(R.id.user_listview_item_poster_progressBar);
            }
        }

        public FindAdapter(ArrayList<UserModel> arrayList) {
            this.arrayList = arrayList;
        }

        @Override
        public findViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_listview_item, parent, false);
            return new findViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final findViewHolder holder, int position) {
            final UserModel userModel = arrayList.get(position);
            holder.friendNameTextView.setText(userModel.getUserName());
            holder.friendEmailTextView.setText(userModel.getUserEmail());
            /*
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
                    });*/
            holder.addFriendImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseCurd firebaseCurd = new FirebaseCurd(getActivity());
                    DatabaseReference friendsReference = firebaseCurd.getmFriendsReference();
                    firebaseCurd.addFriendModel(userModel);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }
}