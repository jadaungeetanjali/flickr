package com.example.pc.flickr.fragments;


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

import com.example.pc.flickr.R;
import com.example.pc.flickr.models.FriendModel;
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
public class RequestFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<FriendModel> requestArrayList;
    private RequestAdapter requestAdapter;
    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_request, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.request_fragment_recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        requestArrayList = new ArrayList<>();

        FirebaseCurd firebaseCurd = new FirebaseCurd(getActivity());
        DatabaseReference requestsReference = firebaseCurd.getmRequestsReference();
        requestsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    FriendModel friendModel = postSnapshot.getValue(FriendModel.class);
                    requestArrayList.add(friendModel);
                }
                requestAdapter = new RequestAdapter(requestArrayList);
                recyclerView.setAdapter(requestAdapter);
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

    private class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.requestViewHolder> {
        private ArrayList<FriendModel> arrayList;

        class requestViewHolder extends RecyclerView.ViewHolder {
            ImageView friendImageView,addFriendImageView;
            TextView friendNameTextView, friendEmailTextView;

            //ProgressBar userListImageViewProgressBar;
            public requestViewHolder(View itemView) {
                super(itemView);
                friendNameTextView = (TextView) itemView.findViewById(R.id.request_listItem_name_textView);
                friendEmailTextView = (TextView) itemView.findViewById(R.id.request_listItem_email_textView);
                addFriendImageView = (ImageView) itemView.findViewById(R.id.request_listItem_add_imageView);
                //userListImageViewProgressBar = (ProgressBar) itemView.requestViewById(R.id.user_listview_item_poster_progressBar);
            }
        }

        public RequestAdapter(ArrayList<FriendModel> arrayList) {
            this.arrayList = arrayList;
        }

        @Override
        public requestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_listview_item, parent, false);
            return new requestViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final requestViewHolder holder, int position) {
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
            holder.addFriendImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseCurd firebaseCurd = new FirebaseCurd(getActivity());
                    firebaseCurd.addFriendModel(friendModel);
                    firebaseCurd.getmRequestsReference().child(friendModel.getFriendId()).removeValue();
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }
}