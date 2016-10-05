/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.parser;

import android.support.annotation.Nullable;

import com.popular_movies.app.GlobalConstant;
import com.popular_movies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Provides a list of movie objects by parsing a json string
 */
public class JSONParserMovie {

    /**
     * Parse the feed of a json string to a movie object
     *
     * @param content is a json object string
     * @return movie object
     */
    @Nullable
    public static ArrayList<Movie> parseFeed(String content) {

        ArrayList<Movie> movies = new ArrayList<>();

        try {
            // Get a json object from the json string content
            JSONObject moviesJson = new JSONObject(content);
            // Get a json array of movies
            JSONArray moviesJsonArray = moviesJson.getJSONArray(GlobalConstant.RESULTS);

//            for (int i = 0; i < moviesJsonArray.length(); i++) {
//                Movie movie = new Movie();
//                JSONObject movieJson = moviesJsonArray.getJSONObject(i);
//
//                // Set the movie's Id
//                movie.setId(movieJson.getString(GlobalConstant.ID));
//                // Set the movie's poster
//                movie.setPosterPath(movieJson.getString(GlobalConstant.POSTER_PATH));
//
////                movie.setPosterBitmap(posterBitmap);
//
//                // Set the movie's original title
//                movie.setOriginalTitle(movieJson.getString(GlobalConstant.ORIGINAL_TITLE));
//                // Set the movie's title
//                movie.setTitle(movieJson.getString(GlobalConstant.TITLE));
//                // Set the movie's overall description
//                movie.setOverall(movieJson.getString(GlobalConstant.OVERVIEW));
//
//                // Get the genres
//                JSONArray genreArray = movieJson.getJSONArray(GlobalConstant.GENRE_IDS);
//
//                // Set the movie's genre
////                movie.setGenre(genreArray.toString());
//                // Set the movie's release date
//                movie.setReleaseDate(movieJson.getString(GlobalConstant.RELEASE_DATE));
//                // Set the movie's backdrop path
//                movie.setBackDropPath(movieJson.getString(GlobalConstant.BACKDROP_PATH));
//                // Set the movie's popularity
//                movie.setPopularity(movieJson.getString(GlobalConstant.POPULARITY));
//                // Set the movie's vote count
//                movie.setVoteCount(movieJson.getString(GlobalConstant.VOTE_COUNT));
//                // Set the movie's vote average
//                movie.setVoteAverage(movieJson.getString(GlobalConstant.VOTE_AVERAGE));
//
//                // Add the movie object to an array of movies
//                movies.add(movie);
//            }
        } catch (JSONException e) {
            e.getMessage();
        } finally {
            return movies;
        }
    }

    /**
     * Parse a single feed of a json string to movie objects
     *
     * @param content is a json object string
     * @return a movie object
     */
//    @Nullable
//    public static Movie parseSingleFeed(String content) {
//        Movie movie = new Movie();
//
//        try {
//            JSONObject movieJson = new JSONObject(content);
//
//            // Set the movie's Id
//            movie.setId(movieJson.getString(GlobalConstant.ID));
//            // Set the movie's poster
//            movie.setPosterPath(movieJson.getString(GlobalConstant.POSTER_PATH));
//            // Set the movie's original title
//            movie.setOriginalTitle(movieJson.getString(GlobalConstant.ORIGINAL_TITLE));
//            // Set the movie's title
//            movie.setTitle(movieJson.getString(GlobalConstant.TITLE));
//            // Set the movie's overall description
//            movie.setOverall(movieJson.getString(GlobalConstant.OVERVIEW));
//
//            // Get the genres
//            JSONArray genreArray = movieJson.getJSONArray(GlobalConstant.GENRES);
//
//            // Set the movie's genre
////            movie.setGenre(genreArray.toString());
//            // Set the movie's release date
//            movie.setReleaseDate(movieJson.getString(GlobalConstant.RELEASE_DATE));
//            // Set the movie's backdrop path
//            movie.setBackDropPath(movieJson.getString(GlobalConstant.BACKDROP_PATH));
//            // Set the movie's popularity
//            movie.setPopularity(movieJson.getString(GlobalConstant.POPULARITY));
//            // Set the movie's vote count
//            movie.setVoteCount(movieJson.getString(GlobalConstant.VOTE_COUNT));
//            // Set the movie's vote average
//            movie.setVoteAverage(movieJson.getString(GlobalConstant.VOTE_AVERAGE));
//        } catch (JSONException e) {
//            e.getMessage();
//        } finally {
//            return movie;
//        }
//    }
}

    

