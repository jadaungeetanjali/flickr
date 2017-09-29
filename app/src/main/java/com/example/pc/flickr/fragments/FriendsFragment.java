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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pc.flickr.FriendListActivity;
import com.example.pc.flickr.R;
import com.example.pc.flickr.models.UserModel;
import com.example.pc.flickr.services.FirebaseCurd;
import com.firebase.ui.auth.User;
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
public class FriendsFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<UserModel> friendsArrayList;
    private FriendsAdapter friendsAdapter;
    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_friends, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.friends_fragment_recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        friendsArrayList = new ArrayList<>();

        FirebaseCurd firebaseCurd =new  FirebaseCurd(getActivity());
        DatabaseReference usersReference = firebaseCurd.getmUsersReference();
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UserModel userModel = postSnapshot.getValue(UserModel.class);
                    friendsArrayList.add(userModel);
                }
                friendsAdapter = new FriendsAdapter(friendsArrayList);
                recyclerView.setAdapter(friendsAdapter);
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
    private class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.friendsViewHolder> {
        private ArrayList<UserModel> arrayList;

        class friendsViewHolder extends RecyclerView.ViewHolder {
            ImageView friendImageView;
            TextView friendNameTextView,friendEmailTextView;
            //ProgressBar userListImageViewProgressBar;
            public friendsViewHolder(View itemView) {
                super(itemView);
                friendNameTextView = (TextView) itemView.findViewById(R.id.friends_listItem_name_textView);
                friendEmailTextView = (TextView) itemView.findViewById(R.id.friends_listItem_email_textView);
                //userListImageViewProgressBar = (ProgressBar) itemView.findViewById(R.id.user_listview_item_poster_progressBar);
            }
        }

        public FriendsAdapter(ArrayList<UserModel> arrayList) {
            this.arrayList = arrayList;
        }

        @Override
        public friendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_listview_item, parent, false);
            return new friendsViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final friendsViewHolder holder, int position) {
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
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(),FriendListActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("id",userModel.getUserId());
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }
}
