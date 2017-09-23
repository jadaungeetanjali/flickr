package com.example.pc.flickr.models;

/**
 * Created by deepn on 9/23/2017.
 */

public class VideoModel {
    public String name;
    public String type;
    public String image;
    public String id;

    public VideoModel(String id,String name,String type,String image){
        this.name = name;
        this.type = type;
        this.image = image;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getImage() {
        return image;
    }

    public String getId() {
        return id;
    }
}
