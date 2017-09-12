package com.example.pc.flickr;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pc.flickr.R;

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
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 */
public class HorizontalListFragment extends Fragment {
    public PrimaryMainAdapter mAdapter;
    String type;
    RecyclerView recyclerView;

    public HorizontalListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_horizontal_list, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_main);
        ArrayList<String> arrayList = new ArrayList<String>();
        String urlHeading[] = this.getArguments().getStringArray("urlHeading");
        String urls[] = this.getArguments().getStringArray("urls");
        type = this.getArguments().getString("type");
        FetchTask fetchTask = new FetchTask();
        fetchTask.execute(urls[0]);
        mAdapter = new PrimaryMainAdapter(arrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        return rootView;
    }

    private class PrimaryMainAdapter extends RecyclerView.Adapter<PrimaryMainAdapter.MyViewHolder> {

        private ArrayList<String> arrayList;

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView listItemTextView;
            public MyViewHolder(View itemview){
                super(itemview);
                listItemTextView = (TextView) itemview.findViewById(R.id.list_textview_item);
            }
        }

        public PrimaryMainAdapter(ArrayList<String> arrayList){
            this.arrayList = arrayList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_textview, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            String str = arrayList.get(position);
            holder.listItemTextView.setText(str);
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    public class FetchTask extends AsyncTask<String, Void, String> {
        private ArrayList<String> jsonMovieParser(String jsonMovie)throws JSONException {
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

        private ArrayList<String> jsonTvParser(String jsontvShows)throws JSONException {
            ArrayList<String> tvShowsArray = new ArrayList<>();
            JSONObject tvShowsObject = new JSONObject(jsontvShows);
            JSONArray list = tvShowsObject.getJSONArray("results");

            for (int i = 0; i < list.length();i++){
                JSONObject populartvShows = list.getJSONObject(i);
                String title = populartvShows.get("name").toString();
                String first_air_date = populartvShows.get("first_air_date").toString();
                String vote_average = populartvShows.get("vote_average").toString();
                tvShowsArray.add(title + "\n" + first_air_date + "\n" + vote_average);
            }
            return tvShowsArray;
        }


        private ArrayList<String> jsonCelebsParser(String jsonCelebrities)throws JSONException {
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


        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonData = null;
            try {

                URL url = new URL(params[0]);
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
            return jsonData;
        }
        protected void onPostExecute(String jsonData) {
            ArrayList<String> arrayList = new ArrayList<>();
            super.onPostExecute(jsonData);
                try {
                    if(type == "movies") {
                        arrayList = jsonMovieParser(jsonData);
                    }
                    else if(type == "tv"){
                        arrayList = jsonTvParser(jsonData);
                    }
                    else if (type == "celebs"){
                        arrayList = jsonCelebsParser(jsonData);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            mAdapter = new PrimaryMainAdapter(arrayList);
            recyclerView.setAdapter(mAdapter);
            }
        }

}
