package com.example.pc.flickr;

import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;


// MoviesDetails activity to display the detail of particualr movie
public class MoviesDetails extends AppCompatActivity {
    RecyclerView recyclerView;
    private MovieAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_movie_layout);


        recyclerView = (RecyclerView) findViewById(R.id.detail_recycler_view);


        ArrayList<String> recyclerArrayList = new ArrayList<String>();

        /*TextView title, overview, vote_average, tagline, release_date, language;
        title = (TextView) findViewById(R.id.main_title);
        overview = (TextView) findViewById(R.id.overview);
        vote_average = (TextView) findViewById(R.id.vote_average);
        tagline = (TextView) findViewById(R.id.tagline);
        release_date = (TextView) findViewById(R.id.release_date);
        language = (TextView) findViewById(R.id.language);
        List<String> arrayList = new ArrayList<String>();
        /*String url = "https://api.themoviedb.org/3/movie/400?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US";*/
        String url1 = "https://api.themoviedb.org/3/movie/400/credits?api_key=fe56cdee4dfea0c18403e0965acfa23b";
        FetchTask callMovieData = new FetchTask();
        /*callMovieData.execute(url);

        try {
            //fetching data from FetchTask to populate textViews
            String data = callMovieData.get().toString();
            arrayList = jsonMovieParser(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //populating data in particular text views
        /*title.setText(arrayList.get(0));
        overview.setText(arrayList.get(1));
        vote_average.setText(arrayList.get(2));
        tagline.setText(arrayList.get(3));
        release_date.setText(arrayList.get(4));
        language.setText(arrayList.get(5));*/
        callMovieData.execute(url1);
        try {
            String data = callMovieData.get().toString();
            recyclerArrayList = jsonCastParser(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //to populate the data in recyclerView
        mAdapter = new MovieAdapter(recyclerArrayList);
        // to set the horizontal linear layout for recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }

    private class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.movieViewHolder> {
        private ArrayList<String> recyclerArrayList;
        class movieViewHolder extends RecyclerView.ViewHolder {
            TextView listItemTextView;

            public movieViewHolder(View itemView) {
                super(itemView);
                listItemTextView = (TextView) itemView.findViewById(R.id.castTextView);
            }
        }
        public MovieAdapter(ArrayList<String> arrayList) {
            this.recyclerArrayList = arrayList;
        }
        @Override
        public movieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_textview, parent, false);
            return new movieViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(movieViewHolder holder, int position) {
          String str = recyclerArrayList.get(position);
            holder.listItemTextView.setText(str);
        }

        @Override
        public int getItemCount() {
            return recyclerArrayList.size();
        }
        }
        /*
    private ArrayList<String> jsonMovieParser(String jsonMovie)throws JSONException {
        // fetching data in json
        JSONObject movieObject = new JSONObject(jsonMovie);
        String title = movieObject.get("title").toString();
        String overview = movieObject.get("overview").toString();
        String vote_average = movieObject.get("vote_average").toString();
        String tagline = movieObject.get("tagline").toString();
        String release_date = movieObject.get("release_date").toString();
        String language = movieObject.get("original_language").toString();
        String[] array = {title, overview, vote_average, tagline, release_date, language};
        //storing data from array in arraylist
        ArrayList<String> movieArray = new ArrayList<>(Arrays.asList(array));
        return movieArray;
    }*/
    private ArrayList<String> jsonCastParser(String jsonCast)throws JSONException{
        ArrayList<String> recyclerArray = new ArrayList<>();
        JSONObject castObject = new JSONObject(jsonCast);
        JSONArray castList = castObject.getJSONArray("cast");
        for (int i = 0; i < castList.length(); i++){
            JSONObject cast = castList.getJSONObject(i);
            String name = cast.get("name").toString();
            String character = cast.get("character").toString();
            recyclerArray.add(name + "\n" + character);
        }
        return recyclerArray;
    }
}

