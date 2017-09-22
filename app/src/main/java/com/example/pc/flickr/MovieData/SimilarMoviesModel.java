package com.example.pc.flickr.MovieData;

/**
 * Created by PC on 9/22/2017.
 */

public class SimilarMoviesModel {
    public String similarMovieName;
    public String similarMovieVoteAverage;
    public String similarMovieimage;

    public SimilarMoviesModel(String name, String voteAverage, String image){
        this.similarMovieName = name;
        this.similarMovieVoteAverage = voteAverage;
        this.similarMovieimage = image;
    }

    public String getSimilarMovieVoteAverage() {
        return similarMovieVoteAverage;
    }

    public String getSimilarMovieName() {
        return similarMovieName;
    }

    public String getSimilarMovieimage() {
        return similarMovieimage;
    }
}
