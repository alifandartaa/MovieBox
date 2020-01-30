package com.example.consumerapp.viewmodel;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.consumerapp.db.DatabaseContract;
import com.example.consumerapp.model.Favorite;

import java.util.ArrayList;

import static com.example.consumerapp.db.DatabaseContract.FavoriteColumns.CONTENT_URI;

public class FavoriteViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Favorite>> listFavorites;

    public FavoriteViewModel() {
    }

    public void setFavorites(Context context){
        listFavorites = new MutableLiveData<>();
        ArrayList<Favorite> listItems = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(CONTENT_URI, null, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Favorite favorite = new Favorite(
                    cursor.getInt(cursor.getColumnIndex(DatabaseContract.FavoriteColumns.ID)),
                    cursor.getString(cursor.getColumnIndex(DatabaseContract.FavoriteColumns.TITLE)),
                    cursor.getString(cursor.getColumnIndex(DatabaseContract.FavoriteColumns.TYPE)),
                    cursor.getString(cursor.getColumnIndex(DatabaseContract.FavoriteColumns.DESC)),
                    cursor.getString(cursor.getColumnIndex(DatabaseContract.FavoriteColumns.POSTER))
            );

            Log.d("tag22", "setFavorites: " + favorite.getId() + " " + favorite.getTitle() + " " + favorite.getType());

            listItems.add(favorite);
            cursor.moveToNext();
        }
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
