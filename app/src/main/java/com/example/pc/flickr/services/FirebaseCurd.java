package com.example.pc.flickr.services;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.pc.flickr.R;
import com.example.pc.flickr.models.FavoriteModel;
import com.example.pc.flickr.models.FriendModel;
import com.example.pc.flickr.models.UserModel;
import com.example.pc.flickr.models.WishListModel;
import com.firebase.ui.auth.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

/**
 * Created by deepn on 9/21/2017.
 */

public class FirebaseCurd {
    private Activity activity;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mWishListReference;
    private DatabaseReference mFavoriteReference;
    private DatabaseReference mUserReference;
    private DatabaseReference mUsersReference;
    private DatabaseReference mWatchListReference;
    private DatabaseReference mDataReference;
    private DatabaseReference mFriendsReference;
    private DatabaseReference mRequestsReference;
    private DatabaseReference mRequestReference;
    private String user_id,user_name,user_email,user_imgUrl;


    public FirebaseCurd(Activity activity){
        this.activity = activity;
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        SharedPreferences sharedPref = activity.getSharedPreferences("MyPref", 0);
        user_id = sharedPref.getString("user_id",null);
        user_name = sharedPref.getString("user_name", null);
        user_email = sharedPref.getString("user_email",null);
        user_imgUrl = sharedPref.getString("user_imgUrl", null);
        mUserReference = mDatabaseReference.child("User").child(user_id);
        mUsersReference = mDatabaseReference.child("User");
        mFriendsReference = mDatabaseReference.child("Friends").child(user_id);
        mRequestReference = mDatabaseReference.child("Requests");
        mRequestsReference = mDatabaseReference.child("Requests").child(user_id);
        mDataReference = mDatabaseReference.child("Data");
        mWishListReference = mDataReference.child(user_id).child("WishList");
        mWatchListReference = mDataReference.child(user_id).child("WatchList");
        mFavoriteReference = mDataReference.child(user_id).child("Favorite");

    }
    //Get Refrences
    public DatabaseReference getmWatchListReference() {
        return mWatchListReference;
    }

    public DatabaseReference getmDatabaseReference() {
        return mDatabaseReference;
    }

    public DatabaseReference getmFavoriteReference() {
        return mFavoriteReference;
    }

    public DatabaseReference getmUserReference() {
        return mUserReference;
    }

    public DatabaseReference getmWishListReference() {
        return mWishListReference;
    }

    public DatabaseReference getmDataReference() {
        return mDataReference;
    }

    public DatabaseReference getmUsersReference() {
        return mUsersReference;
    }

    public DatabaseReference getmFriendsReference() {
        return mFriendsReference;
    }

    public DatabaseReference getmRequestsReference() {
        return mRequestsReference;
    }


    //Post method of firebase are here
    public void addWishListModel(WishListModel wishListModel){
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId",user_id);
        result.put("itemId", wishListModel.getItemId());
        result.put("itemType", wishListModel.getItemType());
        result.put("itemName", wishListModel.getItemName());
        result.put("imgUrl", wishListModel.getImgUrl());
        result.put("itemRating", wishListModel.getItemRating());
        Log.v("result",result.toString());
        mWishListReference.child(wishListModel.getItemId()).updateChildren(result);
    }

    public void addUserModel(UserModel userModel){
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId",userModel.getUserId());
        result.put("userName", userModel.getUserName());
        result.put("userEmail", userModel.getUserEmail());
        result.put("userImgUrl", userModel.getUserImgUrl());
        mUsersReference.child(userModel.getUserId()).updateChildren(result);
    }

    public void addFriendModel(FriendModel friendModel){
        HashMap<String, Object> result = new HashMap<>();
        result.put("friendId", friendModel.getFriendId());
        result.put("friendName", friendModel.getFriendName());
        result.put("friendEmail", friendModel.getFriendEmail());
        result.put("friendImgUrl", friendModel.getFriendImgUrl());
        mFriendsReference.child(friendModel.getFriendId()).updateChildren(result);
    }

    public void addRequestModel(UserModel friendModel){
        HashMap<String, Object> result = new HashMap<>();
        result.put("friendId", user_id);
        result.put("friendName", user_name);
        result.put("friendEmail", user_email);
        result.put("friendImgUrl", user_imgUrl);
        mRequestReference.child(friendModel.getUserId()).child(user_id).updateChildren(result);
    }

    public void addWatchListModel(WishListModel watchListModel){
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId",user_id);
        result.put("itemId", watchListModel.getItemId());
        result.put("itemType", watchListModel.getItemType());
        result.put("itemName", watchListModel.getItemName());
        result.put("imgUrl", watchListModel.getImgUrl());
        result.put("itemRating", watchListModel.getItemRating());
        Log.v("result",result.toString());
        mWatchListReference.child(watchListModel.getItemId()).updateChildren(result);
    }
    public void addFavoriteModel(FavoriteModel favoriteModel){
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId",user_id);
        result.put("itemId", favoriteModel.getItemId());
        result.put("itemType", favoriteModel.getItemType());
        result.put("itemName", favoriteModel.getItemName());
        result.put("imgUrl", favoriteModel.getImgUrl());
        Log.v("result",result.toString());
        mFavoriteReference.child(favoriteModel.getItemId()).updateChildren(result);
    }


    //Get Methods

    public ArrayList<WishListModel> getWishList(){
        final ArrayList<WishListModel> arrayList = new ArrayList<>();
        mWishListReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    WishListModel wishListModel = postSnapshot.getValue(WishListModel.class);
                    arrayList.add(wishListModel);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        return arrayList;
    }
    public ArrayList<WishListModel> getWatchList(){
        final ArrayList<WishListModel> arrayList = new ArrayList<>();
        mWatchListReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    WishListModel wishListModel = postSnapshot.getValue(WishListModel.class);
                    arrayList.add(wishListModel);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        return arrayList;
    }
}