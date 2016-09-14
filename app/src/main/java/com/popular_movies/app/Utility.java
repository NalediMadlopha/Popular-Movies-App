package com.popular_movies.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Naledi Madlopha on 2016/08/02.
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
            convertedDate = "No date was found";
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
    public static String getSortOrderPref(Context context) {
        String sortOrder = null;

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPrefs.contains("sort_order")) {
            sortOrder = sharedPrefs.getString("sort_order", "");
        }

        return sortOrder;
    }

    public static String requestQuery(Context context) {
        String requestQuery = null;
        String sortOrder = getSortOrderPref(context);

        switch (sortOrder) {
                case "Most Popular": // In case the sort order is set to popular mMovieList
                    requestQuery = GlobalConstant.POPULAR_MOVIES_QUERY;
                    break;
                case "Top Rated": // In case the sort order is set to top rated mMovieList
                    requestQuery = GlobalConstant.TOP_RATED_MOVIES_QUERY;
                    break;
        }

        return requestQuery;
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
