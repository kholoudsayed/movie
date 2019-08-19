package com.example.kholoud.movieapp;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {


    // Def*****************************************************
    private final static String LOG_TAG = MovieAdapter.class.getSimpleName();
    private final ArrayList<Movie> ArrMovies;
    private OnItemClickListener onItemClickListener;

    public static final float POSTER_ASPECT_RATIO = 2.0f;

    //Implement Inerface Method *************
    public interface OnItemClickListener {
        void send_details(Movie movie, int position);
    }

    // Constractor  *********************************
    public MovieAdapter(ArrayList<Movie> ArrMovies, OnItemClickListener onItemClickListener) {
        this.ArrMovies = ArrMovies;
        this.onItemClickListener = onItemClickListener;

    }


    @NonNull
    //Override functions **************************************************
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(R.layout.movie_item, parent, shouldAttachToParentImmediately);
        final Context context = view.getContext();
//get number of col****************
        int NumCol = 2;

        view.getLayoutParams().height = (int) (parent.getWidth() / NumCol * POSTER_ASPECT_RATIO);


        MovieViewHolder viewHolder = new MovieViewHolder(view);
        return viewHolder;
    }


    //***********************************************************************************************
    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder holder, int position) {
        final Movie movie = ArrMovies.get(position);
        final Context context = holder.view.getContext();

        holder.movie = movie;
        holder.mMovietitle.setText(movie.getOriginalTitle());

        String posterUrl = movie.getPosterPath();
//if ERRor
        if (posterUrl == null) {
            holder.mMovietitle.setVisibility(View.VISIBLE);
        }

        Picasso.get()
                .load(movie.getPosterPath())
                .config(Bitmap.Config.RGB_565)
                .placeholder(R.drawable.image_placeholder)
                .into(holder.imageview,
                        new Callback() {
                            @Override
                            public void onSuccess() {
                                if (holder.movie.getId() != movie.getId()) {
                                    holder.cleanUp();
                                } else {
                                    holder.imageview.setVisibility(View.VISIBLE);
                                }
                            }
                            @Override
                            public void onError(Exception e) {
                                holder.mMovietitle.setVisibility(View.VISIBLE);
                            }
                        }
                );
//********************************************************************
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.send_details(movie,holder.getAdapterPosition());
            }
        });
    }


    @Override
    public int getItemCount() {
        return ArrMovies.size();
    }

    @Override
    public void onViewRecycled(MovieViewHolder holder) {
        super.onViewRecycled(holder);
        holder.cleanUp();
    }

    //Inner Class
    public class MovieViewHolder extends RecyclerView.ViewHolder {
        public final View view;

        public Movie movie;

        @BindView(R.id.movie_thumbnail)
        ImageView imageview;

        @BindView(R.id.movie_title)
        TextView mMovietitle;

        public MovieViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;

        }
//******************************

        public void cleanUp() {
            Picasso.get().cancelRequest(imageview);



            imageview.setImageBitmap(null);
            imageview.setVisibility(View.INVISIBLE);
            mMovietitle.setVisibility(View.GONE);
        }

    }
    public void add(List<Movie> movies) {
        ArrMovies.clear();
        ArrMovies.addAll(movies);
        notifyDataSetChanged();
    }

    public void addData(Cursor cursor) {

        ArrMovies.clear();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(MovieContract.MovieData._MOVIE_ID);
                String v_average = cursor.getString(MovieContract.MovieData._MOVIE_VOTE_AVERAGE);
                String title = cursor.getString(MovieContract.MovieData._MOVIE_TITLE);
                String backdropPath = cursor.getString(MovieContract.MovieData._MOVIE_BACKDROP_PATH);
                String overview = cursor.getString(MovieContract.MovieData._MOVIE_OVERVIEW);
                String releaseDate = cursor.getString(MovieContract.MovieData._MOVIE_RELEASE_DATE);
                String posterPath = cursor.getString(MovieContract.MovieData._MOVIE_POSTER_PATH);
                Movie movie = new Movie(id,v_average,title,backdropPath,overview,releaseDate,posterPath);
                ArrMovies.add(movie);
            } while (cursor.moveToNext());

        }
        notifyDataSetChanged();
    }

    public ArrayList<Movie> getMovies() {
        return ArrMovies;
    }


}