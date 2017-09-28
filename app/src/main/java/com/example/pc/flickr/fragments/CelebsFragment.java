package com.example.pc.flickr.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.flickr.MoviesDetails;
import com.example.pc.flickr.R;
import com.example.pc.flickr.json_parsers.DetailJsonParser;
import com.example.pc.flickr.models.CastModel;
import com.example.pc.flickr.models.FavoriteModel;
import com.example.pc.flickr.models.SimilarItemModel;
import com.example.pc.flickr.services.FirebaseCurd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
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
import java.util.Arrays;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class CelebsFragment extends Fragment {
    private CelebsImagesAdapter celebsImagesAdapter;
    private CelebsMovieCreditAdapter celebsMovieCreditAdapter;
    RecyclerView recyclerViewCelebMovieCredit;
    RecyclerView recyclerViewCelebImages;
    public TextView title, biography, dateOfBirth, placeOfBirth, alsoKnownAs, detailDOB, detailPlaceOfBirth ;
    public ImageView profile;
    public String id, type;
    private Button button;
    private Boolean favorite = false;

    public CelebsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_celebs, container, false);
        title = (TextView) rootView.findViewById(R.id.detail_celebs_mainTitle);
        biography = (TextView) rootView.findViewById(R.id.detail_celebs_biography);
        dateOfBirth = (TextView) rootView.findViewById(R.id.detail_celebs_born);
        placeOfBirth = (TextView) rootView.findViewById(R.id.detail_celebs_placeOfBirth);
        alsoKnownAs = (TextView) rootView.findViewById(R.id.detail_celebs_alsoKnownAs);
        detailDOB = (TextView) rootView.findViewById(R.id.detail_celebs_bornDate);
        detailPlaceOfBirth = (TextView) rootView.findViewById(R.id.detail_celebs_birthPlace);
        profile = (ImageView) rootView.findViewById(R.id.detail_celebs_profile);

        recyclerViewCelebImages = (RecyclerView) rootView.findViewById(R.id.detail_celebs_imagesRecyclerView);
        LinearLayoutManager layoutManagerCelebImages= new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCelebImages.setLayoutManager(layoutManagerCelebImages);
        recyclerViewCelebImages.setItemAnimator(new DefaultItemAnimator());

        recyclerViewCelebMovieCredit = (RecyclerView) rootView.findViewById(R.id.detail_celebs_movieCreditsRecyclerView);
        LinearLayoutManager layoutManagerCelebsMovieCredit= new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCelebMovieCredit.setLayoutManager(layoutManagerCelebsMovieCredit);
        recyclerViewCelebMovieCredit.setItemAnimator(new DefaultItemAnimator());

        button = (Button) rootView.findViewById(R.id.detail_celebs_favorite_button);

        Bundle bundle = getArguments();
        type = bundle.getString("type");
        id = bundle.getString("id");
        ArrayList<String> urlList = new ArrayList<>();
        urlList.add("https://api.themoviedb.org/3/person/" +id+ "?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
        urlList.add("https://api.themoviedb.org/3/person/" +id+ "/images?api_key=fe56cdee4dfea0c18403e0965acfa23b");
        urlList.add("https://api.themoviedb.org/3/person/" +id+ "/movie_credits?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");

        ConnectivityManager connectivityManager = (ConnectivityManager)  getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if ((networkInfo != null) && networkInfo.isConnected()) {
            FetchTask fetchCelebsData = new FetchTask();
            fetchCelebsData.execute(urlList.get(0), urlList.get(1), urlList.get(2));
        }
        else{
            Toast.makeText(getContext(), "Please Connect to internet...", Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    private class CelebsMovieCreditAdapter extends RecyclerView.Adapter<CelebsMovieCreditAdapter.celebsMovieCreditViewHolder> {
        private ArrayList<SimilarItemModel> celebsMovieCreditArrayList;

        class celebsMovieCreditViewHolder extends RecyclerView.ViewHolder {
            ImageView celebsMovieCreditImageView;
            TextView celebsMovieCreditNameTextView ;
            TextView celebsMovieCreditVoteAverageTextView;
            ProgressBar celebsMovieCreditProgressBar;

            public celebsMovieCreditViewHolder(View itemView) {
                super(itemView);
                celebsMovieCreditNameTextView = (TextView) itemView.findViewById(R.id.main_child_title_textView); //change id to similarMovieName
                celebsMovieCreditImageView = (ImageView) itemView.findViewById(R.id.main_child_imageView); //change id to similarMovieImage
                celebsMovieCreditVoteAverageTextView = (TextView) itemView.findViewById(R.id.main_child_vote_textView);
                celebsMovieCreditProgressBar = (ProgressBar) itemView.findViewById(R.id.main_image_progressBar);
                itemView.setOnClickListener(new View.OnClickListener() {
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
                });
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
            Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500"+similarItemModel.getSimilarItemimage())
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
    private class CelebsImagesAdapter extends RecyclerView.Adapter<CelebsImagesAdapter.celebsImagesViewHolder> {
        private ArrayList<CelebImageModel> celebImageModelArrayList;

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


    private class CelebsModel{
        public String title;
        public String biography;
        public String dateOfBirth;
        public String placeOfBirth;
        public String profile_url;
        public String alsoKnownAs;
        public CelebsModel(String title, String biography, String dateOfBirth, String placeOfBirth, String profile_url, String alsoKnownAs){
            this.title = title;
            this.biography = biography;
            this.dateOfBirth = dateOfBirth;
            this.placeOfBirth = placeOfBirth;
            this.profile_url = profile_url;
            this.alsoKnownAs = alsoKnownAs;
        }

        public String getTitle() {
            return title;
        }

        public String getBiography() {
            return biography;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public String getPlaceOfBirth() {
            return placeOfBirth;
        }

        public String getProfile_url() {
            return profile_url;
        }

        public String getAlsoKnownAs() {
            return alsoKnownAs;
        }
    }
     private class CelebImageModel{
         public String celebImage;
         public CelebImageModel(String celebImage){
             this.celebImage = celebImage;
         }

         public String getCelebImage() {
             return celebImage;
         }
     }

    public class FetchTask extends AsyncTask<String, Void, ArrayList<String>> {

        private CelebsModel jsonCelebsParser(String jsonCeleb)throws JSONException {
            JSONObject celebObject = new JSONObject(jsonCeleb);
            String title = celebObject.get("name").toString();
            String biography = celebObject.get("biography").toString();
            String dateOfBirth = celebObject.get("birthday").toString();
            String placeOfBirth = celebObject.get("place_of_birth").toString();
            String profile = celebObject.get("profile_path").toString();
           /* JSONArray alsoKnownAsArray = celebObject.getJSONArray("also_known_as");
            String alsoKnownAs = alsoKnownAsArray.get(0).toString(); */

            CelebsModel celebsModel=new CelebsModel(title, biography, dateOfBirth, placeOfBirth, profile, "");
            return celebsModel;
        }

        public ArrayList<CelebImageModel> jsonCelebImageParser(String jsonCelebImage) throws JSONException {
            ArrayList<CelebImageModel> celebImageArray = new ArrayList<>();
            JSONObject celebImageObject = new JSONObject(jsonCelebImage);
            JSONArray celebImageList = celebImageObject.getJSONArray("profiles");
            for (int i = 0; i < celebImageList.length(); i++) {
                JSONObject celebImage = celebImageList.getJSONObject(i);
                String image = celebImage.get("file_path").toString();
                CelebImageModel celebImageModel= new CelebImageModel(image);
                celebImageArray.add(celebImageModel);
            }
            return celebImageArray;
        }

        public ArrayList<SimilarItemModel> jsonCelebMovieCreditParser(String jsonCelebMovieCredit) throws JSONException {
            ArrayList<SimilarItemModel> celebMovieCreditArray = new ArrayList<>();
            JSONObject celebMovieCreditObject = new JSONObject(jsonCelebMovieCredit);
            JSONArray celebMovieCreditList = celebMovieCreditObject.getJSONArray("cast");
            for (int i = 0; i < celebMovieCreditList.length(); i++) {
                JSONObject movieCredit = celebMovieCreditList.getJSONObject(i);
                String movieCreditName = movieCredit.get("title").toString();
                String movieCreditVoteAverage = movieCredit.get("vote_average").toString();
                String movieCreditPoster = movieCredit.get("poster_path").toString();
                String movieCreditId = movieCredit.get("id").toString();
                SimilarItemModel SimilarItemModel = new SimilarItemModel(movieCreditId, movieCreditName, movieCreditVoteAverage, movieCreditPoster);
                celebMovieCreditArray.add(SimilarItemModel);
            }
            return celebMovieCreditArray;
        }

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
        protected void onPostExecute(ArrayList<String> jsonArray) {
            super.onPostExecute(jsonArray);
            ArrayList<CelebImageModel> celebImageModelArray = new ArrayList<>();
            ArrayList<SimilarItemModel> celebMovieCreditArray = new ArrayList<>();
            try {
                final CelebsModel celebsModel = jsonCelebsParser(jsonArray.get(0));
                title.setText(celebsModel.getTitle());
                biography.setText(celebsModel.getBiography());
                dateOfBirth.setText(celebsModel.getDateOfBirth());
                placeOfBirth.setText(celebsModel.getPlaceOfBirth());
                detailDOB.setText(celebsModel.getDateOfBirth());
                detailPlaceOfBirth.setText(celebsModel.getPlaceOfBirth());
                alsoKnownAs.setText(celebsModel.getAlsoKnownAs());
                Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500"+celebsModel.getProfile_url()).into(profile);


                FirebaseCurd firebaseCurd = new FirebaseCurd(getActivity());
                DatabaseReference mFavoriteReference = firebaseCurd.getmFavoriteReference();
                mFavoriteReference.child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        FavoriteModel favoriteModel = dataSnapshot.getValue(FavoriteModel.class);
                        if (favoriteModel != null){
                            button.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorDanger));
                            button.setText("Remove form WatchList");
                            //progressBar.setVisibility(View.GONE);
                            //mainContainer.setVisibility(View.VISIBLE);
                            favorite = true;
                        }
                        else {
                            favorite = false;
                            //progressBar.setVisibility(View.GONE);
                            //mainContainer.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });



                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseCurd firebaseCurd = new FirebaseCurd(getActivity());
                        if (!favorite) {
                            FavoriteModel favoriteModel = new FavoriteModel(
                                    id, type, celebsModel.getTitle(), celebsModel.getProfile_url());

                            firebaseCurd.addFavoriteModel(favoriteModel);
                            button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorDanger));
                            Toast.makeText(getContext(), "Added to Favorite", Toast.LENGTH_SHORT).show();
                            button.setText("Remove form Favorite");
                        }
                        else {
                            DatabaseReference favoriteReference = firebaseCurd.getmFavoriteReference();
                            favoriteReference.child(id).removeValue();
                            button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorSuccess));
                            Toast.makeText(getContext(), "Removed from Favorite", Toast.LENGTH_SHORT).show();
                            button.setText("ADD TO Favorite");
                        }
                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                celebImageModelArray = jsonCelebImageParser(jsonArray.get(1));
                //Log.v("output", celebImageModelArray.get(0).toString());
                celebsImagesAdapter = new CelebsImagesAdapter(celebImageModelArray);
                recyclerViewCelebImages.setAdapter(celebsImagesAdapter );
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                celebMovieCreditArray = jsonCelebMovieCreditParser(jsonArray.get(2));
                celebsMovieCreditAdapter = new CelebsMovieCreditAdapter(celebMovieCreditArray);
                recyclerViewCelebMovieCredit.setAdapter(celebsMovieCreditAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
