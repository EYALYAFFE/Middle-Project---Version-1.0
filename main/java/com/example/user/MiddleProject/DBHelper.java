package com.example.user.MiddleProject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper{
    public static final String TABLE_NAME = "movies";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_YEAR = "year";
    private static final String COL_RATE = "rate";
    private static final String COL_PLOT = "plot";
    private static final String COL_URL = "url";
    private static final String COL_PATH = "path";


    public DBHelper(Context context){
        super(context, "movies.db", null, 1);
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(String.format
                /////////movies////////id///////////////////////////////title/////year//rate//plot////url///path
                ("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT, %s TEXT)",
                TABLE_NAME,COL_ID,COL_TITLE,COL_RATE,COL_YEAR,COL_PLOT,COL_URL,COL_PATH));
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public void addMovie(Movie movie) throws SQLiteConstraintException{
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE,movie.getTitle());
        values.put(COL_YEAR,movie.getYear());
        values.put(COL_RATE,movie.getRate());
        values.put(COL_PLOT,movie.getPlot());
        values.put(COL_URL,movie.getUrl());
        values.put(COL_PATH,movie.getPath());
        db.insertOrThrow(TABLE_NAME,null,values);
        db.close();
    }

    public ArrayList<Movie> getAllMovies(){
        ArrayList<Movie> movies = new ArrayList<Movie>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,null,null,null,null,null,null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(COL_ID));
            String title = cursor.getString(cursor.getColumnIndex(COL_TITLE));
            int year = cursor.getInt(cursor.getColumnIndex(COL_YEAR));
            String rate = cursor.getString(cursor.getColumnIndex(COL_RATE));
            String plot = cursor.getString(cursor.getColumnIndex(COL_PLOT));
            String url = cursor.getString(cursor.getColumnIndex(COL_URL));
            String path = cursor.getString(cursor.getColumnIndex(COL_PATH));
            movies.add(new Movie(id, title, year,rate, plot, url,path));
        }
        db.close();
        return movies;
    }
    public void deleteMovie(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME,COL_ID+"="+id,null);
        db.close();
    }

    public void deleteALL(){
        SQLiteDatabase db = getReadableDatabase();
        db.delete(TABLE_NAME,null,null);
    }
    public void updateMovie(Movie movie){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE,movie.getTitle());
        values.put(COL_YEAR,movie.getYear());
        values.put(COL_RATE,movie.getRate());
        values.put(COL_PLOT,movie.getPlot());
        values.put(COL_URL,movie.getUrl());
        values.put(COL_PATH,movie.getPath());

        db.update(TABLE_NAME,values,COL_ID+"="+movie.getId(),null);
        db.close();
    }


}
