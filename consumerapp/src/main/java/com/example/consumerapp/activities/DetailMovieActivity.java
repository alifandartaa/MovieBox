package com.example.consumerapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.consumerapp.R;
import com.example.consumerapp.model.Favorite;
import com.google.android.material.snackbar.Snackbar;

import static com.example.consumerapp.db.DatabaseContract.FavoriteColumns.CONTENT_URI;

public class DetailMovieActivity extends AppCompatActivity {

    public static final String EXTRA_FAVORITE = "extra_favorite";
    private TextView tvTitle, tvOverview;
    private ImageView posterMovie;
    private Favorite favorite;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        init();

        intent = getIntent();
        favorite = intent.getParcelableExtra(EXTRA_FAVORITE);
        Glide.with(this).load(favorite.getPoster()).into(posterMovie);
        tvTitle.setText(favorite.getTitle());
        tvOverview.setText(favorite.getDesc());
    }

    private void init() {
        tvTitle = findViewById(R.id.tv_value_title);
        tvOverview = findViewById(R.id.tv_value_overview);
        posterMovie = findViewById(R.id.img_movie);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btn_favorite || !intent.hasExtra(EXTRA_FAVORITE)) {
            showAlertDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAlertDialog() {
        String currentId;
        currentId = String.valueOf(favorite.getId());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.favorite);
        final String finalCurrentId = currentId;
        alertDialogBuilder
                .setMessage(getString(R.string.string_message_alert))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.alert_confirm_positive), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Uri uri = Uri.parse(CONTENT_URI + "/" + finalCurrentId);
                        getContentResolver().delete(uri, null, null);
                        showSnackbar(getResources().getString(R.string.fav_delete));
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.alert_confirm_negative), new DialogInterface.OnClickListener() {
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

    private void showSnackbar(String message) {
        Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_SHORT).show();
    }

}
