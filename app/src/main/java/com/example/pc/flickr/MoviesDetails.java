package com.example.pc.flickr;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.flickr.json_parsers.DetailJsonParser;
import com.example.pc.flickr.models.CastModel;
import com.example.pc.flickr.models.DetailItemModel;
import com.example.pc.flickr.models.ReviewModel;
import com.example.pc.flickr.models.SimilarItemModel;
import com.example.pc.flickr.models.WishListModel;
import com.example.pc.flickr.services.FirebaseCurd;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


// MoviesDetails activity to display the detail of particular movie
public class MoviesDetails extends AppCompatActivity {
    RecyclerView recyclerViewCast, recyclerViewReviews, recyclerViewSimilar;
    private CastAdapter castAdapter;
    private ReviewAdapter reviewAdapter;
    private SimilarMoviesAdapter similarMoviesAdapter;
    private TextView title, overview, vote_average, tagline, release_date, language, internet_connectivity;
    private ProgressBar progressBar;
    private ImageView poster;
    private LinearLayout mainContainer;
    private Button button;
    private String type, id;
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
        type = bundle.getString("type");
        id = bundle.getString("id");
        ArrayList<String> urlList = new ArrayList<>();
        switch (type){
            case "movies":
                urlList.add("https://api.themoviedb.org/3/movie/"+ id +"?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
                urlList.add("https://api.themoviedb.org/3/movie/"+ id +"/credits?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
                urlList.add("https://api.themoviedb.org/3/movie/"+ id +"/reviews?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
                urlList.add("https://api.themoviedb.org/3/movie/"+ id +"/similar?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
                break;
            case "tv":
                urlList.add("https://api.themoviedb.org/3/tv/"+ id +"?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
                urlList.add("https://api.themoviedb.org/3/tv/"+ id +"/credits?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
                urlList.add("https://api.themoviedb.org/3/movie/"+ id +"/reviews?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
                urlList.add("https://api.themoviedb.org/3/tv/"+ id +"/similar?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
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
        internet_connectivity = (TextView) findViewById(R.id.internet_connectivity);
        button = (Button) findViewById(R.id.detail_wishlist);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if ((networkInfo != null) && networkInfo.isConnected()) {
            internet_connectivity.setVisibility(View.GONE);
            ScrollView scrollView = (ScrollView) findViewById(R.id.detail_movie_scrollView);
            scrollView.setVisibility(View.VISIBLE);
            FetchTask callMovieData = new FetchTask();
            callMovieData.execute(urlList.get(0), urlList.get(1), urlList.get(2), urlList.get(3));
        }
        else{
            Toast.makeText(this, "Please Connect to internet...", Toast.LENGTH_SHORT).show();
        }
    }
    // CastAdapter class to populate data in castRecyclerView
    private class CastAdapter extends RecyclerView.Adapter<CastAdapter.castViewHolder> {
        private ArrayList<CastModel> castArrayList;

        class castViewHolder extends RecyclerView.ViewHolder {
            ImageView castImageView;
            TextView castNameTextView;
            TextView castCharacterTextView;
            ProgressBar castProgressBar;

            public castViewHolder(View itemView) {
                super(itemView);
                castProgressBar = (ProgressBar) itemView.findViewById(R.id.cast_image_progressBar);
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
        public void onBindViewHolder(final castViewHolder holder, int position) {
            CastModel castModel = castArrayList.get(position);
            holder.castNameTextView.setText(castModel.getName());
            holder.castCharacterTextView.setText(castModel.getCharacter());
            Picasso.with(getBaseContext()).load("https://image.tmdb.org/t/p/w500"+castModel.getImage()).
                    into(holder.castImageView,new com.squareup.picasso.Callback() {

                        @Override
                        public void onSuccess() {
                            holder.castProgressBar.setVisibility(View.GONE);
                            holder.castImageView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError() {

                        }
                    });
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
        private ArrayList<SimilarItemModel> similarMoviesArrayList;

        class similarMoviesViewHolder extends RecyclerView.ViewHolder {
            ImageView similarMovieImageView;
            TextView similarMovieNameTextView ;
            TextView similarMovieVoteAverageTextView;
            ProgressBar similarMovieProgressBar;

            public similarMoviesViewHolder(View itemView) {
                super(itemView);
                similarMovieProgressBar = (ProgressBar) itemView.findViewById(R.id.main_image_progressBar);
                similarMovieNameTextView = (TextView) itemView.findViewById(R.id.main_child_title_textView); //change id to similarMovieName
                similarMovieImageView = (ImageView) itemView.findViewById(R.id.main_child_imageView); //change id to similarMovieImage
                similarMovieVoteAverageTextView = (TextView) itemView.findViewById(R.id.main_child_vote_textView); //change id to similarMovieVote
            }
        }

        public SimilarMoviesAdapter(ArrayList<SimilarItemModel> arrayList) {
            this.similarMoviesArrayList = arrayList;
        }

        @Override
        public similarMoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_vertical_card, parent, false); //change layout id
            return new similarMoviesViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final similarMoviesViewHolder holder, int position) {
            SimilarItemModel SimilarItemModel = similarMoviesArrayList.get(position);
            holder.similarMovieNameTextView.setText(SimilarItemModel.getSimilarItemName());
            holder.similarMovieVoteAverageTextView.setText(SimilarItemModel.getSimilarItemVoteAverage());
            Picasso.with(getBaseContext()).load("https://image.tmdb.org/t/p/w500"+SimilarItemModel.getSimilarItemimage())
                    .into(holder.similarMovieImageView,new com.squareup.picasso.Callback() {

                        @Override
                        public void onSuccess() {
                            holder.similarMovieProgressBar.setVisibility(View.GONE);
                            holder.similarMovieImageView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }

        @Override
        public int getItemCount() {
            return similarMoviesArrayList.size();
        }
    }


    //DetailItemModel class is created to initialise all the variables using constructor

    // CastModel class to initialise all the variables of castRecyclerView

    // ReviewModel class to initialise all the variables of reviewsRecyclerView

    // SimilarItemModel class to initialise all the variables of similarMoviesRecyclerView

    public class FetchTask extends AsyncTask<String, Void, ArrayList<String>> {
        // jsonMovieParser to parse the jsonData of MovieDetails

        // jsonCastParser to parse the jsonData for cast

        // jsonReviewsParser to parse the jsonData for reviews

        // jsonSimilarMoviesParser to parse the jsonData for SimilarMovies



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
            ArrayList<SimilarItemModel> similarMoviesArray = new ArrayList<>();
            try {
                DetailJsonParser detailJsonParser = new DetailJsonParser();
                final DetailItemModel DetailItemModel = detailJsonParser.jsonMovieDetailParser(jsonArray.get(0));
                Log.v("title",DetailItemModel.getTitle());
                title.setText(DetailItemModel.getTitle());
                overview.setText(DetailItemModel.getOverview());
                vote_average.setText(DetailItemModel.getVote_avg());
                tagline.setText(DetailItemModel.getTagline());
                release_date.setText(DetailItemModel.getRelease_date());
                language.setText(DetailItemModel.getLanguage());
                Picasso.with(getBaseContext()).load("https://image.tmdb.org/t/p/w500"+DetailItemModel.getImg_url()).into(poster);

                castArray = detailJsonParser.jsonMovieCastParser(jsonArray.get(1));
                castAdapter = new CastAdapter(castArray);
                recyclerViewCast.setAdapter(castAdapter);

                reviewArray = detailJsonParser.jsonMovieReviewsParser(jsonArray.get(2));
                //Log.v("review",jsonArray.get(2));
                reviewAdapter = new ReviewAdapter(reviewArray);
                recyclerViewReviews.setAdapter(reviewAdapter);

                similarMoviesArray = detailJsonParser.jsonSimilarMoviesParser(jsonArray.get(3));
                similarMoviesAdapter = new SimilarMoviesAdapter(similarMoviesArray);
                recyclerViewSimilar.setAdapter(similarMoviesAdapter);


                progressBar = (ProgressBar) findViewById(R.id.detail_progressBar);
                mainContainer = (LinearLayout) findViewById(R.id.detail_mainContainer);
                progressBar.setVisibility(View.GONE);
                mainContainer.setVisibility(View.VISIBLE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.v("movie",DetailItemModel.getTitle());
                        WishListModel wishListModel = new WishListModel(
                                "tyagideepu133",id,type,DetailItemModel.getTitle(),DetailItemModel.getImg_url(),DetailItemModel.getVote_avg());
                        FirebaseCurd firebaseCurd = new FirebaseCurd();
                        firebaseCurd.addWhistListModel(wishListModel);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

