package com.example.secondappcataloguemovie.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secondappcataloguemovie.R;
import com.example.secondappcataloguemovie.activities.DetailMovieActivity;
import com.example.secondappcataloguemovie.activities.DetailTvShowActivity;
import com.example.secondappcataloguemovie.adapter.FavoriteAdapter;
import com.example.secondappcataloguemovie.viewmodel.FavoriteViewModel;
import com.example.secondappcataloguemovie.model.Favorite;

import java.util.ArrayList;

import static com.example.secondappcataloguemovie.activities.DetailMovieActivity.EXTRA_FAVORITE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {

    public FavoriteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rvFavorite = view.findViewById(R.id.rv_favorite);
        rvFavorite.setHasFixedSize(true);

        rvFavorite.setLayoutManager(new LinearLayoutManager(getContext()));
        final FavoriteAdapter listFavAdapter = new FavoriteAdapter();
        listFavAdapter.notifyDataSetChanged();
        rvFavorite.setAdapter(listFavAdapter);

        final FavoriteViewModel favoriteViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(FavoriteViewModel.class);
        favoriteViewModel.setFavorites(getContext());
        favoriteViewModel.getFavorites().observe(getViewLifecycleOwner(), new Observer<ArrayList<Favorite>>() {
            @Override
            public void onChanged(ArrayList<Favorite> favorites) {
                if(favorites != null){
                    listFavAdapter.setData(favorites);
                    favoriteViewModel.clear();
                }
                listFavAdapter.setOnItemClickCallback(new FavoriteAdapter.OnItemClickCallback() {
                    @Override
                    public void onItemClicked(Favorite favorite) {
                        String type = favorite.getType();
                        if(type.equals("Movie")){
                            Intent intent1 = new Intent(getActivity(), DetailMovieActivity.class);
                            intent1.putExtra(EXTRA_FAVORITE, favorite);
                            startActivity(intent1);
                        }else if(type.equals("TvShow")){
                            Intent intent2 = new Intent(getActivity(), DetailTvShowActivity.class);
                            intent2.putExtra(EXTRA_FAVORITE, favorite);
                            startActivity(intent2);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }
}
