package com.example.pc.flickr.models;

/**
 * Created by deepn on 9/21/2017.
 */

public class WishListModel{
    private String itemId;
    private String itemType;
    private String itemName;
    private String imgUrl;
    private String userId;
    private String itemRating;

    public WishListModel(){}

    public WishListModel(String userId, String itemId, String itemType, String itemName, String imgUrl, String rating){
        this.userId = userId;
        this.itemId = itemId;
        this.itemType = itemType;
        this.itemName = itemName;
        this.imgUrl = imgUrl;
        this.itemRating = rating;
    }

    public String getItemRating() {
        return itemRating;
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