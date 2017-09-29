package com.example.pc.flickr.models;

/**
 * Created by deepn on 9/28/2017.
 */

public class FriendModel {
    private String friendId;
    private String friendName;
    private String friendEmail;
    private String friendImgUrl;

    public FriendModel(){}

    public FriendModel(String friendId, String friendName, String friendEmail, String friendImgUrl){
        this.friendId = friendId;
        this.friendName = friendName;
        this.friendEmail = friendEmail;
        this.friendImgUrl = friendImgUrl;
    }

    public String getFriendEmail() {
        return friendEmail;
    }

    public String getFriendId() {
        return friendId;
    }

    public String getFriendImgUrl() {
        return friendImgUrl;
    }

    public String getFriendName() {
        return friendName;
    }
}
