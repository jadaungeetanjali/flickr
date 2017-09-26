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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.flickr.MoviesDetails;
import com.example.pc.flickr.R;
import com.example.pc.flickr.models.CastModel;
import com.example.pc.flickr.models.SimilarItemModel;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class CelebsFragment extends Fragment {
    private CelebsKnownForAdapter celebsKnownForAdapter;
    private CelebsImagesAdapter celebsImagesAdapter;
    RecyclerView recyclerViewCelebKnownFor;
    RecyclerView recyclerViewCelebImages;
    public TextView title, biography, dateOfBirth, placeOfBirth, alsoKnownAs, detailDOB, detailPlaceOfBirth ;
    public ImageView profile;
    public String id, type;

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

        recyclerViewCelebKnownFor = (RecyclerView) rootView.findViewById(R.id.detail_celebs_knownFor_RecyclerView);
        LinearLayoutManager layoutManagerKnownFor = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCelebKnownFor.setLayoutManager(layoutManagerKnownFor);
        recyclerViewCelebKnownFor.setItemAnimator(new DefaultItemAnimator());

        recyclerViewCelebImages = (RecyclerView) rootView.findViewById(R.id.detail_celebs_imagesRecyclerView);
        LinearLayoutManager layoutManagerCelebImages= new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCelebImages.setLayoutManager(layoutManagerCelebImages);
        recyclerViewCelebImages.setItemAnimator(new DefaultItemAnimator());


        Bundle bundle = getArguments();
        type = bundle.getString("type");
        id = bundle.getString("id");
        ArrayList<String> urlList = new ArrayList<>();
        urlList.add("https://api.themoviedb.org/3/person/" +id+ "?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
        urlList.add("https://api.themoviedb.org/3/person/popular?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US&page=1");
        urlList.add("https://api.themoviedb.org/3/person/" +id+ "/images?api_key=fe56cdee4dfea0c18403e0965acfa23b");

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
    private class CelebsKnownForAdapter extends RecyclerView.Adapter<CelebsKnownForAdapter.celebsKnownForViewHolder> {
        private ArrayList<CelebsKnownForModel> celebsKnownForModelArrayList;
        class celebsKnownForViewHolder extends RecyclerView.ViewHolder {
            ImageView celebsKnownForImageView;
            TextView celebsKnownForNameTextView ;
            TextView celebsKnownForVoteAverageTextView;

            public celebsKnownForViewHolder(View itemView) {
                super(itemView);
                celebsKnownForNameTextView = (TextView) itemView.findViewById(R.id.main_child_title_textView);
                celebsKnownForImageView = (ImageView) itemView.findViewById(R.id.main_child_imageView);
                celebsKnownForVoteAverageTextView = (TextView) itemView.findViewById(R.id.main_child_vote_textView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(),MoviesDetails.class);
                        Bundle mBundle = new Bundle();
                        CelebsKnownForModel celebsKnownForModel = celebsKnownForModelArrayList.get(getAdapterPosition());
                        mBundle.putString("type",type);
                        mBundle.putString("id",celebsKnownForModel.getKnownForItemId());
                        intent.putExtras(mBundle);
                        startActivity(intent);
                    }
                });
            }
        }

        public CelebsKnownForAdapter(ArrayList<CelebsKnownForModel> arrayList) {
            this.celebsKnownForModelArrayList = arrayList;
        }

        @Override
        public celebsKnownForViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_vertical_card, parent, false); //change layout id
            return new celebsKnownForViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final celebsKnownForViewHolder holder, int position) {
                //Log.v("output", "hii");
                CelebsKnownForModel celebsKnownForModel = celebsKnownForModelArrayList.get(position);
                holder.celebsKnownForNameTextView.setText(celebsKnownForModel.getKnownForName());
                holder.celebsKnownForVoteAverageTextView.setText(celebsKnownForModel.getKnownForVoteAverage());
                Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500" + celebsKnownForModel.getKnownForimage())
                        .into(holder.celebsKnownForImageView);

        }

        @Override
        public int getItemCount() {
            return celebsKnownForModelArrayList.size();
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
     private  class CelebsKnownForModel{
        public String knownForName;
        public String knownForVoteAverage;
        public String knownForimage;
        public String knownForId;

        public CelebsKnownForModel(String id,String name,String voteAverage,String image){
            this.knownForId = id;
            this.knownForName = name;
            this.knownForVoteAverage = voteAverage;
            this.knownForimage = image;
        }

         public String getKnownForName() {
             return knownForName;
         }

         public String getKnownForVoteAverage() {
             return knownForVoteAverage;
         }

         public String getKnownForimage() {
             return knownForimage;
         }

         public String getKnownForItemId() {
             return knownForId;
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

        private ArrayList<CelebsKnownForModel> jsonCelebsKnownForParser(String jsonCelebsKnownFor) throws JSONException {
            ArrayList<CelebsKnownForModel> celebsKnownForArray = new ArrayList<>();
            JSONObject celebsKnownForObject = new JSONObject(jsonCelebsKnownFor);
            JSONArray celebsKnownForList = celebsKnownForObject.getJSONArray("results");
            for (int i = 0; i < celebsKnownForList.length(); i++) {
                //Log.v("output", "Hello done");
                JSONObject celebsKnownFor = celebsKnownForList.getJSONObject(i);
                JSONArray celebsKnownForJSONArray = celebsKnownFor.getJSONArray("known_for");
                for (int j = 0; j < celebsKnownForJSONArray.length(); j++) {
                    JSONObject celebs = celebsKnownForJSONArray.getJSONObject(j);
                    String knownForId = celebs.get("id").toString();
                    String knownForName = celebs.get("title").toString();
                    String knownForVoteAverage = celebs.get("vote_average").toString();
                    String knownForPoster = celebs.get("poster_path").toString();
                    CelebsKnownForModel celebsKnownForModel = new CelebsKnownForModel(knownForId, knownForName, knownForVoteAverage, knownForPoster);
                    celebsKnownForArray.add(celebsKnownForModel);
                    //Log.v("output", knownForName);
                }
                //Log.v("output", celebsKnownForArray.get(1).toString());
            }
            Log.v("output", celebsKnownForArray.get(1).getKnownForName());
            return celebsKnownForArray;
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
            ArrayList<CelebsKnownForModel> celebsKnownForArray = new ArrayList<>();
            ArrayList<CelebImageModel> celebImageModelArray = new ArrayList<>();
            try {
                CelebsModel celebsModel = jsonCelebsParser(jsonArray.get(0));
                title.setText(celebsModel.getTitle());
                biography.setText(celebsModel.getBiography());
                dateOfBirth.setText(celebsModel.getDateOfBirth());
                placeOfBirth.setText(celebsModel.getPlaceOfBirth());
                detailDOB.setText(celebsModel.getDateOfBirth());
                detailPlaceOfBirth.setText(celebsModel.getPlaceOfBirth());
                alsoKnownAs.setText(celebsModel.getAlsoKnownAs());
                Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500"+celebsModel.getProfile_url()).into(profile);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                celebsKnownForArray = jsonCelebsKnownForParser(jsonArray.get(1));
                celebsKnownForAdapter = new CelebsKnownForAdapter(celebsKnownForArray);
                Log.v("output", celebsKnownForArray.get(1).getKnownForName());
                recyclerViewCelebKnownFor.setAdapter(celebsKnownForAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                celebImageModelArray = jsonCelebImageParser(jsonArray.get(2));
                Log.v("output", celebImageModelArray.get(0).toString());
                celebsImagesAdapter = new CelebsImagesAdapter(celebImageModelArray);
                recyclerViewCelebImages.setAdapter(celebsImagesAdapter );


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
