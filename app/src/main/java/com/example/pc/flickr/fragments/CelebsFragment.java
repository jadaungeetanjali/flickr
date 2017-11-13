package com.example.pc.flickr.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.flickr.Adapters.CelebAdapters;
import com.example.pc.flickr.services.Connectivity;
import com.example.pc.flickr.R;
import com.example.pc.flickr.json_parsers.DetailCelebsJsonParser;
import com.example.pc.flickr.models.CelebImageModel;
import com.example.pc.flickr.models.CelebsModel;
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
    public TextView biography, dateOfBirth;
    public ImageView profile, button;
    public String id, type;
    private FetchTask fetchTask;
    private ProgressBar progressBar;
    private LinearLayout mainContainer;
    private Boolean favorite = false, internet;
    private Toolbar toolbar;
    private ActionBar actionBar;

    public CelebsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_celebs, container, false);
        toolbar = (Toolbar) rootView.findViewById(R.id.detail_movie_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        actionBar = (ActionBar) ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        biography = (TextView) rootView.findViewById(R.id.detail_celebs_biography_textView);
        dateOfBirth = (TextView) rootView.findViewById(R.id.detail_celebs_born_textView);
        profile = (ImageView) rootView.findViewById(R.id.detail_celeb_poster);

        recyclerViewCelebImages = (RecyclerView) rootView.findViewById(R.id.detail_celebs_images_recyclerVIew);
        LinearLayoutManager layoutManagerCelebImages = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCelebImages.setLayoutManager(layoutManagerCelebImages);
        recyclerViewCelebImages.setItemAnimator(new DefaultItemAnimator());

        recyclerViewCelebMovieCredit = (RecyclerView) rootView.findViewById(R.id.detail_celebs_similar_recyclerView);
        LinearLayoutManager layoutManagerCelebsMovieCredit = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCelebMovieCredit.setLayoutManager(layoutManagerCelebsMovieCredit);
        recyclerViewCelebMovieCredit.setItemAnimator(new DefaultItemAnimator());

        button = (ImageView) rootView.findViewById(R.id.detail_celebs_add_favorite);

        Bundle bundle = getArguments();
        type = bundle.getString("type");
        id = bundle.getString("id");
        ArrayList<String> urlList = new ArrayList<>();
        urlList.add("https://api.themoviedb.org/3/person/" + id + "?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
        urlList.add("https://api.themoviedb.org/3/person/" + id + "/images?api_key=fe56cdee4dfea0c18403e0965acfa23b");
        urlList.add("https://api.themoviedb.org/3/person/" + id + "/movie_credits?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");

        Connectivity connectivity = new Connectivity(getActivity());
        if (connectivity.internetConnectivity()) {
            fetchTask = new FetchTask();
            internet = true;
            fetchTask.execute(urlList.get(0), urlList.get(1), urlList.get(2));
        } else {
            internet = false;
            connectivity.checkNetworkConnection();
            Toast.makeText(getContext(), "Please Connect to internet...", Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }


    public class FetchTask extends AsyncTask<String, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonData = null;
            ArrayList<String> jsonArray = new ArrayList<String>();
            for (String param : params) {
                try {
                    //setting the urlConnection
                    URL url = new URL(param);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    InputStream stream = urlConnection.getInputStream();
                    if (stream == null) {
                        jsonData = null;
                    }
                    StringBuffer stringBuffer = new StringBuffer();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    String inputLine;
                    while ((inputLine = reader.readLine()) != null) {
                        stringBuffer.append(inputLine + "\n");
                    }
                    if (stringBuffer.length() == 0) {
                        jsonData = null;
                    }
                    jsonData = stringBuffer.toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
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
                //title.setText(celebsModel.getTitle());
                actionBar.setTitle(celebsModel.getTitle());
                biography.setText(celebsModel.getBiography());
                dateOfBirth.setText(celebsModel.getDateOfBirth()+"\n"+celebsModel.getPlaceOfBirth()+" ");
                Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500" + celebsModel.getProfile_url()).into(profile);


                FirebaseCurd firebaseCurd = new FirebaseCurd(getActivity());
                DatabaseReference mFavoriteReference = firebaseCurd.getmFavoriteReference();
                mFavoriteReference.child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        FavoriteModel favoriteModel = dataSnapshot.getValue(FavoriteModel.class);
                        if (favoriteModel != null) {
                            favorite = true;
                            button.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorDangerDark));
                        } else {
                            favorite = false;
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
                            button.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorDangerDark));
                            Toast.makeText(getContext(), "Added to Favorite", Toast.LENGTH_SHORT).show();
                        } else {
                            DatabaseReference favoriteReference = firebaseCurd.getmFavoriteReference();
                            favoriteReference.child(id).removeValue();
                            button.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorWhite));
                            Toast.makeText(getContext(), "Removed from Favorite", Toast.LENGTH_SHORT).show();
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
                recyclerViewCelebImages.setAdapter(celebsImagesAdapter);
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

        //On cancel handles async task cacelation
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (internet)
            fetchTask.cancel(true);
    }
}