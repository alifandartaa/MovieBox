package com.example.secondappcataloguemovie.activities;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.secondappcataloguemovie.R;
import com.example.secondappcataloguemovie.db.FavoriteHelper;
import com.example.secondappcataloguemovie.model.Favorite;
import com.example.secondappcataloguemovie.model.Movie;
import com.google.android.material.snackbar.Snackbar;

import static com.example.secondappcataloguemovie.db.DatabaseContract.FavoriteColumns.CONTENT_URI;
import static com.example.secondappcataloguemovie.db.DatabaseContract.FavoriteColumns.DESC;
import static com.example.secondappcataloguemovie.db.DatabaseContract.FavoriteColumns.ID;
import static com.example.secondappcataloguemovie.db.DatabaseContract.FavoriteColumns.POSTER;
import static com.example.secondappcataloguemovie.db.DatabaseContract.FavoriteColumns.TITLE;
import static com.example.secondappcataloguemovie.db.DatabaseContract.FavoriteColumns.TYPE;

public class DetailMovieActivity extends AppCompatActivity{

    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_FAVORITE = "extra_favorite";
    TextView tvTitle, tvRelease, tvOverview, tvPopularity, tvVoteAverage;
    ImageView posterMovie;
    private Movie movie;
    private Favorite favorite;
    private FavoriteHelper favoriteHelper;
    private ImageView btnFavorite;
    private MenuItem menuItem;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        init();


        favoriteHelper = FavoriteHelper.getInstance(getApplicationContext());
        favoriteHelper.open();

        intent = getIntent();
        if(intent.hasExtra(EXTRA_MOVIE)){
            movie = intent.getParcelableExtra(EXTRA_MOVIE);
            Glide.with(this).load(movie.getPosterPath()).into(posterMovie);
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
        }else if(intent.hasExtra(EXTRA_FAVORITE)){
            favorite = intent.getParcelableExtra(EXTRA_FAVORITE);
            Glide.with(this).load(favorite.getPoster()).into(posterMovie);
            tvTitle.setText(favorite.getTitle());
            tvOverview.setText(favorite.getDesc());
        }
    }

    private void init() {
        tvTitle = findViewById(R.id.tv_value_title);
        tvRelease = findViewById(R.id.tv_value_release);
        tvOverview = findViewById(R.id.tv_value_overview);
        tvPopularity = findViewById(R.id.tv_value_popularity);
        tvVoteAverage = findViewById(R.id.tv_value_vote);
        posterMovie = findViewById(R.id.img_movie);
        btnFavorite = findViewById(R.id.btn_favorite);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        menuItem = menu.findItem(R.id.btn_favorite);
//        if (getIntent().hasExtra(EXTRA_FAVORITE)) {
//            menuItem.setVisible(false);
//            return true;
//        }
//        return super.onPrepareOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btn_favorite || !intent.hasExtra(EXTRA_FAVORITE)) {
            showAlertDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAlertDialog() {
        String currentId = " ";
        if(intent.hasExtra(EXTRA_FAVORITE)){
            currentId = String.valueOf(favorite.getId());
        }else if(intent.hasExtra(EXTRA_MOVIE)){
            currentId = String.valueOf(movie.getId());
        }

        final boolean checkFavorite = favoriteHelper.checkFavorite(currentId);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Favorite");
        final String finalCurrentId = currentId;
        alertDialogBuilder
                .setMessage("Apa kamu yakin?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                            if(!checkFavorite){
                                ContentValues values = new ContentValues();
                                values.put(ID, movie.getId());
                                values.put(TITLE, movie.getTitle());
                                values.put(TYPE, movie.getClass().getSimpleName());
                                values.put(DESC, movie.getOverview());
                                values.put(POSTER, movie.getPosterPath());
                                Log.d("tag","tes" + " " + movie.getId() + " " + movie.getTitle() + " " + movie.getClass().getSimpleName());
                                showSnackbar(getResources().getString(R.string.fav_added));
                                getContentResolver().insert(CONTENT_URI, values);
//                                btnFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
                            }else{
                                Log.d("tag","Masuuuk");
                                Uri uri = Uri.parse(CONTENT_URI + "/" + finalCurrentId);
                                getContentResolver().delete(uri, null, null);
                                showSnackbar(getResources().getString(R.string.fav_delete));
//                                btnFavorite.setImageResource(R.drawable.baseline_favorite_border_black_18dp);
                            }
                            dialog.dismiss();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        favoriteHelper.close();
    }

    private void showSnackbar(String message) {
        Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_SHORT).show();
    }

}