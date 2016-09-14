/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.popular_movies.app.GlobalConstant;

import java.util.ArrayList;

/**
 * Provides a favourite movies handler
 */
public class FavouriteMoviesHandler {

    private ArrayList<Movie> mFavouriteMovieList = new ArrayList<>();
    private SharedPreferences mPrefs;
    private Gson mGson = new Gson();

    /**
     * Constructor, gets the default shared preferences and gets the movie list
     *
     * @param context which the method is called from
     */
    public FavouriteMoviesHandler(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        mFavouriteMovieList = getMovieList();
    }

    /**
     * Gets a list of movie json objects
     *
     * @return list of movie json objects
     */
    public ArrayList<Movie> getMovieList() {
        // -- Check if the movie list is contained in the shared preferences
        if (mPrefs.contains(GlobalConstant.FAVOURITE_MOVIE_LIST)) {
            String favouriteMovieListJson = mPrefs.getString(GlobalConstant.FAVOURITE_MOVIE_LIST, "");

            // --- Check if the list in shared preferences is not null
            if (favouriteMovieListJson != null) {
                mFavouriteMovieList = mGson.fromJson(favouriteMovieListJson, ArrayList.class);
            }
        }
        return mFavouriteMovieList;
    }

    // Check if the movie has not already been added as a favourite
    public boolean isFavourite(Movie movie) {
        // if the movie ID is contained in the movie list
        if (mFavouriteMovieList.contains(movie)) {
            return true;
        } else {
            return false;
        }
    }

    // Add the movie to the favourite movie list
    public void addMovie(Movie movie) {
        mFavouriteMovieList.add(movie);

        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        String favouriteMovieListJson = mGson.toJson(mFavouriteMovieList);
        prefsEditor.putString(GlobalConstant.FAVOURITE_MOVIE_LIST, favouriteMovieListJson);
        prefsEditor.commit();
    }

    // Remove the movie to the favourite movie list
    public void removeMovie(Movie movie) {
        mFavouriteMovieList.remove(movie);

        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        String favouriteMovieListJson = mGson.toJson(mFavouriteMovieList);
        prefsEditor.putString(GlobalConstant.FAVOURITE_MOVIE_LIST, favouriteMovieListJson);
        prefsEditor.commit();
    }
}