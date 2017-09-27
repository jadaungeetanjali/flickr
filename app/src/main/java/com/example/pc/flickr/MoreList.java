package com.example.pc.flickr;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.flickr.json_parsers.MoreListJsonParser;
import com.example.pc.flickr.models.ListDataModel;
import com.example.pc.flickr.models.MoreListModel;
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

public class MoreList extends AppCompatActivity {
    RecyclerView recyclerViewMoreList;
    private MoreListAdapter moreListAdapter;
    public String type,subType;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.more_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerViewMoreList = (RecyclerView) findViewById(R.id.recyclerView_moreList);
        progressBar = (ProgressBar) findViewById(R.id.more_progressBar);
        LinearLayoutManager layoutManagerMovieList = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewMoreList.setLayoutManager(layoutManagerMovieList);
        recyclerViewMoreList.setItemAnimator(new DefaultItemAnimator());
        Bundle bundle  = this.getIntent().getExtras();
        type = bundle.getString("type");
        subType = bundle.getString("subType");
        getSupportActionBar().setTitle(subType);
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
    //MoreListAdapter to inflate the data in recyclerView
    private class MoreListAdapter extends RecyclerView.Adapter<MoreListAdapter.moreListViewHolder> {
        private ArrayList<MoreListModel> moreListArrayList;

        class moreListViewHolder extends RecyclerView.ViewHolder {
            ImageView moreListImageView;
            TextView moreListNameTextView;
            TextView moreListReleaseDateTextView;
            TextView moreListRatingTextView;
            TextView moreListIdTextView;
            ProgressBar moreListProgressBar;
            public moreListViewHolder(View itemView) {
                super(itemView);
                moreListNameTextView = (TextView) itemView.findViewById(R.id.more_list_name);
                moreListImageView = (ImageView) itemView.findViewById(R.id.more_list_poster);
                moreListReleaseDateTextView = (TextView) itemView.findViewById(R.id.more_list_release_date);
                moreListRatingTextView = (TextView) itemView.findViewById(R.id.more_list_ratings);
                moreListIdTextView = (TextView) itemView.findViewById(R.id.more_list_id);
                moreListProgressBar = (ProgressBar) itemView.findViewById(R.id.more_list_poster_progressBar);
            }
        }

        public MoreListAdapter(ArrayList<MoreListModel> arrayList) {
            this.moreListArrayList = arrayList;
        }

        @Override
        public moreListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.more_list_textview, parent, false);
            return new moreListViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final moreListViewHolder holder, final int position) {
            final MoreListModel moreListModel = moreListArrayList.get(position);
            holder.moreListNameTextView.setText(moreListModel.getName());
            holder.moreListReleaseDateTextView.setText(moreListModel.getReleaseDate());
            holder.moreListRatingTextView.setText(moreListModel.getRating());
            holder.moreListIdTextView.setText(moreListModel.getId());
            Picasso.with(getBaseContext()).load("https://image.tmdb.org/t/p/w500" + moreListModel.getImage())
                    .into(holder.moreListImageView,new com.squareup.picasso.Callback() {

                        @Override
                        public void onSuccess() {
                            holder.moreListProgressBar.setVisibility(View.GONE);
                            holder.moreListImageView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError() {

                        }
                    });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String type2;
                    Intent intent = new Intent(MoreList.this,MoviesDetails.class);
                    if (type.equals("movie")){
                        type2 = "movies";
                    }
                    else {
                        type2=type;
                    }
                    Bundle mBundle = new Bundle();
                    mBundle.putString("type",type2);
                    mBundle.putString("id",moreListModel.getId());
                    Log.i("string",type +" / " +moreListModel.getId() );
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return moreListArrayList.size();
        }
    }

    public class FetchTask extends AsyncTask<String, Void, ArrayList<String>> {

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
            ArrayList<MoreListModel> movieListsArray = new ArrayList<>();
            ArrayList<MoreListModel> tvListArray = new ArrayList<>();
            ArrayList<MoreListModel> celebsListArray = new ArrayList<>();
            MoreListJsonParser moreListJsonParser = new MoreListJsonParser();
                //Log.v("type", type);
            //populate data for movie
                if(type.compareTo("movie") == 0) {
                    // Log.v("type", type);
                    try {
                        for (int i = 0; i < 5; i++) {
                            movieListsArray = moreListJsonParser.jsonMovieListParser(jsonArray.get(i), movieListsArray);
                            moreListAdapter = new MoreListAdapter(movieListsArray);
                            recyclerViewMoreList.setAdapter(moreListAdapter);
                            progressBar.setVisibility(View.GONE);
                            recyclerViewMoreList.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                //populate data for tvShows
                else if (type.compareTo("tv") == 0){
                    try {
                        for (int i = 0; i < 5; i++) {
                            tvListArray = moreListJsonParser.jsonTvListParser(jsonArray.get(i),tvListArray);
                            moreListAdapter = new MoreListAdapter(tvListArray);
                            recyclerViewMoreList.setAdapter(moreListAdapter);
                            progressBar.setVisibility(View.GONE);
                            recyclerViewMoreList.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //populate data for celebrities
                else if (type.compareTo("person") == 0){
                    try {
                        for (int i = 0; i < 5; i++) {
                            celebsListArray = moreListJsonParser.jsonCelebsListParser(jsonArray.get(i), celebsListArray);
                            moreListAdapter = new MoreListAdapter(celebsListArray);
                            recyclerViewMoreList.setAdapter(moreListAdapter);
                            progressBar.setVisibility(View.GONE);
                            recyclerViewMoreList.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
}
