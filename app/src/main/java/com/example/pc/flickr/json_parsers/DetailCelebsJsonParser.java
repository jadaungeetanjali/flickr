package com.example.pc.flickr.json_parsers;

import com.example.pc.flickr.models.CelebImageModel;
import com.example.pc.flickr.models.CelebsModel;
import com.example.pc.flickr.models.SimilarItemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by PC on 9/28/2017.
 */

public class DetailCelebsJsonParser {
    public CelebsModel jsonCelebsParser(String jsonCeleb)throws JSONException {
        JSONObject celebObject = new JSONObject(jsonCeleb);
        String title = celebObject.get("name").toString();
        String biography = celebObject.get("biography").toString();
        String dateOfBirth = celebObject.get("birthday").toString();
        String placeOfBirth = celebObject.get("place_of_birth").toString();
        String profile = celebObject.get("profile_path").toString();

        CelebsModel celebsModel=new CelebsModel(title, biography, dateOfBirth, placeOfBirth, profile, "");
        return celebsModel;
    }
    public ArrayList<CelebImageModel> jsonCelebImageParser(String jsonCelebImage) throws JSONException {
        ArrayList<CelebImageModel> celebImageArray = new ArrayList<>();
        JSONObject celebImageObject = new JSONObject(jsonCelebImage);
        JSONArray celebImageList = celebImageObject.getJSONArray("profiles");
        for (int i = 0; i < celebImageList.length(); i++) {
            JSONObject celebImage = celebImageList.getJSONObject(i);
            String image = celebImage.get("file_path").toString();
            CelebImageModel celebImageModel= new CelebImageModel(image);
            celebImageArray.add(celebImageModel);
        }
        return celebImageArray;
    }
    public ArrayList<SimilarItemModel> jsonCelebMovieCreditParser(String jsonCelebMovieCredit) throws JSONException {
        ArrayList<SimilarItemModel> celebMovieCreditArray = new ArrayList<>();
        JSONObject celebMovieCreditObject = new JSONObject(jsonCelebMovieCredit);
        JSONArray celebMovieCreditList = celebMovieCreditObject.getJSONArray("cast");
        for (int i = 0; i < celebMovieCreditList.length(); i++) {
            JSONObject movieCredit = celebMovieCreditList.getJSONObject(i);
            String movieCreditName = movieCredit.get("title").toString();
            String movieCreditVoteAverage = movieCredit.get("vote_average").toString();
            String movieCreditPoster = movieCredit.get("poster_path").toString();
            String movieCreditId = movieCredit.get("id").toString();
            SimilarItemModel SimilarItemModel = new SimilarItemModel(movieCreditId, movieCreditName, movieCreditVoteAverage, movieCreditPoster);
            celebMovieCreditArray.add(SimilarItemModel);
        }
        return celebMovieCreditArray;
    }
}
