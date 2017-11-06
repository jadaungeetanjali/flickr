package com.example.pc.flickr.models;

/**
 * Created by deepn on 9/21/2017.
 */

public class SimilarItemModel{
    public String similarItemimage;
    public String similarItemId;


    public void setSimilarItemId(String similarItemId) {
        this.similarItemId = similarItemId;
    }

    public void setSimilarItemimage(String similarItemimage) {
        this.similarItemimage = similarItemimage;
    }

    public String getSimilarItemId() {
        return similarItemId;
    }

    public String getSimilarItemimage() {
        return similarItemimage;
    }

}