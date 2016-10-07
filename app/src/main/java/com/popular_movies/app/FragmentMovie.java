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

    public void updateUI(ArrayList<Movie> movies) {
        mLinearLayout.setVisibility(View.INVISIBLE);

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

    private void fetchMovies(final ApiInterface apiService) {

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
                    Movie movie = mGson.fromJson(movieList.get(i), Movie.class);

                    mMovies.add(movie);
                }

                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                prefsEditor.putString(GlobalConstant.LOCAL_MOVIES_CATEGORY, Utility.getSortOrderPref(getActivity()));
                prefsEditor.commit();

                updateUI(mMovies);
                return;
        }

        call.enqueue(new Callback<ResponseMovies>() {
            @Override
            public void onResponse(Call<ResponseMovies>call, Response<ResponseMovies> response) {

                ArrayList<String> localMovieStore = new ArrayList<>();

                mMovies = (ArrayList) response.body().getResults();

                for (int i = 0; i < mMovies.size(); i++) {
                    String movieJson = mGson.toJson(mMovies.get(i));

                    localMovieStore.add(movieJson);
                }

                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                String localMovieStoreJson = mGson.toJson(localMovieStore);
                prefsEditor.putString(GlobalConstant.LOCAL_MOVIES, localMovieStoreJson);
                prefsEditor.putString(GlobalConstant.LOCAL_MOVIES_CATEGORY, Utility.getSortOrderPref(getActivity()));
                prefsEditor.commit();

                updateUI(mMovies);
            }

            @Override
            public void onFailure(Call<ResponseMovies>call, Throwable t) {
                // Log error here since request failed
                Log.e("Retrofit Error [Movie]", t.toString());
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

                Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();

                if (mPrefs.contains(GlobalConstant.LOCAL_MOVIES) && mPrefs.contains(GlobalConstant.LOCAL_MOVIES_CATEGORY)) {
                    String localMovieStoreJson = mPrefs.getString(GlobalConstant.LOCAL_MOVIES, "");
                    String localMovieStoreCategory = mPrefs.getString(GlobalConstant.LOCAL_MOVIES_CATEGORY, "");

                    if (localMovieStoreCategory.equals(Utility.getSortOrderPref(getActivity()))) {
                        ArrayList<String> localMovieStore = mGson.fromJson(localMovieStoreJson, ArrayList.class);
                        mMovies = new ArrayList<>();

                        for (int i = 0; i < localMovieStore.size(); i++) {
                            Movie movie = mGson.fromJson(localMovieStore.get(i), Movie.class);
                            mMovies.add(movie);
                        }

                        updateUI(mMovies);
                    } else {
                        mLinearLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    mLinearLayout.setVisibility(View.VISIBLE);
                }
            }
        }
    };

}
