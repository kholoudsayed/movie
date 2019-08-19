package com.example.kholoud.movieapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Trailers implements Parcelable {



    @SuppressWarnings("unused")

    @SerializedName("id")
    private String Id;
    @SerializedName("key")
    private String Key;
    @SerializedName("name")
    private String Name;
    @SerializedName("site")
    private String Site;
    @SerializedName("size")
    private String Size;

    public Trailers() {
    }





    public String getName() {
        return Name;
    }

    public String getKey() {
        return Key;
    }

    public String getTrailerUrl() {
        return "http://www.youtube.com/watch?v=" + Key;
    }
    public void setId(String mId) {
        this.Id = mId;
    }

    public void setKey(String mKey) {
        this.Key = mKey;
    }

    public void setName(String mName) {
        this.Name = mName;
    }

    public void setSite(String mSite) {
        this.Site = mSite;
    }

    public void setSize(String mSize) {
        this.Size = mSize;
    }

    public static final Parcelable.Creator<Trailers> CREATOR = new Creator<Trailers>() {
        public Trailers createFromParcel(Parcel source) {
            Trailers trailer = new Trailers();
            trailer.Id = source.readString();
            trailer.Key = source.readString();
            trailer.Name = source.readString();
            trailer.Site = source.readString();
            trailer.Size = source.readString();
            return trailer;
        }

        public Trailers[] newArray(int size) {
            return new Trailers[size];
        }
    };

    public int describeContents() {
        return 0;
    }
//******************************************************************
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(Id);
        parcel.writeString(Key);
        parcel.writeString(Name);
        parcel.writeString(Site);
        parcel.writeString(Size);
    }
}