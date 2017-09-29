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

import com.example.pc.flickr.MoviesDetails;
import com.example.pc.flickr.R;
import com.example.pc.flickr.fragments.CelebsFragment;
import com.example.pc.flickr.models.CelebImageModel;
import com.example.pc.flickr.models.SimilarItemModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by PC on 9/29/2017.
 */

public class CelebAdapters {

    public static class CelebsMovieCreditAdapter extends RecyclerView.Adapter<CelebsMovieCreditAdapter.celebsMovieCreditViewHolder> {
        public ArrayList<SimilarItemModel> celebsMovieCreditArrayList;

        class celebsMovieCreditViewHolder extends RecyclerView.ViewHolder {
            ImageView celebsMovieCreditImageView;
            TextView celebsMovieCreditNameTextView ;
            TextView celebsMovieCreditVoteAverageTextView;
            ProgressBar celebsMovieCreditProgressBar;

            public celebsMovieCreditViewHolder(View itemView) {
                super(itemView);
                celebsMovieCreditNameTextView = (TextView) itemView.findViewById(R.id.main_child_title_textView);
                celebsMovieCreditImageView = (ImageView) itemView.findViewById(R.id.main_child_imageView);
                celebsMovieCreditVoteAverageTextView = (TextView) itemView.findViewById(R.id.main_child_vote_textView);
                celebsMovieCreditProgressBar = (ProgressBar) itemView.findViewById(R.id.main_image_progressBar);
                /*itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(),MoviesDetails.class);
                        Bundle mBundle = new Bundle();
                        SimilarItemModel similarItemModel = celebsMovieCreditArrayList.get(getAdapterPosition());
                        mBundle.putString("type",type);
                        mBundle.putString("id",similarItemModel.getSimilarItemId());
                        intent.putExtras(mBundle);
                        startActivity(intent);
                    }
                });*/
            }
        }

        public CelebsMovieCreditAdapter(ArrayList<SimilarItemModel> arrayList) {
            this.celebsMovieCreditArrayList = arrayList;
        }

        @Override
        public celebsMovieCreditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_vertical_card, parent, false); //change layout id
            return new celebsMovieCreditViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final celebsMovieCreditViewHolder holder, int position) {
            SimilarItemModel similarItemModel = celebsMovieCreditArrayList.get(position);
            holder.celebsMovieCreditNameTextView.setText(similarItemModel.getSimilarItemName());
            Log.v("output", similarItemModel.getSimilarItemName());
            holder.celebsMovieCreditVoteAverageTextView.setText(similarItemModel.getSimilarItemVoteAverage());
            Picasso.with().load("https://image.tmdb.org/t/p/w500"+similarItemModel.getSimilarItemimage())
                    .into(holder.celebsMovieCreditImageView, new com.squareup.picasso.Callback(){
                        @Override
                        public void onSuccess() {
                            holder.celebsMovieCreditProgressBar.setVisibility(View.GONE);
                            holder.celebsMovieCreditImageView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }

        @Override
        public int getItemCount() {
            return celebsMovieCreditArrayList.size();
        }
    }

    public static class CelebsImagesAdapter extends RecyclerView.Adapter<CelebsImagesAdapter.celebsImagesViewHolder> {
        public ArrayList<CelebImageModel> celebImageModelArrayList;

        class celebsImagesViewHolder extends RecyclerView.ViewHolder {
            ImageView celebsImagesImageView;

            public celebsImagesViewHolder(View itemView) {
                super(itemView);
                celebsImagesImageView = (ImageView) itemView.findViewById(R.id.celeb_images);
            }
        }

        public CelebsImagesAdapter(ArrayList<CelebImageModel> arrayList) {
            this.celebImageModelArrayList = arrayList;
        }

        @Override
        public celebsImagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.celeb_images_textview, parent, false); //change layout id
            return new celebsImagesViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final celebsImagesViewHolder holder, int position) {
            CelebImageModel celebImageModel = celebImageModelArrayList.get(position);
            Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500" + celebImageModel.getCelebImage())
                    .into(holder.celebsImagesImageView);

        }

        @Override
        public int getItemCount() {
            return celebImageModelArrayList.size();
        }
    }

}
