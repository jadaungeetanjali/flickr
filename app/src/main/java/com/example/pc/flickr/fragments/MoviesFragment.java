package com.example.pc.flickr.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.flickr.adapters.DetailAdapter;
import com.example.pc.flickr.json_parsers.DetailMovieJsonParser;
import com.example.pc.flickr.json_parsers.DetailTvJsonParser;
import com.example.pc.flickr.models.DetailMovieModel;
import com.example.pc.flickr.models.WishListModel;
import com.example.pc.flickr.services.AsyncTaskCompleteListener;
import com.example.pc.flickr.services.CommonFetchTask;
import com.example.pc.flickr.services.Connectivity;
import com.example.pc.flickr.R;
import com.example.pc.flickr.models.CastModel;
import com.example.pc.flickr.models.ReviewModel;
import com.example.pc.flickr.models.SimilarItemModel;
import com.example.pc.flickr.models.VideoModel;
import com.example.pc.flickr.services.FirebaseCurd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment {
    private DetailAdapter.CastAdapter castAdapter;
    private DetailAdapter.ReviewAdapter reviewAdapter;
    private DetailAdapter.SimilarMoviesAdapter similarMoviesAdapter;
    private DetailAdapter.VideoAdapter videoAdapter;
    RecyclerView recyclerViewCast, recyclerViewReviews, recyclerViewSimilar,recyclerViewVideo;
    private TextView overview,genre,language,releaseDate,runtime,status;
    private ProgressBar progressBar;
    private ImageView rateitButton,wishListButton,watchListButton;
    private LinearLayout mainContainer;
    private String type, id;
    private Toolbar toolbar;
    private CommonFetchTask callMovieData;
    private ActionBar actionBar;
    private RatingBar vote_average;
    private boolean wishlist,watchlist;
    public MoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        //Initalizing detail layout members view
        overview = (TextView) rootView.findViewById(R.id.detail_movie_details);
        toolbar = (Toolbar) rootView.findViewById(R.id.detail_movie_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        actionBar = (ActionBar) ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        vote_average = (RatingBar) rootView.findViewById(R.id.detail_movie_rating);
        releaseDate = (TextView) rootView.findViewById(R.id.detail_movie_release_date);
        genre = (TextView) rootView.findViewById(R.id.detail_movie_genre);
        status = (TextView) rootView.findViewById(R.id.detail_movie_status);
        language = (TextView) rootView.findViewById(R.id.detail_movie_language);
        runtime = (TextView) rootView.findViewById(R.id.detail_movie_runtime);

        rateitButton = (ImageView) rootView.findViewById(R.id.detail_movie_user_rating);
        wishListButton = (ImageView) rootView.findViewById(R.id.detail_movie_add_wishlist);
        watchListButton = (ImageView) rootView.findViewById(R.id.detail_movie_user_watchlist);

        //Initializing Cast Recycler view
        recyclerViewCast = (RecyclerView) rootView.findViewById(R.id.detail_movie_cast_recyclerView);
        LinearLayoutManager layoutManagerCast = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCast.setLayoutManager(layoutManagerCast);
        recyclerViewCast.setItemAnimator(new DefaultItemAnimator());

        //Initializing Review Recycler View
        recyclerViewReviews = (RecyclerView) rootView.findViewById(R.id.detail_movie_review_recyclerVIew);
        LinearLayoutManager layoutManagerReviews = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewReviews.setLayoutManager(layoutManagerReviews);
        recyclerViewReviews.setItemAnimator(new DefaultItemAnimator());

        //Initializing Similar Recycler View
        recyclerViewSimilar = (RecyclerView) rootView.findViewById(R.id.detail_movie_similar_recyclerView);
        LinearLayoutManager layoutManagerSimilar = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSimilar.setLayoutManager(layoutManagerSimilar);
        recyclerViewSimilar.setItemAnimator(new DefaultItemAnimator());

        recyclerViewVideo = (RecyclerView) rootView.findViewById(R.id.detail_movie_videos_recyclerView);
        LinearLayoutManager layoutManagerVideo = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewVideo.setLayoutManager(layoutManagerVideo);
        recyclerViewVideo.setItemAnimator(new DefaultItemAnimator());

        Bundle bundle = getArguments();
        id = bundle.getString("id");
        type = bundle.getString("type");
        ArrayList<String> urlList = new ArrayList<>();
        switch (type){
            case "movies":
                urlList.add("https://api.themoviedb.org/3/movie/" + id + "?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
                urlList.add("https://api.themoviedb.org/3/movie/" + id + "/credits?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
                urlList.add("https://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
                urlList.add("https://api.themoviedb.org/3/movie/" + id + "/similar?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
                urlList.add("https://api.themoviedb.org/3/movie/" + id + "/videos?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
                break;
            case "tv":
                urlList.add("https://api.themoviedb.org/3/tv/" + id + "?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
                urlList.add("https://api.themoviedb.org/3/tv/" + id + "/credits?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
                urlList.add("https://api.themoviedb.org/3/tv/" + id + "/credits?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
                urlList.add("https://api.themoviedb.org/3/tv/" + id + "/similar?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
                urlList.add("https://api.themoviedb.org/3/tv/" + id + "/videos?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
                break;
            default:
        }

        Connectivity connectivity = new Connectivity(getActivity());

        if (connectivity.internetConnectivity()) {
            callMovieData = new CommonFetchTask(getContext(), new FetchTask());
            callMovieData.execute(urlList.get(0), urlList.get(1), urlList.get(2), urlList.get(3),urlList.get(4));
        }
        else{
            //internet = false;
            connectivity.checkNetworkConnection();
            Toast.makeText(getContext(), "Please Connect to internet...", Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }





    public class FetchTask implements AsyncTaskCompleteListener<ArrayList<String>>{

        @Override
        public void onTaskComplete(ArrayList<String> jsonArray) {

            //for (String name:jsonArray){
            //Log.v("output",object.toString());
            //}
            ArrayList<CastModel> castArray = new ArrayList<>();
            ArrayList<ReviewModel> reviewArray = new ArrayList<>();
            ArrayList<SimilarItemModel> similarMoviesArray = new ArrayList<>();
            ArrayList<VideoModel> videosArray = new ArrayList<>();
            try {

                //DetailTvJsonParser detailJsonParser = new DetailTvJsonParser();
                DetailMovieJsonParser detailMovieJsonParser = new DetailMovieJsonParser();
                DetailTvJsonParser detailTvJsonParser = new DetailTvJsonParser();
                //final DetailItemModel DetailItemModel;
                final DetailMovieModel detailMovieModel;
                switch (type){
                    case "tv":
                        Log.e("type",jsonArray.get(0));
                        detailMovieModel = detailTvJsonParser.jsonTvDetailParser(jsonArray.get(0));
                        similarMoviesArray = detailTvJsonParser.jsonSimilarParser(jsonArray.get(3));
                        castArray = detailTvJsonParser.jsonTVCastParser(jsonArray.get(2));
                        videosArray = detailTvJsonParser.jsonVideoParser(jsonArray.get(4));
                        break;
                    case "movies":
                        detailMovieModel = detailMovieJsonParser.jsonMovieDetailParser(jsonArray.get(0));
                        reviewArray = detailMovieJsonParser.jsonMovieReviewsParser(jsonArray.get(2));
                        reviewAdapter = new DetailAdapter.ReviewAdapter(reviewArray);
                        recyclerViewReviews.setAdapter(reviewAdapter);
                        castArray = detailMovieJsonParser.jsonMovieCastParser(jsonArray.get(1));
                        similarMoviesArray = detailMovieJsonParser.jsonSimilarParser(jsonArray.get(3));
                        videosArray = detailMovieJsonParser.jsonVideoParser(jsonArray.get(4));
                        break;
                    default:
                        detailMovieModel = detailMovieJsonParser.jsonMovieDetailParser(jsonArray.get(0));
                }
                overview.setText(detailMovieModel.getOverview());
                actionBar.setTitle(detailMovieModel.getTitle());
                genre.setText(detailMovieModel.getGeneres());
                language.setText(detailMovieModel.getLanguage());
                releaseDate.setText(detailMovieModel.getReleaseDate());
                runtime.setText(detailMovieModel.getRuntime());
                status.setText(detailMovieModel.getReleasedStatus());
                castAdapter = new DetailAdapter.CastAdapter(getContext(),castArray);
                recyclerViewCast.setAdapter(castAdapter);
                similarMoviesAdapter = new DetailAdapter.SimilarMoviesAdapter(getContext(),similarMoviesArray,type);
                recyclerViewSimilar.setAdapter(similarMoviesAdapter);
                videoAdapter = new DetailAdapter.VideoAdapter(videosArray,getContext());
                recyclerViewVideo.setAdapter(videoAdapter);
                FirebaseCurd firebaseCurd = new FirebaseCurd(getActivity());
                DatabaseReference mWatchListReference = firebaseCurd.getmWatchListReference();
                DatabaseReference mWishListReference = firebaseCurd.getmWishListReference();
                mWishListReference.child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        WishListModel wishListModel = dataSnapshot.getValue(WishListModel.class);
                        if (wishListModel != null){
                            wishListButton.setColorFilter(getResources().getColor(R.color.colorDanger));
                            wishlist = true;
                        }
                        else {
                            wishlist = false;
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                 //Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });




                mWatchListReference.child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        WishListModel wishListModel = dataSnapshot.getValue(WishListModel.class);
                        if (wishListModel != null){
                            watchListButton.setColorFilter(getResources().getColor(R.color.colorDanger));
                            watchlist = true;
                        }
                        else {
                            watchlist = false;
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });


                watchListButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseCurd firebaseCurd = new FirebaseCurd(getActivity());
                        if (!watchlist) {
                            WishListModel wishListModel = new WishListModel(
                                    "000", id, type, detailMovieModel.getTitle(), detailMovieModel.getPosterPath(), detailMovieModel.getVoteAvg());

                            firebaseCurd.addWatchListModel(wishListModel);
                            watchListButton.setColorFilter(getResources().getColor(R.color.colorDanger));
                            Toast.makeText(getContext(), "Added to WatchList", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            DatabaseReference watchlistReference = firebaseCurd.getmWatchListReference();
                            watchlistReference.child(id).removeValue();
                            watchListButton.setColorFilter(getResources().getColor(R.color.colorWhite));
                            Toast.makeText(getContext(), "Removed from WatchList", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                wishListButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!wishlist) {
                            WishListModel wishListModel = new WishListModel(
                                    "tyagideepu133", id, type, detailMovieModel.getTitle(), detailMovieModel.getPosterPath(), detailMovieModel.getVoteAvg());
                            FirebaseCurd firebaseCurd = new FirebaseCurd(getActivity());
                            firebaseCurd.addWishListModel(wishListModel);
                            wishListButton.setColorFilter(getResources().getColor(R.color.colorDanger));
                            Toast.makeText(getContext(), "Added to WishList", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            FirebaseCurd firebaseCurd = new FirebaseCurd(getActivity());
                            DatabaseReference wishListReference = firebaseCurd.getmWishListReference();
                            wishListReference.child(id).removeValue();
                            wishListButton.setColorFilter(getResources().getColor(R.color.colorWhite));
                            Toast.makeText(getContext(), "Removed from WishList", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

        //On cancel handles async task cancellation


    @Override
    public void onPause(){
        super.onPause();
    }

}