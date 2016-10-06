/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.app;

/**
 * Provides static values to the entire application
 */
public class GlobalConstant {

    public final static String GENRES = "genres";

    // Replace the value of this variable with an API KEY
    public final static String C5CA40DED62975B80638B7357FD69E9 = "9c5ca40ded62975b80638b7357fd69e9";
    public final static String HTTPS_API_THEMOVIEDB_ORG_3 = "https://api.themoviedb.org/3";
    public final static String API_KEY = "api_key";
    public final static String MOVIE_POPULAR = "/movie/popular";
    public final static String MOVIE_TOP_RATED = "/movie/top_rated";

    public final static String QUERY_POPULAR_MOVIES = HTTPS_API_THEMOVIEDB_ORG_3 + MOVIE_POPULAR;
    public final static String QUERY_TOP_RATED_MOVIES = HTTPS_API_THEMOVIEDB_ORG_3
            + MOVIE_TOP_RATED;

    public final static String QUERY_SINGLE_MOVIE_QUERY = HTTPS_API_THEMOVIEDB_ORG_3 +
            "/movie/";

    public final static String QUERY_GENRE = HTTPS_API_THEMOVIEDB_ORG_3
            + GlobalConstant.GENRE_MOVIE_LIST;
    public final static String GENRE_MOVIE_LIST = "/genre/movie/list";
    public final static String MOVIE = "movie";
    public final static String MOVIES = "movies";
    public final static String VND_YOUTUBE = "vnd.youtube:";
    public final static String HTTP_WWW_YOUTUBE_COM_WATCH_V = "http://www.youtube.com/watch?v=";
    public final static String FAVOURITE_MOVIE_LIST = "FavouriteMovieList";
    public final static String DETAIL_FRAGMENT_TAG = "DETAIL_FRAGMENT_TAG";

    public final static String MOST_POPULAR = "Most Popular";
    public final static String TOP_RATED = "Top Rated";
    public final static String FAVOURITE = "Favourite";


}
