package com.example.pc.flickr;

import android.graphics.Movie;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pc.flickr.MovieData.CastModel;
import com.example.pc.flickr.MovieData.DataModel;
import com.example.pc.flickr.MovieData.ReviewModel;
import com.example.pc.flickr.MovieData.SimilarMoviesModel;
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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;


// MoviesDetails activity to display the detail of particular movie
public class MoviesDetails extends AppCompatActivity {
    RecyclerView recyclerViewCast, recyclerViewReviews, recyclerViewSimilar;
    private CastAdapter castAdapter;
    private ReviewAdapter reviewAdapter;
    private SimilarMoviesAdapter similarMoviesAdapter;
    public TextView title, overview, vote_average, tagline, release_date, language;
    public ImageView poster;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_movie_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerViewCast = (RecyclerView) findViewById(R.id.castRecyclerView);
        // to set the horizontal linear layout for recycler view
        LinearLayoutManager layoutManagerCast = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCast.setLayoutManager(layoutManagerCast);
        recyclerViewCast.setItemAnimator(new DefaultItemAnimator());

        recyclerViewReviews = (RecyclerView) findViewById(R.id.reviewsRecyclerView);
        LinearLayoutManager layoutManagerReviews = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewReviews.setLayoutManager(layoutManagerReviews);
        recyclerViewReviews.setItemAnimator(new DefaultItemAnimator());

        recyclerViewSimilar = (RecyclerView) findViewById(R.id.similarMoviesRecyclerView);
        LinearLayoutManager layoutManagerSimilar = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSimilar.setLayoutManager(layoutManagerSimilar);
        recyclerViewSimilar.setItemAnimator(new DefaultItemAnimator());

