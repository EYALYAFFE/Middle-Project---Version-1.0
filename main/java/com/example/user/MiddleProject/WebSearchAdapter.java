package com.example.user.MiddleProject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 8/11/2017.
 */

public class WebSearchAdapter extends ArrayAdapter<Movie> {

    public WebSearchAdapter(Context context, ArrayList<Movie> users){
        super(context, 0, users);
    }

    public View getView(int position, View convertView,ViewGroup parent) {
        Movie movie = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.web_search_row, parent, false);
        }
        // Lookup view for data population
        TextView movie_title = (TextView) convertView.findViewById(R.id.titleTVWEBSEARCH);
        TextView movie_year = (TextView) convertView.findViewById(R.id.yearTVWEBSEARCH);
        TextView movie_plot = (TextView) convertView.findViewById(R.id.plotTVWEBSEARCH);
        movie_title.setText(""+movie.getTitle());
        movie_year.setText(""+movie.getYear());
        movie_plot.setText(""+movie.getPlot());
        return convertView;
    }
}
