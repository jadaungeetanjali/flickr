package com.example.pc.flickr.json_parsers;

import com.example.pc.flickr.models.CastModel;
import com.example.pc.flickr.models.DetailItemModel;
import com.example.pc.flickr.models.ReviewModel;
import com.example.pc.flickr.models.SimilarItemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by deepn on 9/22/2017.
 */

public class DetailJsonParser {
    public DetailItemModel jsonMovieDetailParser(String jsonMovie)throws JSONException {
        // fetching data in json
        JSONObject movieObject = new JSONObject(jsonMovie);
        String title = movieObject.get("title").toString();
        String overview = movieObject.get("overview").toString();
        String vote_average = movieObject.get("vote_average").toString();
        String tagline = movieObject.get("tagline").toString();
        String release_date = movieObject.get("release_date").toString();
        String language = movieObject.get("original_language").toString();
        String poster = movieObject.get("poster_path").toString();
        //creating object of DetailItemModel class to initialise constructor with movieDetails
        DetailItemModel DetailItemModel=new DetailItemModel(title, overview, vote_average, tagline, release_date, language, poster);
        return DetailItemModel;
    }

    public ArrayList<CastModel> jsonMovieCastParser(String jsonCast) throws JSONException {
        ArrayList<CastModel> castArray = new ArrayList<>();
        JSONObject castObject = new JSONObject(jsonCast);
        JSONArray castList = castObject.getJSONArray("cast");
        for (int i = 0; i < castList.length(); i++) {
            JSONObject cast = castList.getJSONObject(i);
            String itemId = cast.get("id").toString();
            String name = cast.get("name").toString();
            String character = cast.get("character").toString();
            String image = cast.get("profile_path").toString();

            CastModel castModel = new CastModel(itemId,name,character,image);
            castArray.add(castModel);
        }
        return castArray;
    }

    public ArrayList<ReviewModel> jsonMovieReviewsParser(String jsonReviews) throws JSONException {
        ArrayList<ReviewModel> reviewsArray = new ArrayList<>();
        JSONObject reviewsObject = new JSONObject(jsonReviews);
        JSONArray reviewsList = reviewsObject.getJSONArray("results");
        for (int i = 0; i < reviewsList.length(); i++) {
            JSONObject review = reviewsList.getJSONObject(i);
            String author = review.get("author").toString();
            String content = review.get("content").toString();

            ReviewModel reviewModel = new ReviewModel(author, content);
            reviewsArray.add(reviewModel);
        }
        return reviewsArray;
    }

    public ArrayList<SimilarItemModel> jsonSimilarMoviesParser(String jsonSimilarMovies) throws JSONException {
        ArrayList<SimilarItemModel> similarMoviesArray = new ArrayList<>();
        JSONObject similarMoviesObject = new JSONObject(jsonSimilarMovies);
        JSONArray similarMoviesList = similarMoviesObject.getJSONArray("results");
        for (int i = 0; i < similarMoviesList.length(); i++) {
            JSONObject similarMovies = similarMoviesList.getJSONObject(i);
            String similarItemName = similarMovies.get("title").toString();
            String similarItemVoteAverage = similarMovies.get("vote_average").toString();
            String similarItemPoster = similarMovies.get("poster_path").toString();
            String similarItemId = similarMovies.get("id").toString();
            SimilarItemModel SimilarItemModel = new SimilarItemModel(similarItemId, similarItemName, similarItemVoteAverage, similarItemPoster);
            similarMoviesArray.add(SimilarItemModel);
        }
        return similarMoviesArray;
    }
}
