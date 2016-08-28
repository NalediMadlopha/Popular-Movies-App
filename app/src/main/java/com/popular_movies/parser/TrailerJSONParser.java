package com.popular_movies.parser;

import android.support.annotation.Nullable;
import android.util.Log;

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

    // TODO: Add a description
    @Nullable
    public static List<Trailer> parseFeed(String content) {

        try {
            JSONObject trailersJson = new JSONObject(content);
            JSONArray trailersJsonArray = trailersJson.getJSONArray(GlobalConstant.sRESULTS);

            ArrayList<Trailer> trailers = new ArrayList<>();

            for (int i = 0; i < trailersJsonArray.length(); i++) {
                Trailer trailer = new Trailer();
                JSONObject trailerJson = trailersJsonArray.getJSONObject(i);

                // Set the trailer's Id
                trailer.setId(trailerJson.getString(GlobalConstant.sID));
                // Set the trailers's iso_639_1
                trailer.setIso_639_1(trailerJson.getString(GlobalConstant.sISO_639_1));
                // Set the trailers's iso_3166_1
                trailer.setIso_3166_1(trailerJson.getString(GlobalConstant.sISO_3166_1));
                // Set the trailers's key
                trailer.setKey(trailerJson.getString(GlobalConstant.sKEY));
                // Set the trailers's name
                trailer.setName(trailerJson.getString(GlobalConstant.sNAME));
                // Set the trailers's site
                trailer.setSite(trailerJson.getString(GlobalConstant.sSITE));
                // Set the trailers's size
                trailer.setSize(trailerJson.getString(GlobalConstant.sSIZE));
                // Set the trailers's type
                trailer.setType(trailerJson.getString(GlobalConstant.sTYPE));

                // Add the trailer object to an array of trailers
                trailers.add(trailer);
            }
            return trailers;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
            return null;
        }
    }
}

    

