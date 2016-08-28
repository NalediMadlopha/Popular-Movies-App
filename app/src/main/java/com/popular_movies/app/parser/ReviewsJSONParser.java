package com.popular_movies.app.parser;

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
            JSONArray reviewsJsonArray = reviewsJson.getJSONArray(GlobalConstant.sRESULTS);

            ArrayList<Review> reviews = new ArrayList<>();

            for (int i = 0; i < reviewsJsonArray.length(); i++) {

                // Create a new review object
                Review review = new Review();
                JSONObject reviewJson = reviewsJsonArray.getJSONObject(i);

                // Set the review Id
                review.setId(reviewJson.getString(GlobalConstant.sID));

                // Set the author
                review.setAuthor(reviewJson.getString(GlobalConstant.sAUTHOR));

                // Set the review content
                review.setReviewMessage(reviewJson.getString(GlobalConstant.sCONTENT));

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

    

