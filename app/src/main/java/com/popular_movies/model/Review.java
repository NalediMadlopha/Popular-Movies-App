/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.model;

import com.google.gson.annotations.SerializedName;

/**
 * Provides a review object template
 */
public class Review {

    @SerializedName("id")
    private String mId;
    @SerializedName("page")
    private String mMovieId;
    private String mAuthor;
    private String mContent;

    public Review() {
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getMovieId() {
        return mMovieId;
    }

    public void setMovieId(String mMovieId) {
        this.mMovieId = mMovieId;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String reviewer) {
        this.mAuthor = reviewer;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        this.mContent = content;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + mId + '\'' +
                ", movie id=" + mMovieId + '\'' +
                ", author='" + mAuthor + '\'' +
                ", content='" + mContent + '\'' +
                '}';
    }
}
