package com.example.secondappcataloguemovie.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class TvShow implements Parcelable {
    private int id;
    private String title;
    private String overview;
    private String posterPath;
    private String posterUrl = "https://image.tmdb.org/t/p/w154";

    public TvShow(JSONObject object) {
        try{
            this.id = object.getInt("id");
            this.title = object.getString("name");
            this.overview = object.getString("overview");
            String poster = object.getString("poster_path");
            this.posterPath = posterUrl + poster;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public TvShow(){

    }

    protected TvShow(Parcel in) {
        id = in.readInt();
        title = in.readString();
        overview = in.readString();
        this.posterPath = in.readString();
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

    public static final Creator<TvShow> CREATOR = new Creator<TvShow>() {
        @Override
        public TvShow createFromParcel(Parcel in) {
            return new TvShow(in);
        }

        @Override
        public TvShow[] newArray(int size) {
            return new TvShow[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(this.posterPath);
    }
}
