package com.example.secondappcataloguemovie.viewmodel;

import android.content.Context;
import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.secondappcataloguemovie.db.DatabaseContract;
import com.example.secondappcataloguemovie.db.FavoriteHelper;
import com.example.secondappcataloguemovie.model.Favorite;

import java.util.ArrayList;

public class FavoriteViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Favorite>> listFavorites;

    public FavoriteViewModel() {
    }

    public void setFavorites(Context context) {
        listFavorites = new MutableLiveData<>();
        ArrayList<Favorite> listItems = new ArrayList<>();
        FavoriteHelper favoriteHelper = FavoriteHelper.getInstance(context);
        favoriteHelper.open();

        Cursor cursor = favoriteHelper.queryAll();
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Favorite favorite = new Favorite(
                    cursor.getInt(cursor.getColumnIndex(DatabaseContract.FavoriteColumns.ID)),
                    cursor.getString(cursor.getColumnIndex(DatabaseContract.FavoriteColumns.TITLE)),
                    cursor.getString(cursor.getColumnIndex(DatabaseContract.FavoriteColumns.TYPE)),
                    cursor.getString(cursor.getColumnIndex(DatabaseContract.FavoriteColumns.DESC)),
                    cursor.getString(cursor.getColumnIndex(DatabaseContract.FavoriteColumns.POSTER))
            );

            listItems.add(favorite);
            cursor.moveToNext();
        }
        favoriteHelper.close();
        cursor.close();
        listFavorites.postValue(listItems);
    }

    public void clear() {
        listFavorites.postValue(null);
    }

    public LiveData<ArrayList<Favorite>> getFavorites() {
        return listFavorites;
    }
}
