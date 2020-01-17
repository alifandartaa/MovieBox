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

import com.example.secondappcataloguemovie.activities.DetailMovieActivity;
import com.example.secondappcataloguemovie.adapter.MovieAdapter;
import com.example.secondappcataloguemovie.model.Movie;
import com.example.secondappcataloguemovie.api.MovieViewModel;
import com.example.secondappcataloguemovie.R;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment {

    private RecyclerView rvMovies;
    private MovieViewModel moviesViewModel;
    private ArrayList<Movie> list = new ArrayList<>();
    private  ProgressBar progressBar;
    private final MovieAdapter listMovieAdapter = new MovieAdapter();
    public static Bundle savedState;

    public MoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        savedState = savedInstanceState;

        rvMovies = view.findViewById(R.id.rv_movies);
        rvMovies.setHasFixedSize(true);

        progressBar = view.findViewById(R.id.progressBar);

        rvMovies.setLayoutManager(new LinearLayoutManager(getContext()));

        listMovieAdapter.notifyDataSetChanged();
        rvMovies.setAdapter(listMovieAdapter);

        moviesViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MovieViewModel.class);
        moviesViewModel.setMovies();
        showLoading(true);
        moviesViewModel.getMovies().observe(getViewLifecycleOwner(), new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
                if(movies != null){
                    listMovieAdapter.setData(movies);
                    moviesViewModel.clear();
                    showLoading(false);
                }
            }
        });

        listMovieAdapter.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Movie data) {
                showSelectedMovie(data);
            }
        });
    }

    private void showSelectedMovie(Movie movie) {
        Movie dataMovie = new Movie();
        dataMovie.setId(movie.getId());
        dataMovie.setTitle(movie.getTitle());
        dataMovie.setOverview(movie.getOverview());
        dataMovie.setPosterPath(movie.getPosterPath());

        Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
        intent.putExtra("extra_movie", dataMovie);
        startActivity(intent);
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(VISIBLE);
        } else {
            progressBar.setVisibility(GONE);
        }
    }

    public void bindFragmentMovieByQuery(String query) {
        moviesViewModel= new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MovieViewModel.class);
        moviesViewModel.searchMovie(query);
        moviesViewModel.getMovies().observe(getViewLifecycleOwner(), new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
                if(movies != null){
                    listMovieAdapter.setData(movies);
                    moviesViewModel.clear();
                    showLoading(false);
                }
            }
        });
    }

    public void clearList() {
        listMovieAdapter.clearData(list);
    }
}
