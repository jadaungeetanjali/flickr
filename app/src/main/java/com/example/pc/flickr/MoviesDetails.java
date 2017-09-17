package com.example.pc.flickr;

import android.graphics.Movie;
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
import android.widget.ArrayAdapter;
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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;


// MoviesDetails activity to display the detail of particualr movie
public class MoviesDetails extends AppCompatActivity {
    RecyclerView recyclerViewCast, recyclerViewReviews;
    private CastAdapter castAdapter;
    private ReviewAdapter reviewAdapter;
    public TextView title, overview, vote_average, tagline, release_date, language;
    public ImageView poster;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_movie_layout);

        recyclerViewCast = (RecyclerView) findViewById(R.id.cast_recycler_view);
        LinearLayoutManager layoutManagerCast = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCast.setLayoutManager(layoutManagerCast);
        recyclerViewCast.setItemAnimator(new DefaultItemAnimator());

        recyclerViewReviews = (RecyclerView) findViewById(R.id.reviews);
        LinearLayoutManager layoutManagerReviews = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewReviews.setLayoutManager(layoutManagerReviews);
        recyclerViewReviews.setItemAnimator(new DefaultItemAnimator());

        //String urlHeading[] = this.getIntent().getExtras().getStringArray("urlHeading");
        //ArrayList<String> urlHeadingList = new ArrayList<>(Arrays.asList(urlHeading));
        String urls[] = this.getIntent().getExtras().getStringArray("urls");

        title = (TextView) findViewById(R.id.main_title);
        overview = (TextView) findViewById(R.id.overview);
        vote_average = (TextView) findViewById(R.id.vote_average);
        tagline = (TextView) findViewById(R.id.tagline);
        release_date = (TextView) findViewById(R.id.release_date);
        language = (TextView) findViewById(R.id.language);
        poster = (ImageView) findViewById(R.id.poster);

        FetchTask callMovieData = new FetchTask();
        callMovieData.execute(urls[0], urls[1], urls[2]);

        // to set the horizontal linear layout for recycler view
    }

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
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_textview, parent, false);
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


    //new class is created to initialise all the variables
    private class DataModel{
        public String title;
        public String overview;
        public String vote_avg;
        public String tagline;
        public String release_date;
        public String language;
        public String img_url;
        //constructor to initialise all the variables
        public DataModel(String title,String overview, String vote_avg,
                         String tagline, String release_date, String language,String img_url){
            this.title = title;
            this.overview = overview;
            this.vote_avg = vote_avg;
            this.tagline = tagline;
            this.release_date = release_date;
            this.language = language;
            this.img_url = img_url;
        }
        public String getTitle(){

            return title;
        }
        public String getOverview(){

            return overview;
        }
        public String getVote_avg(){
            return vote_avg;
        }
        public String getTagline(){
            return tagline;
        }
        public String getRelease_date(){
            return release_date;
        }
        public String getLanguage(){
            return language;
        }
        public String getImg_url(){
            return  img_url;
        }
    }

    private class CastModel{
        public String name;
        public String character;
        public String image;

        public CastModel(String name,String character,String image){
            this.name = name;
            this.character = character;
            this.image = image;
        }

        public String getCharacter() {
            return character;
        }

        public String getName() {
            return name;
        }

        public String getImage() {
            return image;
        }
    }
    private class ReviewModel{
        public String author;
        public String content;
        public ReviewModel(String author, String content){
            this.author = author;
            this.content = content;
        }

        public String getAuthor() {
            return author;
        }

        public String getContent() {
            return content;
        }
    }

    public class FetchTask extends AsyncTask<String, Void, ArrayList<String>> {

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

            DataModel dataModel=new DataModel(title, overview, vote_average, tagline, release_date, language, poster);

            return dataModel;
        }



    private ArrayList<CastModel> jsonCastParser(String jsonCast) throws JSONException {
        ArrayList<CastModel> recyclerArray = new ArrayList<>();
        JSONObject castObject = new JSONObject(jsonCast);
        JSONArray castList = castObject.getJSONArray("cast");
        for (int i = 0; i < castList.length(); i++) {
            JSONObject cast = castList.getJSONObject(i);
            String name = cast.get("name").toString();
            String character = cast.get("character").toString();
            String image = cast.get("profile_path").toString();
            CastModel castModel = new CastModel(name,character,image);
            recyclerArray.add(castModel);
        }
        return recyclerArray;
    }

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

        //doInBackground method to set up url connection and return jsondata
        @Override
        protected ArrayList<String> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonData = null;
            ArrayList<String> jsonArray = new ArrayList<>();
            for (String param : params){
                try {
                    //setting the urlconnection

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
            //for (String name:jsonArray){
                //Log.v("output",name);
            //}

            ArrayList<CastModel> castArray = new ArrayList<>();
            ArrayList<ReviewModel> reviewArray = new ArrayList<>();
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
                Log.v("review",jsonArray.get(2));
                reviewAdapter = new ReviewAdapter(reviewArray);
                recyclerViewReviews.setAdapter(reviewAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

