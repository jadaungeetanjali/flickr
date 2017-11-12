package com.example.pc.flickr.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pc.flickr.MoreList;
import com.example.pc.flickr.MoviesDetails;
import com.example.pc.flickr.R;
import com.example.pc.flickr.models.MoreListModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by PC on 10/2/2017.
 */
public class MoreListAdapter extends RecyclerView.Adapter<MoreListAdapter.moreListViewHolder> {
    public ArrayList<MoreListModel> moreListArrayList;
    public Context context;
    public String type;

    public static class moreListViewHolder extends RecyclerView.ViewHolder {
        ImageView moreListImageView;
        TextView moreListNameTextView;
        TextView moreListReleaseDateTextView;
        TextView moreListRatingTextView;
        ProgressBar moreListProgressBar;
        public moreListViewHolder(View itemView) {
            super(itemView);
            moreListNameTextView = (TextView) itemView.findViewById(R.id.more_list_name);
            moreListImageView = (ImageView) itemView.findViewById(R.id.more_list_poster);
            moreListReleaseDateTextView = (TextView) itemView.findViewById(R.id.more_list_release_date);
            moreListRatingTextView = (TextView) itemView.findViewById(R.id.more_list_ratings);
            moreListProgressBar = (ProgressBar) itemView.findViewById(R.id.more_list_poster_progressBar);
        }
    }

    public MoreListAdapter(ArrayList<MoreListModel> arrayList, Context context, String type) {
        this.moreListArrayList = arrayList;
        this.context = context;
        this.type = type;
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
        Picasso.with(context).load("https://image.tmdb.org/t/p/w500" + moreListModel.getImage())
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
                Intent intent = new Intent(context,MoviesDetails.class);
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
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moreListArrayList.size();
    }
}