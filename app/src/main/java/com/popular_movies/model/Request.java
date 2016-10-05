/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.popular_movies.app.GlobalConstant;
import com.popular_movies.database.DataSourceMovie;
import com.popular_movies.database.DataSourceReview;
import com.popular_movies.database.DataSourceTrailer;
import com.popular_movies.parser.JSONParserGenre;
import com.popular_movies.parser.JSONParserReview;
import com.popular_movies.parser.JSONParserTrailer;

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

    public Request() {
    }

    /**
     * Fetches most popular movies
     * @return an array list of most popular movies
     */
    public static String mostPopularMovies() {
        Uri builtUri = Uri.parse(GlobalConstant.QUERY_POPULAR_MOVIES)
                .buildUpon().appendQueryParameter(GlobalConstant.API_KEY, GlobalConstant.C5CA40DED62975B80638B7357FD69E9)
                .build();

        // Request top rated movies
        String mostPopularMoviesJson = request(builtUri);

        return mostPopularMoviesJson;
    }

    /**
     * Fetches top rated movies
     *
     * @return json object of a list of top rated movies
     */
    public static String topRatedMovies() {
        Uri builtUri = Uri.parse(GlobalConstant.QUERY_TOP_RATED_MOVIES)
                .buildUpon().appendQueryParameter(GlobalConstant.API_KEY, GlobalConstant.C5CA40DED62975B80638B7357FD69E9)
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
    public static String favouriteMovies(String movieId) {
        Uri builtUri = Uri.parse(GlobalConstant.QUERY_SINGLE_MOVIE_QUERY + movieId)
                .buildUpon().appendQueryParameter(GlobalConstant.API_KEY, GlobalConstant.C5CA40DED62975B80638B7357FD69E9)
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
    public static String movieGenres() {
        Uri builtUri = Uri.parse(GlobalConstant.QUERY_GENRE)
                .buildUpon().appendQueryParameter(GlobalConstant.API_KEY, GlobalConstant.C5CA40DED62975B80638B7357FD69E9)
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
    public static String trailers(int movieId) {
        Uri builtUri = Uri.parse(GlobalConstant.HTTPS_API_THEMOVIEDB_ORG_3 + "/movie/" + movieId + "/videos")
                .buildUpon().appendQueryParameter(GlobalConstant.API_KEY, GlobalConstant.C5CA40DED62975B80638B7357FD69E9)
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
    public static String reviews(int movieId) {
        Uri builtUri = Uri.parse(GlobalConstant.HTTPS_API_THEMOVIEDB_ORG_3 + "/movie/" + movieId + "/reviews")
                .buildUpon().appendQueryParameter(GlobalConstant.API_KEY, GlobalConstant.C5CA40DED62975B80638B7357FD69E9)
                .build();

        // Request movie reviews of the specified movie
        String movieReviewsJson = request(builtUri);

        return movieReviewsJson;
    }

    public static void syncGenreNames(Context context) {

        String movieGenresJson = movieGenres();

        // Parse the response to a movie objects array list
        ArrayList<Genre> genres = JSONParserGenre.parseFeed(movieGenresJson);

        if (genres != null && !genres.isEmpty()) {
            Gson gson = new Gson();

            // Initialize the shared preference object
            SharedPreferences prefs = PreferenceManager.
                    getDefaultSharedPreferences(context);

            /**
             * This code persists the names of the movie genres onto a shared preference.
             * The genre preference is always overwritten, in case a new genre is added
             **/
            SharedPreferences.Editor prefsEditor = prefs.edit();
            String favouriteMovieListJson = gson.toJson(genres);
            prefsEditor.putString(GlobalConstant.GENRES, favouriteMovieListJson);
            prefsEditor.commit();
        }
    }

//    private static void syncMostPopularMovies(Context context) {
//
//        String mostPopularMoviesJson = mostPopularMovies();
//
//        // Parse the response to a movie objects array list
//        ArrayList<Movie> movieArrayList = JSONParserMovie.parseFeed(mostPopularMoviesJson);
//
//        try {
//            if (movieArrayList != null && !movieArrayList.isEmpty()) {
//
//                DataSourceMovie dataSourceMovie = new DataSourceMovie(context);
//                dataSourceMovie.open();
//
//                for (int i = 0; i < movieArrayList.size(); i++) {
//
//                    // Get the names of the movie genres
//                    String genreNames = Utility.getGenreNames(context, movieArrayList.get(i).getGenre());
//                    // Change the names of the movie genres
//                    movieArrayList.get(i).setGenre(genreNames);
//                    // Set the movie category
//                    movieArrayList.get(i).setCategory(GlobalConstant.MOST_POPULAR);
//
//                    int movieId = Integer.parseInt(movieArrayList.get(i).getId());
//
//                    syncTrailer(context, movieId);
//                    syncReview(context, movieId);
//
//                    dataSourceMovie.addMovie(movieArrayList.get(i));
//                }
//
//                dataSourceMovie.close();
//            }
//        } catch (JSONException e) {
//            e.getMessage();
//        }
//    }

//    private static void syncTopRatedMovies(Context context) {
//
//        try {
//            String topRatedMoviesJson = topRatedMovies();
//
//            // Parse the response to a movie objects array list
//            ArrayList<Movie> movieArrayList = JSONParserMovie.parseFeed(topRatedMoviesJson);
//
//            if (movieArrayList != null && !movieArrayList.isEmpty()) {
//
//                DataSourceMovie dataSourceMovie = new DataSourceMovie(context);
//                dataSourceMovie.open();
//
//                for (int i = 0; i < movieArrayList.size(); i++) {
//
//                    // Get the names of the movie genres
//                    String genreNames = Utility.getGenreNames(context, movieArrayList.get(i).getGenre());
//                    // Change the names of the movie genres
//                    movieArrayList.get(i).setGenre(genreNames);
//                    // Set the movie category
//                    movieArrayList.get(i).setCategory(GlobalConstant.TOP_RATED);
//
//                    int movieId = Integer.parseInt(movieArrayList.get(i).getId());
//
//                    syncTrailer(context, movieId);
//                    syncReview(context, movieId);
//
//                    dataSourceMovie.addMovie(movieArrayList.get(i));
//                }
//
//                dataSourceMovie.close();
//
//            }
//        } catch (JSONException e) {
//            e.getMessage();
//        }
//    }

    private static void syncTrailer (Context context, int movieId) {

        String fetchMovieTrailersJson = trailers(movieId);

        // Parse the response to a trailer object array list
        ArrayList<Trailer> trailerArrayList = JSONParserTrailer.parseFeed(fetchMovieTrailersJson);

        if (trailerArrayList != null && !trailerArrayList.isEmpty()) {
            DataSourceTrailer dataSourceTrailer = new DataSourceTrailer(context);
            dataSourceTrailer.open();

            for (int i = 0; i < trailerArrayList.size(); i++) {
                // Add the trailer to the local SQLite database
                dataSourceTrailer.addTrailer(trailerArrayList.get(i));
            }
            dataSourceTrailer.close();
        }
    }

    private static void syncReview (Context context, int movieId) {

        String fetchMovieReviewsJson = reviews(movieId);

        // Parse the response to a review object array list
        ArrayList<Review> reviewArrayList = JSONParserReview.parseFeed(fetchMovieReviewsJson);
        if (reviewArrayList != null && !reviewArrayList.isEmpty()) {
            DataSourceReview dataSourceReview = new DataSourceReview(context);
            dataSourceReview.open();

            for (int i = 0; i < reviewArrayList.size(); i++) {

                String movieIdString = String.valueOf(movieId);

                // Set the review's movie id
                reviewArrayList.get(i).setMovieId(movieIdString);

                // Add the review to the local SQLite database
                dataSourceReview.addReview(reviewArrayList.get(i));
            }

            dataSourceReview.close();
        }
    }

    public static void sync (Context context) {
        DataSourceMovie dataSourceMovie = new DataSourceMovie(context);
        dataSourceMovie.open();
        dataSourceMovie.truncate();
        syncGenreNames(context);
//        syncMostPopularMovies(context);
//        syncTopRatedMovies(context);
    }

//    public static ArrayList<Movie> movies(Context context) {
//
//        ArrayList<Movie> movieArrayList;
//        String displayCategory = Utility.getSortOrderPref(context);
//
//        switch (displayCategory) {
//            case GlobalConstant.FAVOURITE:
//
//                // Get Favourite movies
//                FavouriteMoviesHandler favouriteMoviesHandler = new FavouriteMoviesHandler(context);
//                movieArrayList = favouriteMoviesHandler.getMovieList();
////                if (movieArrayList != null && !movieArrayList.isEmpty()) {
//
////                    StringBuilder selection = new StringBuilder();
////                    selection.append("movie_id IN(");
////                    String[] selectionArgs = new String[movieIdArrayList.size()];
////
////                    for (int i = 0; i < movieIdArrayList.size(); i++) {
////                        if (i != (movieIdArrayList.size() - 1)) {
////                            selection.append("?, ");
////                        } else {
////                            selection.append("?)");
////                        }
////
////                        selectionArgs[i] = movieIdArrayList.get(i).getId();
////                    }
////
////                    movieArrayList.clear();
////                    movieArrayList = dataSourceMovie.getMovies(selection.toString(), selectionArgs);
////                }
//                break;
//            default:
//                DataSourceMovie dataSourceMovie = new DataSourceMovie(context);
//                dataSourceMovie.open();
//
//                String[] movieCategory = { displayCategory };
//                movieArrayList = dataSourceMovie.getMovies("movie_category=?", movieCategory);
//
//                dataSourceMovie.close();
//                break;
//        }
//
//        return movieArrayList;
//    }

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
