package com.example.secondappcataloguemovie;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.example.secondappcataloguemovie.model.Favorite;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.secondappcataloguemovie.FavoriteWidget.EXTRA_ITEM;
import static com.example.secondappcataloguemovie.db.DatabaseContract.FavoriteColumns.CONTENT_URI;
import static com.example.secondappcataloguemovie.db.DatabaseContract.FavoriteColumns.DESC;
import static com.example.secondappcataloguemovie.db.DatabaseContract.FavoriteColumns.ID;
import static com.example.secondappcataloguemovie.db.DatabaseContract.FavoriteColumns.POSTER;
import static com.example.secondappcataloguemovie.db.DatabaseContract.FavoriteColumns.TITLE;
import static com.example.secondappcataloguemovie.db.DatabaseContract.FavoriteColumns.TYPE;

class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final List<Favorite> mWidgetItems = new ArrayList<>();
    private final Context mContext;
    private Cursor cursor;

    StackRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {
        cursor = mContext.getContentResolver().query(CONTENT_URI, null, null, null, null);
        for (int i = 0; i < Objects.requireNonNull(cursor).getCount(); i++) {
            cursor.moveToNext();
            Favorite favorite = new Favorite(
                    cursor.getInt(cursor.getColumnIndex(ID)),
                    cursor.getString(cursor.getColumnIndex(TITLE)),
                    cursor.getString(cursor.getColumnIndex(TYPE)),
                    cursor.getString(cursor.getColumnIndex(DESC)),
                    cursor.getString(cursor.getColumnIndex(POSTER))
            );
            mWidgetItems.add(favorite);
            Log.d("Stack Widget Service", "onCreate: " + favorite);
        }
    }

    @Override
    public void onDataSetChanged() {
        if (cursor != null) {
            cursor.close();
        }

        final long tokenID = Binder.clearCallingIdentity();
        cursor = mContext.getContentResolver().query(CONTENT_URI, null, null, null, null);
        Binder.restoreCallingIdentity(tokenID);
    }

    @Override
    public void onDestroy() {
        mWidgetItems.clear();
    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setTextViewText(R.id.tv_titlewidget, mWidgetItems.get(position).getTitle());

        try {
            Bitmap bitmap = Glide.with(mContext)
                    .asBitmap()
                    .load(mWidgetItems.get(position).getPoster())
                    .submit(512, 512)
                    .get();

            rv.setImageViewBitmap(R.id.imageView, bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bundle extras = new Bundle();
        extras.putInt(EXTRA_ITEM, position);
        Intent fillIntent = new Intent();
        fillIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageView, fillIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
