package com.example.pc.flickr.fragments;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pc.flickr.MoreList;
import com.example.pc.flickr.MoviesDetails;
import com.example.pc.flickr.R;
import com.example.pc.flickr.data.MovieDbApiContract;
import com.example.pc.flickr.data.MovieDbHelper;
import com.example.pc.flickr.models.ListDataModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.Arrays;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class HorizontalListFragment extends Fragment {
    public MainParentAdapter mAdapter;
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
        mAdapter = new HorizontalListFragment.MainParentAdapter(urlHeadingList);
        recyclerView.setAdapter(mAdapter);
        return rootView;
    }

    //----------------------------------------------------------------------------------------------------------//
    private class MainParentAdapter extends RecyclerView.Adapter<MainParentAdapter.MyViewHolder> {
        private MainChildAdapter childAdapter;
        private ArrayList<String> arrayList;

        class MyViewHolder extends RecyclerView.ViewHolder{
            LinearLayout parentButton;
            TextView parentCardViewHeading;
            RecyclerView childRecyclerView;
            public MyViewHolder(View itemview){
                super(itemview);
                parentCardViewHeading = (TextView) itemview.findViewById(R.id.main_horizontal_card_heading);
                childRecyclerView = (RecyclerView) itemview.findViewById(R.id.main_child_recyclerView);
                parentButton = (LinearLayout) itemview.findViewById(R.id.main_horizontal_card_Button);
            }
        }

        public MainParentAdapter(ArrayList<String> arrayList){
            this.arrayList = arrayList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.main_horizontal_card, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final String str = arrayList.get(position);
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
                    childAdapter = new HorizontalListFragment.MainChildAdapter(cursor, cursor.getCount());
                    holder.childRecyclerView.setAdapter(childAdapter);
                }
            }
            GetFilterData filterData = new GetFilterData();
            filterData.execute(str);
            holder.parentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sType;
                    String[] subType={"now_playing","popular","top_rated","upcoming",
                           "popular","airing_today","top_rated","on_the_air",
                           "popular"};
                    int sPosition =0;
                    if (type.equals("movies")) {
                        sType = "movie";
                        sPosition = 0;
                    }
                    else if (type.equals("tv")){
                        sType = "tv";
                        sPosition = 1;
                    }
                    else {
                        sType = "person";
                        sPosition = 2;
                    }
                    Intent intent = new Intent(getContext(),MoreList.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("type",sType);
                    mBundle.putString("subType",subType[(sPosition*4)+position]);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//





    //----------------------------------------------------------------------------------------------------//
    private class MainChildAdapter extends RecyclerView.Adapter<MainChildAdapter.MyViewHolder> {

        private Cursor cursor;
        private int mCount;
        private ArrayList<ListDataModel> dataList;

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView childViewTitle;
            TextView childViewVote;
            TextView childViewPopularity;
            ImageView childImageView;
            ProgressBar progressBar;
            public MyViewHolder(View itemview){
                super(itemview);
                childViewTitle = (TextView) itemview.findViewById(R.id.main_child_title_textView);
                childViewVote = (TextView) itemview.findViewById(R.id.main_child_vote_textView);
                childViewPopularity = (TextView) itemview.findViewById(R.id.main_child_popularity_textView);
                childImageView = (ImageView) itemview.findViewById(R.id.main_child_imageView);
                progressBar = (ProgressBar) itemview.findViewById(R.id.main_image_progressBar);
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

        public MainChildAdapter(Cursor cursor, int mCount){
            this.mCount = mCount;
            this.cursor = cursor;
            dataProvider();
        }

        public void dataProvider(){
            dataList = new ArrayList<>();
            while (cursor.moveToNext()){
                ListDataModel listDataModel = new ListDataModel(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(6),
                        cursor.getString(8),cursor.getString(4),cursor.getString(5));
                dataList.add(listDataModel);
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
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            ListDataModel dataModel = dataList.get(position);
            holder.childViewTitle.setText(dataModel.getName());
            holder.childViewVote.setText(dataModel.getVote_avg());
            holder.childViewPopularity.setText(dataModel.getPopularity());
            //final ProgressBar progressBar = holder.progressBar;
            final int radius = 10;
            final int margin = 5;
            final Transformation transformation = new RoundedCornersTransformation(radius, margin);
            Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500"+dataModel.getImg_url()).transform(transformation)
                    .into(holder.childImageView,new com.squareup.picasso.Callback() {

                        @Override
                        public void onSuccess() {
                            holder.progressBar.setVisibility(View.GONE);
                            holder.childImageView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError() {

                        }
                    });

        }

        @Override
        public int getItemCount() {
            return mCount;
        }
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//


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
