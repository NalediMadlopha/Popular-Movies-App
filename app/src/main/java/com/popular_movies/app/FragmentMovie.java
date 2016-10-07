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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.popular_movies.adapter.MoviesAdapter;
import com.popular_movies.model.FavouriteMoviesHandler;
import com.popular_movies.model.Genre;
import com.popular_movies.model.Movie;
import com.popular_movies.model.ResponseGenres;
import com.popular_movies.model.ResponseMovies;
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
    private LinearLayout mLinearLayout;
    private ArrayList<Movie> mMovies;

    private ApiInterface mApiService =
            ApiClient.getClient().create(ApiInterface.class);

    private SharedPreferences mPrefs;
    Gson mGson = new Gson();

    public FragmentMovie() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_main, container, false);
        mLinearLayout = (LinearLayout) mRootView.findViewById(R.id.no_connection);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

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

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            mMovies = savedInstanceState.getParcelableArrayList(GlobalConstant.MOVIES);
        }
    }

    /**
     * Updates the UI
     *
     * @param movies is an array list of movie objects to be displayed on the UI
     */
    public void updateUI(ArrayList<Movie> movies) {
        mLinearLayout.setVisibility(View.INVISIBLE);

        // Auto fits the movie item on the grid based on the screen space
        GridAutofitLayoutManager layoutManager = new GridAutofitLayoutManager(getActivity(), 200);

        final RecyclerView recyclerView = (RecyclerView) mRootView.findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerView.Adapter adapterViewAdapter = new MoviesAdapter(getActivity(), movies);
        recyclerView.setAdapter(adapterViewAdapter);
    }

    /**
     * Fetches the genres from the api and saves them on a shared service
     *
     * @param apiService is an api interface
     */
    private void fetchGenres(ApiInterface apiService) {
        Call<ResponseGenres> call
                = apiService.getGenres(GlobalConstant.C5CA40DED62975B80638B7357FD69E9);

        call.enqueue(new Callback<ResponseGenres>() {
            @Override
            public void onResponse(Call<ResponseGenres>call, Response<ResponseGenres> response) {
                List<Genre> genres = response.body().getGenres();

                // Shared preference to store the genres locally
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                String genreJson = mGson.toJson(genres);
                prefsEditor.putString(GlobalConstant.GENRES, genreJson);
                prefsEditor.commit();
            }

            @Override
            public void onFailure(Call<ResponseGenres>call, Throwable t) {
                // Log error here since request failed
                Log.e("Retrofit Error [Genre]", t.toString());
            }
        });
    }

    /**
     * Fetches the movies from the api and saves them on a shared service
     *
     * @param apiService is an api interface
     */
    private void fetchMovies(final ApiInterface apiService) {

        Call<ResponseMovies> call = null;

        // Get the movies based on the sort order preference
        switch (Utility.getSortOrderPref(getActivity())) {
            case GlobalConstant.MOST_POPULAR: // Get most popular movies
                call = apiService.getMostPopularMovies(GlobalConstant.C5CA40DED62975B80638B7357FD69E9);
                break;
            case GlobalConstant.TOP_RATED: // Get top rated movies
                call = apiService.getTopRatedMovies(GlobalConstant.C5CA40DED62975B80638B7357FD69E9);
                break;
            case GlobalConstant.FAVOURITE: // Get favourite movies
                // Instantiate the favourite movies handler
                FavouriteMoviesHandler mFavouriteMoviesHandler = new FavouriteMoviesHandler(getActivity());

                // Get the favourite movies
                ArrayList<String> movieList =  mFavouriteMoviesHandler.getMovieList();

                mMovies = new ArrayList<>();

                for (int i = 0; i < movieList.size(); i++) {
                    Movie movie = mGson.fromJson(movieList.get(i), Movie.class);

                    mMovies.add(movie);
                }

                // Store the movie category on a shared preference for offline usage
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                prefsEditor.putString(GlobalConstant.LOCAL_MOVIES_CATEGORY, Utility.getSortOrderPref(getActivity()));
                prefsEditor.commit();

                // Update the UI
                updateUI(mMovies);
                return;
        }

        call.enqueue(new Callback<ResponseMovies>() {
            @Override
            public void onResponse(Call<ResponseMovies>call, Response<ResponseMovies> response) {
                // Used to store the movies for offline usage
                ArrayList<String> localMovieStore = new ArrayList<>();
                mMovies = (ArrayList) response.body().getResults(); // Get the response result

                for (int i = 0; i < mMovies.size(); i++) {
                    String movieJson = mGson.toJson(mMovies.get(i));

                    localMovieStore.add(movieJson);
                }

                // Store the movies locally on a shared preference for offline usage
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                String localMovieStoreJson = mGson.toJson(localMovieStore);
                prefsEditor.putString(GlobalConstant.LOCAL_MOVIES, localMovieStoreJson);
                prefsEditor.putString(GlobalConstant.LOCAL_MOVIES_CATEGORY, Utility.getSortOrderPref(getActivity()));
                prefsEditor.commit();

                // Update the UI
                updateUI(mMovies);
            }

            @Override
            public void onFailure(Call<ResponseMovies>call, Throwable t) {
                // Log error here since request failed
                Log.e("Retrofit Error [Movie]", t.toString());
            }
        });
    }

    // Initialize a broadcast receiver
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Check if the connection state is connected
            if (Utility.isOnline(context)) {
                fetchGenres(mApiService); // Fetch genres from the api
                fetchMovies(mApiService); // Fetch movies from the api
            } else {

                // Notify the user about the lose of internet connection
                Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();

                // Check if there are movies stored locally
                if (mPrefs.contains(GlobalConstant.LOCAL_MOVIES) && mPrefs.contains(GlobalConstant.LOCAL_MOVIES_CATEGORY)) {
                    String localMovieStoreJson = mPrefs.getString(GlobalConstant.LOCAL_MOVIES, "");
                    String localMovieStoreCategory = mPrefs.getString(GlobalConstant.LOCAL_MOVIES_CATEGORY, "");

                    if (localMovieStoreCategory.equals(Utility.getSortOrderPref(getActivity()))) {
                        // Get the local movies
                        ArrayList<String> localMovieStore = mGson.fromJson(localMovieStoreJson, ArrayList.class);
                        mMovies = new ArrayList<>();

                        for (int i = 0; i < localMovieStore.size(); i++) {
                            Movie movie = mGson.fromJson(localMovieStore.get(i), Movie.class);
                            mMovies.add(movie);
                        }

                        // Update the UI
                        updateUI(mMovies);
                    } else {
                        // Display the no internet connection view
                        mLinearLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    // Display the no internet connection view
                    mLinearLayout.setVisibility(View.VISIBLE);
                }
            }
        }
    };

}
