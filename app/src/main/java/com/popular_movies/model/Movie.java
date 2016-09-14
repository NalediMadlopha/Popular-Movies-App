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

    private String id;
    private String posterPath;
    private String backDropDate;
    private String originalTitle;
    private String title;
    private String overall;
    private String genre;
    private String releaseDate;
    private String popularity;
    private String voteCount;
    private String voteAverage;

    public Movie() {
    }

    private Movie(Parcel in) {
        id = in.readString();
        posterPath = in.readString();
        backDropDate = in.readString();
        originalTitle = in.readString();
        title = in.readString();
        overall = in.readString();
        genre = in.readString();
        releaseDate = in.readString();
        popularity = in.readString();
        voteCount = in.readString();
        voteAverage = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = sHTTPS_IMAGE_TMDB_ORG_T_P_W342 + posterPath;
    }

    public String getBackDropDate() {
        return backDropDate;
    }

    public void setBackDropDate(String backDropDate) {
        this.backDropDate = sHTTPS_IMAGE_TMDB_ORG_T_P_W342 + backDropDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverall() {
        return overall;
    }

    public void setOverall(String overall) {
        this.overall = overall;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = Utility.convertDate(releaseDate);
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(String voteCount) {
        this.voteCount = voteCount;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public String toString() {
        return "Movie{" +
                "mId='" + id + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", backDropDate='" + backDropDate + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", mTitle='" + title + '\'' +
                ", mOverall='" + overall + '\'' +
                ", genre='" + genre + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", popularity='" + popularity + '\'' +
                ", voteCount='" + voteCount + '\'' +
                ", voteAverage='" + voteAverage + '\'' +
                '}';
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(posterPath);
        parcel.writeString(backDropDate);
        parcel.writeString(originalTitle);
        parcel.writeString(title);
        parcel.writeString(overall);
        parcel.writeString(genre);
        parcel.writeString(releaseDate);
        parcel.writeString(popularity);
        parcel.writeString(voteCount);
        parcel.writeString(voteAverage);
      }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {

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
