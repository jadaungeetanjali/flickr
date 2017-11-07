package com.example.pc.flickr.services;

/**
 * Created by Deepanshu on 11/7/2017.
 */

public interface AsyncTaskCompleteListener<T> {
    public void onTaskComplete(T result);
}
