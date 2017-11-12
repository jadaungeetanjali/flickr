package com.example.pc.flickr.json_parsers;

import com.example.pc.flickr.models.CastModel;
import com.example.pc.flickr.models.DetailItemModel;
import com.example.pc.flickr.models.ReviewModel;
import com.example.pc.flickr.models.SimilarItemModel;
import com.example.pc.flickr.models.VideoModel;
import com.example.pc.flickr.util.movies.MovieCastJsonConfig;
import com.example.pc.flickr.util.movies.MovieVideosJsonConfig;
import com.example.pc.flickr.util.movies.MoviesSimilarJsonConfig;
import com.example.pc.flickr.util.tv.TvDetailJsonConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by deepn on 9/22/2017.
 */

public class DetailTvJsonParser {


    public ArrayList<CastModel> jsonTVCastParser(String jsonCast) throws JSONException {
        ArrayList<CastModel> castArray = new ArrayList<>();
        MovieCastJsonConfig jsonConfig = new MovieCastJsonConfig();

        JSONObject castObject = new JSONObject(jsonCast);
        //Iterating through cast
        JSONArray castList = castObject.getJSONArray(jsonConfig.CAST);
        for (int i = 0; i < castList.length(); i++) {
            JSONObject cast = castList.getJSONObject(i);
            CastModel castModel = new CastModel();
            castModel.setCharacter(cast.get(jsonConfig.CHARACTER).toString());
            castModel.setId(cast.get(jsonConfig.CAST_ID).toString());
            castModel.setName(cast.get(jsonConfig.CAST_NAME).toString());
            castModel.setImage(cast.get(jsonConfig.CAST_IMAGE_URL).toString());
            castArray.add(castModel);
        }
        return castArray;
    }
    public ArrayList<VideoModel> jsonVideoParser(String jsonVideos) throws JSONException {
        ArrayList<VideoModel> videoArray = new ArrayList<>();
        MovieVideosJsonConfig jsonConfig = new MovieVideosJsonConfig();

        JSONObject videoObject = new JSONObject(jsonVideos);
        //Iterating through videos
        JSONArray videoList = videoObject.getJSONArray(jsonConfig.VIDEO_ARRAY);
        for (int i = 0; i < videoList.length(); i++) {
            JSONObject video = videoList.getJSONObject(i);
            VideoModel videoModel = new VideoModel();
            videoModel.setName(video.get(jsonConfig.VIDEO_NAME).toString());
            videoModel.setImageUrl(video.get(jsonConfig.VIDEO_URL).toString());
            videoModel.setVideoUrl(video.get(jsonConfig.VIDEO_URL).toString());
            videoArray.add(videoModel);
        }
        return videoArray;
    }

    public DetailItemModel jsonTvDetailParser(String jsonMovie)throws JSONException {
        // fetching data in json
        TvDetailJsonConfig jsonConfig = new TvDetailJsonConfig();
        JSONObject movieObject = new JSONObject(jsonMovie);
        String title = movieObject.get(jsonConfig.SHOW_TITLE).toString();
        String overview = movieObject.get(jsonConfig.SHOW_OVERVIEW).toString();
        String vote_average = movieObject.get(jsonConfig.VOTE_AVERAGE).toString();
        String tagline = movieObject.get(jsonConfig.SHOW_STATUS).toString();
        String release_date = movieObject.get(jsonConfig.FIRST_AIR_DATE).toString();
        String language = movieObject.get(jsonConfig.LANGUAGE).toString();
        String poster = movieObject.get(jsonConfig.IMAGE_URL).toString();
        //creating object of DetailItemModel class to initialise constructor with movieDetails
        DetailItemModel DetailItemModel=new DetailItemModel(title, overview, vote_average, tagline, release_date, language, poster);
        return DetailItemModel;
    }
    public ArrayList<SimilarItemModel> jsonSimilarParser(String jsonSimilarMovies) throws JSONException {
        ArrayList<SimilarItemModel> similarMoviesArray = new ArrayList<>();
        MoviesSimilarJsonConfig jsonConfig = new MoviesSimilarJsonConfig();
        JSONObject similarMoviesObject = new JSONObject(jsonSimilarMovies);
        JSONArray similarMoviesList = similarMoviesObject.getJSONArray(jsonConfig.MOVIE_ARRAY);
        for (int i = 0; i < similarMoviesList.length(); i++) {
            JSONObject similarMovies = similarMoviesList.getJSONObject(i);
            SimilarItemModel similarItemModel = new SimilarItemModel();
            similarItemModel.setSimilarItemId(similarMovies.get(jsonConfig.MOVIE_ID).toString());
            similarItemModel.setSimilarItemimage(similarMovies.get(jsonConfig.MOVIE_IMAGE_URL).toString());
            similarItemModel.setSimilarItemimage(similarMovies.get("vote_average").toString());
            similarMoviesArray.add(similarItemModel);
        }
        return similarMoviesArray;
    }
}
