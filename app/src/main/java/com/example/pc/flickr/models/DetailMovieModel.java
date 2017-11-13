package com.example.pc.flickr.models;

/**
 * Created by Deepanshu on 11/6/2017.
 */

public class DetailMovieModel {
    private String adult;
    private String generes;
    private String language;
    private String title;
    private String overview;
    private String voteAvg;
    private String releaseDate;
    private String runtime;
    private String releasedStatus;
    private String posterPath;


    public void setAdult(String adult) {
        this.adult = adult;
    }

    public void setGeneres(String generes) {
        this.generes = generes;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setReleasedStatus(String releasedStatus) {
        this.releasedStatus = releasedStatus;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVoteAvg(String voteAvg) {
        this.voteAvg = voteAvg;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getAdult() {
        return adult;
    }

    public String getGeneres() {
        return generes;
    }

    public String getLanguage() {
        return language;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getReleasedStatus() {
        return releasedStatus;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getTitle() {
        return title;
    }

    public String getVoteAvg() {
        return voteAvg;
    }

    public String getPosterPath() {
        return posterPath;
    }
}
