package com.example.pc.flickr.models;

/**
 * Created by deepn on 9/21/2017.
 */

public class SimilarItemModel{
    public String similarItemName;
    public String similarItemVoteAverage;
    public String similarItemimage;
    public String similarItemId;

    public SimilarItemModel(String id,String name,String voteAverage,String image){
        this.similarItemName = name;
        this.similarItemVoteAverage = voteAverage;
        this.similarItemimage = image;
        this.similarItemId = id;
    }

    public String getSimilarItemId() {
        return similarItemId;
    }

    public String getSimilarItemimage() {
        return similarItemimage;
    }

    public String getSimilarItemName() {
        return similarItemName;
    }

    public String getSimilarItemVoteAverage() {
        return similarItemVoteAverage;
    }
}