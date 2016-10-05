/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.model;

import com.google.gson.annotations.SerializedName;

/**
 * Provides a genre object template
 */
public class Genre {

    @SerializedName("id")
    private String mId;
    @SerializedName("name")
    private String mName;

    public Genre() {
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                '}';
    }
}
