package com.example.pc.flickr.MovieData;

/**
 * Created by PC on 9/22/2017.
 */

public class CastModel {
    public String name;
    public String character;
    public String image;

    public CastModel(String name,String character,String image){
        this.name = name;
        this.character = character;
        this.image = image;
    }

    public String getCharacter() {
        return character;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }
}
