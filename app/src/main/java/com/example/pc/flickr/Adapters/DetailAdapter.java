package com.example.pc.flickr.Adapters;

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

import com.example.pc.flickr.MoviesDetails;
import com.example.pc.flickr.R;
import com.example.pc.flickr.models.CastModel;
import com.example.pc.flickr.models.ReviewModel;
import com.example.pc.flickr.models.SimilarItemModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

/**
 * Created by PC on 9/29/2017.
 */

public class DetailAdapter {

    // CastAdapter class to populate data in castRecyclerView
    public static class CastAdapter extends RecyclerView.Adapter<CastAdapter.castViewHolder> {
        public ArrayList<CastModel> castArrayList;
        public Context castContext;

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
                itemView.setOnClickListener(new View.OnClickListener() {
                   @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(castContext,MoviesDetails.class);
                        Bundle mBundle = new Bundle();
                        CastModel castModel = castArrayList.get(getAdapterPosition());
                        mBundle.putString("type","celebs");
                        mBundle.putString("id",castModel.getId());
                        intent.putExtras(mBundle);
                        castContext.startActivity(intent);
                    }
                });
            }
        }

        public CastAdapter(Context context, ArrayList<CastModel> arrayList) {
            this.castArrayList = arrayList;
            this.castContext = context;
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
            Picasso.with(castContext).load("https://image.tmdb.org/t/p/w500"+castModel.getImage()).
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
    public static class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.reviewViewHolder> {
        public ArrayList<ReviewModel> reviewArrayList;

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
    public static class SimilarMoviesAdapter extends RecyclerView.Adapter<SimilarMoviesAdapter.similarMoviesViewHolder> {
        public ArrayList<SimilarItemModel> similarMoviesArrayList;
        public Context similarMovieContext;
        public String type;

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
                similarMovieVoteAverageTextView = (TextView) itemView.findViewById(R.id.main_child_vote_textView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(similarMovieContext,MoviesDetails.class);
                        Bundle mBundle = new Bundle();
                        SimilarItemModel similarItemModel = similarMoviesArrayList.get(getAdapterPosition());
                        mBundle.putString("type",type);
                        mBundle.putString("id",similarItemModel.getSimilarItemId());
                        intent.putExtras(mBundle);
                        similarMovieContext.startActivity(intent);
                    }
                });
            }
        }

        public SimilarMoviesAdapter(Context context, ArrayList<SimilarItemModel> arrayList, String type) {
            this.similarMoviesArrayList = arrayList;
            this.similarMovieContext = context;
            this.type = type;
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
            Picasso.with(similarMovieContext).load("https://image.tmdb.org/t/p/w500"+SimilarItemModel.getSimilarItemimage())
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
}