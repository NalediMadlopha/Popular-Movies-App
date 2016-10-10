/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.app;

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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
    private ArrayList<Movie> mMovies;
    private Context mContext;

    private ApiInterface mApiService =
            ApiClient.getClient().create(ApiInterface.class);

    private SharedPreferences mPrefs;
    Gson mGson = new Gson();

    public MovieFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events
        setHasOptionsMenu(true);

        mContext = getActivity();
        mContext.registerReceiver(broadcastReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_sort_order) {

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(R.string.app_name)
                    .setItems(R.array.pref_order_by_titles, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();

                            String order = Utility.getOrderByPref(mContext);

                            switch (which) {
                                case 1:
                                    if (!Utility.getOrderByPref(mContext).equals(GlobalConstant.TOP_RATED)) {
                                        order = GlobalConstant.TOP_RATED;
                                    }

                                    break;
                                case 2:
                                    if (!Utility.getOrderByPref(mContext).equals(GlobalConstant.FAVOURITE)) {
                                        order = GlobalConstant.FAVOURITE;
                                    }

                                    break;
                                default:
                                    if (!Utility.getOrderByPref(mContext).equals(GlobalConstant.MOST_POPULAR)) {
                                        order = GlobalConstant.MOST_POPULAR;
                                    }

                            }

                            prefsEditor.putString(GlobalConstant.SORT_ORDER, order);
                            prefsEditor.commit();
                        }
                    });

            builder.create()
                    .show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Set the action bar
        setupActionBar();

        loadMovies();
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
        unbinder.unbind();
        mContext.unregisterReceiver(broadcastReceiver);
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

    public interface Callback {
        public void onItemSelected();
    }


    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            // Resize the action bar title
            actionBar.setTitle(Html.fromHtml("<small>Popular Movies</small>"));
            actionBar.setSubtitle(Html.fromHtml("<small>" + Utility.getOrderByPref(getActivity()) + "</small>"));
        }
    }

    // Loads the movie from the api service if there is internet connection
    // Loads the movies from the shared preferences if there is not internet connection
    private void loadMovies() {
        // Check if there are movies stored locally
        if (mPrefs.contains(GlobalConstant.LOCAL_MOVIES) && mPrefs.contains(GlobalConstant.LOCAL_MOVIES_CATEGORY)) {
            String localMovieStoreJson = mPrefs.getString(GlobalConstant.LOCAL_MOVIES, "");
            String localMovieStoreCategory = mPrefs.getString(GlobalConstant.LOCAL_MOVIES_CATEGORY, "");

            // Gheck if the movie preference has changed
            if (localMovieStoreCategory.equals(Utility.getOrderByPref(getActivity()))) {
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
                // Check if there is internet connection
                if (Utility.isOnline(mContext)) {
                    noConnectionView.setVisibility(View.INVISIBLE);
                    progressView.setVisibility(View.VISIBLE);

                    fetchGenres(mApiService); // Fetch genres from the api
                    fetchMovies(mApiService); // Fetch movies from the api
                } else {
                    recyclerView.setVisibility(View.INVISIBLE);
                    // Display the no internet connection view
                    noConnectionView.setVisibility(View.VISIBLE);
                }
            }
        } else {
            if (Utility.isOnline(mContext)) {
                fetchGenres(mApiService); // Fetch genres from the api
                fetchMovies(mApiService); // Fetch movies from the api
            } else {
                recyclerView.setVisibility(View.INVISIBLE);
                // Display the no internet connection view
                noConnectionView.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Updates the UI
     *
     * @param movies is an array list of movie objects to be displayed on the UI
     */
    public void updateUI(ArrayList<Movie> movies) {
        noConnectionView.setVisibility(View.INVISIBLE);
        progressView.setVisibility(View.INVISIBLE);

        if (movies != null || !movies.isEmpty()) {
            // Auto fits the movie item on the grid based on the screen space
            GridAutofitLayoutManager layoutManager = new GridAutofitLayoutManager(mContext, 200);

            final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
            recyclerView.setLayoutManager(layoutManager);

            RecyclerView.Adapter adapterViewAdapter = new MoviesAdapter(mContext, movies);
            recyclerView.setAdapter(adapterViewAdapter);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.INVISIBLE);
            // Display the no internet connection view
            Toast.makeText(mContext, "No movies found", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Fetches the genres from the api and saves them on a shared service
     *
     * @param apiService is an api interface
     */
    private void fetchGenres(ApiInterface apiService) {
        Call<ResponseGenres> call
                = apiService.getGenres(GlobalConstant.C5CA40DED62975B80638B7357FD69E9);

        call.enqueue(new  retrofit2.Callback<ResponseGenres>() {
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
        switch (Utility.getOrderByPref(mContext)) {
            case GlobalConstant.MOST_POPULAR: // Get most popular movies
                call = apiService.getMostPopularMovies(GlobalConstant.C5CA40DED62975B80638B7357FD69E9);
                break;
            case GlobalConstant.TOP_RATED: // Get top rated movies
                call = apiService.getTopRatedMovies(GlobalConstant.C5CA40DED62975B80638B7357FD69E9);
                break;
            case GlobalConstant.FAVOURITE: // Get favourite movies
                // Instantiate the favourite movies handler
                FavouriteMoviesHandler mFavouriteMoviesHandler = new FavouriteMoviesHandler(getActivity());

                // Used to store the movies for offline usage
                ArrayList<String> localMovieStore = mFavouriteMoviesHandler.getMovieList();;

                mMovies = new ArrayList<>();

                for (int i = 0; i < localMovieStore.size(); i++) {
                    Movie movie = mGson.fromJson(localMovieStore.get(i), Movie.class);

                    mMovies.add(movie);
                }

                // Store the movie category on a shared preference for offline usage
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                String localMovieStoreJson = mGson.toJson(localMovieStore);
                prefsEditor.putString(GlobalConstant.LOCAL_MOVIES, localMovieStoreJson);
                prefsEditor.putString(GlobalConstant.LOCAL_MOVIES_CATEGORY, Utility.getOrderByPref(mContext));
                prefsEditor.commit();

                // Update the UI
                updateUI(mMovies);
                return;
        }

        call.enqueue(new retrofit2.Callback<ResponseMovies>() {
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
                prefsEditor.putString(GlobalConstant.LOCAL_MOVIES_CATEGORY, Utility.getOrderByPref(getActivity()));
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
            loadMovies();
        }
    };

}
