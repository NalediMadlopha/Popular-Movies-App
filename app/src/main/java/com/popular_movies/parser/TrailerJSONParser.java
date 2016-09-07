package com.popular_movies.parser;

import android.support.annotation.Nullable;

import com.popular_movies.app.GlobalConstant;
import com.popular_movies.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naledi Madlopha on 2016/08/09.
 */
public class TrailerJSONParser {

    private static final String LOG_TAG = TrailerJSONParser.class.getSimpleName();

    /**
     * Parse the feed of a json string to a list of trailer objects
     *
     * @param content is a json object string
     * @return list of trailer objects
     */
    @Nullable
    public static List<Trailer> parseFeed(String content) {

        ArrayList<Trailer> trailers = new ArrayList<>();

        try {
            // Get a json object from the json string content
            JSONObject trailersJson = new JSONObject(content);
            // Get a json array of movies
            JSONArray trailersJsonArray = trailersJson.getJSONArray(GlobalConstant.RESULTS);

            for (int i = 0; i < trailersJsonArray.length(); i++) {
                Trailer trailer = new Trailer();
                JSONObject trailerJson = trailersJsonArray.getJSONObject(i);

                // Set the trailer's Id
                trailer.setId(trailerJson.getString(GlobalConstant.ID));
                // Set the trailers's iso_639_1
                trailer.setIso_639_1(trailerJson.getString(GlobalConstant.ISO_639_1));
                // Set the trailers's iso_3166_1
                trailer.setIso_3166_1(trailerJson.getString(GlobalConstant.ISO_3166_1));
                // Set the trailers's key
                trailer.setKey(trailerJson.getString(GlobalConstant.KEY));
                // Set the trailers's name
                trailer.setName(trailerJson.getString(GlobalConstant.NAME));
                // Set the trailers's site
                trailer.setSite(trailerJson.getString(GlobalConstant.SITE));
                // Set the trailers's size
                trailer.setSize(trailerJson.getString(GlobalConstant.SIZE));
                // Set the trailers's type
                trailer.setType(trailerJson.getString(GlobalConstant.TYPE));

                // Add the trailer object to an array of trailers
                trailers.add(trailer);
            }
        } catch (JSONException e) {
            e.getMessage();
        } finally {
            return trailers;
        }
    }
}

    

