package com.popular_movies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.popular_movies.app.Utils;

/**
 * Created by Naledi Madlopha on 2016/07/28.
 */
public class Movie implements Parcelable {

    private final String THE_MOVIE_DB_BASE_URL = "https://image.tmdb.org/t/p/w342";

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

    public Movie(String id, String posterPath, String backDropDate, String originalTitle,
                 String title, String overall, String genre, String releaseDate,
                 String popularity, String voteCount, String voteAverage) {
        this.id = id;
        this.posterPath = posterPath;
        this.backDropDate = backDropDate;
        this.originalTitle = originalTitle;
        this.title = title;
        this.overall = overall;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
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

    public Movie(String title, String genre, String releaseDate) {
        this.title = title;
        this.genre = genre;
        this.releaseDate = releaseDate;
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
        this.posterPath = THE_MOVIE_DB_BASE_URL + posterPath;
    }

    public String getBackDropDate() {
        return backDropDate;
    }

    public void setBackDropDate(String backDropDate) {
        this.backDropDate = THE_MOVIE_DB_BASE_URL + backDropDate;
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
        this.releaseDate = Utils.convertDate(releaseDate);
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
                "id='" + id + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", backDropDate='" + backDropDate + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", title='" + title + '\'' +
                ", overall='" + overall + '\'' +
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
