package com.example.pc.flickr.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.flickr.Adapters.DetailAdapter;
import com.example.pc.flickr.YoutubeActivity;
import com.example.pc.flickr.json_parsers.DetailMovieJsonParser;
import com.example.pc.flickr.models.DetailMovieModel;
import com.example.pc.flickr.services.AsyncTaskCompleteListener;
import com.example.pc.flickr.services.CommonFetchTask;
import com.example.pc.flickr.services.Connectivity;
import com.example.pc.flickr.R;
import com.example.pc.flickr.models.CastModel;
import com.example.pc.flickr.models.ReviewModel;
import com.example.pc.flickr.models.SimilarItemModel;
import com.example.pc.flickr.models.VideoModel;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment {
    private DetailAdapter.CastAdapter castAdapter;
    private DetailAdapter.ReviewAdapter reviewAdapter;
    private DetailAdapter.SimilarMoviesAdapter similarMoviesAdapter;
    private VideoAdapter videoAdapter;
    RecyclerView recyclerViewCast, recyclerViewReviews, recyclerViewSimilar,recyclerViewVideo;
    private TextView title, overview, status, tagline, release_date, category, internet_connectivity;
    private ProgressBar progressBar;
    private ImageView poster,wishListButton,wishListButton2;
    private LinearLayout mainContainer;
    private Button button;
    private String type, id;
    private Toolbar toolbar;
    private Boolean wishList, watchList, internet;
    private CommonFetchTask callMovieData;
    private ActionBar actionBar;
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
        //vote_average = (TextView) rootView.findViewById(R.id.detail_movie_rating);
        //tagline = (TextView) rootView.findViewById(R.id.detail_movie_tagline);
        release_date = (TextView) rootView.findViewById(R.id.detail_movie_release_date);
        category = (TextView) rootView.findViewById(R.id.detail_movie_genre);
        status = (TextView) rootView.findViewById(R.id.detail_movie_status);
        //internet_connectivity = (TextView) rootView.findViewById(R.id.detail_movie_internet_connectivity);
        //button = (Button) rootView.findViewById(R.id.detail_movie_watchlist);
        //wishListButton = (ImageView) rootView.findViewById(R.id.detail_movie_wishlist_button);
        //wishListButton2 = (ImageView) rootView.findViewById(R.id.detail_movie_wishlist_button_2);
        //progressBar = (ProgressBar) rootView.findViewById(R.id.detail_movie_progressBar);
        //mainContainer = (LinearLayout) rootView.findViewById(R.id.detail_movie_mainContainer);

        //Initializing Cast Recycler view
        recyclerViewCast = (RecyclerView) rootView.findViewById(R.id.detail_movie_cast_recyclerView);
        LinearLayoutManager layoutManagerCast = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCast.setLayoutManager(layoutManagerCast);
        recyclerViewCast.setItemAnimator(new DefaultItemAnimator());

        //Initializing Review Recycler View
        //recyclerViewReviews = (RecyclerView) rootView.findViewById(R.id.detail_movie_reviewsRecyclerView);
        //LinearLayoutManager layoutManagerReviews = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        //recyclerViewReviews.setLayoutManager(layoutManagerReviews);
        //recyclerViewReviews.setItemAnimator(new DefaultItemAnimator());

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
            //internet_connectivity.setVisibility(View.GONE);
            //ScrollView scrollView = (ScrollView) rootView.findViewById(R.id.detail_movie_scrollView);
            //scrollView.setVisibility(View.VISIBLE);
            callMovieData = new CommonFetchTask(getContext(), new FetchTask());
            callMovieData.execute(urlList.get(0), urlList.get(1), urlList.get(2), urlList.get(3),urlList.get(4));
            internet = true;
        }
        else{
            //internet = false;
            connectivity.checkNetworkConnection();
            Toast.makeText(getContext(), "Please Connect to internet...", Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }


    private class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.videoViewHolder> {
        private ArrayList<VideoModel> videoArrayList;

        class videoViewHolder extends RecyclerView.ViewHolder {
            ImageView videoImageView;
            TextView videoNameTextView ;
            ProgressBar videoProgressBar;

            public videoViewHolder(View itemView) {
                super(itemView);
                //videoProgressBar = (ProgressBar) itemView.findViewById(R.id.video_progressBar);
                //videoNameTextView = (TextView) itemView.findViewById(R.id.video_textView);
                videoImageView = (ImageView) itemView.findViewById(R.id.video_imageView);
            }
        }

        public VideoAdapter(ArrayList<VideoModel> arrayList) {
            this.videoArrayList = arrayList;
        }

        @Override
        public videoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_video_listitem, parent, false); //change layout id
            return new videoViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final videoViewHolder holder, int position) {
            final VideoModel videoModel = videoArrayList.get(position);
            //holder.videoNameTextView.setText(videoModel.getName());
            Picasso.with(getContext()).load("https://img.youtube.com/vi/"+videoModel.getImageUrl()+"/sddefault.jpg")
                    .into(holder.videoImageView,new com.squareup.picasso.Callback() {

                        @Override
                        public void onSuccess() {
                            //holder.videoProgressBar.setVisibility(View.GONE);
                            //holder.videoImageView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError() {

                        }
                    });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    //bundle.putString("url",videoModel.getImage());
                    Intent intent = new Intent(getActivity().getApplication(), YoutubeActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return videoArrayList.size();
        }
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
                //final DetailItemModel DetailItemModel;
                DetailMovieModel detailMovieModel;
                switch (type){
                    case "movies":

                        detailMovieModel = detailMovieJsonParser.jsonMovieDetailParser(jsonArray.get(0));
                        //reviewArray = detailJsonParser.jsonMovieReviewsParser(jsonArray.get(2));
                        //reviewAdapter = new DetailAdapter.ReviewAdapter(reviewArray);
                        //recyclerViewReviews.setAdapter(reviewAdapter);

                        break;
                    //case "tv":
                    //DetailItemModel = detailJsonParser.jsonTvDetailParser(jsonArray.get(0));
                    //similarMoviesArray = detailJsonParser.jsonTvSimilarParser(jsonArray.get(3));
                    //    break;
                    default:
                        detailMovieModel = detailMovieJsonParser.jsonMovieDetailParser(jsonArray.get(0));
                }
                //Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.detail_toolbar);
                //toolbar.setTitle(DetailItemModel.getTitle());
                //title.setText(DetailItemModel.getTitle());
                overview.setText(detailMovieModel.getOverview());
                actionBar.setTitle(detailMovieModel.getTitle());
                //vote_average.setText(DetailItemModel.getVote_avg());
                //tagline.setText(DetailItemModel.getTagline());
                //release_date.setText(DetailItemModel.getRelease_date());
                //language.setText(DetailItemModel.getLanguage());
                //Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500"+DetailItemModel.getImg_url()).into(poster);

                castArray = detailMovieJsonParser.jsonMovieCastParser(jsonArray.get(1));
                castAdapter = new DetailAdapter.CastAdapter(getContext(),castArray);
                recyclerViewCast.setAdapter(castAdapter);
                similarMoviesArray = detailMovieJsonParser.jsonSimilarParser(jsonArray.get(3));
                similarMoviesAdapter = new DetailAdapter.SimilarMoviesAdapter(getContext(),similarMoviesArray,type);
                recyclerViewSimilar.setAdapter(similarMoviesAdapter);

                videosArray = detailMovieJsonParser.jsonVideoParser(jsonArray.get(4));
                videoAdapter = new VideoAdapter(videosArray);
                recyclerViewVideo.setAdapter(videoAdapter);
                //FirebaseCurd firebaseCurd = new FirebaseCurd(getActivity());
                //DatabaseReference mWatchListReference = firebaseCurd.getmWatchListReference();
                //DatabaseReference mWishListReference = firebaseCurd.getmWishListReference();
                //mWishListReference.child(id).addValueEventListener(new ValueEventListener() {
                //    @Override
                //    public void onDataChange(DataSnapshot dataSnapshot) {
                //        WishListModel wishListModel = dataSnapshot.getValue(WishListModel.class);
                //        if (wishListModel != null){
                //            wishListButton.setVisibility(View.GONE);
                //            wishListButton2.setVisibility(View.VISIBLE);
                //            wishList = true;
                //        }
                //        else {
                //            wishList = false;
                //        }

                //    }

                //    @Override
                //    public void onCancelled(DatabaseError error) {
                // Failed to read value
                //        Log.w(TAG, "Failed to read value.", error.toException());
                //    }
                //});




                /*mWatchListReference.child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        WishListModel wishListModel = dataSnapshot.getValue(WishListModel.class);
                        if (wishListModel != null){
                            button.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorDanger));
                            button.setText("Remove form WatchList");
                            progressBar.setVisibility(View.GONE);
                            mainContainer.setVisibility(View.VISIBLE);
                            watchList = true;
                        }
                        else {
                            watchList = false;
                            progressBar.setVisibility(View.GONE);
                            mainContainer.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });*/


                /*button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseCurd firebaseCurd = new FirebaseCurd(getActivity());
                        if (!watchList) {
                            WishListModel wishListModel = new WishListModel(
                                    "tyagideepu133", id, type, DetailItemModel.getTitle(), DetailItemModel.getImg_url(), DetailItemModel.getVote_avg());

                            firebaseCurd.addWatchListModel(wishListModel);
                            button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorDanger));
                            Toast.makeText(getContext(), "Added to WatchList", Toast.LENGTH_SHORT).show();
                            button.setText("Remove form WatchList");
                        }
                        else {
                            DatabaseReference watchlistReference = firebaseCurd.getmWatchListReference();
                            watchlistReference.child(id).removeValue();
                            button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorSuccess));
                            Toast.makeText(getContext(), "Removed from WatchList", Toast.LENGTH_SHORT).show();
                            button.setText("ADD TO WatchLIST");
                        }
                    }
                });*/
                /*wishListButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!wishList) {
                            Log.v("movie", DetailItemModel.getTitle());
                            WishListModel wishListModel = new WishListModel(
                                    "tyagideepu133", id, type, DetailItemModel.getTitle(), DetailItemModel.getImg_url(), DetailItemModel.getVote_avg());
                            FirebaseCurd firebaseCurd = new FirebaseCurd(getActivity());
                            firebaseCurd.addWishListModel(wishListModel);
                            Toast.makeText(getContext(), "Added to WishList", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            FirebaseCurd firebaseCurd = new FirebaseCurd(getActivity());
                            DatabaseReference wishListReference = firebaseCurd.getmWishListReference();
                            wishListReference.child(id).removeValue();
                            wishListButton.setVisibility(View.VISIBLE);
                            wishListButton2.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Removed from WishList", Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/
                /*wishListButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!wishList) {
                            Log.v("movie", DetailItemModel.getTitle());
                            WishListModel wishListModel = new WishListModel(
                                    "tyagideepu133", id, type, DetailItemModel.getTitle(), DetailItemModel.getImg_url(), DetailItemModel.getVote_avg());
                            FirebaseCurd firebaseCurd = new FirebaseCurd(getActivity());
                            firebaseCurd.addWishListModel(wishListModel);
                            Toast.makeText(getContext(), "Added to WishList", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            FirebaseCurd firebaseCurd = new FirebaseCurd(getActivity());
                            DatabaseReference wishListReference = firebaseCurd.getmWishListReference();
                            wishListReference.child(id).removeValue();
                            wishListButton.setVisibility(View.VISIBLE);
                            wishListButton2.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Removed from WishList", Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/


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