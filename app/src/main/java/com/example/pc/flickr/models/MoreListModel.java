package com.example.pc.flickr.models;

/**
 * Created by PC on 9/25/2017.
 */

public class MoreListModel {
    public String name;
    public String releaseDate;
    public String image;
    public String rating;
    public String id;

    public MoreListModel(String name,String releaseDate,String image, String rating, String id){
        this.name = name;
        this.releaseDate = releaseDate;
        this.image = image;
        this.rating = rating;
        this.id = id;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getRating() {
        return rating;
    }

    public String getId() {
        return id;
    }
}
