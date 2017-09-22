package com.example.pc.flickr.json_parsers;

import com.example.pc.flickr.models.ListDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by deepn on 9/22/2017.
 */

public class MainJsonParser {

    public ArrayList<ListDataModel> jsonMovieParser(String jsonMovie, String type, String subType, ArrayList<ListDataModel> listDataModel)throws JSONException {

        JSONObject movieObject = new JSONObject(jsonMovie);
        JSONArray list = movieObject.getJSONArray("results");
        for (int i = 0; i < list.length();i++){
            JSONObject popularMovie = list.getJSONObject(i);
            String title = popularMovie.get("title").toString();
            String id = popularMovie.get("id").toString();
            String popularity = Math.round(Double.parseDouble(popularMovie.get("popularity").toString())) + "";
            String vote_average = popularMovie.get("vote_average").toString();
            String imgUrl = popularMovie.get("poster_path").toString();
            ListDataModel dataModel = new ListDataModel(id,title,popularity,vote_average,imgUrl,type,subType);
            listDataModel.add(dataModel);
        }
        return listDataModel;
    }
    public ArrayList<ListDataModel> jsonTvParser(String jsontvShows, String type, String subType, ArrayList<ListDataModel> listDataModel)throws JSONException {

        JSONObject tvShowsObject = new JSONObject(jsontvShows);
        JSONArray list = tvShowsObject.getJSONArray("results");
        for (int i = 0; i < list.length();i++){
            JSONObject populartvShows = list.getJSONObject(i);
            String id = populartvShows.get("id").toString();
            String title = populartvShows.get("name").toString();
            String popularity = Math.round(Double.parseDouble(populartvShows.get("popularity").toString())) + "";
            String vote_average = populartvShows.get("vote_average").toString();
            String imgUrl = populartvShows.get("poster_path").toString();
            ListDataModel dataModel = new ListDataModel(id,title,popularity,vote_average,imgUrl,type,subType);
            listDataModel.add(dataModel);
        }
        return listDataModel;
    }

    public ArrayList<ListDataModel> jsonCelebsParser(String jsonCelebrities, String type, String subType, ArrayList<ListDataModel> listDataModel)throws JSONException {

        JSONObject celebritiesObject = new JSONObject(jsonCelebrities);
        JSONArray list = celebritiesObject.getJSONArray("results");
        for (int i = 0; i < list.length();i++){
            JSONObject popularCelebrities = list.getJSONObject(i);
            String id = popularCelebrities.get("id").toString();
            String title = popularCelebrities.get("name").toString();
            JSONObject known_for = popularCelebrities.getJSONArray("known_for").getJSONObject(0);
            String popularity = Math.round(Double.parseDouble(popularCelebrities.get("popularity").toString())) + "";
            String vote_average = known_for.get("vote_average").toString();
            String imgUrl = popularCelebrities.get("profile_path").toString();
            ListDataModel dataModel = new ListDataModel(id,title,popularity,vote_average,imgUrl,type,subType);
            listDataModel.add(dataModel);
        }
        return listDataModel;
    }
}