        Bundle bundle  = this.getIntent().getExtras();
        String type = bundle.getString("type");
        String id = bundle.getString("id");
        ArrayList<String> urlList = new ArrayList<>();
        switch (type){
            case "movies":
                urlList.add("https://api.themoviedb.org/3/movie/"+ id +"?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
                urlList.add("https://api.themoviedb.org/3/movie/"+ id +"/credits?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
                urlList.add("https://api.themoviedb.org/3/movie/"+ id +"/reviews?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
                urlList.add("https://api.themoviedb.org/3/movie/"+ id +"/similar?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
                break;
            default:
                urlList.add("https://api.themoviedb.org/3/movie/"+ id +"?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
                urlList.add("https://api.themoviedb.org/3/movie/"+ id +"/credits?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
                urlList.add("https://api.themoviedb.org/3/movie/"+ id +"/reviews?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
                urlList.add("https://api.themoviedb.org/3/movie/"+ id +"/similar?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
        }
        // initialising the textViews to be populated with data
        title = (TextView) findViewById(R.id.main_title);
        overview = (TextView) findViewById(R.id.overview);
        vote_average = (TextView) findViewById(R.id.vote_average);
        tagline = (TextView) findViewById(R.id.tagline);
        release_date = (TextView) findViewById(R.id.release_date);
        language = (TextView) findViewById(R.id.language);
        poster = (ImageView) findViewById(R.id.poster);

        FetchTask callMovieData = new FetchTask();
        callMovieData.execute(urlList.get(0),urlList.get(1),urlList.get(2),urlList.get(3));

    }
    // CastAdapter class to populate data in castRecyclerView
    private class CastAdapter extends RecyclerView.Adapter<CastAdapter.castViewHolder> {
        private ArrayList<CastModel> castArrayList;

        class castViewHolder extends RecyclerView.ViewHolder {
            ImageView castImageView;
            TextView castNameTextView;
            TextView castCharacterTextView;

            public castViewHolder(View itemView) {
                super(itemView);
                castNameTextView = (TextView) itemView.findViewById(R.id.castName);
                castImageView = (ImageView) itemView.findViewById(R.id.castImageView);
                castCharacterTextView = (TextView) itemView.findViewById(R.id.castCharacter);
            }
        }

        public CastAdapter(ArrayList<CastModel> arrayList) {
            this.castArrayList = arrayList;
        }

        @Override
        public castViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_textview, parent, false);
            return new castViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(castViewHolder holder, int position) {
            CastModel castModel = castArrayList.get(position);
            holder.castNameTextView.setText(castModel.getName());
            holder.castCharacterTextView.setText(castModel.getCharacter());
            Picasso.with(getBaseContext()).load("https://image.tmdb.org/t/p/w500"+castModel.getImage()).into(holder.castImageView);
        }

        @Override
        public int getItemCount() {
            return castArrayList.size();
        }
    }
    // ReviewAdapter class to populate data in reviewsRecyclerView
    private class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.reviewViewHolder> {
        private ArrayList<ReviewModel> reviewArrayList;

        class reviewViewHolder extends RecyclerView.ViewHolder {
            TextView authorTextView, contentTextView;

            public reviewViewHolder(View itemView) {
                super(itemView);
                authorTextView = (TextView) itemView.findViewById(R.id.author);
                contentTextView = (TextView) itemView.findViewById(R.id.content);
            }
        }

        public ReviewAdapter(ArrayList<ReviewModel> arrayList) {
            this.reviewArrayList = arrayList;
        }

        @Override
        public reviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_textview, parent, false);
            return new reviewViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(reviewViewHolder holder, int position) {
            ReviewModel reviewModel = reviewArrayList.get(position);
            holder.authorTextView.setText(reviewModel.getAuthor());
            holder.contentTextView.setText(reviewModel.getContent());
        }

        @Override
        public int getItemCount() {
            return reviewArrayList.size();
        }
    }
    // SimilarMovieAdapter class to populate data in similarMovieRecyclerView
    private class SimilarMoviesAdapter extends RecyclerView.Adapter<SimilarMoviesAdapter.similarMoviesViewHolder> {
        private ArrayList<SimilarMoviesModel> similarMoviesArrayList;

        class similarMoviesViewHolder extends RecyclerView.ViewHolder {
            ImageView similarMovieImageView;
            TextView similarMovieNameTextView ;
            TextView similarMovieVoteAverageTextView;

            public similarMoviesViewHolder(View itemView) {
                super(itemView);
                similarMovieNameTextView = (TextView) itemView.findViewById(R.id.main_child_title_textView);
                similarMovieImageView = (ImageView) itemView.findViewById(R.id.main_child_imageView);
                similarMovieVoteAverageTextView = (TextView) itemView.findViewById(R.id.main_child_vote_textView);
            }
        }

        public SimilarMoviesAdapter(ArrayList<SimilarMoviesModel> arrayList) {
            this.similarMoviesArrayList = arrayList;
        }

        @Override
        public similarMoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_vertical_card, parent, false); //change layout id
            return new similarMoviesViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(similarMoviesViewHolder holder, int position) {
            SimilarMoviesModel similarMoviesModel = similarMoviesArrayList.get(position);
            holder.similarMovieNameTextView.setText(similarMoviesModel.getSimilarMovieName());
            holder.similarMovieVoteAverageTextView.setText(similarMoviesModel.getSimilarMovieVoteAverage());
            Picasso.with(getBaseContext()).load("https://image.tmdb.org/t/p/w500"+similarMoviesModel.getSimilarMovieimage()).into(holder.similarMovieImageView);
        }

        @Override
        public int getItemCount() {
            return similarMoviesArrayList.size();
        }
    }


    //DataModel class is created to initialise all the variables using constructor

        //constructor to initialise all the variables of textViews and imageView in detail_movie_layout

    // CastModel class to initialise all the variables of castRecyclerView
    // ReviewModel class to initialise all the variables of reviewsRecyclerView

    // SimilarMoviesModel class to initialise all the variables of similarMoviesRecyclerView



    public class FetchTask extends AsyncTask<String, Void, ArrayList<String>> {
        // jsonMovieParser to parse the jsonData of MovieDetails
        private DataModel jsonMovieParser(String jsonMovie)throws JSONException {
            // fetching data in json
            JSONObject movieObject = new JSONObject(jsonMovie);
            String title = movieObject.get("title").toString();
            String overview = movieObject.get("overview").toString();
            String vote_average = movieObject.get("vote_average").toString();
            String tagline = movieObject.get("tagline").toString();
            String release_date = movieObject.get("release_date").toString();
            String language = movieObject.get("original_language").toString();
            String poster = movieObject.get("poster_path").toString();
            //creating object of dataModel class to initialise constructor with movieDetails
            DataModel dataModel=new DataModel(title, overview, vote_average, tagline, release_date, language, poster);

            return dataModel;
        }
     // jsonCastParser to parse the jsonData for cast
    private ArrayList<CastModel> jsonCastParser(String jsonCast) throws JSONException {
        ArrayList<CastModel> castArray = new ArrayList<>();
        JSONObject castObject = new JSONObject(jsonCast);
        JSONArray castList = castObject.getJSONArray("cast");
        for (int i = 0; i < castList.length(); i++) {
            JSONObject cast = castList.getJSONObject(i);
            String name = cast.get("name").toString();
            String character = cast.get("character").toString();
            String image = cast.get("profile_path").toString();

            CastModel castModel = new CastModel(name,character,image);
            castArray.add(castModel);
        }
        return castArray;
    }
    // jsonReviewsParser to parse the jsonData for reviews
    private ArrayList<ReviewModel> jsonReviewsParser(String jsonReviews) throws JSONException {
        ArrayList<ReviewModel> reviewsArray = new ArrayList<>();
        JSONObject reviewsObject = new JSONObject(jsonReviews);
        JSONArray reviewsList = reviewsObject.getJSONArray("results");
        for (int i = 0; i < reviewsList.length(); i++) {
            JSONObject review = reviewsList.getJSONObject(i);
            String author = review.get("author").toString();
            String content = review.get("content").toString();

            ReviewModel reviewModel = new ReviewModel(author, content);
            reviewsArray.add(reviewModel);
        }
        return reviewsArray;
    }
    // jsonSimilarMoviesParser to parse the jsonData for SimilarMovies
    private ArrayList<SimilarMoviesModel> jsonSimilarMoviesParser(String jsonSimilarMovies) throws JSONException {
        ArrayList<SimilarMoviesModel> similarMoviesArray = new ArrayList<>();
        JSONObject similarMoviesObject = new JSONObject(jsonSimilarMovies);
        JSONArray similarMoviesList = similarMoviesObject.getJSONArray("results");
        for (int i = 0; i < similarMoviesList.length(); i++) {
            JSONObject similarMovies = similarMoviesList.getJSONObject(i);
            String similarMovieName = similarMovies.get("title").toString();
            String similarMovieVoteAverage = similarMovies.get("vote_average").toString();
            String similarMoviePoster = similarMovies.get("poster_path").toString();

            SimilarMoviesModel similarMoviesModel = new SimilarMoviesModel(similarMovieName,similarMovieVoteAverage,similarMoviePoster);
            similarMoviesArray.add(similarMoviesModel);
            }
            return similarMoviesArray;
    }

        //doInBackground method to set up url connection and return jsonData
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
        // onPostExecute data to populate data after fetching data
        protected void onPostExecute(ArrayList<String> jsonArray) {
            super.onPostExecute(jsonArray);
            //for (String name:jsonArray){
                //Log.v("output",name);
            //}

            ArrayList<CastModel> castArray = new ArrayList<>();
            ArrayList<ReviewModel> reviewArray = new ArrayList<>();
            ArrayList<SimilarMoviesModel> similarMoviesArray = new ArrayList<>();
            try {
                DataModel dataModel = jsonMovieParser(jsonArray.get(0));
                Log.v("title",dataModel.getTitle());
                title.setText(dataModel.getTitle());
                overview.setText(dataModel.getOverview());
                vote_average.setText(dataModel.getVote_avg());
                tagline.setText(dataModel.getTagline());
                release_date.setText(dataModel.getRelease_date());
                language.setText(dataModel.getLanguage());
                Picasso.with(getBaseContext()).load("https://image.tmdb.org/t/p/w500"+dataModel.getImg_url()).into(poster);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                castArray = jsonCastParser(jsonArray.get(1));
                castAdapter = new CastAdapter(castArray);
                recyclerViewCast.setAdapter(castAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                reviewArray = jsonReviewsParser(jsonArray.get(2));
                //Log.v("review",jsonArray.get(2));
                reviewAdapter = new ReviewAdapter(reviewArray);
                recyclerViewReviews.setAdapter(reviewAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                similarMoviesArray = jsonSimilarMoviesParser(jsonArray.get(3));
                similarMoviesAdapter = new SimilarMoviesAdapter(similarMoviesArray);
                recyclerViewSimilar.setAdapter(similarMoviesAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

