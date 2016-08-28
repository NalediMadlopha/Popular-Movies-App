package com.popular_movies.model;

/**
 * Created by Naledi Madlopha on 2016/08/14.
 */
public class Review {

    private String mId;
    private String mAuthor;
    private String mContent;

    public Review() {
    }

    public Review(String id, String reviewer, String content) {
        this.mId = id;
        this.mAuthor = reviewer;
        this.mContent = content;
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
