package com.example.pc.flickr;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.pc.flickr.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 */
public class HorizontalListFragment extends Fragment {
    public ArrayAdapter<String> arrayAdapter;

    public HorizontalListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_horizontal_list, container, false);
        ListView list = (ListView) rootView.findViewById(R.id.listView_main);
        List<String> arrayList = new ArrayList<String>();
        String urlHeading[] = this.getArguments().getStringArray("urlHeading");
        String urls[] = this.getArguments().getStringArray("urls");
        String type = this.getArguments().getString("type");
        FetchTask fetchTask = new FetchTask();
        fetchTask.execute(urls[0]);
        try {
            String data = fetchTask.get().toString();
            if(type == "movies") {
                arrayList = jsonMovieParser(data);
            }
            else if(type == "tv"){
                arrayList = jsonTvParser(data);
            }
            else if (type == "celebs"){
                arrayList = jsonCelebsParser(data);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_textview,R.id.list_textview_item, arrayList);
        list.setAdapter(arrayAdapter);
        return rootView;
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
            movieArray.add(title + "\n" + release_date + "\n" + vote_average + "\n");
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
            String release_date = populartvShows.get("release_date").toString();
            String vote_average = populartvShows.get("vote_average").toString();
            tvShowsArray.add(title + "\n" + release_date + "\n" + vote_average);
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
            celebritiesArray.add(name + "\n" + movie + "\n" + vote_average + "\n");
        }
        return celebritiesArray;
    }


}
