package com.example.pc.flickr.json_parsers;

/**
 * Created by Deepanshu on 11/6/2017.
 */

import com.example.pc.flickr.models.CastModel;
import com.example.pc.flickr.models.DetailItemModel;
import com.example.pc.flickr.models.DetailMovieModel;
import com.example.pc.flickr.models.ReviewModel;
import com.example.pc.flickr.models.SimilarItemModel;
import com.example.pc.flickr.models.VideoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailMovieJsonParser {

    public DetailMovieModel jsonMovieDetailParser(String jsonMovie)throws JSONException {
        // fetching data in json
        JSONObject movieObject = new JSONObject(jsonMovie);
        String adult = movieObject.get("adult").toString();
        JSONArray genresMovies = movieObject.getJSONArray("genres");
        String genres = genresMovies.getJSONObject(0).get("name").toString();
        String language = movieObject.get("original_language").toString();
        String title = movieObject.get("title").toString();
        String overview = movieObject.get("overview").toString();
        String voteAverage = movieObject.get("vote_average").toString();
        String releaseDate = movieObject.get("release_date").toString();
        String runtime = movieObject.get("runtime").toString();
        String releasedStatus = movieObject.get("status").toString();
        //Converting runtime from 139 to 2:19
        if (runtime != null){
            int hours = Integer.parseInt(runtime)/60;
            int minutes = Integer.parseInt(runtime) - hours * 60;
            runtime = hours + ":" + minutes;
        }
        //Setting values in detailMovieModel
        DetailMovieModel detailMovieModel = new DetailMovieModel();
        detailMovieModel.setAdult(adult);
        detailMovieModel.setGeneres(genres);
        detailMovieModel.setLanguage(language);
        detailMovieModel.setTitle(title);
        detailMovieModel.setOverview(overview);
        detailMovieModel.setVoteAvg(voteAverage);
        detailMovieModel.setReleaseDate(releaseDate);
        detailMovieModel.setRuntime(runtime);
        detailMovieModel.setReleasedStatus(releasedStatus);
        return detailMovieModel;
    }

    public ArrayList<CastModel> jsonCastParser(String jsonCast) throws JSONException {
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
    public ArrayList<VideoModel> jsonVideoParser(String jsonVideos) throws JSONException {
        ArrayList<VideoModel> videoArray = new ArrayList<>();
        JSONObject videoObject = new JSONObject(jsonVideos);
        JSONArray videoList = videoObject.getJSONArray("results");
        for (int i = 0; i < videoList.length(); i++) {
            JSONObject video = videoList.getJSONObject(i);
            String itemId = video.get("id").toString();
            String name = video.get("name").toString();
            String type = video.get("type").toString();
            String image = video.get("key").toString();

            VideoModel videoModel = new VideoModel(itemId,name,type,image);
            videoArray.add(videoModel);
        }
        return videoArray;
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

    public ArrayList<SimilarItemModel> jsonSimilarParser(String jsonSimilarMovies) throws JSONException {
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
