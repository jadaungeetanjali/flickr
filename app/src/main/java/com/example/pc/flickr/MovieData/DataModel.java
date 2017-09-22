package com.example.pc.flickr.MovieData;

/**
 * Created by PC on 9/22/2017.
 */

public class DataModel {
    public String title;
    public String overview;
    public String vote_avg;
    public String tagline;
    public String release_date;
    public String language;
    public String img_url;

    public DataModel(String title, String overview, String vote_avg,
                     String tagline, String release_date, String language, String img_url) {
        this.title = title;
        this.overview = overview;
        this.vote_avg = vote_avg;
        this.tagline = tagline;
        this.release_date = release_date;
        this.language = language;
        this.img_url = img_url;
    }

    public String getTitle() {

        return title;
    }
    public String getOverview() {
        return overview;
    }

    public String getVote_avg() {
        return vote_avg;
    }

    public String getTagline() {
        return tagline;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getLanguage() {
        return language;
    }

    public String getImg_url() {
        return img_url;
    }
}

