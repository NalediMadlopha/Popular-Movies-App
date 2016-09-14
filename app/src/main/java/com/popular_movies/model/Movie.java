/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.popular_movies.app.Utility;

/**
 * Provides a movie object template
 */
public class Movie implements Parcelable {

    private final static String sHTTPS_IMAGE_TMDB_ORG_T_P_W342 = "https://image.tmdb.org/t/p/w342";

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

    public Movie() {
    }

    private Movie(Parcel in) {
        mId = in.readString();
        mPosterPath = in.readString();
        mBackDropDate = in.readString();
        mOriginalTitle = in.readString();
        mTitle = in.readString();
        mOverall = in.readString();
        mGenre = in.readString();
        mReleaseDate = in.readString();
        mPopularity = in.readString();
        mVoteCount = in.readString();
        mVoteAverage = in.readString();
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
        this.mPosterPath = sHTTPS_IMAGE_TMDB_ORG_T_P_W342 + posterPath;
    }

    public String getBackDropDate() {
        return mBackDropDate;
    }

    public void setBackDropDate(String backDropDate) {
        this.mBackDropDate = sHTTPS_IMAGE_TMDB_ORG_T_P_W342 + backDropDate;
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
        this.mReleaseDate = Utility.convertDate(releaseDate);
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
    public int describeContents() { return 0; }

    @Override
    public String toString() {
        return "Movie{" +
                "mId='" + mId + '\'' +
                ", posterPath='" + mPosterPath + '\'' +
                ", backDropDate='" + mBackDropDate + '\'' +
                ", originalTitle='" + mOriginalTitle + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mOverall='" + mOverall + '\'' +
                ", genre='" + mGenre + '\'' +
                ", releaseDate='" + mReleaseDate + '\'' +
                ", popularity='" + mPopularity + '\'' +
                ", voteCount='" + mVoteCount + '\'' +
                ", voteAverage='" + mVoteAverage + '\'' +
                '}';
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mPosterPath);
        parcel.writeString(mBackDropDate);
        parcel.writeString(mOriginalTitle);
        parcel.writeString(mTitle);
        parcel.writeString(mOverall);
        parcel.writeString(mGenre);
        parcel.writeString(mReleaseDate);
        parcel.writeString(mPopularity);
        parcel.writeString(mVoteCount);
        parcel.writeString(mVoteAverage);
      }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
}
