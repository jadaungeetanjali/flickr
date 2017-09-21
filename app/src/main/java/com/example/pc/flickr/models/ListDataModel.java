package com.example.pc.flickr.models;

/**
 * Created by deepn on 9/21/2017.
 */

public class ListDataModel{
    private String name;
    private String popularity;
    private String vote_avg;
    private String img_url;
    private String id;
    private String type;
    private String subType;
    public ListDataModel(String id,String name,String popularity,String vote_avg,String img_url,String type,String subType){
        this.img_url = img_url;
        this.name = name;
        this.vote_avg = vote_avg;
        this.popularity = popularity;
        this.id = id;
        this.type = type;
        this.subType = subType;
    }

    public String getName(){
        return name;
    }
    public String getPopularity(){
        return popularity;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getVote_avg() {
        return vote_avg;
    }

    public String getId() {
        return id;
    }

    public String getSubType() {
        return subType;
    }

    public String getType() {
        return type;
    }
}