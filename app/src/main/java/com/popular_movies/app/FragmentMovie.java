/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.popular_movies.adapter.MoviesAdapter;
import com.popular_movies.model.FavouriteMoviesHandler;
import com.popular_movies.model.Genre;
import com.popular_movies.model.Movie;
import com.popular_movies.model.ResponseGenres;
import com.popular_movies.model.ResponseMovies;
import com.popular_movies.model.Review;
import com.popular_movies.model.Trailer;
import com.popular_movies.rest.ApiClient;
import com.popular_movies.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Provides the movie fragment
 */
public class FragmentMovie extends Fragment {

    private View mRootView;

    private ArrayList<Movie> mMovies;
    private ArrayList<Trailer> mTrailers;
    private ArrayList<Review> mReviews;
    private ApiInterface mApiService =
            ApiClient.getClient().create(ApiInterface.class);

    public FragmentMovie() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_main, container, false);

//            mRootView = inflater.inflate(R.layout.activity_error_no_network, container, false);

        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

        if (savedInstanceState != null) {
            mMovies = savedInstanceState.getParcelableArrayList(GlobalConstant.MOVIES);
            updateUI(mMovies);
        }

        return mRootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(GlobalConstant.MOVIES, mMovies);
        super.onSaveInstanceState(outState);
    }

    public void updateUI(ArrayList<Movie> movies) {
        GridAutofitLayoutManager layoutManager = new GridAutofitLayoutManager(getActivity(), 200);

        final RecyclerView recyclerView = (RecyclerView) mRootView.findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerView.Adapter adapterViewAdapter = new MoviesAdapter(getActivity(), movies);
        recyclerView.setAdapter(adapterViewAdapter);
    }

    private void fetchGenres(ApiInterface apiService) {
        Call<ResponseGenres> call
                = apiService.getGenres(GlobalConstant.C5CA40DED62975B80638B7357FD69E9);

        call.enqueue(new Callback<ResponseGenres>() {
            @Override
            public void onResponse(Call<ResponseGenres>call, Response<ResponseGenres> response) {
                List<Genre> genres = response.body().getGenres();

                Gson gson = new Gson();

                // Initialize the shared preference object
                SharedPreferences prefs = PreferenceManager.
                        getDefaultSharedPreferences(getActivity());

                /**
                 * This code persists the names of the movie genres onto a shared preference.
                 * The genre preference is always overwritten, in case a new genre is added
                 **/
                SharedPreferences.Editor prefsEditor = prefs.edit();
                String genreJson = gson.toJson(genres);
                prefsEditor.putString(GlobalConstant.GENRES, genreJson);
                prefsEditor.commit();
            }

            @Override
            public void onFailure(Call<ResponseGenres>call, Throwable t) {
                // Log error here since request failed
                Log.e("Retrofit Error", t.toString());
            }
        });
    }

    private void fetchMovies(ApiInterface apiService) {

        Call<ResponseMovies> call = null;

        switch (Utility.getSortOrderPref(getActivity())) {
            case GlobalConstant.MOST_POPULAR:
                call = apiService.getMostPopularMovies(GlobalConstant.C5CA40DED62975B80638B7357FD69E9);
                break;
            case GlobalConstant.TOP_RATED:
                call = apiService.getTopRatedMovies(GlobalConstant.C5CA40DED62975B80638B7357FD69E9);
                break;
            case GlobalConstant.FAVOURITE:
                // Instantiate the favourite movies handler
                FavouriteMoviesHandler mFavouriteMoviesHandler = new FavouriteMoviesHandler(getActivity());

                ArrayList<String> movieList =  mFavouriteMoviesHandler.getMovieList();

                mMovies = new ArrayList<>();

                for (int i = 0; i < movieList.size(); i++) {
                    Gson gson = new Gson();
                    Movie movie = gson.fromJson(movieList.get(i), Movie.class);

                    mMovies.add(movie);
                }

                updateUI(mMovies);
                return;
        }

        call.enqueue(new Callback<ResponseMovies>() {
            @Override
            public void onResponse(Call<ResponseMovies>call, Response<ResponseMovies> response) {
                mMovies = (ArrayList) response.body().getResults();
                
                updateUI(mMovies);
            }

            @Override
            public void onFailure(Call<ResponseMovies>call, Throwable t) {
                // Log error here since request failed
                Log.e("Retrofit Error", t.toString());
            }
        });
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Utility.isOnline(context)) {
                fetchGenres(mApiService);
                fetchMovies(mApiService);
            } else {

                if (mMovies != null && !mMovies.isEmpty()) {
                    updateUI(mMovies);
                } else {
                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

}
