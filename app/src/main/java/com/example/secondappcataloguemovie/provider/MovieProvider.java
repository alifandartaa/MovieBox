package com.example.secondappcataloguemovie.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.secondappcataloguemovie.db.FavoriteHelper;

import java.util.Objects;

import static com.example.secondappcataloguemovie.db.DatabaseContract.AUTHORITY;
import static com.example.secondappcataloguemovie.db.DatabaseContract.FavoriteColumns.CONTENT_URI;
import static com.example.secondappcataloguemovie.db.DatabaseContract.FavoriteColumns.TABLE_NAME;

public class MovieProvider extends ContentProvider {
    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;
    private FavoriteHelper favoriteHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, MOVIE);
        sUriMatcher.addURI(AUTHORITY,
                TABLE_NAME + "/#",
                MOVIE_ID);
    }

    @Override
    public boolean onCreate() {
        favoriteHelper = FavoriteHelper.getInstance(getContext());
        favoriteHelper.open();
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        favoriteHelper = FavoriteHelper.getInstance(getContext());
        favoriteHelper.open();
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                cursor = favoriteHelper.queryAll();
                break;
            case MOVIE_ID:
                cursor = favoriteHelper.queryById(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        long added;
        if (sUriMatcher.match(uri) == MOVIE) {
            added = favoriteHelper.insert(values);
        } else {
            added = 0;
        }
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI, null);
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int updated;
        if (sUriMatcher.match(uri) == MOVIE_ID) {
            updated = favoriteHelper.update(uri.getLastPathSegment(), values);
        } else {
            updated = 0;
        }
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI, null);
        return updated;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int deleted;
        if (sUriMatcher.match(uri) == MOVIE_ID) {
            deleted = favoriteHelper.deleteById(uri.getLastPathSegment());
        } else {
            deleted = 0;
        }
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI, null);
        return deleted;
    }

}
