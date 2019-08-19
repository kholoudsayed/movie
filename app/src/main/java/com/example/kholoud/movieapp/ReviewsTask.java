package com.example.kholoud.movieapp;

import android.os.AsyncTask;
import android.util.Log;

import com.example.kholoud.movieapp.BuildConfig;
import com.example.kholoud.movieapp.Reviews;
import com.example.kholoud.movieapp.Trailers;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReviewsTask extends AsyncTask<Long, Void, List<Reviews>> {

    @SuppressWarnings("unused")
    public static String LOG_TAG = ReviewsTask.class.getSimpleName();
    private final Listener listener;

    /**
     * Interface definition for a callback to be invoked when reviews are loaded.
     */
    interface Listener {
        void on_reviews_loaded(List<Reviews> reviews);
    }
    public ReviewsTask(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected List<Reviews> doInBackground(Long... params) {
        List<Reviews> reviews = new ArrayList<>();
        if (params.length == 0) {


            return null;
        }
        long movieId = params[0];

        try {

            URL url;
            url = new URL("http://api.themoviedb.org/3/movie/"+movieId +"/reviews?api_key="+BuildConfig.API_KEY);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            String results = IOUtils.toString(inputStream);
            parseJson(results,reviews);
            inputStream.close();
            return reviews;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
//*****************************************************


    public static void parseJson(String data, List<Reviews> list){
        Reviews reviews = new Reviews();

        try {
            JSONObject mainObject = new JSONObject(data);
            Log.v(LOG_TAG,mainObject.toString());
            JSONArray resArray = mainObject.getJSONArray("results");
            int i=0;
            while (i<resArray.length())
            {
                JSONObject jsonObject = resArray.getJSONObject(i);
                reviews.setId(jsonObject.getString("id"));
                reviews.setAuthor(jsonObject.getString("author"));
                reviews.setUrl(jsonObject.getString("url"));
                reviews.setContent(jsonObject.getString("content"));
                list.add(reviews);
            i++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error  JSON Parsing");
        }

    }

    @Override
    protected void onPostExecute(List<Reviews> reviews) {
        if (reviews != null) listener.on_reviews_loaded(reviews);
        else {
            listener.on_reviews_loaded(new ArrayList<Reviews>());
        }
    }
}