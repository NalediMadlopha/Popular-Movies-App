/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.parser;

import android.support.annotation.Nullable;

import com.popular_movies.app.GlobalConstant;
import com.popular_movies.model.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a list of movie review objects by parsing a json string
 */
public class ReviewJSONParser {

    /**
     * Parse the feed of a json string to a list of review objects
     *
     * @param content is a json object string
     * @return list of review objects
     */
    @Nullable
    public static List<Review> parseFeed(String content) {

        ArrayList<Review> reviews = new ArrayList<>();

        try {
            // Get a json object from the json string content
            JSONObject reviewsJson = new JSONObject(content);
            // Get a json array of movies
            JSONArray reviewsJsonArray = reviewsJson.getJSONArray(GlobalConstant.RESULTS);

            for (int i = 0; i < reviewsJsonArray.length(); i++) {
                // Create a new review object
                Review review = new Review();
                JSONObject reviewJson = reviewsJsonArray.getJSONObject(i);

                // Set the review Id
                review.setId(reviewJson.getString(GlobalConstant.ID));

                // Set the author
                review.setAuthor(reviewJson.getString(GlobalConstant.AUTHOR));

                // Set the review content
                review.setContent(reviewJson.getString(GlobalConstant.CONTENT));

                // Add the review object to an array of trailers
                reviews.add(review);
            }
        } catch (JSONException e) {
            e.getMessage();
        } finally {
            return reviews;
        }
    }
}

    

