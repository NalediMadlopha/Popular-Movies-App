/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.model;

import android.content.Context;
import android.net.Uri;

import com.popular_movies.app.GlobalConstant;
import com.popular_movies.database.MovieDbHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Handles the API calls
 */
public class Request {

    public static ArrayList<Movie> mMovies;
    private ArrayList<Genre> mGenres = new ArrayList<>();
    private ArrayList<Trailer> mTrailers = new ArrayList<>();
    private ArrayList<Review> mReviews = new ArrayList<>();
    private Context mContext;
    private MovieDbHelper mMovieDbHelper;

    public Request() {

//        if (mMovieDbHelper == null) {
//            mMovieDbHelper = new MovieDbHelper(context);
//        }
    }

    /**
     * Fetches popular movies
     *
     * @return json object of a list of popular movies
     */
    public void databaseSync(String movieCategory) {

//       String query = (movieCategory.equals(GlobalConstant.MOST_POPULAR))
//               ? GlobalConstant.QUERY_POPULAR_MOVIES : GlobalConstant.QUERY_TOP_RATED_MOVIES;
//
//       StringRequest request = new StringRequest(com.android.volley.Request.Method.GET,
//               query, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                // Parse the response to a movie objects array list
//                ArrayList<Movie> movies = JSONParserMovie.parseFeed(response);
//
//                for (int i = 0; i < movies.size(); i++) {
//                    mMovieDbHelper.putMovie(movies.get(i), "");
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.getMessage();
//            }
//        });
//        Volley.newRequestQueue(mContext).add(request);
    }

    /**
     * Fetches most popular movies
     * @return an array list of most popular movies
     */
//    public ArrayList<Movie> mostPopularMovies() {
//        return mMovieDbHelper.getMovies(GlobalConstant.MOST_POPULAR);
//    }

    /**
     * Fetches top rated movies
     *
     * @return json object of a list of top rated movies
     */
    public static String fetchTopRatedMovies() {
        Uri builtUri = Uri.parse(GlobalConstant.HTTPS_API_THEMOVIEDB_ORG_3 + GlobalConstant.MOVIE_TOP_RATED)
                .buildUpon().appendQueryParameter(GlobalConstant.API_KEY_PARAMETER, GlobalConstant.C5CA40DED62975B80638B7357FD69E9)
                .build();

        // Request top rated movies
        String topRatedMoviesJson = request(builtUri);

        return topRatedMoviesJson;
    }

    /**
     * Fetches the user's favourite movies
     *
     * @param movieId is the unique movie id
     * @return json object of a list of the user's favourite movies
     */
    public static String fetchFavouriteMovies(String movieId) {
        Uri builtUri = Uri.parse(GlobalConstant.HTTPS_API_THEMOVIEDB_ORG_3 + GlobalConstant.SINGLE_MOVIE_QUERY + movieId)
                .buildUpon().appendQueryParameter(GlobalConstant.API_KEY_PARAMETER, GlobalConstant.C5CA40DED62975B80638B7357FD69E9)
                .build();

        // Request a specific movie
        String moviesJson = request(builtUri);

        return moviesJson;
    }

    /**
     * Fetches all the movie genres
     *
     * @return json object of a list of movie genres
     */
    public static String fetchMovieGenres() {
        Uri builtUri = Uri.parse(GlobalConstant.HTTPS_API_THEMOVIEDB_ORG_3 + GlobalConstant.GENRE_MOVIE_LIST)
                .buildUpon().appendQueryParameter(GlobalConstant.API_KEY_PARAMETER, GlobalConstant.C5CA40DED62975B80638B7357FD69E9)
                .build();

        // Request movie genres
        String movieGenresJson = request(builtUri);

        return movieGenresJson;
    }

    /**
     * Fetches the movie's trailers
     *
     * @param movieId is the unique movie id
     * @return json object of a list of movie genres
     */
    public static String fetchMovieTrailers(int movieId) {
        Uri builtUri = Uri.parse(GlobalConstant.HTTPS_API_THEMOVIEDB_ORG_3 + "/movie/" + movieId + "/videos")
                .buildUpon().appendQueryParameter(GlobalConstant.API_KEY_PARAMETER, GlobalConstant.C5CA40DED62975B80638B7357FD69E9)
                .build();

        // Request movie trailers of the specified movie
        String movieTrailersJson = request(builtUri);

        return movieTrailersJson;
    }

    /**
     * Fetches the movie's reviews
     *
     * @param movieId is the unique movie id
     * @return json object of a list of movie reviews
     */
    public static String fetchMovieReviews(int movieId) {
        Uri builtUri = Uri.parse(GlobalConstant.HTTPS_API_THEMOVIEDB_ORG_3 + "/movie/" + movieId + "/reviews")
                .buildUpon().appendQueryParameter(GlobalConstant.API_KEY_PARAMETER, GlobalConstant.C5CA40DED62975B80638B7357FD69E9)
                .build();

        // Request movie reviews of the specified movie
        String movieReviewsJson = request(builtUri);

        return movieReviewsJson;
    }

    /**
     * Makes a call to the API
     *
     * @param builtUri the API url
     * @return json string of the result
     */
    private static String request(Uri builtUri) {
        /*
         * These two variables need to be declared outside the try/catch
         * so that they can be closed in the finally block.
         */
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonString = null;

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
            jsonString = buffer.toString(); // JSON string from the buffer
        }catch (MalformedURLException e) {
            e.getMessage();
        }  catch (IOException e) {
            e.getMessage();
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
                    e.getMessage();
                }
            }

            return jsonString;
        }
    }
}
