package com.example.kholoud.movieapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDbHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "movies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //*************************************
    //Create Table With Name Title ****
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_MOVIES_TABLE = "CREATE TABLE " + MovieContract.MovieData.TABLE_NAME
                + " (" +
                MovieContract.MovieData._ID + " INTEGER PRIMARY KEY," +
                MovieContract.MovieData.MOVIE_ID + " INTEGER NOT NULL, " +
                MovieContract.MovieData.MOVIE_VOTE_AVERAGE + " TEXT NOT NULL, " +
                MovieContract.MovieData.MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieData.MOVIE_BACKDROP_PATH + " TEXT NOT NULL," +
                MovieContract.MovieData.MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.MovieData.MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieContract.MovieData.MOVIE_POSTER_PATH + " TEXT NOT NULL " +
                " );";
        sqLiteDatabase.execSQL(CREATE_MOVIES_TABLE);
    }
//*********************************************************************
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieData.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
