package com.example.pc.flickr;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.pc.flickr.data.MovieDbApiContract;
import com.example.pc.flickr.data.MovieDbHelper;

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
        ArrayList<String> jsonArray = fetchTask(urls);
        ArrayList<ListDataModel> listDataModel = new ArrayList<>();
        for (int i= 0; i < urls.length ; i++){
            try {
                if (type[i].equals("movies")){
                    listDataModel = jsonMovieParser(jsonArray.get(i),type[i],subType[i],listDataModel);
                }
                else if (type[i].equals("tv")){
                    listDataModel = jsonTvParser(jsonArray.get(i),type[i],subType[i],listDataModel);
                }
                else{
                    listDataModel = jsonCelebsParser(jsonArray.get(i),type[i],subType[i],listDataModel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        DatabaseInsert(listDataModel);
        //Toast.makeText(this, "New Data Synced", Toast.LENGTH_SHORT).show();

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

    private ArrayList<ListDataModel> jsonMovieParser(String jsonMovie, String type, String subType, ArrayList<ListDataModel> listDataModel)throws JSONException {

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
    private ArrayList<ListDataModel> jsonTvParser(String jsontvShows, String type, String subType, ArrayList<ListDataModel> listDataModel)throws JSONException {

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

    private ArrayList<ListDataModel> jsonCelebsParser(String jsonCelebrities, String type, String subType, ArrayList<ListDataModel> listDataModel)throws JSONException {

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
    private class ListDataModel{
        private String name;
        private String popularity;
        private String vote_avg;
        private String img_url;
        private String id;
        private String type;
        private String subType;
        public ListDataModel(String id,String name,String popularity,String vote_avg,String img_url,String type,String subType){
            this.img_url = img_url;
            this.name = name;
            this.vote_avg = vote_avg;
            this.popularity = popularity;
            this.id = id;
            this.type = type;
            this.subType = subType;
        }

        public String getName(){
            return name;
        }
        public String getPopularity(){
            return popularity;
        }

        public String getImg_url() {
            return img_url;
        }

        public String getVote_avg() {
            return vote_avg;
        }

        public String getId() {
            return id;
        }

        public String getSubType() {
            return subType;
        }

        public String getType() {
            return type;
        }
    }

    private void DatabaseInsert(ArrayList<ListDataModel> listDataModel){
        MovieDbHelper movieDbHelper = new MovieDbHelper(this);
        SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        db.delete(MovieDbApiContract.ApiData.TABLE_NAME,null,null);
        ContentValues values = new ContentValues();
        for (ListDataModel dataModel:listDataModel){
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
