// Package Def Part **********************************
package com.example.kholoud.movieapp;


// import Def Part **********************************

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
//import some classes in My Backage
import com.example.kholoud.movieapp.MovieDetailActivity;
import com.example.kholoud.movieapp.MovieDetailFragment;

import com.example.kholoud.movieapp.MovieContract;
import com.example.kholoud.movieapp.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
// Part 9 But I Use it here to Be easy More

import butterknife.BindView;
import butterknife.ButterKnife;


public class Dashboard extends AppCompatActivity implements  MovieAdapter.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor>{

    //DEF ********************************************************************************
    private static final String TAG = Dashboard.class.getSimpleName();
    // DEF API
    String API = BuildConfig.API_KEY;
    //Check Tablet Or Not  ?
    private boolean isTablet;
    //Loader DEF
    private static final int FAVORITE_LOADER = 0;

    String popularURL;

    String topRatedURL;

    ArrayList<Movie> PopularList;

    ArrayList<Movie> TopRatedList;
    ///part  9 in lessons
    // Bind View Recycle View
    @BindView(R.id.recycled_movie_grid)

    RecyclerView movie_grid_recyclerView;

    // Bind View ProgressBar View

    @BindView(R.id.indeterminateBar)

    ProgressBar ProgressBar;



    private MovieAdapter movieadapter;

    // Sorted By  Popular  movie As a Defult ***************
    private String SortBy = CatchMovie.POPULAR;


    private static final String EXTRA_MOVIES = "EXTRA_MOVIES_ADDED";

    private static final String EXTRA_SORT_BY = "EXTRA_SORT_BY";




    //Override Functions
    //**********************************************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);
        // ButterKnife Bind Call ;
        ButterKnife.bind(this);
        // to Hide Progress bar*
        ProgressBar.setVisibility(View.INVISIBLE);
        // Redefine the recyclerView;
        // Number of Colume 2
        movie_grid_recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        //******************************************************
        // Redefine Adapter and but Defult and constractor on it
        movieadapter = new MovieAdapter(new ArrayList<Movie>(), this);
        movie_grid_recyclerView.setAdapter(movieadapter);
        //***************************************************

        //To make A Large Screen APp
        isTablet = findViewById(R.id.movie_detail_container) != null;
//*******************************************************

        if (savedInstanceState != null) {
            SortBy = savedInstanceState.getString(EXTRA_SORT_BY);
            if (savedInstanceState.containsKey(EXTRA_MOVIES)) {
                List<Movie> movies = savedInstanceState.getParcelableArrayList(EXTRA_MOVIES);
                movieadapter.add(movies);
                findViewById(R.id.indeterminateBar).setVisibility(View.GONE);
//To recive any Update Properties
                if (SortBy.equals(CatchMovie.FAVORITES)) {
                    getSupportLoaderManager().initLoader(FAVORITE_LOADER, null,this);
                }
            }
            update_empty();
        } else {
            //Fatch Movie Null ***********************************************
            if(NetworkUtils.networkStatus(Dashboard.this))  new CatchMovie().execute();
            else{
                // If fail Send This Massege  ***
                AlertDialog.Builder dialog = new AlertDialog.Builder(Dashboard.this);
                dialog.setTitle(getString(R.string.title_network_alert));
                dialog.setMessage(getString(R.string.message_network_alert));
                dialog.setCancelable(false);
                dialog.show();
            }
            //***********************************************
        }
    }

    @Override
    public void onBackPressed() {
        if (SortBy.equals(CatchMovie.FAVORITES)) {
            getSupportLoaderManager().destroyLoader(FAVORITE_LOADER);
        }
        SortBy = CatchMovie.POPULAR;
        Refresh(SortBy);
    }
    // If Start ***********
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume(){
        super.onResume();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Def Movies **
        ArrayList<Movie> movies = movieadapter.getMovies();
        if (movies != null && !movies.isEmpty()) outState.putParcelableArrayList(EXTRA_MOVIES, movies);

        outState.putString(EXTRA_SORT_BY, SortBy);

        if (!SortBy.equals(CatchMovie.FAVORITES))  getSupportLoaderManager().destroyLoader(FAVORITE_LOADER);

    }

    //sorted By  POPULAR , TOP_RATED or Fav
