package com.example.pc.flickr;

import android.os.AsyncTask;

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

/**
 * Created by deepn on 9/10/2017.
 */

public class FetchTask extends AsyncTask<String, Void, String>{

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
    protected void onPostExecute(String jsonMovie) {
        super.onPostExecute(jsonMovie);
    }
}
