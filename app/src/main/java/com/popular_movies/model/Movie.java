package com.popular_movies.model;

import com.popular_movies.app.Utils;

import java.io.Serializable;

/**
 * Created by Naledi Madlopha on 2016/07/28.
 */
public class Movie implements Serializable {

    private static long serialVersionUID = 0L;

    private final String THE_MOVIE_DB_BASE_URL = "https://image.tmdb.org/t/p/w342";

    private String mId;
    private String mPosterPath;
    private String mBackDropDate;
    private String mOriginalTitle;
    private String mTitle;
    private String mOverall;
    private String mGenre;
    private String mReleaseDate;
    private String mPopularity;
    private String mVoteCount;
    private String mVoteAverage;

    protected int mPoster; // drawable reference id

    public Movie() {
    }

    public Movie(String id, String posterPath, String backDropDate, String originalTitle,
                 String title, String overall, String genre, String releaseDate,
                 String popularity, String voteCount, String voteAverage) {
        this.mId = id;
        this.mPosterPath = posterPath;
        this.mBackDropDate = backDropDate;
        this.mOriginalTitle = originalTitle;
        this.mTitle = title;
        this.mOverall = overall;
        this.mGenre = genre;
        this.mReleaseDate = releaseDate;
        this.mPopularity = popularity;
        this.mVoteCount = voteCount;
        this.mVoteAverage = voteAverage;
    }

    public Movie(String title, String genre, String releaseDate) {
        this.mTitle = title;
        this.mGenre = genre;
        this.mReleaseDate = releaseDate;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String posterPath) {
        this.mPosterPath = THE_MOVIE_DB_BASE_URL + posterPath;
    }

    public String getBackDropDate() {
        return mBackDropDate;
    }

    public void setBackDropDate(String backDropDate) {
        this.mBackDropDate = THE_MOVIE_DB_BASE_URL + backDropDate;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.mOriginalTitle = originalTitle;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getOverall() {
        return mOverall;
    }

    public void setOverall(String overall) {
        this.mOverall = overall;
    }

    public String getGenre() {
        return mGenre;
    }

    public void setGenre(String genre) {
        this.mGenre = genre;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.mReleaseDate = Utils.convertDate(releaseDate);
    }

    public String getPopularity() {
        return mPopularity;
    }

    public void setPopularity(String popularity) {
        this.mPopularity = popularity;
    }

    public String getVoteCount() {
        return mVoteCount;
    }

    public void setVoteCount(String voteCount) {
        this.mVoteCount = voteCount;
    }

    public String getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.mVoteAverage = voteAverage;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "mId='" + mId + '\'' +
                ", mPosterPath='" + mPosterPath + '\'' +
                ", mBackDropDate='" + mBackDropDate + '\'' +
                ", mOriginalTitle='" + mOriginalTitle + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mOverall='" + mOverall + '\'' +
                ", mGenre='" + mGenre + '\'' +
                ", mReleaseDate='" + mReleaseDate + '\'' +
                ", mPopularity='" + mPopularity + '\'' +
                ", mVoteCount='" + mVoteCount + '\'' +
                ", mVoteAverage='" + mVoteAverage + '\'' +
                '}';
    }
}
