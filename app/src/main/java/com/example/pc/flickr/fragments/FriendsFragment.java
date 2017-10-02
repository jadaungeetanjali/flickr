package com.example.pc.flickr.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.flickr.FriendListActivity;
import com.example.pc.flickr.R;
import com.example.pc.flickr.models.FriendModel;
import com.example.pc.flickr.services.Connectivity;
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
    private ArrayList<FriendModel> friendsArrayList;
    private FriendsAdapter friendsAdapter;
    private DatabaseReference friendsReference;
    private ValueEventListener valueEventListener;
    private ProgressBar progressBar;
    private boolean internet;

    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_friends, container, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.friends_fragment_progressBar);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.friends_fragment_recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        friendsArrayList = new ArrayList<>();


        FirebaseCurd firebaseCurd = new FirebaseCurd(getActivity());
        friendsReference = firebaseCurd.getmFriendsReference();

        Connectivity connectivity = new Connectivity(getActivity());
        if (connectivity.internetConnectivity()) {
            internet = true;
            valueEventListener = friendsReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        FriendModel friendModel = postSnapshot.getValue(FriendModel.class);
                        friendsArrayList.add(friendModel);
                    }
                    friendsAdapter = new FriendsAdapter(friendsArrayList);
                    recyclerView.setAdapter(friendsAdapter);
                    friendsAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });

        } else {
            internet = false;
            connectivity.checkNetworkConnection();
        }

        return rootView;
    }
    private class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.friendsViewHolder> {
        private ArrayList<FriendModel> arrayList;

        class friendsViewHolder extends RecyclerView.ViewHolder {
            ImageView friendImageView,removeFriend;
            TextView friendNameTextView,friendEmailTextView;
            LinearLayout friendsLinearLayout;

            //ProgressBar userListImageViewProgressBar;
            public friendsViewHolder(View itemView) {
                super(itemView);
                friendNameTextView = (TextView) itemView.findViewById(R.id.friends_listItem_name_textView);
                friendEmailTextView = (TextView) itemView.findViewById(R.id.friends_listItem_email_textView);
                friendsLinearLayout = (LinearLayout) itemView.findViewById(R.id.friends_listItem_linearLayout);
                removeFriend = (ImageView) itemView.findViewById(R.id.friends_listItem_remove_imageView);
                //userListImageViewProgressBar = (ProgressBar) itemView.findViewById(R.id.user_listview_item_poster_progressBar);
            }
        }

        public FriendsAdapter(ArrayList<FriendModel> arrayList) {
            this.arrayList = arrayList;
        }

        @Override
        public friendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_listview_item, parent, false);
            return new friendsViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final friendsViewHolder holder,final int position) {
            final FriendModel friendModel = arrayList.get(position);
            holder.friendNameTextView.setText(friendModel.getFriendName());
            holder.friendEmailTextView.setText(friendModel.getFriendEmail());
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
            holder.friendsLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(),FriendListActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("id",friendModel.getFriendId());
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
            });

            holder.removeFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseCurd firebaseCurd = new FirebaseCurd(getActivity());
                    firebaseCurd.getmFriendsReference().child(friendModel.getFriendId()).removeValue();
                    SharedPreferences sharedPref = getActivity().getSharedPreferences("MyPref", 0);
                    String user_id = sharedPref.getString("user_id",null);
                    firebaseCurd.getmFriendReference().child(friendModel.getFriendId()).child(user_id).removeValue();
                    Toast.makeText(getContext(), "Removed from friends", Toast.LENGTH_SHORT).show();
                    arrayList.remove(position);
                    friendsAdapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (internet)
            friendsReference.removeEventListener(valueEventListener);
    }
}
