package com.example.pc.flickr;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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

public class MovieList extends AppCompatActivity {
    RecyclerView recyclerViewMovieList;
    private MovieListAdapter movieListAdapter;
    public String type,subType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        recyclerViewMovieList = (RecyclerView) findViewById(R.id.recyclerView_movieList);
        LinearLayoutManager layoutManagerMovieList = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewMovieList.setLayoutManager(layoutManagerMovieList);
        recyclerViewMovieList.setItemAnimator(new DefaultItemAnimator());
        Bundle bundle  = this.getIntent().getExtras();
        type = bundle.getString("type");
        subType = bundle.getString("subType");
        Log.i("data",type + " / " + subType);

        String url="https://api.themoviedb.org/3/" + type + "/" + subType + "?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US&page=";

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if ((networkInfo != null) && networkInfo.isConnected()) {
            FetchTask callMovieData = new FetchTask();
            callMovieData.execute(url);
        }
        else{
            Toast.makeText(this, "Please Connect to internet...", Toast.LENGTH_SHORT).show();
        }
    }
    private class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.movieListViewHolder> {
        private ArrayList<MovieListModel> movieListArrayList;

        class movieListViewHolder extends RecyclerView.ViewHolder {
            ImageView movieListImageView;
            TextView movieListNameTextView;
            TextView movieListReleaseDateTextView;
            TextView movieListRatingTextView;
            TextView movieListIdTextView;
            public movieListViewHolder(View itemView) {
                super(itemView);
                movieListNameTextView = (TextView) itemView.findViewById(R.id.movie_name);
                movieListImageView = (ImageView) itemView.findViewById(R.id.movie_poster);
                movieListReleaseDateTextView = (TextView) itemView.findViewById(R.id.movie_realease_date);
                movieListRatingTextView = (TextView) itemView.findViewById(R.id.movie_ratings);
                movieListIdTextView = (TextView) itemView.findViewById(R.id.movie_id);
            }
        }

        public MovieListAdapter(ArrayList<MovieListModel> arrayList) {
            this.movieListArrayList = arrayList;
        }

        @Override
        public movieListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_textview, parent, false);
            return new movieListViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(movieListViewHolder holder, int position) {
            MovieListModel movieListModel = movieListArrayList.get(position);
            holder.movieListNameTextView.setText(movieListModel.getName());
            holder.movieListReleaseDateTextView.setText(movieListModel.getReleaseDate());
            holder.movieListRatingTextView.setText(movieListModel.getRating());
            holder.movieListIdTextView.setText(movieListModel.getId());
            Picasso.with(getBaseContext()).load("https://image.tmdb.org/t/p/w500" + movieListModel.getImage()).into(holder.movieListImageView);
        }

        @Override
        public int getItemCount() {
            return movieListArrayList.size();
        }
    }
    private class MovieListModel{
        public String name;
        public String releaseDate;
        public String image;
        public String rating;
        public String id;

        public MovieListModel(String name,String releaseDate,String image, String rating, String id){
            this.name = name;
            this.releaseDate = releaseDate;
            this.image = image;
            this.rating = rating;
            this.id = id;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public String getName() {
            return name;
        }

        public String getImage() {
            return image;
        }

        public String getRating() {
            return rating;
        }

        public String getId() {
            return id;
        }
    }

    public class FetchTask extends AsyncTask<String, Void, ArrayList<String>> {

        private ArrayList<MovieListModel> jsonMovieParser(String jsonMovieList, ArrayList<MovieListModel> movieListsArray) throws JSONException {
            //ArrayList<MovieListModel> movieListArray = new ArrayList<>();
            JSONObject movieListObject = new JSONObject(jsonMovieList);
            JSONArray moviesList = movieListObject.getJSONArray("results");
            for (int i = 0; i < moviesList.length(); i++) {
                JSONObject movies = moviesList.getJSONObject(i);
                String movieListName = movies.get("title").toString();
                String movieListReleaseDate = movies.get("release_date").toString();
                String movieListImage = movies.get("poster_path").toString();
                String movieListRating = movies.get("vote_average").toString();
                String movieListId = movies.get("id").toString();
                MovieListModel movieListModel = new MovieListModel(movieListName, movieListReleaseDate, movieListImage, movieListRating, movieListId);
                movieListsArray.add(movieListModel);
            }
            return movieListsArray;
        }

        private ArrayList<MovieListModel> jsonTvParser(String jsonTvList, ArrayList<MovieListModel> tvListArray) throws JSONException{
            //ArrayList<MovieListModel> tvListArray = new ArrayList<>();
            JSONObject tvListObject = new JSONObject(jsonTvList);
            JSONArray tvList = tvListObject.getJSONArray("results");
            for (int i = 0; i < tvList.length(); i++) {
                JSONObject tvShows = tvList.getJSONObject(i);
                String tvListName = tvShows.get("name").toString();
                String tvListAirDate = tvShows.get("first_air_date").toString();
                String tvListImage = tvShows.get("poster_path").toString();
                String tvListRating = tvShows.get("vote_average").toString();
                String tvListId = tvShows.get("id").toString();
                MovieListModel movieListModel = new MovieListModel(tvListName, tvListAirDate, tvListImage, tvListRating, tvListId);
                tvListArray.add(movieListModel);
            }
            return tvListArray;
        }
        private ArrayList<MovieListModel> jsonCelebsParser(String jsonCelebsList, ArrayList<MovieListModel> celebsListArray) throws JSONException{
            //ArrayList<MovieListModel> celebsListArray = new ArrayList<>();
            JSONObject celebsListObject = new JSONObject(jsonCelebsList);
            JSONArray celebsList = celebsListObject.getJSONArray("results");
            for (int i = 0; i < celebsList.length(); i++) {
                JSONObject celebs = celebsList.getJSONObject(i);
                String celebsListName = celebs.get("name").toString();
                //String tvListAirDate = tvShows.get("first_air_date").toString();
                String celebsListImage = celebs.get("profile_path").toString();
                //String celebsListRating = tvShows.get("vote_average").toString();
                String celebsListId = celebs.get("id").toString();
                MovieListModel movieListModel = new MovieListModel(celebsListName, "", celebsListImage, "", celebsListId);
                celebsListArray.add(movieListModel);
            }
            return celebsListArray;
        }


        @Override
        protected ArrayList<String> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonData = null;
            ArrayList<String> jsonArray = new ArrayList<>();
            for (int i=1;i<6;i++){
                try {
                    //Log.v("url", param);
                    //setting the urlConnection
                    URL url = new URL(params[0]+i);
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
        protected void onPostExecute(ArrayList<String> jsonArray) {
            super.onPostExecute(jsonArray);
            ArrayList<MovieListModel> movieListsArray = new ArrayList<>();
            ArrayList<MovieListModel> tvListArray = new ArrayList<>();
            ArrayList<MovieListModel> celebsListArray = new ArrayList<>();

                //Log.v("type", type);
                if(type.compareTo("movie") == 0) {
                    // Log.v("type", type);
                    try {
                        for (int i = 0; i < 5; i++) {
                            movieListsArray = jsonMovieParser(jsonArray.get(i), movieListsArray);
                        }
                        movieListAdapter = new MovieListAdapter(movieListsArray);
                        recyclerViewMovieList.setAdapter(movieListAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else if (type.compareTo("tv") == 0){
                    try {
                        for (int i = 0; i < 5; i++) {
                            tvListArray = jsonTvParser(jsonArray.get(i),tvListArray);
                        }
                        movieListAdapter = new MovieListAdapter(tvListArray);
                        recyclerViewMovieList.setAdapter(movieListAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if (type.compareTo("person") == 0){
                    try {
                        for (int i = 0; i < 5; i++) {
                            celebsListArray = jsonCelebsParser(jsonArray.get(i), celebsListArray);
                        }
                        movieListAdapter = new MovieListAdapter(celebsListArray);
                        recyclerViewMovieList.setAdapter(movieListAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
}
