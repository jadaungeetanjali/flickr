package com.example.pc.flickr.services;

import android.util.Log;

import com.example.pc.flickr.models.FavoriteModel;
import com.example.pc.flickr.models.WishListModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

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

    //Post method of firebase are here
    public void addWhistListModel(WishListModel wishListModel){
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid",wishListModel.getUserId());
        result.put("itemId", wishListModel.getItemId());
        result.put("itemType", wishListModel.getItemType());
        result.put("itemName", wishListModel.getItemName());
        result.put("itemImgUrl", wishListModel.getImgUrl());
        result.put("itemRating", wishListModel.getItemRating());
        Log.v("result",result.toString());
        mWishListReference.child(wishListModel.getItemId()).updateChildren(result);
    }

    public void addWatchListModel(WishListModel watchListModel){
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid",watchListModel.getUserId());
        result.put("id", watchListModel.getItemId());
        result.put("type", watchListModel.getItemType());
        result.put("name", watchListModel.getItemName());
        result.put("imgUrl", watchListModel.getImgUrl());
        result.put("rating", watchListModel.getItemRating());
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

}