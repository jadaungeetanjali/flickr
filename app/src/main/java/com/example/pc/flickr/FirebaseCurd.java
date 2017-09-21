package com.example.pc.flickr;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by deepn on 9/21/2017.
 */

public class FirebaseCurd {
    private DatabaseReference mDatabase;

    public FirebaseCurd(){
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    //Post method of firebase are here




    private class WhistListModel{
        private int id;
        private String itemId;
        private String itemType;
        private String itemName;
        private String imgUrl;
        private String userId;

        public int getId() {
            return id;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public String getItemId() {
            return itemId;
        }

        public String getItemName() {
            return itemName;
        }

        public String getItemType() {
            return itemType;
        }

        public String getUserId() {
            return userId;
        }
    }
}
