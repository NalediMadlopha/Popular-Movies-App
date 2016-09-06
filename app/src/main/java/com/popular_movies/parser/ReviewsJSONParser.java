package com.popular_movies.parser;

import android.support.annotation.Nullable;
import android.util.Log;

import com.popular_movies.app.GlobalConstant;
import com.popular_movies.model.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naledi Madlopha on 2016/08/09.
 */
public class ReviewsJSONParser {

    private static final String LOG_TAG = ReviewsJSONParser.class.getSimpleName();

    // TODO: Add a description
    @Nullable
    public static List<Review> parseFeed(String content) {

        try {
            JSONObject reviewsJson = new JSONObject(content);
            JSONArray reviewsJsonArray = reviewsJson.getJSONArray(GlobalConstant.RESULTS);

            ArrayList<Review> reviews = new ArrayList<>();

            for (int i = 0; i < reviewsJsonArray.length(); i++) {

                // Create a new review object
                Review review = new Review();
                JSONObject reviewJson = reviewsJsonArray.getJSONObject(i);

                // Set the review Id
                review.setId(reviewJson.getString(GlobalConstant.ID));

                // Set the author
                review.setAuthor(reviewJson.getString(GlobalConstant.AUTHOR));

                // Set the review content
                review.setReviewMessage(reviewJson.getString(GlobalConstant.CONTENT));

                // Add the review object to an array of trailers
                reviews.add(review);
            }
            return reviews;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
            return null;
        }
    }
}

    

