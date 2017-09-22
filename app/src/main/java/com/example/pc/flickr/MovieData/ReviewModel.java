package com.example.pc.flickr.MovieData;

/**
 * Created by PC on 9/22/2017.
 */

public class ReviewModel {
    public String author;
    public String content;
    public ReviewModel(String author, String content){
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
