package com.example.pc.flickr;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.example.pc.flickr.models.CastModel;
import com.example.pc.flickr.models.DetailItemModel;
import com.example.pc.flickr.models.ReviewModel;
import com.example.pc.flickr.models.SimilarItemModel;
import com.example.pc.flickr.models.WishListModel;
import com.example.pc.flickr.services.FetchApiService;
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


// MoviesDetails activity to display the detail of particular movie
public class MoviesDetails extends AppCompatActivity {
    private String type, id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_movie_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = this.getIntent().getExtras();
        type = bundle.getString("type");
        id = bundle.getString("id");


        switch (type) {
            case "movies":

                break;
            case "tv":
                urlList.add("https://api.themoviedb.org/3/tv/" + id + "?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
                urlList.add("https://api.themoviedb.org/3/tv/" + id + "/credits?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
                urlList.add("https://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
                urlList.add("https://api.themoviedb.org/3/tv/" + id + "/similar?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
                break;
            default:
                urlList.add("https://api.themoviedb.org/3/movie/" + id + "?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
                urlList.add("https://api.themoviedb.org/3/movie/" + id + "/credits?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
                urlList.add("https://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
                urlList.add("https://api.themoviedb.org/3/movie/" + id + "/similar?api_key=fe56cdee4dfea0c18403e0965acfa23b&language=en-US");
        }
    }
    // CastAdapter class to populate data in castRecyclerView
}

