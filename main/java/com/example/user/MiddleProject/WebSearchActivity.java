package com.example.user.MiddleProject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class WebSearchActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String weburl = "http://api.themoviedb.org/3/search/movie?api_key=";
    private static final String v3 = "fc127fc069832e95eced154bf705240c&query=";
    //private static final String v4 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmYzEyN2ZjMDY5ODMyZTk1ZWNlZDE1NGJmNzA1MjQwYyIsInN1YiI6IjU5NzcxNzQ0YzNhMzY4NGFlNDAwMTgzNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.oVTk-7dPyrSO2b-xlYaacrliMay292wYarX3wzlKCb8";
    private static final String POSTER_PREFIX = "https://image.tmdb.org/t/p/w500/";

    private Button go;
    private EditText keyword;
    private ListView list_results;
    protected WebSearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_web_search);
        go = (Button) findViewById(R.id.goWEBSEARCH);
        go.setOnClickListener(this);
        keyword = (EditText) findViewById(R.id.keywordWEBSEARCH);
        list_results = (ListView) findViewById(R.id.resultsWEBSEARCH);
        list_results.setOnItemClickListener(this);
    }

    public void onClick(View view){
        DownloadMovieTask task = new DownloadMovieTask();
        task.execute(keyword.getText().toString());
    }


    class DownloadMovieTask extends AsyncTask<String,Void,ArrayList<Movie>>{
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();

        protected ArrayList<Movie> doInBackground(String... strings) {
            String fixed = strings[0].replaceAll(" ","%20");
            ArrayList<Movie> movies = new ArrayList<Movie>();
            try{
                String surl=weburl+v3+fixed;
                URL url = new URL(surl);
                connection= (HttpURLConnection) url.openConnection();

                if(connection.getResponseCode()!=HttpURLConnection.HTTP_OK){
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = reader.readLine();
                while (line!=null){
                    builder.append(line);
                    line=reader.readLine();
                }

                JSONObject rootobj = new JSONObject(builder.toString());
                JSONArray results = rootobj.getJSONArray("results");
                for (int i=0;i<results.length();i++){
                    JSONObject movie = results.getJSONObject(i);
                    String title = movie.getString("title");
                    String plot = movie.getString("overview");
                    String t1_year=movie.getString("release_date");
                    String t2_year="0";
                    int year;
                    if (!t1_year.isEmpty()){
                        t2_year=""+t1_year.charAt(0) + t1_year.charAt(1) + t1_year.charAt(2) + t1_year.charAt(3);
                        year = Integer.parseInt(t2_year);
                    }
                    else {
                        year = Integer.parseInt(t2_year);
                    }
                    String t_url=POSTER_PREFIX+movie.getString("poster_path");
                    String rate = movie.getString("vote_average");
                    movies.add(new Movie(title,year,rate,plot,t_url,null));
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return movies;
        }

        protected void onPostExecute(ArrayList<Movie> movies){
            if (movies!=null) {
                adapter = new WebSearchAdapter(WebSearchActivity.this,movies);
                //adapter.clear();
                //adapter.addAll(movies);
                list_results.setAdapter(adapter);
            }
        }
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){
        Intent toMovieFromWeb = new Intent(this,MovieActivity.class);
        toMovieFromWeb.putExtra("state","new");
        toMovieFromWeb.putExtra("movie", (Parcelable) adapterView.getItemAtPosition(position));
        startActivity(toMovieFromWeb);
    }
}

