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

public class Celebrities extends AppCompatActivity {
    public ArrayAdapter<String> celebritiesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celebrities);
        ListView list = (ListView) findViewById(R.id.celebritiesList);
        List<String> arrayList = new ArrayList<String>();
        String url = "https://api.themoviedb.org/3/person/popular?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US&page=1";
        FetchTask callCelebritiesData = new FetchTask();
        callCelebritiesData.execute(url);
        try {
            String data = callCelebritiesData.get().toString();
            arrayList = jsonCelebritiesParser(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        celebritiesAdapter = new ArrayAdapter<String>(this, R.layout.celebrities_textview, arrayList);
        list.setAdapter(celebritiesAdapter);
    }
    private ArrayList<String> jsonCelebritiesParser(String jsonCelebrities)throws JSONException {
        ArrayList<String> celebritiesArray = new ArrayList<>();
        JSONObject celebritiesObject = new JSONObject(jsonCelebrities);
        JSONArray list = celebritiesObject.getJSONArray("results");
        for (int i = 0; i < list.length();i++){
            JSONObject popularCelebrities = list.getJSONObject(i);
            String name = popularCelebrities.get("name").toString();
            JSONObject known_for = popularCelebrities.getJSONArray("known_for").getJSONObject(0);
            String movie = known_for.get("title").toString();
            String vote_average = known_for.get("vote_average").toString();
            celebritiesArray.add(name + "\n" + movie + "\n" + vote_average + "\n");
        }
        return celebritiesArray;
    }
}

