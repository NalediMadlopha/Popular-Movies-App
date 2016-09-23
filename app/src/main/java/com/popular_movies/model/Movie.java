/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.popular_movies.app.Utility;

/**
 * Provides a movie object template
 */
public class Movie implements Parcelable {

    private final static String sHTTPS_IMAGE_TMDB_ORG_T_P_W342 = "https://image.tmdb.org/t/p/w342";

    private String id;
    private String poster_path;
    private Bitmap poster_bitmap;
    private String backdrop_path;
    private Bitmap backdrop_date_bitmap;
    private String original_title;
    private String title;
    private String overview;
    private String genre_ids;
    private String release_date;
    private String popularity;
    private String vote_count;
    private String vote_average;
    private String category;

    public Movie() {
    }

    private Movie(Parcel in) {
        id = in.readString();
        poster_path = in.readString();
        backdrop_path = in.readString();
        original_title = in.readString();
        title = in.readString();
        overview = in.readString();
        genre_ids = in.readString();
        release_date = in.readString();
        popularity = in.readString();
        vote_count = in.readString();
        vote_average = in.readString();
        category = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public void setPosterPath(String posterPath) {
        this.poster_path = sHTTPS_IMAGE_TMDB_ORG_T_P_W342 + posterPath;
    }

    public Bitmap getPosterBitmap() {
        return poster_bitmap;
    }

    public void setPosterBitmap(Bitmap poster_bitmap) {
        this.poster_bitmap = poster_bitmap;
    }

    public String getBackDroPath() {
        return backdrop_path;
    }

    public void setBackDropPath(String backdrop_path) {
        this.backdrop_path = sHTTPS_IMAGE_TMDB_ORG_T_P_W342 + backdrop_path;
    }

    public Bitmap getBackDropDateBitmap() {
        return backdrop_date_bitmap;
    }

    public void setBackDropDateBitmap(Bitmap backDropDateBitmap) {
        backdrop_date_bitmap = backDropDateBitmap;
    }

    public String getOriginalTitle() {
        return original_title;
    }

    public void setOriginalTitle(String original_title) {
        this.original_title = original_title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverall() {
        return overview;
    }

    public void setOverall(String overview) {
        this.overview = overview;
    }

    public String getGenre() {
        return genre_ids;
    }

    public void setGenre(String genre_ids) {
        this.genre_ids = genre_ids;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public void setReleaseDate(String release_date) {
        this.release_date = Utility.convertDate(release_date);
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getVoteCount() {
        return vote_count;
    }

    public void setVoteCount(String vote_count) {
        this.vote_count = vote_count;
    }

    public String getVoteAverage() {
        return vote_average;
    }

    public void setVoteAverage(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getCategory() { return category; }

    public void setCategory(String category) { this.category = category; }

    @Override
    public int describeContents() { return 0; }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", poster_path='" + poster_path + '\'' +
                ", backdrop_path='" + backdrop_path + '\'' +
                ", original_title='" + original_title + '\'' +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", genre_ids='" + genre_ids + '\'' +
                ", release_date='" + release_date + '\'' +
                ", popularity='" + popularity + '\'' +
                ", vote_count='" + vote_count + '\'' +
                ", vote_average='" + vote_average + '\'' +
                ", category='" + category + '\'' +
                '}';
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(poster_path);
        parcel.writeString(backdrop_path);
        parcel.writeString(original_title);
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeString(genre_ids);
        parcel.writeString(release_date);
        parcel.writeString(popularity);
        parcel.writeString(vote_count);
        parcel.writeString(vote_average);
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
