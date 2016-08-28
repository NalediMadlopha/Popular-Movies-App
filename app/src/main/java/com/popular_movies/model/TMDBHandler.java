package com.popular_movies.model;

import android.net.Uri;
import android.util.Log;

import com.popular_movies.app.GlobalConstant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Naledi Madlopha on 2016/08/13.
 */
public class TMDBHandler {

    private final static String LOG_TAG = TMDBHandler.class.getSimpleName();

        public static String fetchPopularMovies() {
        Uri builtUri = Uri.parse(GlobalConstant.sBASE_URL + GlobalConstant.sPOPULAR_MOVIES_QUERY)
                .buildUpon().appendQueryParameter(GlobalConstant.sAPI_KEY_PARAM, GlobalConstant.sAPI_KEY)
                .build();

        // Request popular movies
        String popularMoviesJson = request(builtUri);

        return popularMoviesJson;
    }

    // TODO: Add method description
    public static String fetchTopRatedMovies() {
        Uri builtUri = Uri.parse(GlobalConstant.sBASE_URL + GlobalConstant.sTOP_RATED_MOVIES_QUERY)
                .buildUpon().appendQueryParameter(GlobalConstant.sAPI_KEY_PARAM, GlobalConstant.sAPI_KEY)
                .build();

        // Request top rated movies
        String topRatedMoviesJson = request(builtUri);

        return topRatedMoviesJson;
    }

    // TODO: Add method description
    public static String fetchFavouriteMovies(String movieId) {
        Uri builtUri = Uri.parse(GlobalConstant.sBASE_URL + GlobalConstant.sSINGE_MOVIE_QUERY + movieId)
                .buildUpon().appendQueryParameter(GlobalConstant.sAPI_KEY_PARAM, GlobalConstant.sAPI_KEY)
                .build();

        // Request a specific movie
        String moviesJson = request(builtUri);

        return moviesJson;
    }

    // TODO: Add method description
    public static String fetchMovieGenres() {
        Uri builtUri = Uri.parse(GlobalConstant.sBASE_URL + GlobalConstant.sMOVIE_GENRE_QUERY)
                .buildUpon().appendQueryParameter(GlobalConstant.sAPI_KEY_PARAM, GlobalConstant.sAPI_KEY)
                .build();

        // Request movie genres
        String movieGenresJson = request(builtUri);

        return movieGenresJson;
    }

    // TODO: Add method description
    public static String fetchMovieTrailers(int movieId) {
        Uri builtUri = Uri.parse(GlobalConstant.sBASE_URL + "/movie/" + movieId + "/videos")
                .buildUpon().appendQueryParameter(GlobalConstant.sAPI_KEY_PARAM, GlobalConstant.sAPI_KEY)
                .build();

        // Request movie trailers of the specified movie
        String movieTrailersJson = request(builtUri);

        return movieTrailersJson;
    }

    // TODO: Add method description
    public static String fetchMovieReviews(int movieId) {
        Uri builtUri = Uri.parse(GlobalConstant.sBASE_URL + "/movie/" + movieId + "/reviews")
                .buildUpon().appendQueryParameter(GlobalConstant.sAPI_KEY_PARAM, GlobalConstant.sAPI_KEY)
                .build();

        // Request movie reviews of the specified movie
        String movieReviewsJson = request(builtUri);

        return movieReviewsJson;
    }

    // TODO: Add method description
    private static String request(Uri builtUri) {
        /*
         * These two variables need to be declared outside the try/catch
         * so that they can be closed in the finally block.
         */
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(builtUri.toString());

            // Create the request to The Movie DB and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a string
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            // Check if the buffer is not empty
            if (buffer.length() == 0) {
                return null;
            }
            String jsonString = buffer.toString(); // JSON string from the buffer

            return jsonString;
        }catch (MalformedURLException e) {
            Log.e(LOG_TAG, e.getMessage());
            return null;
        }  catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
            return null;
        } finally {
            // Check if the connection exists
            if (urlConnection != null) {
                urlConnection.disconnect(); // Disconnect the connection
            }

            // Check if the reader exists
            if (reader != null) {
                try {
                    reader.close(); // Close the reader
                } catch (final IOException e) {
                    Log.e(LOG_TAG, e.getMessage());
                }
            }
        }
    }
}
