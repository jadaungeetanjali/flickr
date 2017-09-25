package com.example.pc.flickr.json_parsers;

import com.example.pc.flickr.models.MoreListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by PC on 9/25/2017.
 */

public class MoreListJsonParser {
    //jsonMovieListParser to parse the data for movies
    public ArrayList<MoreListModel> jsonMovieListParser(String jsonMovieList, ArrayList<MoreListModel> movieListsArray) throws JSONException {
        JSONObject movieListObject = new JSONObject(jsonMovieList);
        JSONArray moviesList = movieListObject.getJSONArray("results");
        for (int i = 0; i < moviesList.length(); i++) {
            JSONObject movies = moviesList.getJSONObject(i);
            String movieListName = movies.get("title").toString();
            String movieListReleaseDate = movies.get("release_date").toString();
            String movieListImage = movies.get("poster_path").toString();
            String movieListRating = movies.get("vote_average").toString();
            String movieListId = movies.get("id").toString();
            MoreListModel moreListModel = new MoreListModel(movieListName, movieListReleaseDate, movieListImage, movieListRating, movieListId);
            movieListsArray.add(moreListModel);
        }
        return movieListsArray;
    }
    //jsonTvListParser to parse the data for tvShows
    public ArrayList<MoreListModel> jsonTvListParser(String jsonTvList, ArrayList<MoreListModel> tvListArray) throws JSONException{
        JSONObject tvListObject = new JSONObject(jsonTvList);
        JSONArray tvList = tvListObject.getJSONArray("results");
        for (int i = 0; i < tvList.length(); i++) {
            JSONObject tvShows = tvList.getJSONObject(i);
            String tvListName = tvShows.get("name").toString();
            String tvListAirDate = tvShows.get("first_air_date").toString();
            String tvListImage = tvShows.get("poster_path").toString();
            String tvListRating = tvShows.get("vote_average").toString();
            String tvListId = tvShows.get("id").toString();
            MoreListModel movieListModel = new MoreListModel(tvListName, tvListAirDate, tvListImage, tvListRating, tvListId);
            tvListArray.add(movieListModel);
        }
        return tvListArray;
    }
    //jsonCelebsParser to parse the data for celebrities
    public ArrayList<MoreListModel> jsonCelebsListParser(String jsonCelebsList, ArrayList<MoreListModel> celebsListArray) throws JSONException{
        JSONObject celebsListObject = new JSONObject(jsonCelebsList);
        JSONArray celebsList = celebsListObject.getJSONArray("results");
        for (int i = 0; i < celebsList.length(); i++) {
            JSONObject celebs = celebsList.getJSONObject(i);
            String celebsListName = celebs.get("name").toString();
            String celebsListImage = celebs.get("profile_path").toString();
            String celebsListId = celebs.get("id").toString();
            MoreListModel movieListModel = new MoreListModel(celebsListName, "", celebsListImage, "", celebsListId);
            celebsListArray.add(movieListModel);
        }
        return celebsListArray;
    }

}
