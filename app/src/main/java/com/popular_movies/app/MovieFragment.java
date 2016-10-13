/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.app;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Provides the movie fragment
 */
public class MovieFragment extends Fragment {
    @BindView(R.id.recycle_view) RecyclerView recyclerView;
    @BindView(R.id.load_movies_progress_indicator) LinearLayout progressView; 
    @BindView(R.id.no_connection) LinearLayout noConnectionView;

    private Unbinder unbinder;
    private View view;
    private ArrayList<Movie> mMovies = new ArrayList<>();
    private Context mActivity;
    private SharedPreferences mSharedPreferences;
    private final static Gson sGson = new Gson();

    public MovieFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events
        setHasOptionsMenu(true);
        mActivity = getActivity();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);

        GridAutofitLayoutManager layoutManager = new GridAutofitLayoutManager(mActivity, 200);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register a connectivity broadcast receiver
        mActivity.registerReceiver(broadcastReceiver,
                new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override
    public void onPause() {
        super.onPause();
        mActivity.unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_sort_order) {

            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle(R.string.app_name)
                    .setItems(R.array.pref_order_by_titles, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            String moviecategory = Utility.getMovieCategoryPref(mActivity);

                            switch (which) {
                                case 1:
                                    if (!moviecategory.equals(GlobalConstant.TOP_RATED)) {
                                        moviecategory = GlobalConstant.TOP_RATED;
                                    }
                                    break;
                                case 2:
                                    if (!moviecategory.equals(GlobalConstant.FAVOURITE)) {
                                        moviecategory = GlobalConstant.FAVOURITE;
                                    }
                                    break;
                                default:
                                    if (!moviecategory.equals(GlobalConstant.MOST_POPULAR)) {
                                        moviecategory = GlobalConstant.MOST_POPULAR;
                                    }
                            }

                            SharedPreferences.Editor prefsEditor = mSharedPreferences.edit();
                            prefsEditor.putString(GlobalConstant.MOVIE_CATEGORY, moviecategory);
                            prefsEditor.commit();

                            if (Utility.isOnline(mActivity)) {
                                requestMovies();
                            } else {
                                mMovies = retreiveLocalMovies();

                                if (mMovies != null) {
                                    updateUI(mMovies);
                                }
                            }
                        }
                    });

            builder.create().show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setRetainInstance(boolean retain) {
        super.setRetainInstance(retain);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = ((AppCompatActivity) mActivity).getSupportActionBar();
        if (actionBar != null) {
            // Resize the action bar title
            actionBar.setTitle(Html.fromHtml("<small>Popular Movies</small>"));
            actionBar.setSubtitle(Html.fromHtml("<small>" + Utility.getMovieCategoryPref(mActivity) + "</small>"));
        }
    }

    /**
     * Updates the UI
     *
     * @param movies is an array list of movie objects to be displayed on the UI
     */
    private void updateUI(ArrayList<Movie> movies) {
        setupActionBar();

        MoviesAdapter adapter = new MoviesAdapter(mActivity, movies);
        recyclerView.setAdapter(adapter);

        boolean isTablet = mActivity.getResources().getBoolean(R.bool.isTablet);

        FragmentManager fragmentManager =
                ((AppCompatActivity) mActivity).getSupportFragmentManager();

        if (isTablet) {
            if (movies != null && !movies.isEmpty()) {

                DetailFragment detailFragment = new DetailFragment();
                // Get the last selected movie
                Movie lastSelectedMovie = adapter.getLastSelection();

                // Supply index input as an argument.
                Bundle args = new Bundle();
                args.putParcelable(GlobalConstant.MOVIE, lastSelectedMovie);
                detailFragment.setArguments(args);

                // Add the fragment to the movie details container (Framelayout)
                fragmentManager.beginTransaction()
                        .replace(R.id.detail_container, detailFragment)
                        .commit();

            } else {
                Toast.makeText(mActivity, "No " + Utility.getMovieCategoryPref(mActivity) +
                        " Movies", Toast.LENGTH_SHORT).show();
                // Add the fragment to the movie details container (Framelayout)
                fragmentManager.beginTransaction()
                        .replace(R.id.detail_container, new DetailFragment())
                        .commit();
            }

        }
    }

    /**
     * Requests the movies from the api and saves them on a shared service
     */
    public void requestMovies() {

        fetchGenres();

        Call<ResponseMovies> call;

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        String movieCategory = Utility.getMovieCategoryPref(mActivity);

        switch (movieCategory) {
            case GlobalConstant.TOP_RATED: // Get top rated movies
                call = apiService.getTopRatedMovies(GlobalConstant.TMDB_API_KEY);
                break;
            case GlobalConstant.FAVOURITE: // Get favourite movies
                mMovies = FavouriteMoviesHandler.getMovieList(getActivity());
                storeLocalMovies(mMovies);
                updateUI(mMovies);
                return;
            default:
                // Get most popular movies
                call = apiService.getMostPopularMovies(GlobalConstant.TMDB_API_KEY);
                break;
        }

        call.enqueue(new Callback<ResponseMovies>() {
            @Override
            public void onResponse(Call<ResponseMovies>call, Response<ResponseMovies> response) {
                mMovies = (ArrayList) response.body().getResults(); // Get the response result
                storeLocalMovies(mMovies);
                updateUI(mMovies);
            }

            @Override
            public void onFailure(Call<ResponseMovies>call, Throwable t) {
                // Log error here since request failed
                Log.e("Retrofit Error [Movie]", t.toString());
            }
        });
    }

    /**
     * Fetches the genres from the api and saves them on a shared service
     */
    private void fetchGenres() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseGenres> call
                = apiService.getGenres(GlobalConstant.TMDB_API_KEY);

        call.enqueue(new Callback<ResponseGenres>() {
            @Override
            public void onResponse(Call<ResponseGenres>call, Response<ResponseGenres> response) {
                List<Genre> genres = response.body().getGenres();

                // Shared preference to store the genres locally
                SharedPreferences.Editor prefsEditor = mSharedPreferences.edit();
                String genreJson = sGson.toJson(genres);
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

    private void storeLocalMovies(ArrayList<Movie> movies) {

        ArrayList<String> localMovieStore = new ArrayList<>();

        for (int i = 0; i < movies.size(); i++) {
            String movieJson = sGson.toJson(movies.get(i));

            localMovieStore.add(movieJson);
        }

        // Store the movies locally on a shared preference for offline usage
        SharedPreferences.Editor prefsEditor = mSharedPreferences.edit();
        String localMovieStoreJson = sGson.toJson(localMovieStore);
        prefsEditor.putString(GlobalConstant.LOCAL_MOVIES, localMovieStoreJson);
        prefsEditor.putString(GlobalConstant.LOCAL_MOVIES_CATEGORY, Utility.getMovieCategoryPref(mActivity));
        prefsEditor.commit();
    }

    private ArrayList<Movie> retreiveLocalMovies() {

        ArrayList<Movie> movies = new ArrayList<>();
        String movieCategory = Utility.getMovieCategoryPref(mActivity);

        if (mSharedPreferences.contains(GlobalConstant.LOCAL_MOVIES)) {
            String movieJson = mSharedPreferences.getString(GlobalConstant.LOCAL_MOVIES, "");
            ArrayList<String> localMovieStore = sGson.fromJson(movieJson, ArrayList.class);

            for (int i = 0; i < localMovieStore.size(); i++) {
                Movie movie = sGson.fromJson(localMovieStore.get(i), Movie.class);
                movies.add(movie);
            }
        }
        return movies;
    }

    // Initialize a broadcast receiver
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Utility.isOnline(mActivity)) {
                requestMovies();
            } else {
                Toast.makeText(context, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                mMovies = retreiveLocalMovies();
                updateUI(mMovies);
            }
        }
    };

}
