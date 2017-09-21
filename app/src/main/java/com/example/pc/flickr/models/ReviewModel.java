package com.example.pc.flickr.models;

/**
 * Created by deepn on 9/21/2017.
 */

public class ReviewModel{
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