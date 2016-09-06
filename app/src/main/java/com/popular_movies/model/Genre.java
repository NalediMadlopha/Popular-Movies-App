/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.model;

/**
 * Provides a genre object template
 */
public class Genre {

    private int mId;
    private String mName;

    public Genre() {
    }

    public Genre(int id, String name) {
        this.mId = id;
        this.mName = name;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String name) {
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
