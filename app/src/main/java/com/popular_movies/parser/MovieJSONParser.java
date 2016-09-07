/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.parser;

import android.support.annotation.Nullable;

import com.popular_movies.app.GlobalConstant;
import com.popular_movies.model.Genre;
import com.popular_movies.model.Movie;
import com.popular_movies.model.TMDBHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a list of movie objects by parsing a json string
 */
public class MovieJSONParser {

    // Store the genre Ids
    private static List<Integer> sGenreIdList;

    /**
     * Parse the feed of a json string to a movie object
     *
     * @param content is a json object string
     * @return movie object
     */
    @Nullable
    public static ArrayList<Movie> parseFeed(String content) {

        ArrayList<Movie> movies = new ArrayList<>();
        sGenreIdList = new ArrayList<>();

        try {
            // Get a json object from the json string content
            JSONObject moviesJson = new JSONObject(content);
            // Get a json array of movies
            JSONArray moviesJsonArray = moviesJson.getJSONArray(GlobalConstant.RESULTS);

            // Fetch the movie genres from themoviedb.org API
            String genresJsonString = TMDBHandler.fetchMovieGenres();
            List<Genre> genres = GenreJSONParser.parseFeed(genresJsonString);

            for (int i = 0; i < moviesJsonArray.length(); i++) {
                Movie movie = new Movie();
                JSONObject movieJson = moviesJsonArray.getJSONObject(i);

                // Set the movie's Id
                movie.setId(movieJson.getString(GlobalConstant.ID));
                // Set the movie's poster
                movie.setPosterPath(movieJson.getString(GlobalConstant.POSTER_PATH));
                // Set the movie's original title
                movie.setOriginalTitle(movieJson.getString(GlobalConstant.ORIGINAL_TITLE));
                // Set the movie's title
                movie.setTitle(movieJson.getString(GlobalConstant.TITLE));
                // Set the movie's overall description
                movie.setOverall(movieJson.getString(GlobalConstant.OVERVIEW));

                JSONArray genreArray = movieJson.getJSONArray(GlobalConstant.GENRE_IDS);

                // Loop through the movie's genre array
                for (int ii = 0; ii < genreArray.length(); ii++) {
                    // Get the id attribute and add it to the genre id list
                    sGenreIdList.add(genreArray.getInt(ii));
                }

                // Get the genre names
                String genreNames = getGenreNames(sGenreIdList, genres);

                // Set the movie's genre
                movie.setGenre(genreNames);
                // Set the movie's release date
                movie.setReleaseDate(movieJson.getString(GlobalConstant.RELEASE_DATE));
                // Set the movie's backdrop path
                movie.setBackDropDate(movieJson.getString(GlobalConstant.BACKDROP_PATH));
                // Set the movie's popularity
                movie.setPopularity(movieJson.getString(GlobalConstant.POPULARITY));
                // Set the movie's vote count
                movie.setVoteCount(movieJson.getString(GlobalConstant.VOTE_COUNT));
                // Set the movie's vote average
                movie.setVoteAverage(movieJson.getString(GlobalConstant.VOTE_AVERAGE));

                // Add the movie object to an array of movies
                movies.add(movie);
            }
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
    @Nullable
    public static Movie parseSingleFeed(String content) {
        Movie movie = new Movie();
        sGenreIdList = new ArrayList<>();

        try {
            JSONObject movieJson = new JSONObject(content);

            // Set the movie's Id
            movie.setId(movieJson.getString(GlobalConstant.ID));
            // Set the movie's poster
            movie.setPosterPath(movieJson.getString(GlobalConstant.POSTER_PATH));
            // Set the movie's original title
            movie.setOriginalTitle(movieJson.getString(GlobalConstant.ORIGINAL_TITLE));
            // Set the movie's title
            movie.setTitle(movieJson.getString(GlobalConstant.TITLE));
            // Set the movie's overall description
            movie.setOverall(movieJson.getString(GlobalConstant.OVERVIEW));

            // Fetch the movie genres from themoviedb.org API
            String genresJsonString = TMDBHandler.fetchMovieGenres();
            // List of all genres
            List<Genre> genres = GenreJSONParser.parseFeed(genresJsonString);
            // Get the genres
            JSONArray genreArray = movieJson.getJSONArray(GlobalConstant.GENRES);

            // Loop through the movie's genre array
            for (int i = 0; i < genreArray.length(); i++) {
                // Get each genre json object
                JSONObject genreJsonObject = genreArray.getJSONObject(i);
                // Get the id attribute and add it to the genre id list
                sGenreIdList.add(genreJsonObject.getInt("id"));
            }
            // Get the genre names
            String genreNames = getGenreNames(sGenreIdList, genres);

            // Set the movie's genre
            movie.setGenre(genreNames);
            // Set the movie's release date
            movie.setReleaseDate(movieJson.getString(GlobalConstant.RELEASE_DATE));
            // Set the movie's backdrop path
            movie.setBackDropDate(movieJson.getString(GlobalConstant.BACKDROP_PATH));
            // Set the movie's popularity
            movie.setPopularity(movieJson.getString(GlobalConstant.POPULARITY));
            // Set the movie's vote count
            movie.setVoteCount(movieJson.getString(GlobalConstant.VOTE_COUNT));
            // Set the movie's vote average
            movie.setVoteAverage(movieJson.getString(GlobalConstant.VOTE_AVERAGE));
        } catch (JSONException e) {
            e.getMessage();
        } finally {
            return movie;
        }
    }

    /**
     * Converts a list of genre ids to a list of genre names
     *
     * @param genreIdArray an array of genre id
     * @param genres a list of genre objects
     * @return a movie object
     */
    public static String getGenreNames(List<Integer> genreIdArray, List<Genre> genres) throws JSONException {
        String genreNames = "";

        // Loop through the movie's genres
        for (int i = 0; i < genreIdArray.size(); i++) {
            // Loop through all the genres
            for (int ii = 0; ii < genres.size(); ii++) {
                int genreId = genreIdArray.get(i);
                Genre genre = genres.get(ii);

                // Check the genre Id match
                if (genreId == genre.getId()) {
                    // Build a genre list, separated by commas
                    genreNames += genre.getmName() + ", ";
                    break;
                }
            }
        }

        // Check if the length of the genre name is greater than 0
        if (genreNames.length() > 0) {
            // Remove the last comma from the list of genre names
            genreNames = genreNames.substring(0, (genreNames.length() - 2));

            // Clear the stored genre ids
            sGenreIdList.clear();
        } else {
            // Set this when the movie is not categorized
            genreNames = "No genre listed";
        }

        return genreNames;
    }
}

    

