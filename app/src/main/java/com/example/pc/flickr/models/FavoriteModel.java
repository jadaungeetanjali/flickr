package com.example.pc.flickr.models;

/**
 * Created by deepn on 9/21/2017.
 */

public class FavoriteModel{
    private String id;
    private String itemId;
    private String itemName;
    private String imgUrl;
    private String userId;

    public FavoriteModel(String userId, String itemId, String itemName, String imgUrl){
        this.userId = userId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.imgUrl = imgUrl;
    }

    public FavoriteModel(String userId, String id,String itemId, String itemType, String itemName, String imgUrl){
        this.userId = userId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.imgUrl = imgUrl;
        this.id = id;

    }
    public String getId() {
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

    public String getUserId() {
        return userId;
    }
}
