package com.example.pc.flickr.json_parsers;

/**
 * Created by Deepanshu on 11/6/2017.
 */

import com.example.pc.flickr.models.CastModel;
import com.example.pc.flickr.models.DetailMovieModel;
import com.example.pc.flickr.models.ReviewModel;
import com.example.pc.flickr.models.SimilarItemModel;
import com.example.pc.flickr.models.VideoModel;
import com.example.pc.flickr.utilities.movies.MovieCastJsonConfig;
import com.example.pc.flickr.utilities.movies.MovieDetailJsonConfig;
import com.example.pc.flickr.utilities.movies.MovieReviewJsonConfig;
import com.example.pc.flickr.utilities.movies.MovieVideosJsonConfig;
import com.example.pc.flickr.utilities.movies.MoviesSimilarJsonConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailMovieJsonParser {

    public DetailMovieModel jsonMovieDetailParser(String jsonMovie)throws JSONException {
        //Importing jsonConfig from utilities
        MovieDetailJsonConfig jsonConfig = new MovieDetailJsonConfig();
        // fetching data in json
        JSONObject movieObject = new JSONObject(jsonMovie);

        //Getting genres index 0 from movieObject
        JSONArray genresMovies = movieObject.getJSONArray(jsonConfig.GENRES);

        //Converting runtime from 139 to 2:19 and storing it into genres
        String runtime =  movieObject.get(jsonConfig.RUNTIME).toString();
        if (runtime != null){
            int hours = Integer.parseInt(runtime)/60;
            int minutes = Integer.parseInt(runtime) - hours * 60;
            runtime = hours + ":" + minutes;
        }

        //Setting values in detailMovieModel
        DetailMovieModel detailMovieModel = new DetailMovieModel();
        detailMovieModel.setAdult(movieObject.get(jsonConfig.ADULT).toString());
        detailMovieModel.setGeneres(genresMovies.getJSONObject(0).get(jsonConfig.GENRES_NAME).toString());
        detailMovieModel.setLanguage(movieObject.get(jsonConfig.LANGUAGE).toString());
        detailMovieModel.setTitle(movieObject.get(jsonConfig.TITLE).toString());
        detailMovieModel.setOverview(movieObject.get(jsonConfig.OVERVIEW).toString());
        detailMovieModel.setVoteAvg(movieObject.get(jsonConfig.VOTE_AVERAGE).toString());
        detailMovieModel.setReleaseDate(movieObject.get(jsonConfig.RELEASE_DATE).toString());
        detailMovieModel.setRuntime(runtime);
        detailMovieModel.setReleasedStatus(movieObject.get(jsonConfig.RELEASE_STATUS).toString());
        return detailMovieModel;
    }

    public ArrayList<CastModel> jsonMovieCastParser(String jsonCast) throws JSONException {
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

    public ArrayList<ReviewModel> jsonMovieReviewsParser(String jsonReviews) throws JSONException {
        ArrayList<ReviewModel> reviewsArray = new ArrayList<>();
        MovieReviewJsonConfig jsonConfig = new MovieReviewJsonConfig();

        JSONObject reviewsObject = new JSONObject(jsonReviews);
        JSONArray reviewsList = reviewsObject.getJSONArray(jsonConfig.REVIEWS_ARRAY);
        for (int i = 0; i < reviewsList.length(); i++) {
            JSONObject review = reviewsList.getJSONObject(i);
            ReviewModel reviewModel = new ReviewModel();
            reviewModel.setAuthor(review.get(jsonConfig.AUTHOR_NAME).toString());
            reviewModel.setContent(review.get(jsonConfig.AUTHOR_CONTENT).toString());
            reviewsArray.add(reviewModel);
        }
        return reviewsArray;
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
            similarMoviesArray.add(similarItemModel);
        }
        return similarMoviesArray;
    }

}
