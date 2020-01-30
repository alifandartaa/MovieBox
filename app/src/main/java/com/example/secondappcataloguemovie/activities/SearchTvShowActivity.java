package com.example.secondappcataloguemovie.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secondappcataloguemovie.R;
import com.example.secondappcataloguemovie.adapter.SearchTvShowAdapter;
import com.example.secondappcataloguemovie.viewmodel.TvShowViewModel;
import com.example.secondappcataloguemovie.model.TvShow;

import java.util.ArrayList;

public class SearchTvShowActivity extends AppCompatActivity {

    public static final String EXTRA_SEARCH_TV_SHOW = "extras_tv_show";
    private TvShowViewModel tvShowViewModel;
    private final SearchTvShowAdapter listSearchTvShowAdapter = new SearchTvShowAdapter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tv_show);

        RecyclerView rvSearch = findViewById(R.id.rv_search_tv_show);
        rvSearch.setHasFixedSize(true);

        rvSearch.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        listSearchTvShowAdapter.notifyDataSetChanged();
        rvSearch.setAdapter(listSearchTvShowAdapter);
        Intent intent = getIntent();
        String mQuery = intent.getStringExtra(EXTRA_SEARCH_TV_SHOW);
        bindTvShowByQuery(mQuery);
    }

    private void bindTvShowByQuery(String query) {
        tvShowViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(TvShowViewModel.class);
        tvShowViewModel.searchTvShow(query);
        tvShowViewModel.getTvShows().observe(this, new Observer<ArrayList<TvShow>>() {
            @Override
            public void onChanged(ArrayList<TvShow> tvShows) {
                if (tvShows != null) {
                    listSearchTvShowAdapter.setData(tvShows);
                    tvShowViewModel.clear();
                }
            }
        });
    }
}
