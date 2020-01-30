package com.example.secondappcataloguemovie.activities;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.secondappcataloguemovie.R;
import com.example.secondappcataloguemovie.db.FavoriteHelper;
import com.example.secondappcataloguemovie.model.Favorite;
import com.example.secondappcataloguemovie.model.TvShow;
import com.google.android.material.snackbar.Snackbar;

import static com.example.secondappcataloguemovie.db.DatabaseContract.FavoriteColumns.CONTENT_URI;
import static com.example.secondappcataloguemovie.db.DatabaseContract.FavoriteColumns.DESC;
import static com.example.secondappcataloguemovie.db.DatabaseContract.FavoriteColumns.ID;
import static com.example.secondappcataloguemovie.db.DatabaseContract.FavoriteColumns.POSTER;
import static com.example.secondappcataloguemovie.db.DatabaseContract.FavoriteColumns.TITLE;
import static com.example.secondappcataloguemovie.db.DatabaseContract.FavoriteColumns.TYPE;

public class DetailTvShowActivity extends AppCompatActivity {

    public static final String EXTRA_TV_SHOW = "extra_tv_show";
    public static final String EXTRA_FAVORITE = "extra_favorite";
    private TextView tvTitle, tvOverview;
    private ImageView posterTvShow;
    private TvShow tvShow;
    private Favorite favorite;
    private FavoriteHelper favoriteHelper;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tv_show);

        init();

        favoriteHelper = FavoriteHelper.getInstance(getApplicationContext());
        favoriteHelper.open();

        intent = getIntent();
        if (intent.hasExtra(EXTRA_TV_SHOW)) {
            tvShow = intent.getParcelableExtra(EXTRA_TV_SHOW);
            Glide.with(this).load(tvShow.getPosterPath()).into(posterTvShow);
            tvTitle.setText(tvShow.getTitle());
            tvOverview.setText(tvShow.getOverview());
        } else if (intent.hasExtra(EXTRA_FAVORITE)) {
            favorite = intent.getParcelableExtra(EXTRA_FAVORITE);
            Glide.with(this).load(favorite.getPoster()).into(posterTvShow);
            tvTitle.setText(favorite.getTitle());
            tvOverview.setText(favorite.getDesc());
        }
    }

    private void init() {
        tvTitle = findViewById(R.id.tv_value_title);
        tvOverview = findViewById(R.id.tv_value_overview);
        posterTvShow = findViewById(R.id.img_tv_show);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btn_favorite || !intent.hasExtra(EXTRA_FAVORITE)) {
            showAlertDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAlertDialog() {
        String currentId = " ";
        if (intent.hasExtra(EXTRA_FAVORITE)) {
            currentId = String.valueOf(favorite.getId());
        } else if (intent.hasExtra(EXTRA_TV_SHOW)) {
            currentId = String.valueOf(tvShow.getId());
        }
        final boolean checkFavorite = favoriteHelper.checkFavorite(currentId);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(R.string.favorite);
        final String finalCurrentId = currentId;
        alertDialogBuilder
                .setMessage(R.string.string_message_alert)
                .setCancelable(false)
                .setPositiveButton(R.string.alert_confirm_positive, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (!checkFavorite) {
                            ContentValues values = new ContentValues();
                            values.put(ID, tvShow.getId());
                            values.put(TITLE, tvShow.getTitle());
                            values.put(TYPE, tvShow.getClass().getSimpleName());
                            values.put(DESC, tvShow.getOverview());
                            values.put(POSTER, tvShow.getPosterPath());
                            showSnackbar(getResources().getString(R.string.fav_added));
                            getContentResolver().insert(CONTENT_URI, values);
                        } else {
                            Uri uri = Uri.parse(CONTENT_URI + "/" + finalCurrentId);
                            getContentResolver().delete(uri, null, null);
                            showSnackbar(getResources().getString(R.string.fav_delete));
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.alert_confirm_negative, new DialogInterface.OnClickListener() {
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
