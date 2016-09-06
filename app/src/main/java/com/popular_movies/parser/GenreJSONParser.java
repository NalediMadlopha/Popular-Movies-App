package com.popular_movies.parser;

import android.support.annotation.Nullable;
import android.util.Log;

import com.popular_movies.app.GlobalConstant;
import com.popular_movies.model.Genre;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 2016/08/09.
 */
public class GenreJSONParser {

    private static final String LOG_TAG = GenreJSONParser.class.getSimpleName();

    // TODO: Add a description
    @Nullable
    public static List<Genre> parseFeed(String content) {

        try {
            JSONObject genresJson = new JSONObject(content);
            JSONArray genresJsonArray = genresJson.getJSONArray(GlobalConstant.GENRES);

            ArrayList<Genre> genres = new ArrayList<>();

            for (int i = 0; i < genresJsonArray.length(); i++) {
                JSONObject genreObject = (JSONObject) genresJsonArray.get(i);

                Genre genre = new Genre();
                genre.setId(genreObject.getInt(GlobalConstant.ID));
                genre.setmName(genreObject.getString(GlobalConstant.NAME));

                genres.add(genre);
            }

            return genres;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
            return null;
        }
    }
}
