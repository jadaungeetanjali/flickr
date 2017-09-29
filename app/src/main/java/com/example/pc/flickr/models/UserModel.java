package com.example.pc.flickr.models;

import com.firebase.ui.auth.User;

/**
 * Created by deepn on 9/27/2017.
 */

public class UserModel {
    private String userId;
    private String userName;
    private String userEmail;
    private String userImgUrl;

    public UserModel(){}

    public UserModel(String userId,String userName,String userEmail,String userImgUrl){
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userImgUrl = userImgUrl;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserImgUrl() {
        return userImgUrl;
    }

    public String getUserName() {
        return userName;
    }
}
