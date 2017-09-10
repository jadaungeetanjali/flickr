package com.example.pc.flickr;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
import java.util.List;


public class MoviesDetails extends AppCompatActivity {
    public ArrayAdapter<String> movieAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_details);
        ListView list = (ListView) findViewById(R.id.movieList);
        List<String> arrayList = new ArrayList<String>();
        movieAdapter = new ArrayAdapter<String>(this, R.layout.movies_textview, arrayList);
        list.setAdapter(movieAdapter);
        String url = "https://api.themoviedb.org/3/movie/popular?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US&page=1";
        MovieData callMovieData = new MovieData();
        callMovieData.execute(url);
    }
    public class  MovieData extends AsyncTask<String, Void, String>{
        private ArrayList<String> jsonMovieParser(String jsonMovie)throws JSONException{
            ArrayList<String> movieArray = new ArrayList<>();
            JSONObject movieObject = new JSONObject(jsonMovie);
            JSONArray list = movieObject.getJSONArray("results");
            for (int i = 0; i < list.length();i++){
                JSONObject popularMovie = list.getJSONObject(i);
                String title = popularMovie.get("title").toString();
                String release_date = popularMovie.get("release_date").toString();
                String vote_average = popularMovie.get("vote_average").toString();
                movieArray.add(title + "\n" + release_date + "\n" + vote_average + "\n");
            }
            return movieArray;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonMovie = null;
            try {

                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream stream = urlConnection.getInputStream();
                if (stream == null){
                    jsonMovie = null;
                }
                StringBuffer stringBuffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(stream));
                String inputLine;
                while ((inputLine = reader.readLine()) != null){
                    stringBuffer.append(inputLine + "\n");
                }
                if (stringBuffer.length() == 0){
                    jsonMovie = null;
                }
                jsonMovie = stringBuffer.toString();
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
            return jsonMovie;
        }
        protected void onPostExecute(String jsonMovie){
            ArrayList<String> movieList = null;
            try {
                movieList = jsonMovieParser(jsonMovie);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (movieList.size() != 0){
                movieAdapter.clear();
                movieAdapter.addAll(movieList);
            }
        }
    }
}

