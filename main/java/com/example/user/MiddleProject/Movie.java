package com.example.user.MiddleProject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 7/25/2017.
 */

public class Movie implements Parcelable {
    private int id;
    private String title;
    private int year;
    private String rate;
    private String plot;
    private String url;
    private String path;

    public Movie(int id, String title, int year, String rate, String plot, String url, String path) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.rate = rate;
        this.plot = plot;
        this.url = url;
        this.path = path;
    }

    public Movie(String title, int year, String rate, String plot, String url, String path) {
        this.title = title;
        this.year = year;
        this.rate = rate;
        this.plot = plot;
        this.url = url;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public static Creator<Movie> getCREATOR() {
        return CREATOR;
    }

    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        year = in.readInt();
        rate = in.readString();
        plot = in.readString();
        url = in.readString();
        path = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeInt(year);
        dest.writeString(rate);
        dest.writeString(plot);
        dest.writeString(url);
        dest.writeString(path);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}

