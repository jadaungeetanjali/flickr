package com.example.pc.flickr;

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
        Bundle bundle  = this.getIntent().getExtras();
        type = bundle.getString("type");
        subType = bundle.getString("subType");
        Log.i("data",type + " / " + subType);
        //recyclerViewMovieList = (RecyclerView) findViewById(R.id.reviewsRecyclerView);
        //LinearLayoutManager layoutManagerReviews = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //recyclerViewMovieList.setLayoutManager(layoutManagerReviews);
        //recyclerViewMovieList.setItemAnimator(new DefaultItemAnimator());
        //String url = "https://api.themoviedb.org/3/movie/popular?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US&page=1";
        //FetchTask callMovieData = new FetchTask();
        //callMovieData.execute(url);
    }
    private class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.movieListViewHolder> {
        private ArrayList<MovieListModel> movieListArrayList;

        class movieListViewHolder extends RecyclerView.ViewHolder {
            ImageView movieListImageView;
            TextView movieListNameTextView;
            TextView movieListReleaseDateTextView;
            TextView movieListRatingTextView;

            public movieListViewHolder(View itemView) {
                super(itemView);
                movieListNameTextView = (TextView) itemView.findViewById(R.id.movie_name);
                movieListImageView = (ImageView) itemView.findViewById(R.id.movie_poster);
                movieListReleaseDateTextView = (TextView) itemView.findViewById(R.id.movie_realease_date);
                movieListRatingTextView = (TextView) itemView.findViewById(R.id.movie_ratings);
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

        public MovieListModel(String name,String releaseDate,String image, String rating){
            this.name = name;
            this.releaseDate = releaseDate;
            this.image = image;
            this.rating = rating;
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
    }

    public class FetchTask extends AsyncTask<String, Void, ArrayList<String>> {

        private ArrayList<MovieListModel> jsonMovieParser(String jsonMovieList) throws JSONException {
            ArrayList<MovieListModel> movieListArray = new ArrayList<>();
            JSONObject movieListObject = new JSONObject(jsonMovieList);
            JSONArray moviesList = movieListObject.getJSONArray("results");
            for (int i = 0; i < moviesList.length(); i++) {
                JSONObject movies = moviesList.getJSONObject(i);
                String movieListName = movies.get("title").toString();
                String movieListReleaseDate = movies.get("release_date").toString();
                String movieListImage = movies.get("poster_path").toString();
                String movieListRating = movies.get("vote_average").toString();
                MovieListModel movieListModel = new MovieListModel(movieListName, movieListReleaseDate, movieListImage, movieListRating  );
                movieListArray.add(movieListModel);
            }
            return movieListArray;
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonData = null;
            ArrayList<String> jsonArray = new ArrayList<>();
            for (String param : params){
                try {
                    //setting the urlConnection
                    URL url = new URL(param);
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
            try {
                movieListsArray = jsonMovieParser(jsonArray.get(1));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            movieListAdapter = new MovieListAdapter(movieListsArray);
            recyclerViewMovieList.setAdapter(movieListAdapter);
        }
    }
}
