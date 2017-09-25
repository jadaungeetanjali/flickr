package com.example.pc.flickr.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.flickr.MoviesDetails;
import com.example.pc.flickr.R;
import com.example.pc.flickr.json_parsers.DetailJsonParser;
import com.example.pc.flickr.models.CastModel;
import com.example.pc.flickr.models.DetailItemModel;
import com.example.pc.flickr.models.ListDataModel;
import com.example.pc.flickr.models.ReviewModel;
import com.example.pc.flickr.models.SimilarItemModel;
import com.example.pc.flickr.models.VideoModel;
import com.example.pc.flickr.models.WishListModel;
import com.example.pc.flickr.services.FirebaseCurd;
import com.squareup.picasso.Picasso;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment {
    private CastAdapter castAdapter;
    private ReviewAdapter reviewAdapter;
    private SimilarMoviesAdapter similarMoviesAdapter;
    private VideoAdapter videoAdapter;
    RecyclerView recyclerViewCast, recyclerViewReviews, recyclerViewSimilar,recyclerViewVideo;
    private TextView title, overview, vote_average, tagline, release_date, language, internet_connectivity;
    private ProgressBar progressBar;
    private ImageView poster,wishListButton;
    private LinearLayout mainContainer;
    private Button button;
    private String type, id;

    public MoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        //Initalizing detail layout members view
        title = (TextView) rootView.findViewById(R.id.detail_movie_maintitle);
        overview = (TextView) rootView.findViewById(R.id.detail_movie_overview);
        vote_average = (TextView) rootView.findViewById(R.id.detail_movie_vote_average_textView);
        tagline = (TextView) rootView.findViewById(R.id.detail_movie_tagline);
        release_date = (TextView) rootView.findViewById(R.id.detail_movie_releaseTitle);
        language = (TextView) rootView.findViewById(R.id.detail_movie_language);
        poster = (ImageView) rootView.findViewById(R.id.detail_movie_poster);
        internet_connectivity = (TextView) rootView.findViewById(R.id.detail_movie_internet_connectivity);
        button = (Button) rootView.findViewById(R.id.detail_movie_watchlist);
        wishListButton = (ImageView) rootView.findViewById(R.id.detail_movie_wishlist_button);

        progressBar = (ProgressBar) rootView.findViewById(R.id.detail_movie_progressBar);
        mainContainer = (LinearLayout) rootView.findViewById(R.id.detail_movie_mainContainer);

        //Initializing Cast Recycler view
        recyclerViewCast = (RecyclerView) rootView.findViewById(R.id.detail_movie_castRecyclerView);
        LinearLayoutManager layoutManagerCast = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCast.setLayoutManager(layoutManagerCast);
        recyclerViewCast.setItemAnimator(new DefaultItemAnimator());

        //Initializing Review Recycler View
        recyclerViewReviews = (RecyclerView) rootView.findViewById(R.id.detail_movie_reviewsRecyclerView);
        LinearLayoutManager layoutManagerReviews = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewReviews.setLayoutManager(layoutManagerReviews);
        recyclerViewReviews.setItemAnimator(new DefaultItemAnimator());

        //Initializing Similar Recycler View
        recyclerViewSimilar = (RecyclerView) rootView.findViewById(R.id.detail_movie_similarMoviesRecyclerView);
        LinearLayoutManager layoutManagerSimilar = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSimilar.setLayoutManager(layoutManagerSimilar);
        recyclerViewSimilar.setItemAnimator(new DefaultItemAnimator());

        recyclerViewVideo = (RecyclerView) rootView.findViewById(R.id.detail_movie_videosRecyclerView);
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

        ConnectivityManager connectivityManager = (ConnectivityManager)  getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if ((networkInfo != null) && networkInfo.isConnected()) {
            internet_connectivity.setVisibility(View.GONE);
            ScrollView scrollView = (ScrollView) rootView.findViewById(R.id.detail_movie_scrollView);
            scrollView.setVisibility(View.VISIBLE);
            FetchTask callMovieData = new FetchTask();
            callMovieData.execute(urlList.get(0), urlList.get(1), urlList.get(2), urlList.get(3),urlList.get(4));
        }
        else{
            Toast.makeText(getContext(), "Please Connect to internet...", Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    // CastAdapter class to populate data in castRecyclerView
    private class CastAdapter extends RecyclerView.Adapter<CastAdapter.castViewHolder> {
        private ArrayList<CastModel> castArrayList;

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
                        Intent intent = new Intent(getContext(),MoviesDetails.class);
                        Bundle mBundle = new Bundle();
                        CastModel castModel = castArrayList.get(getAdapterPosition());
                        mBundle.putString("type","celebs");
                        mBundle.putString("id",castModel.getId());
                        intent.putExtras(mBundle);
                        startActivity(intent);
                    }
                });
            }
        }

        public CastAdapter(ArrayList<CastModel> arrayList) {
            this.castArrayList = arrayList;
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
            Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500"+castModel.getImage()).
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
    private class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.reviewViewHolder> {
        private ArrayList<ReviewModel> reviewArrayList;

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
    private class SimilarMoviesAdapter extends RecyclerView.Adapter<SimilarMoviesAdapter.similarMoviesViewHolder> {
        private ArrayList<SimilarItemModel> similarMoviesArrayList;

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
                similarMovieVoteAverageTextView = (TextView) itemView.findViewById(R.id.main_child_vote_textView); //change id to similarMovieVote
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(),MoviesDetails.class);
                        Bundle mBundle = new Bundle();
                        SimilarItemModel similarItemModel = similarMoviesArrayList.get(getAdapterPosition());
                        mBundle.putString("type",type);
                        mBundle.putString("id",similarItemModel.getSimilarItemId());
                        intent.putExtras(mBundle);
                        startActivity(intent);
                    }
                });
            }
        }

        public SimilarMoviesAdapter(ArrayList<SimilarItemModel> arrayList) {
            this.similarMoviesArrayList = arrayList;
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
            Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500"+SimilarItemModel.getSimilarItemimage())
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

    private class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.videoViewHolder> {
        private ArrayList<VideoModel> videoArrayList;

        class videoViewHolder extends RecyclerView.ViewHolder {
            ImageView videoImageView;
            TextView videoNameTextView ;
            ProgressBar videoProgressBar;

            public videoViewHolder(View itemView) {
                super(itemView);
                videoProgressBar = (ProgressBar) itemView.findViewById(R.id.video_progressBar);
                videoNameTextView = (TextView) itemView.findViewById(R.id.video_textView);
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
            VideoModel videoModel = videoArrayList.get(position);
            holder.videoNameTextView.setText(videoModel.getName());
            Picasso.with(getContext()).load("https://img.youtube.com/vi/"+videoModel.getImage()+"/0.jpg")
                    .into(holder.videoImageView,new com.squareup.picasso.Callback() {

                        @Override
                        public void onSuccess() {
                            holder.videoProgressBar.setVisibility(View.GONE);
                            holder.videoImageView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }

        @Override
        public int getItemCount() {
            return videoArrayList.size();
        }
    }


    public class FetchTask extends AsyncTask<String, Void, ArrayList<String>> {
        //doInBackground method to set up url connection and return jsonData
        @Override
        protected ArrayList<String> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonData = null;
            ArrayList<String> jsonArray = new ArrayList<>();
            for (String param : params){
                try {
                    //setting the urlConnection
                    URL url = new URL(param);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    InputStream stream = urlConnection.getInputStream();
                    if (stream == null){
                        jsonData = null;
                    }
                    StringBuffer stringBuffer = new StringBuffer();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    String inputLine;
                    while ((inputLine = reader.readLine()) != null){
                        stringBuffer.append(inputLine + "\n");
                    }
                    if (stringBuffer.length() == 0){
                        jsonData = null;
                    }
                    jsonData = stringBuffer.toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    if (urlConnection != null){
                        urlConnection.disconnect();
                    }
                    if (reader != null){
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                jsonArray.add(jsonData);
            }
            return jsonArray;
        }
        // onPostExecute data to populate data after fetching data
        protected void onPostExecute(ArrayList<String> jsonArray) {
            super.onPostExecute(jsonArray);
            //for (String name:jsonArray){
            //Log.v("output",name);
            //}

            ArrayList<CastModel> castArray = new ArrayList<>();
            ArrayList<ReviewModel> reviewArray = new ArrayList<>();
            ArrayList<SimilarItemModel> similarMoviesArray = new ArrayList<>();
            ArrayList<VideoModel> videosArray = new ArrayList<>();
            try {

                DetailJsonParser detailJsonParser = new DetailJsonParser();
                final DetailItemModel DetailItemModel;
                switch (type){
                    case "movies":
                        DetailItemModel = detailJsonParser.jsonMovieDetailParser(jsonArray.get(0));
                        reviewArray = detailJsonParser.jsonMovieReviewsParser(jsonArray.get(2));
                        reviewAdapter = new ReviewAdapter(reviewArray);
                        recyclerViewReviews.setAdapter(reviewAdapter);
                        similarMoviesArray = detailJsonParser.jsonSimilarParser(jsonArray.get(3));

                        break;
                    case "tv":
                        DetailItemModel = detailJsonParser.jsonTvDetailParser(jsonArray.get(0));
                        similarMoviesArray = detailJsonParser.jsonTvSimilarParser(jsonArray.get(3));
                        break;
                    default:
                        DetailItemModel = detailJsonParser.jsonMovieDetailParser(jsonArray.get(0));
                }
                Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.detail_toolbar);
                toolbar.setTitle(DetailItemModel.getTitle());
                title.setText(DetailItemModel.getTitle());
                overview.setText(DetailItemModel.getOverview());
                vote_average.setText(DetailItemModel.getVote_avg());
                tagline.setText(DetailItemModel.getTagline());
                release_date.setText(DetailItemModel.getRelease_date());
                language.setText(DetailItemModel.getLanguage());
                Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500"+DetailItemModel.getImg_url()).into(poster);

                castArray = detailJsonParser.jsonCastParser(jsonArray.get(1));
                castAdapter = new CastAdapter(castArray);
                recyclerViewCast.setAdapter(castAdapter);



                similarMoviesAdapter = new SimilarMoviesAdapter(similarMoviesArray);
                recyclerViewSimilar.setAdapter(similarMoviesAdapter);

                videosArray = detailJsonParser.jsonVideoParser(jsonArray.get(4));
                videoAdapter = new VideoAdapter(videosArray);
                recyclerViewVideo.setAdapter(videoAdapter);


                progressBar.setVisibility(View.GONE);
                mainContainer.setVisibility(View.VISIBLE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.v("movie",DetailItemModel.getTitle());
                        WishListModel wishListModel = new WishListModel(
                                "tyagideepu133",id,type,DetailItemModel.getTitle(),DetailItemModel.getImg_url(),DetailItemModel.getVote_avg());
                        FirebaseCurd firebaseCurd = new FirebaseCurd(getActivity());
                        firebaseCurd.addWatchListModel(wishListModel);
                    }
                });
                wishListButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.v("movie",DetailItemModel.getTitle());
                        WishListModel wishListModel = new WishListModel(
                                "tyagideepu133",id,type,DetailItemModel.getTitle(),DetailItemModel.getImg_url(),DetailItemModel.getVote_avg());
                        FirebaseCurd firebaseCurd = new FirebaseCurd(getActivity());
                        firebaseCurd.addWishListModel(wishListModel);
                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    
}
