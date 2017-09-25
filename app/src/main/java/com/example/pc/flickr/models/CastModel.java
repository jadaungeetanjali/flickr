package com.example.pc.flickr.models;

/**
 * Created by deepn on 9/21/2017.
 */

public class CastModel{
    public String name;
    public String character;
    public String image;
    public String id;

    public CastModel(String id,String name,String character,String image){
        this.name = name;
        this.character = character;
        this.image = image;
        this.id = id;
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

    public String getId() {
        return id;
    }
}
