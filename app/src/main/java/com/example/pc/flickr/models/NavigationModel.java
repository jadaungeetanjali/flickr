package com.example.pc.flickr.models;

/**
 * Created by deepn on 9/24/2017.
 */

public class NavigationModel {

    public int icon;
    public String name;

    // Constructor.
    public NavigationModel(int icon, String name) {

        this.icon = icon;
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }
}