package com.popular_movies.rest;

import com.popular_movies.model.ResponseGenres;
import com.popular_movies.model.ResponseMovies;
import com.popular_movies.model.ResponseReviews;
import com.popular_movies.model.ResponseTrailers;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Provides an api interface for the application
 */
public interface ApiInterface {
    @GET("movie/popular")
    Call<ResponseMovies> getMostPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<ResponseMovies> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<ResponseMovies> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie")
    Call<ResponseMovies> getSelectedMovies(@QueryMap Map<String, String> titles, @Query("api_key") String apiKey);

    @GET("genre/movie/list")
    Call<ResponseGenres> getGenres(@Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<ResponseTrailers> getTrailers(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ResponseReviews> getReviews(@Path("id") int id, @Query("api_key") String apiKey);
}
