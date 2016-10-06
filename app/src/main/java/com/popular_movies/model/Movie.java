/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.popular_movies.app.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a movie object template
 */
public class Movie implements Parcelable {

    private final static String sHTTPS_IMAGE_TMDB_ORG_T_P_W342 = "https://image.tmdb.org/t/p/w342";

    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("adult")
    private boolean adult;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("genre_ids")
    private List<Integer> genreIds = new ArrayList<Integer>();
    @SerializedName("id")
    private Integer id;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("title")
    private String title;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("popularity")
    private Double popularity;
    @SerializedName("vote_count")
    private Integer voteCount;
    @SerializedName("video")
    private Boolean video;
    @SerializedName("vote_average")
    private Double voteAverage;
    private ArrayList<Trailer> trailers;
    private ArrayList<Review> reviews;

    public Movie() {
    }

    public Movie(String posterPath, boolean adult, String overview, String releaseDate, List<Integer> genreIds, Integer id,
                 String originalTitle, String originalLanguage, String title, String backdropPath, Double popularity,
                 Integer voteCount, Boolean video, Double voteAverage) {
        this.posterPath = posterPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.genreIds = genreIds;
        this.id = id;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.title = title;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.voteAverage = voteAverage;
    }

    private Movie(Parcel in) {
        this.posterPath = in.readString();
        this.adult = in.readByte() != 0;
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.genreIds = in.readArrayList(null);
        this.id = in.readInt();
        this.originalTitle = in.readString();
        this.originalLanguage = in.readString();
        this.title = in.readString();
        this.backdropPath = in.readString();
        this.popularity = in.readDouble();
        this.voteCount = in.readInt();
        this.video = in.readByte() != 0;
        this.voteAverage = in.readDouble();
    }

    public String getPosterPath() {
        return sHTTPS_IMAGE_TMDB_ORG_T_P_W342 + posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackDropPath() {
        return sHTTPS_IMAGE_TMDB_ORG_T_P_W342 + backdropPath;
    }

    public void setBackDropPath(String backdrop_path) {
        this.backdropPath = backdrop_path;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOriginalLanguage() { return originalLanguage; }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public void setOriginalTitle(String original_title) {
        this.originalTitle = original_title;
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

    public List<Integer> getGenreIds() { return genreIds; }

    public void setGenreIds(List<Integer> genreIds) { this.genreIds = genreIds; }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getReleaseDate() {
        return Utility.convertDate(releaseDate);
    }

    public void setReleaseDate(String release_date) {
        this.releaseDate = release_date;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int vote_count) {
        this.voteCount = vote_count;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double vote_average) {
        this.voteAverage = vote_average;
    }

    public ArrayList<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(ArrayList<Trailer> trailers) {
        this.trailers = trailers;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public String toString() {
        return "Movie {" +
                "poster_path='" + this.posterPath + '\'' +
                ", adult='" + this.adult + '\'' +
                ", overview='" + this.overview + '\'' +
                ", release_date='" + this.releaseDate + '\'' +
                ", genre_ids='" + this.genreIds + '\'' +
                ", id='" + this.id + '\'' +
                ", original_title='" + this.originalTitle + '\'' +
                ", original_language='" + this.originalLanguage + '\'' +
                ", title='" + this.title + '\'' +
                ", backdrop_path='" + this.backdropPath + '\'' +
                ", popularity='" + this.popularity + '\'' +
                ", vote_count='" + this.voteCount + '\'' +
                ", video='" + this.video + '\'' +
                ", vote_average='" + this.voteAverage + '\'' +
                ", trailers='" + this.trailers + '\'' +
                '}';
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.posterPath);
        parcel.writeByte((byte) (this.adult ? 1 : 0));
        parcel.writeString(this.overview);
        parcel.writeString(this.releaseDate);
        parcel.writeList(this.genreIds);
        parcel.writeInt(this.id);
        parcel.writeString(this.originalTitle);
        parcel.writeString(this.originalLanguage);
        parcel.writeString(this.title);
        parcel.writeString(this.backdropPath);
        parcel.writeDouble(this.popularity);
        parcel.writeInt(this.voteCount);
        parcel.writeByte((byte) (this.video ? 1 : 0));
        parcel.writeDouble(this.voteAverage);
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
