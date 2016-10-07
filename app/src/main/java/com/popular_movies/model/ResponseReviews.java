/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Provides review api response
 */

public class ResponseReviews {

    @SerializedName("results")
    private List<Review> results;

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }
}
