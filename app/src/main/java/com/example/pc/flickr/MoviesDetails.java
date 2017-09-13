package com.example.pc.flickr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MoviesDetails extends AppCompatActivity {
    public ArrayAdapter<String> movieAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_movie_layout);
        TextView title = (TextView) findViewById(R.id.main_title);
        List<String> arrayList = new ArrayList<String>();
        String url = "https://api.themoviedb.org/3/movie/400?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US";
        FetchTask callMovieData = new FetchTask();
        callMovieData.execute(url);
        try {
            String data = callMovieData.get().toString();
            arrayList = jsonMovieParser(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        movieAdapter = new ArrayAdapter<String>(this, R.layout.movies_textview, arrayList);
        list.setAdapter(movieAdapter);
    }
    private ArrayList<String> jsonMovieParser(String jsonMovie)throws JSONException {
        ArrayList<String> movieArray = new ArrayList<>();
        JSONObject movieObject = new JSONObject(jsonMovie);

            String title = movieObject.get("title").toString();
            String release_date = movieObject.get("release_date").toString();
            String vote_average = movieObject.get("vote_average").toString();
            movieArray.add(title + "\n" + release_date + "\n" + vote_average + "\n");

        return movieArray;
    }

}

