package com.example.pc.flickr.models;

/**
 * Created by deepn on 9/21/2017.
 */

public class FavoriteModel{
    private String itemId;
    private String itemName;
    private String itemType;
    private String imgUrl;
    private String userId;

    public FavoriteModel(){}

    public FavoriteModel(String itemId, String itemType, String itemName, String imgUrl){
        this.itemId = itemId;
        this.itemName = itemName;
        this.imgUrl = imgUrl;
        this.itemType = itemType;

    }

    public FavoriteModel(String userId,String itemId, String itemType, String itemName, String imgUrl){
        this.userId = userId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.imgUrl = imgUrl;
        this.itemType = itemType;

    }

    public String getItemType() {
        return itemType;
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
}
