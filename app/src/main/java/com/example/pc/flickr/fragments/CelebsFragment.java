package com.example.pc.flickr.fragments;


import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.flickr.MoviesDetails;
import com.example.pc.flickr.R;
import com.example.pc.flickr.models.CastModel;
import com.example.pc.flickr.models.FavoriteModel;
import com.example.pc.flickr.models.SimilarItemModel;
import com.example.pc.flickr.services.FirebaseCurd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class CelebsFragment extends Fragment {
    private CelebAdapters.CelebsImagesAdapter celebsImagesAdapter;
    private CelebAdapters.CelebsMovieCreditAdapter celebsMovieCreditAdapter;
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
        button = (Button) rootView.findViewById(R.id.detail_celebs_favorite_button);
        progressBar = (ProgressBar) rootView.findViewById(R.id.detail_celebs_mainContainer_progressBar);
        mainContainer = (LinearLayout) rootView.findViewById(R.id.detail_celebs_mainContainer);

        Bundle bundle = getArguments();
        type = bundle.getString("type");
        id = bundle.getString("id");
        ArrayList<String> urlList = new ArrayList<>();
        urlList.add("https://api.themoviedb.org/3/person/" +id+ "?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
        urlList.add("https://api.themoviedb.org/3/person/" +id+ "/images?api_key=fe56cdee4dfea0c18403e0965acfa23b");
        urlList.add("https://api.themoviedb.org/3/person/" +id+ "/movie_credits?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");

        Connectivity connectivity = new Connectivity(urlList, getActivity(), getContext());
        connectivity.celebConnectivity();
        return rootView;
    }




    public  class FetchTask extends AsyncTask<String, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonData = null;
            ArrayList<String> jsonArray = new ArrayList<String>();
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
            DetailCelebsJsonParser detailCelebsJsonParser = new DetailCelebsJsonParser();
            try {
                final CelebsModel celebsModel = detailCelebsJsonParser.jsonCelebsParser(jsonArray.get(0));
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
                            progressBar.setVisibility(View.GONE);
                            mainContainer.setVisibility(View.VISIBLE);
                            favorite = true;
                        }
                        else {
                            favorite = false;
                            progressBar.setVisibility(View.GONE);
                            mainContainer.setVisibility(View.VISIBLE);
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
                celebImageModelArray = detailCelebsJsonParser.jsonCelebImageParser(jsonArray.get(1));
                //Log.v("output", celebImageModelArray.get(0).toString());
                celebsImagesAdapter = new CelebAdapters.CelebsImagesAdapter(getContext(), celebImageModelArray);
                recyclerViewCelebImages.setAdapter(celebsImagesAdapter );
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                celebMovieCreditArray = detailCelebsJsonParser.jsonCelebMovieCreditParser(jsonArray.get(2));
                celebsMovieCreditAdapter = new CelebAdapters.CelebsMovieCreditAdapter(getContext(), celebMovieCreditArray, type);
                recyclerViewCelebMovieCredit.setAdapter(celebsMovieCreditAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
