package com.example.pc.flickr.Adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pc.flickr.MoreList;
import com.example.pc.flickr.MoviesDetails;
import com.example.pc.flickr.R;
import com.example.pc.flickr.data.MovieDbApiContract;
import com.example.pc.flickr.fragments.HorizontalListFragment;
import com.example.pc.flickr.models.ListDataModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by Deepanshu on 11/12/2017.
 */

public class MainAdapters {


    public static class MainParentAdapter extends RecyclerView.Adapter<MainParentAdapter.MyViewHolder> {
        private MainChildAdapter childAdapter;
        private ArrayList<String> arrayList;
        private RecyclerView.RecycledViewPool viewPool;
        private Context context;
        private String type;

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView parentButton;
            TextView parentCardViewHeading;
            RecyclerView childRecyclerView;
            public MyViewHolder(View itemview){
                super(itemview);
                parentCardViewHeading = (TextView) itemview.findViewById(R.id.main_horizontal_card_heading);
                childRecyclerView = (RecyclerView) itemview.findViewById(R.id.main_child_recyclerView);
                parentButton = (TextView) itemview.findViewById(R.id.main_horizontal_card_Button);
            }
        }

        public MainParentAdapter(ArrayList<String> arrayList,Context context,String type){

            this.arrayList = arrayList;
            viewPool = new RecyclerView.RecycledViewPool();
            this.context = context;
            this.type = type;
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
            holder.childRecyclerView.setRecycledViewPool(viewPool);
            holder.childRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            class GetFilterData extends AsyncTask<String,Void,Cursor> {

                @Override
                protected Cursor doInBackground(String... params) {
                    String WHERE = "type=? AND type_sub=?";
                    String args[] = {type,params[0]};
                    return context.getContentResolver().query(MovieDbApiContract.ApiData.CONTENT_URI,
                            null,
                            WHERE,
                            args,
                            null);
                }
                protected void onPostExecute(Cursor cursor) {
                    childAdapter = new MainChildAdapter(cursor, cursor.getCount(),context);
                    holder.childRecyclerView.setAdapter(childAdapter);
                }
            }
            final GetFilterData filterData = new GetFilterData();
            BroadcastReceiver serviceReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    filterData.execute(str);
                }
            };
            LocalBroadcastManager.getInstance(context)
                    .registerReceiver(serviceReceiver, new IntentFilter("myBroadcastIntent"));
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
                    Intent intent = new Intent(context,MoreList.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("type",sType);
                    mBundle.putString("subType",subType[(sPosition*4)+position]);
                    intent.putExtras(mBundle);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        @Override
        public void onViewDetachedFromWindow(MyViewHolder holder) {
            super.onViewDetachedFromWindow(holder);
            holder.childRecyclerView.removeAllViewsInLayout();
            holder.childRecyclerView.removeAllViews();
        }
    }

    public static class MainChildAdapter extends RecyclerView.Adapter<MainChildAdapter.MyViewHolder> {

        private Cursor cursor;
        private int mCount;
        private ArrayList<ListDataModel> dataList;
        private Context context;

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView childViewVote;
            ImageView childImageView;
            ProgressBar progressBar;
            public MyViewHolder(View itemview){
                super(itemview);
                childViewVote = (TextView) itemview.findViewById(R.id.main_child_rating);
                childImageView = (ImageView) itemview.findViewById(R.id.main_child_imageView);
                itemview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context,MoviesDetails.class);
                        Bundle mBundle = new Bundle();
                        ListDataModel dataModel = dataList.get(getAdapterPosition());
                        mBundle.putString("type",dataModel.getType());
                        mBundle.putString("id",dataModel.getId());
                        intent.putExtras(mBundle);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(intent);
                    }
                });
            }
        }

        public MainChildAdapter(Cursor cursor, int mCount, Context context){
            this.mCount = mCount;
            this.cursor = cursor;
            this.context = context;
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
            //holder.childViewTitle.setText(dataModel.getName());
            holder.childViewVote.setText(dataModel.getVote_avg());
            //holder.childViewPopularity.setText(dataModel.getPopularity());
            //final ProgressBar progressBar = holder.progressBar;
            final int radius = 10;
            final int margin = 5;
            final Transformation transformation = new RoundedCornersTransformation(radius, margin);
            Picasso.with(context).load("https://image.tmdb.org/t/p/w500"+dataModel.getImg_url())
                    .into(holder.childImageView,new com.squareup.picasso.Callback() {

                        @Override
                        public void onSuccess() {
                            //holder.progressBar.setVisibility(View.GONE);
                            //holder.childImageView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError() {

                        }
                    });

        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        @Override
        public void onViewDetachedFromWindow(MyViewHolder holder) {
            super.onViewDetachedFromWindow(holder);
            Picasso.with(context).cancelRequest(holder.childImageView);
            holder.childImageView.setImageURI(null);
            holder.itemView.invalidate();
            holder.childImageView.invalidate();
        }

    }


}
