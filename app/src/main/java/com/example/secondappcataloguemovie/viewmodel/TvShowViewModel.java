package com.example.secondappcataloguemovie.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.secondappcataloguemovie.BuildConfig;
import com.example.secondappcataloguemovie.model.TvShow;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class TvShowViewModel extends ViewModel {
    private static final String API_KEY = BuildConfig.API_KEY;
    private MutableLiveData<ArrayList<TvShow>> listTvShow = new MutableLiveData<>();


    public void setTvShow(){
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<TvShow> listItems = new ArrayList<>();

        String url = "https://api.themoviedb.org/3/discover/tv?api_key="+ API_KEY +"&language=en-US";

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for(int i=0; i < list.length(); i++){
                        JSONObject tvShow = list.getJSONObject(i);
                        TvShow mData = new TvShow(tvShow);
                        listItems.add(mData);
                    }
                    listTvShow.postValue(listItems);
                }catch (Exception e){
                    Log.d("Exception", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", Objects.requireNonNull(error.getMessage()));
            }
        });
}

    public void searchTvShow(String query){
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<TvShow> listItems = new ArrayList<>();

        String url = "https://api.themoviedb.org/3/search/tv?api_key="+ API_KEY +"&language=en-US&query="+query;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for(int i=0; i < list.length(); i++){
                        JSONObject tvShow = list.getJSONObject(i);
                        TvShow mData = new TvShow(tvShow);
                        listItems.add(mData);
                    }
                    listTvShow.postValue(listItems);
                }catch (Exception e){
                    Log.d("Exception", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", Objects.requireNonNull(error.getMessage()));
            }
        });
    }

    public LiveData<ArrayList<TvShow>> getTvShows(){
        return listTvShow;
    }

    public void clear() {
        listTvShow.postValue(null);
    }
}
