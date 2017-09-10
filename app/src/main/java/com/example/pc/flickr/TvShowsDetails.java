package com.example.pc.flickr;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class TvShowsDetails extends AppCompatActivity {
    public ArrayAdapter<String> tvShowsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_shows_details);

        ListView list = (ListView) findViewById(R.id.tvShowsList);
        List<String> arrayList = new ArrayList<String>();
        for (int i = 0; i < 10; i++){
            arrayList.add("TV SHOW = " +i);
        }
        tvShowsAdapter = new ArrayAdapter<String>(this, R.layout.tv_shows_textview, arrayList);
        list.setAdapter(tvShowsAdapter);

        TvShowsData callTvShowsData = new TvShowsData();
        callTvShowsData.execute();
    }
    public class  TvShowsData extends AsyncTask<Void, Void, String> {
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

        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonTvShows = null;
            try {

                URL url = new URL("https://api.themoviedb.org/3/tv/popular?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US&page=1");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream stream = urlConnection.getInputStream();
                if (stream == null){
                    jsonTvShows = null;
                }
                StringBuffer stringBuffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(stream));
                String inputLine;
                while ((inputLine = reader.readLine()) != null){
                    stringBuffer.append(inputLine + "\n");
                }
                if (stringBuffer.length() == 0){
                    jsonTvShows = null;
                }
                jsonTvShows = stringBuffer.toString();
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
            return jsonTvShows;
        }
        protected void onPostExecute(String jsonTvShows){
            ArrayList<String> tvShowsList = null;
            try {
                tvShowsList = jsonTvShowsParser(jsonTvShows);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (tvShowsList.size() != 0){
                tvShowsAdapter.clear();
                tvShowsAdapter.addAll(tvShowsList);
            }
        }
    }
}
