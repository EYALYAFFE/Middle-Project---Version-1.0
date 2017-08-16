package com.example.user.MiddleProject;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class MainMovieAdapter extends ArrayAdapter<Movie> {

     public MainMovieAdapter(Context context, ArrayList<Movie> users){
         super(context, 0, users);
     }

     public View getView(int position, View convertView, ViewGroup parent) {
         // Get the data item for this position
         Movie movie = getItem(position);
         // Check if an existing view is being reused, otherwise inflate the view
         if (convertView == null) {
             convertView = LayoutInflater.from(getContext()).inflate(R.layout.main_display_row, parent, false);
         }
         // Lookup view for data population
         TextView movie_title = (TextView) convertView.findViewById(R.id.titleMAINADAPTER);
         TextView movie_year = (TextView) convertView.findViewById(R.id.yearMAINADAPTER);
         TextView movie_rate = (TextView) convertView.findViewById(R.id.rateYEARADAPTER);
         ImageView photo = convertView.findViewById(R.id.ivMAINADAPTER);
         TextView plot = convertView.findViewById(R.id.plotTVMAIN);
         // Populate the data into the template view using the data object
         movie_title.setText(movie.getTitle());
         if (movie.getYear()!=0) {
             movie_year.setText("" + movie.getYear());
         }
         else
             movie_year.setText("YEAR NOT AVAILABLE");
         //picture.setImageURI(Uri.parse(movie.getPath()));
         // Return the completed view to render on screen
         if (movie.getPath()!=null){
             photo.setImageURI(Uri.parse(movie.getPath()));
         }
         else {
             photo.getLayoutParams().height=0;
         }
         if (movie.getPlot().length()==0){
             plot.setText("PLOT NOT AVAILABLE");
         }
         else{
             plot.setText(movie.getPlot());
         }
         if (movie.getRate()==null) {
             movie_rate.setText("N/A");
         }
         else{
             movie_rate.setText(movie.getRate());

         }
         return convertView;
     }
    public boolean areAllItemsEnabled() {
        return true;
    }

    public boolean isEnabled(int position) {
        return true;
    }
}

