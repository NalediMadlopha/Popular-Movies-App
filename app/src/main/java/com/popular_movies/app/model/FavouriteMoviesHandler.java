package com.popular_movies.app.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.popular_movies.app.GlobalConstant;

import java.util.List;

/**
 * Created by Naledi Madlopha on 2016/08/18.
 */
public class FavouriteMoviesHandler {

    private List<String> sFavouriteMovieList;
    private SharedPreferences mPrefs;
    private Gson mGson = new Gson();

    // TODO: Add method description
    public FavouriteMoviesHandler(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        sFavouriteMovieList = getMovieList();
    }

    // TODO: Add method description
    public List<String> getMovieList() {
        // -- Check if the movie list is contained in the shared preferences
        if (mPrefs.contains(GlobalConstant.sFAVOURITE_MOVIE_LIST)) {
            String favouriteMovieListJson = mPrefs.getString(GlobalConstant.sFAVOURITE_MOVIE_LIST, "");

            // --- Check if the list in shared preferences is not null
            if (favouriteMovieListJson != null) {
                sFavouriteMovieList = mGson.fromJson(favouriteMovieListJson, List.class);
            } else {
                return null; // Return null when the are no movie list in the shared preferences
            }
        }
        return sFavouriteMovieList;
    }

    // Check if the movie has not already been added as a favourite
    public boolean isFavourite(String movieId) {
        // Check if the movie ID is not contained in the movie list
        if (sFavouriteMovieList.contains(movieId)) {
            return true;
        } else {
            return false;
        }
    }

    // Add the movie to the favourite movie list
    public void addMovie(String movieId) {
        sFavouriteMovieList.add(movieId);

        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        String favouriteMovieListJson = mGson.toJson(sFavouriteMovieList);
        prefsEditor.putString(GlobalConstant.sFAVOURITE_MOVIE_LIST, favouriteMovieListJson);
        prefsEditor.commit();
    }

    // Remove the movie to the favourite movie list
    public void removeMovie(String movieId) {
        sFavouriteMovieList.remove(movieId);

        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        String favouriteMovieListJson = mGson.toJson(sFavouriteMovieList);
        prefsEditor.putString(GlobalConstant.sFAVOURITE_MOVIE_LIST, favouriteMovieListJson);
        prefsEditor.commit();
    }
}