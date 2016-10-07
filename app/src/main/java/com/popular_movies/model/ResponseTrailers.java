/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Provides trailer api response
 */

public class ResponseTrailers {

    @SerializedName("results")
    private List<Trailer> results;

    public List<Trailer> getResults() {
        return results;
    }

    public void setResults(List<Trailer> results) {
        this.results = results;
    }
}
