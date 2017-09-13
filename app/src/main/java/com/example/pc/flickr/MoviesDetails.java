package com.example.pc.flickr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;



public class MoviesDetails extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_movie_layout);
        LayoutInflater inflater = LayoutInflater.from(MoviesDetails.this);
        View textview = inflater.inflate(R.layout.detail_movie_layout, null);
        TextView title, overview, vote_average, tagline, release_date, language;
        title = (TextView) textview.findViewById(R.id.main_title);
        overview = (TextView) textview.findViewById(R.id.overview);
        vote_average = (TextView) textview.findViewById(R.id.vote_average);
        tagline = (TextView) textview.findViewById(R.id.tagline);
        release_date = (TextView) textview.findViewById(R.id.release_date);
        language = (TextView) textview.findViewById(R.id.language);
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
        title.setText(arrayList.get(0));
        overview.setText(arrayList.get(1));
        vote_average.setText(arrayList.get(2));
        tagline.setText(arrayList.get(3));
        release_date.setText(arrayList.get(4));
        language.setText(arrayList.get(5));
    }
    private ArrayList<String> jsonMovieParser(String jsonMovie)throws JSONException {

        JSONObject movieObject = new JSONObject(jsonMovie);
        String title = movieObject.get("title").toString();
        String overview = movieObject.get("overview").toString();
        String vote_average = movieObject.get("vote_average").toString();
        String tagline = movieObject.get("tagline").toString();
        String release_date = movieObject.get("release_date").toString();
        String language = movieObject.get("original_language").toString();

        String[] array = {title, overview, vote_average, tagline, release_date, language};
        ArrayList<String> movieArray = new ArrayList<>(Arrays.asList(array));
        return movieArray;
    }
}

