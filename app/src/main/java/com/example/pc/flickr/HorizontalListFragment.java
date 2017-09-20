package com.example.pc.flickr;


import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
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
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            String str = arrayList.get(position);
            holder.parentCardViewHeading.setText(str);
            holder.childRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            class GetFilterData extends AsyncTask<String,Void,Cursor>{

                @Override
                protected Cursor doInBackground(String... params) {
                    MovieDbHelper movieDbHelper = new MovieDbHelper(getContext());
                    SQLiteDatabase db = movieDbHelper.getReadableDatabase();
                    String WHERE = "type=? AND type_sub=?";
                    String args[] = {type,params[0]};
                    return getContext().getContentResolver().query(MovieDbApiContract.ApiData.CONTENT_URI,
                            null,
                            WHERE,
                            args,
                            null);
                }
                protected void onPostExecute(Cursor cursor) {
                    childAdapter = new HorizontalListFragment.ChildMainAdapter(cursor, cursor.getCount());
                    holder.childRecyclerView.setAdapter(childAdapter);
                }
            }
            GetFilterData filterData = new GetFilterData();
            filterData.execute(str);
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
        private List<ListDataModel> dataList;


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
                itemview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(),MoviesDetails.class);
                        Bundle mBundle = new Bundle();
                        ListDataModel dataModel = dataList.get(getAdapterPosition());
                        mBundle.putString("type",dataModel.getType());
                        mBundle.putString("id",dataModel.getId());
                        intent.putExtras(mBundle);
                        startActivity(intent);
                    }
                });
            }
        }

        public ChildMainAdapter(Cursor cursor, int mCount){
            this.mCount = mCount;
            this.cursor = cursor;
            dataProvider();
        }

        public void dataProvider(){
            list = new ArrayList<>();
            dataList = new ArrayList<>();
            while (cursor.moveToNext()){
                DataModel dataModel = new DataModel(cursor.getString(2),cursor.getString(3),cursor.getString(6),cursor.getString(8));
                ListDataModel listDataModel = new ListDataModel(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(6),
                        cursor.getString(8),cursor.getString(4),cursor.getString(5));
                dataList.add(listDataModel);
                list.add(dataModel);
            }
            cursor.close();
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
    private class ListDataModel{
        private String name;
        private String popularity;
        private String vote_avg;
        private String img_url;
        private String id;
        private String type;
        private String subType;
        public ListDataModel(String id,String name,String popularity,String vote_avg,String img_url,String type,String subType){
            this.img_url = img_url;
            this.name = name;
            this.vote_avg = vote_avg;
            this.popularity = popularity;
            this.id = id;
            this.type = type;
            this.subType = subType;
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

        public String getId() {
            return id;
        }

        public String getSubType() {
            return subType;
        }

        public String getType() {
            return type;
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
}
