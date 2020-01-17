package com.example.secondappcataloguemovie.api;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.secondappcataloguemovie.model.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class MovieViewModel extends ViewModel {
    private static final String API_KEY = "3148d91de7ab9fa0e608d7be83b7d967";
    private MutableLiveData<ArrayList<Movie>> listMovies = new MutableLiveData<>();

    public MovieViewModel() {
    }

    public void setMovies(){
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Movie> listItems = new ArrayList<>();

        String url = "https://api.themoviedb.org/3/discover/movie?api_key="+ API_KEY +"&language=en-US";

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for(int i=0; i < list.length(); i++){
                        JSONObject movie = list.getJSONObject(i);
                        Movie mData = new Movie(movie);
                        listItems.add(mData);
                    }
                    listMovies.postValue(listItems);
                }catch (Exception e){
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });
    }

    public void searchMovie(String query){
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Movie> listItems = new ArrayList<>();

        String url = "https://api.themoviedb.org/3/search/movie?api_key="+ API_KEY +"&language=en-US&query="+query;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for(int i=0; i < list.length(); i++){
                        JSONObject movie = list.getJSONObject(i);
                        Movie mData = new Movie(movie);
                        listItems.add(mData);
                    }
                    listMovies.postValue(listItems);
                }catch (Exception e){
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });
    }

    public void getReleaseMovies(final ReleaseMovieListener listener){
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Movie> listItems = new ArrayList<>();

        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String newDate = simpleDateFormat.format(date);

        String url = "https://api.themoviedb.org/3/discover/movie?api_key="+ API_KEY +"&primary_release_date.gte="+newDate+"&primary_release_date.lte="+newDate;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for(int i=0; i < list.length(); i++){
                        JSONObject movie = list.getJSONObject(i);
                        Movie mData = new Movie(movie);
                        listItems.add(mData);
                    }
                    listener.onSuccess(listItems);
                    listener.onError(false);
                }catch (Exception e){
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
                listener.onError(true);
                listener.onSuccess(new ArrayList<Movie>());
            }
        });
    }

    public void clear() {
        listMovies.postValue(null);
    }

    public LiveData<ArrayList<Movie>> getMovies(){
        return listMovies;
    }

    public interface ReleaseMovieListener {
        void onSuccess(ArrayList<Movie> movies);

        void onError(boolean isFailed);
    }
}
