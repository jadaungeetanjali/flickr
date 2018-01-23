package com.example.pc.flickr.fragments;


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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.flickr.R;
import com.example.pc.flickr.models.FriendModel;
import com.example.pc.flickr.models.UserModel;
import com.example.pc.flickr.services.Connectivity;
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
public class FindFragment extends Fragment {
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private ArrayList<UserModel> findArrayList;
    private FindAdapter findAdapter;
    private DatabaseReference friendsReference, usersReference;
    private ValueEventListener valueEventListener, userValueListner;
    private ArrayList<FriendModel> friendArrayList;
    private boolean internet;

    public FindFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_find, container, false);

        progressBar = (ProgressBar) rootView.findViewById(R.id.find_fragment_progressBar);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.find_fragment_recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        findArrayList = new ArrayList<>();
        friendArrayList = new ArrayList<>();

        FirebaseCurd firebaseCurd = new FirebaseCurd(getActivity());
        usersReference = firebaseCurd.getmUsersReference();
        friendsReference = firebaseCurd.getmFriendsReference();
        Connectivity connectivity = new Connectivity(getActivity());
        if (connectivity.internetConnectivity()) {
            internet = true;
            valueEventListener = friendsReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        friendArrayList.add(postSnapshot.getValue(FriendModel.class));
                    }
                    userValueListner = usersReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            findArrayList = getData(dataSnapshot);
                            findAdapter = new FindAdapter(findArrayList);
                            recyclerView.setAdapter(findAdapter);
                            findAdapter.notifyDataSetChanged();
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

    private class FindAdapter extends RecyclerView.Adapter<FindAdapter.findViewHolder> {
        private ArrayList<UserModel> arrayList;

        class findViewHolder extends RecyclerView.ViewHolder {
            ImageView friendImageView, addFriendImageView;
            TextView friendNameTextView, friendEmailTextView;

            //ProgressBar userListImageViewProgressBar;
            public findViewHolder(View itemView) {
                super(itemView);
                friendNameTextView = (TextView) itemView.findViewById(R.id.find_listItem_name_textView);
                friendEmailTextView = (TextView) itemView.findViewById(R.id.find_listItem_email_textView);
                addFriendImageView = (ImageView) itemView.findViewById(R.id.find_listItem_remove_imageView);
                friendImageView = (ImageView) itemView.findViewById(R.id.find_listItem_avatar_imageView);
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
        public void onBindViewHolder(final findViewHolder holder, final int position) {
            final UserModel userModel = arrayList.get(position);
            holder.friendNameTextView.setText(userModel.getUserName());
            holder.friendEmailTextView.setText(userModel.getUserEmail());
            Picasso.with(getContext()).load("" + userModel.getUserImgUrl()).into(holder.friendImageView);
            holder.addFriendImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseCurd firebaseCurd = new FirebaseCurd(getActivity());
                    DatabaseReference friendsReference = firebaseCurd.getmRequestsReference();
                    firebaseCurd.addRequestModel(userModel);
                    Toast.makeText(getContext(), "Friend Request sent!!!", Toast.LENGTH_SHORT).show();
                    arrayList.remove(position);
                    findAdapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    private ArrayList<UserModel> getData(DataSnapshot dataSnapshot) {
        ArrayList<UserModel> arrayList = new ArrayList<>();
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            UserModel userModel = postSnapshot.getValue(UserModel.class);
            SharedPreferences sharedPref = getContext().getSharedPreferences("MyPref", 0);
            String user_id = sharedPref.getString("user_id", null);
            if (friendArrayList.size() > 0) {
                for (int i = 0; i < friendArrayList.size(); i++) {
                    if (friendArrayList.get(i).getFriendId().equals(userModel.getUserId()) ||

                            user_id.equals(userModel.getUserId())) {

                    } else {
                        arrayList.add(userModel);
                    }
                }
            } else {
                if (userModel.getUserId().equals(user_id)) {

                } else {
                    arrayList.add(userModel);
                }
            }
        }
        return arrayList;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (internet) {
            friendsReference.removeEventListener(valueEventListener);
            usersReference.removeEventListener(userValueListner);
        }
    }
}