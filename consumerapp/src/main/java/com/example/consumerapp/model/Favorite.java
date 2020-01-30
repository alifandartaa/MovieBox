package com.example.consumerapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Favorite implements Parcelable {
    private int id;
    private String type;
    private String poster;
    private String title;
    private String desc;

    public Favorite(int id, String title, String type, String desc, String poster) {
        this.id = id;
        this.type = type;
        this.poster = poster;
        this.title = title;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getPoster() {
        return poster;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    protected Favorite(Parcel in) {
        id = in.readInt();
        type = in.readString();
        poster = in.readString();
        title = in.readString();
        desc = in.readString();
    }

    public static final Creator<Favorite> CREATOR = new Creator<Favorite>() {
        @Override
        public Favorite createFromParcel(Parcel in) {
            return new Favorite(in);
        }

        @Override
        public Favorite[] newArray(int size) {
            return new Favorite[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(type);
        dest.writeString(poster);
        dest.writeString(title);
        dest.writeString(desc);
    }
}
