/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.model;

/**
 * Provides a review object template
 */
public class Review {

    private String mId;
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

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String reviewer) {
        this.mAuthor = reviewer;
    }

    public String getContent() {
        return mContent;
    }

    public void setReviewMessage(String reviewMessage) {
        this.mContent = reviewMessage;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + mId + '\'' +
                "author='" + mAuthor + '\'' +
                ", content='" + mContent + '\'' +
                '}';
    }
}