//****************************************************************************************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard, menu);

        switch (SortBy) {
            case CatchMovie.POPULAR:
                menu.findItem(R.id.sort_by_popular).setChecked(true);
                break;
            case CatchMovie.TOP_RATED:
                menu.findItem(R.id.sort_by_top_rated).setChecked(true);
                break;
            case CatchMovie.FAVORITES:
                menu.findItem(R.id.sort_by_favorites).setChecked(true);
                break;
        }
        return true;
    }
    //********************************************************************************************
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_top_rated:
                if (SortBy.equals(CatchMovie.FAVORITES)) {
                    getSupportLoaderManager().destroyLoader(FAVORITE_LOADER);
                }
                SortBy = CatchMovie.TOP_RATED;
                Refresh(SortBy);
                item.setChecked(true);
                break;
            case R.id.sort_by_popular:
                if (SortBy.equals(CatchMovie.FAVORITES)) {
                    getSupportLoaderManager().destroyLoader(FAVORITE_LOADER);
                }
                SortBy = CatchMovie.POPULAR;
                Refresh(SortBy);
                item.setChecked(true);
                break;
            case R.id.sort_by_favorites:
                SortBy = CatchMovie.FAVORITES;
                item.setChecked(true);
                Refresh(SortBy);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    ///

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        findViewById(R.id.indeterminateBar).setVisibility(View.VISIBLE);
        return new CursorLoader(this,
                MovieContract.MovieData.CONTENT_URI,
                MovieContract.MovieData.MOVIE_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<Cursor> loader, Cursor cursor) {
        movieadapter.addData(cursor);
        update_empty();
        findViewById(R.id.indeterminateBar).setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<Cursor> loader) {

    }



    //AsyncTask
    public class CatchMovie extends AsyncTask<Void,Void,Void> {

        public final static String POPULAR = "popular";
        public final static String TOP_RATED = "top_rated";
        public final static String FAVORITES = "favorites";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected Void doInBackground(Void... voids) {

            // toGet Data

            popularURL = "https://api.themoviedb.org/3/movie/popular?api_key="+API+"&language=en-US";
            topRatedURL = "https://api.themoviedb.org/3/movie/top_rated?api_key="+API+"&language=en-US";



            PopularList = new ArrayList<>();
            TopRatedList = new ArrayList<>();
            try {
                if(NetworkUtils.networkStatus(Dashboard.this)){
                    PopularList = NetworkUtils.fetchData(popularURL); // popular
                    TopRatedList = NetworkUtils.fetchData(topRatedURL); // top rated

                }else{
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Dashboard.this);
                    dialog.setTitle(getString(R.string.title_network_alert));
                    dialog.setMessage(getString(R.string.message_network_alert));
                    dialog.setCancelable(false);
                    dialog.show();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void  s) {
            super.onPostExecute(s);
            ProgressBar.setVisibility(View.INVISIBLE);
// Load Moveie and Update
            movieadapter = new MovieAdapter(new ArrayList<Movie>(),Dashboard.this);
            movieadapter.add(PopularList);
            movie_grid_recyclerView.setAdapter(movieadapter);
        }
    }

    private void update_empty() {
        if (movieadapter.getItemCount() == 0) {
            if (SortBy.equals(CatchMovie.FAVORITES)) {
                findViewById(R.id.empty_state).setVisibility(View.GONE);
                findViewById(R.id.empty_state_favorites_container).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.empty_state).setVisibility(View.VISIBLE);
                findViewById(R.id.empty_state_favorites_container).setVisibility(View.GONE);
            }
        } else {
            findViewById(R.id.empty_state).setVisibility(View.GONE);
            findViewById(R.id.empty_state_favorites_container).setVisibility(View.GONE);
        }
    }



    private void Refresh(String sort_by) {

        switch (sort_by){
            case CatchMovie.POPULAR:
                movieadapter = new MovieAdapter(new ArrayList<Movie>(),this);
                movieadapter.add(PopularList);
                movie_grid_recyclerView.setAdapter(movieadapter);
                break;
            case CatchMovie.TOP_RATED:
                movieadapter = new MovieAdapter(new ArrayList<Movie>(),this);
                movieadapter.add(TopRatedList);
                movie_grid_recyclerView.setAdapter(movieadapter);
                break;
            case CatchMovie.FAVORITES:
                getSupportLoaderManager().initLoader(FAVORITE_LOADER, null, this);
                break;
        }


    }

    public void send_details(Movie movie, int position) {
        if (isTablet) {

        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(MovieDetailFragment.ARG_MOVIE, movie);
            startActivity(intent);
        }
    }

}



