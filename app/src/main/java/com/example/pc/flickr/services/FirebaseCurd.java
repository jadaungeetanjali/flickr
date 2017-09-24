package com.example.pc.flickr.services;

import android.util.Log;

import com.example.pc.flickr.models.FavoriteModel;
import com.example.pc.flickr.models.WishListModel;
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
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mWishListReference;
    private DatabaseReference mFavoriteReference;
    private DatabaseReference mUserReference;
    private DatabaseReference mWatchListReference;

    public FirebaseCurd(){
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mUserReference = mDatabaseReference.child("Users").child("tyagideepu133");
        mWishListReference = mUserReference.child("WishList");
        mWatchListReference = mUserReference.child("WatchList");
        mFavoriteReference = mUserReference.child("Favorite");

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

    //Post method of firebase are here
    public void addWhistListModel(WishListModel wishListModel){
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId",wishListModel.getUserId());
        result.put("itemId", wishListModel.getItemId());
        result.put("itemType", wishListModel.getItemType());
        result.put("itemName", wishListModel.getItemName());
        result.put("imgUrl", wishListModel.getImgUrl());
        result.put("itemRating", wishListModel.getItemRating());
        Log.v("result",result.toString());
        mWishListReference.child(wishListModel.getItemId()).updateChildren(result);
    }

    public void addWatchListModel(WishListModel watchListModel){
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId",watchListModel.getUserId());
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
        result.put("uid",favoriteModel.getUserId());
        result.put("id", favoriteModel.getItemId());
        result.put("name", favoriteModel.getItemName());
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