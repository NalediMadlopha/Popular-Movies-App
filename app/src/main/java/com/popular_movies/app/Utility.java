/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.popular_movies.model.FavouriteMoviesHandler;
import com.popular_movies.model.Genre;
import com.popular_movies.model.Movie;
import com.popular_movies.model.ResponseGenres;
import com.popular_movies.model.ResponseMovies;
import com.popular_movies.rest.ApiClient;
import com.popular_movies.rest.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Provides general methods
 */
public class Utility {

    /** Checks if there is internet connection */
    public static boolean isOnline(Context context) {

        // Initialize the connectivity manager
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // Check if the network is connected or connecting
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    /**
     * Converts the date format from "y-MM-dd" to "dd MMM y"
     *
     * @param strDate a string date
     * @return converted format date string
     */
    public static String convertDate(String strDate) {
        String convertedDate = null;

        try {
            // Create the date's current format
            SimpleDateFormat currentFormat = new SimpleDateFormat("y-MM-dd");

            // Create a new date
            Date date = currentFormat.parse(strDate);

            // Create a new format
            SimpleDateFormat newFormat = new SimpleDateFormat("dd MMM y");

            // Format the date using the new format
            convertedDate = newFormat.format(date);
        } catch (ParseException e) {
            convertedDate = strDate;
        } finally {
            // Return the converted date
            return convertedDate;
        }
    }

    /**
     * Gets the sort order preference
     *
     * @param context which the method is called from
     * @return string of the sort order value
     */
    public static String getMovieCategoryPref(Context context) {
        String sortOrder;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences.contains(GlobalConstant.MOVIE_CATEGORY)) {
            sortOrder = sharedPreferences.getString(GlobalConstant.MOVIE_CATEGORY, "");
        } else {
            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
            prefsEditor.putString(GlobalConstant.MOVIE_CATEGORY, GlobalConstant.MOST_POPULAR);
            prefsEditor.commit();

            sortOrder = sharedPreferences.getString(GlobalConstant.MOVIE_CATEGORY, "");
        }

        return sortOrder;
    }

    /**
     * Gets the genre preference
     *
     * @param context which the method is called from
     * @return ArrayList of genre objects
     */
    public static ArrayList<Genre> getGenrePref(Context context) {
        ArrayList<Genre> genreArrayList = new ArrayList<>();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPrefs.contains(GlobalConstant.GENRES)) {
            String genreListJson = sharedPrefs.getString(GlobalConstant.GENRES, "");

            try {
                JSONArray jsonArray = new JSONArray(genreListJson);
                Gson gson = new Gson();

                for (int i = 0; i < jsonArray.length(); i++) {
                    Genre genre = gson.fromJson(jsonArray.getJSONObject(i).toString(), Genre.class);
                    genreArrayList.add(genre);
                }
            } catch (JSONException e) {
                e.getMessage();
            }
        }
        return genreArrayList;
    }

    /**
     * Converts a list of genre ids to a list of genre names
     *
     * @param genreIds a list of genre ids
     * @return a string list of genre names
     */
    public static String getGenreNames(Context context, List genreIds) {
        String genreNames = "";

        ArrayList<Genre> genreArrayList = getGenrePref(context);

            for (int i = 0; i < genreIds.size(); i++) {
                int jsonArrayGenreId = (int) genreIds.get(i);

                for (int ii = 0; ii < genreArrayList.size(); ii++) {
                    int arrayListGenreId = Integer.parseInt(genreArrayList.get(ii).getId());

                    if (jsonArrayGenreId == arrayListGenreId) {
                        // Build a genre list, separated by commas
                        genreNames += genreArrayList.get(ii).getName() + ", ";
                        break;
                    }
                }
            }

            // Check if the length of the genre name is greater than 0
            if (genreNames.length() > 0) {
                // Remove the last comma from the list of genre names
                genreNames = genreNames.substring(0, (genreNames.length() - 2));
            } else {
                // Set this when the movie is not categorized
                genreNames = "No genre listed";
            }

        return genreNames;
    }

    /**
     * Sets ListView height dynamically based on the height of the items.
     * This code was taken from the internet. SOURCE: http://stackoverflow.com/questions/1778485/android-listview-display-all-available-items-without-scroll-with-static-header/35955121#35955121
     * @param listView to be resized
     * @return true if the listView is successfully resized, false otherwise
     */
    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;
        } else {
            return false;
        }
    }
}
