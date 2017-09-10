package com.example.pc.flickr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.ArrayAdapter;

import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TvShowsDetails extends AppCompatActivity {
    public ArrayAdapter<String> tvShowsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_shows_details);

        ListView list = (ListView) findViewById(R.id.tvShowsList);
        List<String> arrayList = new ArrayList<String>();
        String url = "https://api.themoviedb.org/3/tv/popular?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US&page=1";
        FetchTask callTvData = new FetchTask();
        callTvData.execute(url);
        try {
            String data = callTvData.get().toString();
            arrayList = jsonTvShowsParser(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tvShowsAdapter = new ArrayAdapter<String>(this, R.layout.tv_shows_textview, arrayList);
        list.setAdapter(tvShowsAdapter);

    }

    private ArrayList<String> jsonTvShowsParser(String jsontvShows)throws JSONException {
        ArrayList<String> tvShowsArray = new ArrayList<>();
        JSONObject tvShowsObject = new JSONObject(jsontvShows);
        JSONArray list = tvShowsObject.getJSONArray("results");

        for (int i = 0; i < list.length();i++){
            JSONObject populartvShows = list.getJSONObject(i);
            String title = populartvShows.get("name").toString();
            String release_date = populartvShows.get("overview").toString();
            String vote_average = populartvShows.get("vote_average").toString();
            tvShowsArray.add(title + "\n" + release_date + "\n" + vote_average);
        }
        return tvShowsArray;
    }


}
