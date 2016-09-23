/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.model;

/**
 * Provides a genre object template
 */
public class Genre {

    private String mId;
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
