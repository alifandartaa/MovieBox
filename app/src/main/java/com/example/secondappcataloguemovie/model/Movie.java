package com.example.secondappcataloguemovie.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class Movie implements Parcelable {
    private int id;
    private String title;
    private String overview;
    private String posterPath;
    private String posterUrl = "https://image.tmdb.org/t/p/w154";

    public Movie(JSONObject object) {
        try{
            this.id = object.getInt("id");
            this.title = object.getString("title");
            this.overview = object.getString("overview");
            String poster = object.getString("poster_path");
            this.posterPath = posterUrl + poster;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Movie() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.overview);
        dest.writeString(this.posterPath);
    }



    protected Movie(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.overview = in.readString();
        this.posterPath = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
