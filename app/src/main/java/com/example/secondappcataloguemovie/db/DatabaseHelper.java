package com.example.secondappcataloguemovie.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "dbmovieapp";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_MOVIE = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            DatabaseContract.FavoriteColumns.TABLE_NAME,
            DatabaseContract.FavoriteColumns.ID,
            DatabaseContract.FavoriteColumns.TITLE,
            DatabaseContract.FavoriteColumns.TYPE,
            DatabaseContract.FavoriteColumns.DESC,
            DatabaseContract.FavoriteColumns.POSTER
    );

    DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.FavoriteColumns.TABLE_NAME);
        onCreate(db);
    }
}
