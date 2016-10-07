/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Provides genre api response
 */

public class ResponseGenres {

    @SerializedName("genres")
    private List<Genre> genres;

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

}
