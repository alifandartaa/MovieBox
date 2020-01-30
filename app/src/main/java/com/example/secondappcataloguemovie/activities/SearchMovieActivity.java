package com.example.secondappcataloguemovie.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secondappcataloguemovie.R;
import com.example.secondappcataloguemovie.adapter.SearchMovieAdapter;
import com.example.secondappcataloguemovie.viewmodel.MovieViewModel;
import com.example.secondappcataloguemovie.model.Movie;

import java.util.ArrayList;

public class SearchMovieActivity extends AppCompatActivity {

    public static final String EXTRA_SEARCH_MOVIE = "extras_movie";
    private MovieViewModel moviesViewModel;
    private final SearchMovieAdapter listSearchMovieAdapter = new SearchMovieAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);

        RecyclerView rvSearch = findViewById(R.id.rv_search_movie);
        rvSearch.setHasFixedSize(true);

        rvSearch.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        listSearchMovieAdapter.notifyDataSetChanged();
        rvSearch.setAdapter(listSearchMovieAdapter);
        Intent intent = getIntent();
        String mQuery = intent.getStringExtra(EXTRA_SEARCH_MOVIE);
        bindMovieByQuery(mQuery);
    }

    private void bindMovieByQuery(String query) {
        moviesViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MovieViewModel.class);
        moviesViewModel.searchMovie(query);
        moviesViewModel.getMovies().observe(this, new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
                if (movies != null) {
                    listSearchMovieAdapter.setData(movies);
                    moviesViewModel.clear();
                }
            }
        });
    }
}
