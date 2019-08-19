package com.example.kholoud.movieapp;

import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.kholoud.movieapp.BuildConfig;
import com.example.kholoud.movieapp.Dashboard;
import com.example.kholoud.movieapp.Trailers;
import com.example.kholoud.movieapp.R;
import com.example.kholoud.movieapp.NetworkUtils;
import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TrailersTask extends AsyncTask<Long, Void, List<Trailers>> {
//Def *****************************
    public static String LOG_TAG = TrailersTask.class.getSimpleName();

    private final Listener listener;

    public TrailersTask(Listener listener) {
        this.listener = listener;
    }

    interface Listener {
        void onLoadFinished(List<Trailers> trailers);
    }

    @Override
    protected List<Trailers> doInBackground(Long... params) {
        List<Trailers> trailers = new ArrayList<>();
        if (params.length == 0) {
            return null;
        }
        long movieId = params[0];
            try {

                URL url;
                url = new URL("http://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=" + BuildConfig.API_KEY);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                String results = IOUtils.toString(inputStream);
                parseJson(results,trailers);
                inputStream.close();
                return trailers;
            } catch (IOException e) {
                e.printStackTrace();
            }


        return null;
    }

    public static void parseJson(String data, List<Trailers> list){
        Trailers trailers = new Trailers();

        try {
            JSONObject mainObject = new JSONObject(data);
            JSONArray resArray = mainObject.getJSONArray("results");
            int i=0;
            while(i< resArray.length()){
                JSONObject jsonObject = resArray.getJSONObject(i);
                trailers.setId(jsonObject.getString("id"));
                trailers.setKey(jsonObject.getString("key"));
                trailers.setName(jsonObject.getString("name"));
                trailers.setSite(jsonObject.getString("site"));
                trailers.setSize(jsonObject.getString("size"));
                list.add(trailers);
                i++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error occurred during JSON Parsing");
        }

    }

    @Override
    protected void onPostExecute(List<Trailers> trailers) {
        if (trailers != null) listener.onLoadFinished(trailers);
        else listener.onLoadFinished(new ArrayList<Trailers>());

    }


}
