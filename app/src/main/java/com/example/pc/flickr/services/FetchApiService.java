package com.example.pc.flickr.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.example.pc.flickr.data.MovieDbApiContract;
import com.example.pc.flickr.data.MovieDbHelper;
import com.example.pc.flickr.json_parsers.MainJsonParser;
import com.example.pc.flickr.models.ListDataModel;
import com.example.pc.flickr.models.WishListModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class FetchApiService extends IntentService {
    public FetchApiService() {
        super("FetchApiService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Service Started!");
        String urls[] = {"https://api.themoviedb.org/3/movie/now_playing?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US&page=1",
                "https://api.themoviedb.org/3/movie/popular?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US&page=1",
                "https://api.themoviedb.org/3/movie/top_rated?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US&page=1",
                "https://api.themoviedb.org/3/movie/upcoming?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US&page=1",
                "https://api.themoviedb.org/3/tv/popular?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US&page=1",
                "https://api.themoviedb.org/3/tv/airing_today?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US&page=1",
                "https://api.themoviedb.org/3/tv/top_rated?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US&page=1",
                "https://api.themoviedb.org/3/tv/on_the_air?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US&page=1",
                "https://api.themoviedb.org/3/person/popular?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US&page=1"
        };
        String type[] = {"movies", "movies", "movies", "movies", "tv", "tv", "tv", "tv", "celebs"};
        String subType[] = {"Now Playing", "Popular", "Top Rated", "Upcoming", "Airing Today", "Popular", "Top Rated", "On The Air", "Popular"};
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if ((networkInfo != null) && networkInfo.isConnected()) {
            ArrayList<String> jsonArray = fetchTask(urls);
            ArrayList<ListDataModel> listDataModel = new ArrayList<>();
            MainJsonParser mainJsonParser = new MainJsonParser();
            for (int i = 0; i < urls.length; i++) {
                try {
                    if (type[i].equals("movies")) {
                        listDataModel = mainJsonParser.jsonMovieParser(jsonArray.get(i), type[i], subType[i], listDataModel);
                    } else if (type[i].equals("tv")) {
                        listDataModel = mainJsonParser.jsonTvParser(jsonArray.get(i), type[i], subType[i], listDataModel);
                    } else {
                        listDataModel = mainJsonParser.jsonCelebsParser(jsonArray.get(i), type[i], subType[i], listDataModel);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            DatabaseInsert(listDataModel);
            //Toast.makeText(this, "New Data Synced", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<String> fetchTask(String urls[]){
        ArrayList<String> jsonArray = new ArrayList<>();
        for (String urlString : urls){
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonData = null;
            try {

                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream stream = urlConnection.getInputStream();
                if (stream == null){
                    jsonData = null;
                }
                StringBuffer stringBuffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(stream));
                String inputLine;
                while ((inputLine = reader.readLine()) != null){
                    stringBuffer.append(inputLine + "\n");
                }
                if (stringBuffer.length() == 0){
                    jsonData = null;
                }
                jsonData = stringBuffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (urlConnection != null){
                    urlConnection.disconnect();
                }
                if (reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            jsonArray.add(jsonData);
        }
        return jsonArray;
    }




    private void DatabaseInsert(ArrayList<ListDataModel> listDataModel) {
        MovieDbHelper movieDbHelper = new MovieDbHelper(this);
        SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        db.delete(MovieDbApiContract.ApiData.TABLE_NAME, null, null);
        ContentValues values = new ContentValues();
        for (ListDataModel dataModel : listDataModel) {
            values.put(MovieDbApiContract.ApiData.COLUMN_ID, dataModel.getId());
            values.put(MovieDbApiContract.ApiData.COLUMN_NAME, dataModel.getName());
            values.put(MovieDbApiContract.ApiData.COLUMN_POPULARITY, dataModel.getPopularity());
            values.put(MovieDbApiContract.ApiData.COLUMN_VOTE_AVERAGE, dataModel.getVote_avg());
            values.put(MovieDbApiContract.ApiData.COLUMN_TYPE, dataModel.getType());
            values.put(MovieDbApiContract.ApiData.COLUMN_TYPE_SUB, dataModel.getSubType());
            values.put(MovieDbApiContract.ApiData.COLUMN_IMG_URL, dataModel.getImg_url());
            values.put(MovieDbApiContract.ApiData.COLUMN_WISH_LIST, false);
            Uri uri = getContentResolver().insert(MovieDbApiContract.ApiData.CONTENT_URI, values);
        }
        db.close();
    }
}
