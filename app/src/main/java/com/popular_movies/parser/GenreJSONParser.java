/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.parser;

import android.support.annotation.Nullable;

import com.popular_movies.app.GlobalConstant;
import com.popular_movies.model.Genre;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a list of genre objects by parsing a json string
 */
public class GenreJSONParser {

    /**
     * Parse the feed of a json string to genre objects
     *
     * @param content is a json object string
     * @return list of genre objects
     */
    @Nullable
    public static List<Genre> parseFeed(String content) {

        ArrayList<Genre> genres = new ArrayList<>();

        try {
            // Get a json object from the json string content
            JSONObject genresJson = new JSONObject(content);
            // Get a json array of genres
            JSONArray genresJsonArray = genresJson.getJSONArray(GlobalConstant.GENRES);

            for (int i = 0; i < genresJsonArray.length(); i++) {
                // Get a single genre json object
                JSONObject genreObject = (JSONObject) genresJsonArray.get(i);

                // Build a genre object
                Genre genre = new Genre();
                genre.setId(genreObject.getInt(GlobalConstant.ID));
                genre.setmName(genreObject.getString(GlobalConstant.NAME));

                // Add the built genre to a genre array list
                genres.add(genre);
            }
        } catch (JSONException e) {
            e.getMessage();
        } finally {
            return genres;
        }
    }
}
