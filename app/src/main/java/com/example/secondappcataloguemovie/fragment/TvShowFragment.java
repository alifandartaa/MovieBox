package com.example.secondappcataloguemovie.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secondappcataloguemovie.activities.DetailTvShowActivity;
import com.example.secondappcataloguemovie.adapter.MovieAdapter;
import com.example.secondappcataloguemovie.adapter.TvShowAdapter;
import com.example.secondappcataloguemovie.api.MovieViewModel;
import com.example.secondappcataloguemovie.api.TvShowViewModel;
import com.example.secondappcataloguemovie.model.Movie;
import com.example.secondappcataloguemovie.model.TvShow;
import com.example.secondappcataloguemovie.R;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFragment extends Fragment {

    private RecyclerView rvTvShow;
    private TvShowViewModel tvShowViewModel;
    private ArrayList<TvShow> list = new ArrayList<>();
    private ProgressBar progressBar;
    private final TvShowAdapter listTvShowAdapter = new TvShowAdapter();

    public TvShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_show, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvTvShow = view.findViewById(R.id.rv_tv_show);
        rvTvShow.setHasFixedSize(true);

        progressBar = view.findViewById(R.id.progressBar_2);

        rvTvShow.setLayoutManager(new LinearLayoutManager(getContext()));

        listTvShowAdapter.notifyDataSetChanged();
        rvTvShow.setAdapter(listTvShowAdapter);

        tvShowViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(TvShowViewModel.class);
        tvShowViewModel.setTvShow();
        showLoading(true);
        tvShowViewModel.getTvShows().observe(getViewLifecycleOwner(), new Observer<ArrayList<TvShow>>() {
            @Override
            public void onChanged(ArrayList<TvShow> tvShows) {
                if(tvShows != null){
                    listTvShowAdapter.setData(tvShows);
                    showLoading(false);
                }
            }
        });

        listTvShowAdapter.setOnItemClickCallback(new TvShowAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(TvShow data) {
                showSelectedTvShow(data);
            }
        });
    }

    private void showSelectedTvShow(TvShow tvShow) {
        TvShow dataTvShow = new TvShow();
        dataTvShow.setId(tvShow.getId());
        dataTvShow.setTitle(tvShow.getTitle());
        dataTvShow.setOverview(tvShow.getOverview());
        dataTvShow.setPosterPath(tvShow.getPosterPath());

        Intent intent = new Intent(getActivity(), DetailTvShowActivity.class);
        intent.putExtra("extra_tv_show", dataTvShow);
        startActivity(intent);
    }

    public void bindFragmentTvShowByQuery(String query) {
        tvShowViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(TvShowViewModel.class);
        tvShowViewModel.searchTvShow(query);
        tvShowViewModel.getTvShows().observe(getViewLifecycleOwner(), new Observer<ArrayList<TvShow>>() {
            @Override
            public void onChanged(ArrayList<TvShow> tvShows) {
                if(tvShows != null){
                    listTvShowAdapter.setData(tvShows);
                    tvShowViewModel.clear();
                    showLoading(false);
                }
            }
        });
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(VISIBLE);
        } else {
            progressBar.setVisibility(GONE);
        }
    }

    public void clearList() {
        listTvShowAdapter.clearData(list);
    }
}
