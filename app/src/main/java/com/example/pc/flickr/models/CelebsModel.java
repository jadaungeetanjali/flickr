package com.example.pc.flickr.models;

/**
 * Created by PC on 9/28/2017.
 */

public class CelebsModel {
    public String title;
    public String biography;
    public String dateOfBirth;
    public String placeOfBirth;
    public String profile_url;
    public String alsoKnownAs;
    public CelebsModel(String title, String biography, String dateOfBirth, String placeOfBirth, String profile_url, String alsoKnownAs){
        this.title = title;
        this.biography = biography;
        this.dateOfBirth = dateOfBirth;
        this.placeOfBirth = placeOfBirth;
        this.profile_url = profile_url;
        this.alsoKnownAs = alsoKnownAs;
    }

    public String getTitle() {
        return title;
    }

    public String getBiography() {
        return biography;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public String getAlsoKnownAs() {
        return alsoKnownAs;
    }
}
