package com.example.consumerapp.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {

    private static final String AUTHORITY = "com.example.secondappcataloguemovie";
    private static final String SCHEME = "content";

    private DatabaseContract() {
    }

    public static class FavoriteColumns implements BaseColumns {
        public static String TABLE_NAME = "favorite";
        public static final Uri CONTENT_URI = new Uri.Builder()
                .scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
        public static String ID = "id";
        public static String TITLE = "title";
        public static String TYPE = "type";
        public static String DESC = "desc";
        public static String POSTER = "poster";
    }

}
