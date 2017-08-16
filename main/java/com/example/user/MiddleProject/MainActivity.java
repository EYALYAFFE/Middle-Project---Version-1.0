package com.example.user.MiddleProject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    Button addMain;//The button for inserting a new movie to the DB
    AlertDialog web_man_nav_dial, one_movie_delete_dialog, delete_all_movies_dialog;
    ListView movies_list;
    DBHelper db;
    MainMovieAdapter adapter;
    Movie movieToDelete;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        addMain = (Button) findViewById(R.id.addBTNMAIN);
        addMain.setOnClickListener(this);
        movies_list = (ListView) findViewById(R.id.moviesLSMAIN);
    }
    protected void onStart() {
        super.onStart();
        db = new DBHelper(this);
        ArrayList<Movie> movies = db.getAllMovies();
        adapter = new MainMovieAdapter(this, movies);
        movies_list.setAdapter(adapter);
        movies_list.setOnItemLongClickListener(this);
        movies_list.setOnItemClickListener(this);
    }
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exit_app:
                finish();
                break;
            case R.id.delete_all:
                delete_all_movies_dialog = new AlertDialog.Builder(this)
                        .setTitle("DELETE ALL")
                        .setMessage("Are you sure you want to delete all DB?")
                        .setPositiveButton("Yes",this)
                        .setNegativeButton("No",this)
                        .setNeutralButton("Cancel",this)
                        .setCancelable(false)
                        .create();
                delete_all_movies_dialog.show();
                break;
        }
        return true;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.addBTNMAIN):
                web_man_nav_dial = new AlertDialog.Builder(this)
                        .setTitle("Navigation")
                        .setMessage("Where do you want to navigate?")
                        .setPositiveButton("manually", this)
                        .setNegativeButton("web", this)
                        .setNeutralButton("cancel", this)
                        .setCancelable(false)
                        .create();
                web_man_nav_dial.show();
        }
    }

    public void onClick(DialogInterface dialogInterface, int button) {
        if (dialogInterface == web_man_nav_dial) {
            switch (button) {
                case DialogInterface.BUTTON_POSITIVE:
                    Intent toMovieMan = new Intent(this, MovieActivity.class);
                    toMovieMan.putExtra("state","manual");
                    startActivity(toMovieMan);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    Intent toWebSearch = new Intent(this, WebSearchActivity.class);
                    startActivity(toWebSearch);
                    break;
                case DialogInterface.BUTTON_NEUTRAL:
                    dialogInterface.dismiss();
                    break;
            }
        }
        if (dialogInterface==one_movie_delete_dialog){
            switch (button) {
                case DialogInterface.BUTTON_POSITIVE:
                    db.deleteMovie(movieToDelete.getId());
                    adapter.clear();
                    adapter.addAll(db.getAllMovies());
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    dialogInterface.dismiss();
                    break;
                case DialogInterface.BUTTON_NEUTRAL:
                    dialogInterface.dismiss();
                    break;
            }
        }
        if (dialogInterface == delete_all_movies_dialog) {
            switch (button) {
                case DialogInterface.BUTTON_POSITIVE:
                    db.deleteALL();
                    adapter.clear();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    dialogInterface.dismiss();
                    break;
                case DialogInterface.BUTTON_NEUTRAL:
                    dialogInterface.dismiss();
                    break;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent toMovieFromMain = new Intent(this, MovieActivity.class);
        boolean edit=true;
        toMovieFromMain.putExtra("state","edit");
        toMovieFromMain.putExtra("movie", (Parcelable) adapterView.getItemAtPosition(position));
        startActivity(toMovieFromMain);
    }

    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
        movieToDelete= (Movie) parent.getItemAtPosition(position);
        one_movie_delete_dialog=new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete the movie?")
                .setPositiveButton("Yes", this)
                .setNegativeButton("No", this)
                .setNeutralButton("Cancel", this)
                .setCancelable(false)
                .create();
        one_movie_delete_dialog.show();
        return true;
    }
}
