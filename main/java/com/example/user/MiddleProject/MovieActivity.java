package com.example.user.MiddleProject;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_IMAGE_CAPTURE=1;
    EditText titleET, yearET, plotET, urlET;
    Button save;
    DBHelper db;
    ImageView posterIV;
    String rate;
    String mCurrentPhotoPath;
    String edit;
    int id;
    String _path;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_movie);

        titleET = (EditText) findViewById(R.id.titleMOVIEACT);
        yearET = (EditText) findViewById(R.id.yearMOVIEACT);
        plotET = (EditText) findViewById(R.id.plotMOVIEACT);
        urlET = (EditText) findViewById(R.id.urlMOVIEACT);
        edit="manual";
        save = (Button) findViewById(R.id.saveMOVIEACT);
        save.setOnClickListener(this);
        db = new DBHelper(this);
        findViewById(R.id.showMOVIEACT).setOnClickListener(this);
        posterIV = (ImageView) findViewById(R.id.posterIVMOVIEACT);
        findViewById(R.id.cancelMOVIEACT).setOnClickListener(this);
        findViewById(R.id.captureBTNMOVIE).setOnClickListener(this);

        Movie movie = getIntent().getParcelableExtra("movie");
        if (movie!=null){
            id=movie.getId();
            titleET.setText(movie.getTitle());
            yearET.setText("" + movie.getYear());
            plotET.setText(movie.getPlot());
            urlET.setText(movie.getUrl());
            _path=movie.getPath();
            rate=movie.getRate();
            edit=getIntent().getStringExtra("state");
            Toast.makeText(this, ""+edit, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.saveMOVIEACT):
                if (edit.equals("edit")){
                    updaterecord();
                    finish();
                }
                else {
                    String title=""+titleET.getText().toString();
                    String year_temp = "" + yearET.getText().toString();
                    int year;
                    if (year_temp.isEmpty()) {
                        year = 0;
                    } else {
                        year = Integer.parseInt(year_temp);
                    }
                    String plot=""+plotET.getText().toString();
                    String url=""+urlET.getText().toString();
                    String path=mCurrentPhotoPath;

                    if (title == "") {
                        Toast.makeText(this, "" + "Movie title is a must field", Toast.LENGTH_SHORT).show();
                    } else {
                        db.addMovie(new Movie(title, year, rate, plot, url, path));
                        finish();
                    }
                }
                break;
            case (R.id.showMOVIEACT):
                    DownloadImageTask task = new DownloadImageTask();
                    task.execute(urlET.getText().toString());
                break;
            case  (R.id.cancelMOVIEACT):
                finish();
                break;
            case (R.id.captureBTNMOVIE):
                openCamera();
                break;
        }
    }

    private void updaterecord(){
        String title=""+titleET.getText().toString();
        String year_temp=""+yearET.getText().toString();
        int year;
        if (year_temp.isEmpty()) {
            year = 0;
        } else {
            year=Integer.parseInt(year_temp);
        }
        String plot=""+plotET.getText().toString();
        String url=""+urlET.getText().toString();
        String path;
        if (mCurrentPhotoPath==null){
            path = _path;
        }
        else {
            path = mCurrentPhotoPath;
        }
        Toast.makeText(this, ""+path, Toast.LENGTH_SHORT).show();
        if (title==""){
            Toast.makeText(this,""+"Movie title is a must field", Toast.LENGTH_SHORT).show();
        } else {
            db.updateMovie(new Movie(id,title, year, rate, plot, url, path));
        }
    }

    protected class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        protected Bitmap doInBackground(String... urls){
                String url = urls[0];
                Bitmap bmp = null;
                try {
                    InputStream in = new java.net.URL(url).openStream();
                    bmp = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                return bmp;
        }
        protected void onPostExecute(Bitmap result) {
                posterIV.setImageBitmap(result);
                File photofile=null;
                OutputStream out = null;
            try {
                photofile=createImageFile();
                out=new FileOutputStream(photofile);
                if (result!=null) {
                    result.compress(Bitmap.CompressFormat.PNG, 100, out);
                }
                else
                    Toast.makeText(MovieActivity.this, ""+"Error accured. URL not valid", Toast.LENGTH_SHORT).show();
                out.flush();
                out.close();
                MediaStore.Images.Media.insertImage(MovieActivity.this.getContentResolver(),result,photofile.getName(),photofile.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            posterIV.setImageURI(Uri.parse(mCurrentPhotoPath));
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.user.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void saveImage(Bitmap finalBitmap, String image_name) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root);
        myDir.mkdirs();
        String fname = "Image-" + image_name+ ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        Log.i("LOAD", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}



