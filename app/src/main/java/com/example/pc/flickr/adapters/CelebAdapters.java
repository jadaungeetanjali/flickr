package com.example.pc.flickr.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pc.flickr.activities.MoviesDetails;
import com.example.pc.flickr.R;
import com.example.pc.flickr.models.CelebImageModel;
import com.example.pc.flickr.models.SimilarItemModel;
import com.example.pc.flickr.util.activities.ActivityConfig;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by PC on 9/29/2017.
 */

public class CelebAdapters {

    public static class CelebsMovieCreditAdapter  extends RecyclerView.Adapter<CelebsMovieCreditAdapter.celebsMovieCreditViewHolder> {
        public ArrayList<SimilarItemModel> similarMoviesArrayList;
        public Context similarMovieContext;
        public String type;

        class celebsMovieCreditViewHolder extends RecyclerView.ViewHolder {
            ImageView similarMovieImageView;
            TextView similarMovieNameTextView ;
            TextView similarMovieVoteAverageTextView;
            ProgressBar similarMovieProgressBar;

            public celebsMovieCreditViewHolder(View itemView) {
                super(itemView);
                //change id to similarMovieName
                similarMovieImageView = (ImageView) itemView.findViewById(R.id.main_child_imageView); //change id to similarMovieImage
                similarMovieVoteAverageTextView = (TextView) itemView.findViewById(R.id.main_child_rating);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(similarMovieContext,MoviesDetails.class);
                        Bundle mBundle = new Bundle();
                        SimilarItemModel similarItemModel = similarMoviesArrayList.get(getAdapterPosition());
                        mBundle.putString("type", ActivityConfig.MOVIES);
                        mBundle.putString("id",similarItemModel.getSimilarItemId());
                        intent.putExtras(mBundle);
                        similarMovieContext.startActivity(intent);
                    }
                });
            }
        }

        public CelebsMovieCreditAdapter(Context context, ArrayList<SimilarItemModel> arrayList, String type) {
            this.similarMoviesArrayList = arrayList;
            this.similarMovieContext = context;
            this.type = type;
        }

        @Override
        public celebsMovieCreditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_vertical_card, parent, false); //change layout id
            return new celebsMovieCreditViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final celebsMovieCreditViewHolder holder, int position) {
            SimilarItemModel SimilarItemModel = similarMoviesArrayList.get(position);
            //holder.similarMovieNameTextView.setText(SimilarItemModel.getSimilarItemimage());
            holder.similarMovieVoteAverageTextView.setText(SimilarItemModel.getSimilarItemvote());
            Picasso.with(similarMovieContext).load("https://image.tmdb.org/t/p/w500"+SimilarItemModel.getSimilarItemimage())
                    .into(holder.similarMovieImageView,new com.squareup.picasso.Callback() {

                        @Override
                        public void onSuccess() {
                            //holder.similarMovieProgressBar.setVisibility(View.GONE);
                            //holder.similarMovieImageView.setVisibility(View.VISIBLE);
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

    public static class CelebsImagesAdapter extends RecyclerView.Adapter<CelebsImagesAdapter.celebsImagesViewHolder> {
        public ArrayList<CelebImageModel> celebImageModelArrayList;
        public Context imageContext;
        public String type;

        class celebsImagesViewHolder extends RecyclerView.ViewHolder {
            ImageView celebsImagesImageView;
            ProgressBar celebsImagesImageViewProgressBar;

            public celebsImagesViewHolder(View itemView) {
                super(itemView);
                celebsImagesImageViewProgressBar = (ProgressBar) itemView.findViewById(R.id.celeb_images_progressBar);
                celebsImagesImageView = (ImageView) itemView.findViewById(R.id.celeb_images);
            }
        }

        public CelebsImagesAdapter(Context context, ArrayList<CelebImageModel> arrayList) {
            this.celebImageModelArrayList = arrayList;
            this.imageContext = context;
        }

        @Override
        public celebsImagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.celeb_images_textview, parent, false); //change layout id
            return new celebsImagesViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final celebsImagesViewHolder holder, int position) {
            CelebImageModel celebImageModel = celebImageModelArrayList.get(position);
            Picasso.with(imageContext).load("https://image.tmdb.org/t/p/w500" + celebImageModel.getCelebImage())
                    .into(holder.celebsImagesImageView, new com.squareup.picasso.Callback() {

                        @Override
                        public void onSuccess() {
                            holder.celebsImagesImageViewProgressBar.setVisibility(View.GONE);
                            holder.celebsImagesImageView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError() {

                        }
                    });

        }

        @Override
        public int getItemCount() {
            return celebImageModelArrayList.size();
        }
    }
}
