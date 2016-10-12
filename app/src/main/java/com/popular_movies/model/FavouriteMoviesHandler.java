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

    private static Gson sGson = new Gson();

    public static ArrayList<Movie> getMovieList(Context context) {
        ArrayList<Movie> movies = new ArrayList<>();
        ArrayList<String> localMovies = getLocalMovies(context);

        if (localMovies != null) {
            for (String movieJson : localMovies) {
                Movie movie = sGson.fromJson(movieJson, Movie.class);
                movies.add(movie);
            }
        }

        return movies;
    }

    private static ArrayList<String> getLocalMovies(Context context) {
        ArrayList<String> movieJsonObjects = null;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        // Check if the movie list is contained in the shared preferences
        if (sharedPreferences.contains(GlobalConstant.FAVOURITE_MOVIES)) {
            String favouriteMovies = sharedPreferences.getString(GlobalConstant.FAVOURITE_MOVIES, "");
            movieJsonObjects = sGson.fromJson(favouriteMovies, ArrayList.class);
        }

        return movieJsonObjects;
    }

    // Check if the movie has not already been added as a favourite
    public static boolean isFavourite(Context context, Movie movie) {
        ArrayList<String> favouriteMovies = getLocalMovies(context);
        String movieJson = sGson.toJson(movie);

        if (favouriteMovies != null) {
            if (favouriteMovies.contains(movieJson)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    // Add the movie to the favourite movie list
    public static void addMovie(Context context, Movie movie) {
        ArrayList<String> favouriteMovies = getLocalMovies(context);
        String movieJson = sGson.toJson(movie);

        if (favouriteMovies != null) {
            favouriteMovies.add(movieJson);
        } else {
            favouriteMovies = new ArrayList<>();
            favouriteMovies.add(movieJson);
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        String favouriteMoviesJson = sGson.toJson(favouriteMovies);
        prefsEditor.putString(GlobalConstant.FAVOURITE_MOVIES, favouriteMoviesJson);
        prefsEditor.commit();
    }

    // Remove the movie to the favourite movie list
    public static void removeMovie(Context context, Movie movie) {

        ArrayList<String> favouriteMovies = getLocalMovies(context);
        String movieJson = sGson.toJson(movie);
        favouriteMovies.remove(movieJson);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        String favouriteMoviesJson = sGson.toJson(favouriteMovies);
        prefsEditor.putString(GlobalConstant.FAVOURITE_MOVIES, favouriteMoviesJson);
        prefsEditor.commit();
    }
}