package com.example.pc.flickr;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.flickr.R;
import com.example.pc.flickr.data.MovieDbApiContract;
import com.example.pc.flickr.data.MovieDbHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

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

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class HorizontalListFragment extends Fragment {
    public PrimaryMainAdapter mAdapter;
    String type;
    private String urls[];
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
        ArrayList<String> urlHeadingList = new ArrayList<>(Arrays.asList(urlHeading));
        urls = this.getArguments().getStringArray("urls");
        type = this.getArguments().getString("type");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new HorizontalListFragment.PrimaryMainAdapter(urlHeadingList);
        recyclerView.setAdapter(mAdapter);
        return rootView;
    }

    private class PrimaryMainAdapter extends RecyclerView.Adapter<PrimaryMainAdapter.MyViewHolder> {
        private ChildMainAdapter childAdapter;
        private ArrayList<String> arrayList;

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView parentCardViewHeading;
            RecyclerView childRecyclerView;
            public MyViewHolder(View itemview){
                super(itemview);
                parentCardViewHeading = (TextView) itemview.findViewById(R.id.main_horizontal_card_heading);
                childRecyclerView = (RecyclerView) itemview.findViewById(R.id.main_child_recyclerView);
            }
        }

        public PrimaryMainAdapter(ArrayList<String> arrayList){
            this.arrayList = arrayList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.main_horizontal_card, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            String str = arrayList.get(position);
            holder.parentCardViewHeading.setText(str);

            FetchTask fetchTask = new FetchTask();
            ArrayList<String> childArrayList = new ArrayList<>();
            ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if ((networkInfo != null) && networkInfo.isConnected()){
                fetchTask.execute(urls[position]);

                try {
                    String jsonData = fetchTask.get();
                    if(type.equals("movies")) {
                        childArrayList = jsonMovieParser(jsonData, str);
                    }
                    else if(type.equals("tv")){
                        childArrayList = jsonTvParser(jsonData, str);
                    }
                    else if (type.equals("celebs")){
                        childArrayList = jsonCelebsParser(jsonData, str);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(getContext(), "Please connect to internet", Toast.LENGTH_LONG).show();
            }
            holder.childRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            Cursor cursor = getData(type,str);
            childAdapter = new HorizontalListFragment.ChildMainAdapter(cursor, cursor.getCount());
            holder.childRecyclerView.setAdapter(childAdapter);

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }
    private class ChildMainAdapter extends RecyclerView.Adapter<ChildMainAdapter.MyViewHolder> {

        private Cursor cursor;
        private int mCount;
        private List<DataModel> list;


        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView childViewTitle;
            TextView childViewVote;
            TextView childViewPopularity;
            ImageView childImageView;
            public MyViewHolder(View itemview){
                super(itemview);
                childViewTitle = (TextView) itemview.findViewById(R.id.main_child_title_textView);
                childViewVote = (TextView) itemview.findViewById(R.id.main_child_vote_textView);
                childViewPopularity = (TextView) itemview.findViewById(R.id.main_child_popularity_textView);
                childImageView = (ImageView) itemview.findViewById(R.id.main_child_imageView);
            }
        }

        public ChildMainAdapter(Cursor cursor, int mCount){
            this.mCount = mCount;
            this.cursor = cursor;
            dataProvider();
        }

        public void dataProvider(){
            list = new ArrayList<>();
            while (cursor.moveToNext()){
                DataModel dataModel = new DataModel(cursor.getString(2),cursor.getString(3),cursor.getString(0),cursor.getString(8));
                list.add(dataModel);
            }
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.main_vertical_card, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            DataModel dataModel = list.get(position);
            holder.childViewTitle.setText(dataModel.getName());
            holder.childViewVote.setText(dataModel.getVote_avg());
            holder.childViewPopularity.setText(dataModel.getPopularity());
            final int radius = 10;
            final int margin = 5;
            final Transformation transformation = new RoundedCornersTransformation(radius, margin);
            Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500"+dataModel.getImg_url()).transform(transformation)
                    .into(holder.childImageView);
        }

        @Override
        public int getItemCount() {
            return mCount;
        }
    }
    private class DataModel{
        private String name;
        private String popularity;
        private String vote_avg;
        private String img_url;
        public DataModel(String name,String popularity,String vote_avg,String img_url){
            this.img_url = img_url;
            this.name = name;
            this.vote_avg = vote_avg;
            this.popularity = popularity;
        }

        public String getName(){
            return name;
        }
        public String getPopularity(){
            return popularity;
        }

        public String getImg_url() {
            return img_url;
        }

        public String getVote_avg() {
            return vote_avg;
        }
    }
    private Cursor getData(String type, String sub_type){
        MovieDbHelper movieDbHelper = new MovieDbHelper(getContext());
        SQLiteDatabase db = movieDbHelper.getReadableDatabase();
        String WHERE = "type=? AND type_sub=?";
        String args[] = {type,sub_type};
        return db.query(MovieDbApiContract.ApiData.TABLE_NAME,
                null,
                WHERE,
                args,
                null,
                null,
                MovieDbApiContract.ApiData._ID
                );
    }




    private ArrayList<String> jsonMovieParser(String jsonMovie, String type)throws JSONException {

        MovieDbHelper movieDbHelper = new MovieDbHelper(getContext());
        SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        ArrayList<String> movieArray = new ArrayList<>();
        JSONObject movieObject = new JSONObject(jsonMovie);
        JSONArray list = movieObject.getJSONArray("results");
        for (int i = 0; i < list.length();i++){
            JSONObject popularMovie = list.getJSONObject(i);
            String title = popularMovie.get("title").toString();
            String id = popularMovie.get("id").toString();
            String popularity = Math.round(Double.parseDouble(popularMovie.get("popularity").toString())) + "";
            String vote_average = popularMovie.get("vote_average").toString();
            String imgUrl = popularMovie.get("poster_path").toString();
            values.put(MovieDbApiContract.ApiData.COLUMN_ID, id);
            values.put(MovieDbApiContract.ApiData.COLUMN_NAME, title);
            values.put(MovieDbApiContract.ApiData.COLUMN_POPULARITY, popularity);
            values.put(MovieDbApiContract.ApiData.COLUMN_VOTE_AVERAGE, vote_average);
            values.put(MovieDbApiContract.ApiData.COLUMN_TYPE, "movies");
            values.put(MovieDbApiContract.ApiData.COLUMN_TYPE_SUB, type);
            values.put(MovieDbApiContract.ApiData.COLUMN_IMG_URL, imgUrl);
            values.put(MovieDbApiContract.ApiData.COLUMN_WISH_LIST, false);
            long rowId = db.insert(MovieDbApiContract.ApiData.TABLE_NAME, null, values);
            movieArray.add(title);
        }
        return movieArray;
    }

    private ArrayList<String> jsonTvParser(String jsontvShows, String type)throws JSONException {
        MovieDbHelper movieDbHelper = new MovieDbHelper(getContext());
        SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();


        ArrayList<String> tvShowsArray = new ArrayList<>();
        JSONObject tvShowsObject = new JSONObject(jsontvShows);
        JSONArray list = tvShowsObject.getJSONArray("results");

        for (int i = 0; i < list.length();i++){
            JSONObject populartvShows = list.getJSONObject(i);
            String id = populartvShows.get("id").toString();
            String title = populartvShows.get("name").toString();
            String popularity = Math.round(Double.parseDouble(populartvShows.get("popularity").toString())) + "";
            String vote_average = populartvShows.get("vote_average").toString();
            String imgUrl = populartvShows.get("poster_path").toString();
            String args[]={i+""};
            values.put(MovieDbApiContract.ApiData.COLUMN_ID, id);
            values.put(MovieDbApiContract.ApiData.COLUMN_NAME, title);
            values.put(MovieDbApiContract.ApiData.COLUMN_POPULARITY, popularity);
            values.put(MovieDbApiContract.ApiData.COLUMN_VOTE_AVERAGE, vote_average);
            values.put(MovieDbApiContract.ApiData.COLUMN_TYPE, "tv");
            values.put(MovieDbApiContract.ApiData.COLUMN_TYPE_SUB, type);
            values.put(MovieDbApiContract.ApiData.COLUMN_IMG_URL, imgUrl);
            values.put(MovieDbApiContract.ApiData.COLUMN_WISH_LIST, false);
            long rowId = db.update(MovieDbApiContract.ApiData.TABLE_NAME,values,"type_id=?",args);
            tvShowsArray.add(title);
        }
        return tvShowsArray;
    }


    private ArrayList<String> jsonCelebsParser(String jsonCelebrities,String type)throws JSONException {
        //MovieDbHelper movieDbHelper = new MovieDbHelper(getContext());
        //SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        ArrayList<String> celebritiesArray = new ArrayList<>();
        JSONObject celebritiesObject = new JSONObject(jsonCelebrities);
        JSONArray list = celebritiesObject.getJSONArray("results");
        for (int i = 0; i < list.length();i++){
            JSONObject popularCelebrities = list.getJSONObject(i);
            String id = popularCelebrities.get("id").toString();
            String title = popularCelebrities.get("name").toString();
            JSONObject known_for = popularCelebrities.getJSONArray("known_for").getJSONObject(0);
            String popularity = Math.round(Double.parseDouble(popularCelebrities.get("popularity").toString())) + "";
            String vote_average = known_for.get("vote_average").toString();
            String imgUrl = popularCelebrities.get("profile_path").toString();
            values.put(MovieDbApiContract.ApiData.COLUMN_ID, id);
            values.put(MovieDbApiContract.ApiData.COLUMN_NAME, title);
            values.put(MovieDbApiContract.ApiData.COLUMN_POPULARITY, popularity);
            values.put(MovieDbApiContract.ApiData.COLUMN_VOTE_AVERAGE, vote_average);
            values.put(MovieDbApiContract.ApiData.COLUMN_TYPE, "celebs");
            values.put(MovieDbApiContract.ApiData.COLUMN_TYPE_SUB, type);
            values.put(MovieDbApiContract.ApiData.COLUMN_IMG_URL, imgUrl);
            values.put(MovieDbApiContract.ApiData.COLUMN_WISH_LIST, false);
            Uri uri = getContext().getContentResolver().insert(MovieDbApiContract.ApiData.CONTENT_URI, values);

            if (uri != null){
                Toast.makeText(getContext(), uri.toString(), Toast.LENGTH_SHORT).show();
            }

            celebritiesArray.add(title);
        }
        return celebritiesArray;
    }
}
