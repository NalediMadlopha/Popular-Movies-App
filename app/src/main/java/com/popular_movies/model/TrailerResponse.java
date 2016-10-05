package com.popular_movies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by root on 2016/10/04.
 */

public class TrailerResponse {

    @SerializedName("results")
    private List<Trailer> results;

    public List<Trailer> getResults() {
        return results;
    }

    public void setResults(List<Trailer> results) {
        this.results = results;
    }
}
