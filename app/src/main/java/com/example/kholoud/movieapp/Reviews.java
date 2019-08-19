package com.example.kholoud.movieapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Reviews implements Parcelable {

    @SerializedName("id")
    private String Id;
    @SerializedName("author")
    private String Author;
    @SerializedName("content")
    private String Content;
    @SerializedName("url")
    private String Url;

    public Reviews() {

    }

    protected Reviews(Parcel in) {
        Id = in.readString();
        Author = in.readString();
        Content = in.readString();
        Url = in.readString();
    }

    public static final Creator<Reviews> CREATOR = new Creator<Reviews>() {
        @Override
        public Reviews createFromParcel(Parcel in) {
            return new Reviews(in);
        }

        @Override
        public Reviews[] newArray(int size) {
            return new Reviews[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Id);
        parcel.writeString(Author);
        parcel.writeString(Content);
        parcel.writeString(Url);
    }


    public void setId(String Id) {
        this.Id = Id;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String Author) {
        this.Author = Author;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Author) {
        this.Content = Content;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String mUrl) {
        this.Url = mUrl;
    }
}
