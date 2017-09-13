package com.example.pc.flickr;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.flickr.R;

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
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 */
public class HorizontalListFragment extends Fragment {
    public PrimaryMainAdapter mAdapter;
    String type;
    private String urls[];
    RecyclerView recyclerView;

    public HorizontalListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_horizontal_list, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_main);
        ArrayList<String> arrayList = new ArrayList<String>();
        String urlHeading[] = this.getArguments().getStringArray("urlHeading");
        ArrayList<String> urlHeadingList = new ArrayList<>(Arrays.asList(urlHeading));
        urls = this.getArguments().getStringArray("urls");
        type = this.getArguments().getString("type");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new HorizontalListFragment.PrimaryMainAdapter(urlHeadingList);
        recyclerView.setAdapter(mAdapter);
        return rootView;
    }

    private class PrimaryMainAdapter extends RecyclerView.Adapter<PrimaryMainAdapter.MyViewHolder> {
        private ChildMainAdapter childAdapter;
        private ArrayList<String> arrayList;

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView parentCardViewHeading;
            RecyclerView childRecyclerView;
            public MyViewHolder(View itemview){
                super(itemview);
                parentCardViewHeading = (TextView) itemview.findViewById(R.id.main_horizontal_card_heading);
                childRecyclerView = (RecyclerView) itemview.findViewById(R.id.main_child_recyclerView);
            }
        }

        public PrimaryMainAdapter(ArrayList<String> arrayList){
            this.arrayList = arrayList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.main_horizontal_card, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            String str = arrayList.get(position);
            holder.parentCardViewHeading.setText(str);

            FetchTask fetchTask = new FetchTask();
            ArrayList<String> childArrayList = new ArrayList<>();
            ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if ((networkInfo != null) && networkInfo.isConnected()){
                fetchTask.execute(urls[position]);

                try {
                    String jsonData = fetchTask.get();
                    if(type == "movies") {
                        childArrayList = jsonMovieParser(jsonData);
                    }
                    else if(type == "tv"){
                        childArrayList = jsonTvParser(jsonData);
                    }
                    else if (type == "celebs"){
                        childArrayList = jsonCelebsParser(jsonData);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(getContext(), "Please connect to internet", Toast.LENGTH_LONG).show();
            }
            holder.childRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            childAdapter = new HorizontalListFragment.ChildMainAdapter(childArrayList);
            holder.childRecyclerView.setAdapter(childAdapter);

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }
    private class ChildMainAdapter extends RecyclerView.Adapter<ChildMainAdapter.MyViewHolder> {

        private ArrayList<String> arrayList;

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView childViewTitle;
            public MyViewHolder(View itemview){
                super(itemview);
                childViewTitle = (TextView) itemview.findViewById(R.id.main_child_title_textView);

            }
        }

        public ChildMainAdapter(ArrayList<String> arrayList){
            this.arrayList = arrayList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.main_vertical_card, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            String str = arrayList.get(position);
            holder.childViewTitle.setText(str);

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    private ArrayList<String> jsonMovieParser(String jsonMovie)throws JSONException {
        ArrayList<String> movieArray = new ArrayList<>();
        JSONObject movieObject = new JSONObject(jsonMovie);
        JSONArray list = movieObject.getJSONArray("results");
        for (int i = 0; i < list.length();i++){
            JSONObject popularMovie = list.getJSONObject(i);
            String title = popularMovie.get("title").toString();
            String release_date = popularMovie.get("release_date").toString();
            String vote_average = popularMovie.get("vote_average").toString();
            movieArray.add(title);
        }
        return movieArray;
    }

    private ArrayList<String> jsonTvParser(String jsontvShows)throws JSONException {
        ArrayList<String> tvShowsArray = new ArrayList<>();
        JSONObject tvShowsObject = new JSONObject(jsontvShows);
        JSONArray list = tvShowsObject.getJSONArray("results");

        for (int i = 0; i < list.length();i++){
            JSONObject populartvShows = list.getJSONObject(i);
            String title = populartvShows.get("name").toString();
            String first_air_date = populartvShows.get("first_air_date").toString();
            String vote_average = populartvShows.get("vote_average").toString();
            tvShowsArray.add(title);
        }
        return tvShowsArray;
    }


    private ArrayList<String> jsonCelebsParser(String jsonCelebrities)throws JSONException {
        ArrayList<String> celebritiesArray = new ArrayList<>();
        JSONObject celebritiesObject = new JSONObject(jsonCelebrities);
        JSONArray list = celebritiesObject.getJSONArray("results");
        for (int i = 0; i < list.length();i++){
            JSONObject popularCelebrities = list.getJSONObject(i);
            String name = popularCelebrities.get("name").toString();
            JSONObject known_for = popularCelebrities.getJSONArray("known_for").getJSONObject(0);
            String movie = known_for.get("title").toString();
            String vote_average = known_for.get("vote_average").toString();
            celebritiesArray.add(name);
        }
        return celebritiesArray;
    }
}
