package com.popular_movies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by root on 2016/10/04.
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
