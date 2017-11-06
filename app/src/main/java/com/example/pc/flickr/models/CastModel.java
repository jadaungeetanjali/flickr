package com.example.pc.flickr.models;

/**
 * Created by deepn on 9/21/2017.
 */

public class CastModel{
    public String name;
    public String character;
    public String image;
    public String id;

    public void setCharacter(String character) {
        this.character = character;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
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
