/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.popular_movies.database.DataSourceMovie;
import com.popular_movies.database.DataSourceTrailer;
import com.popular_movies.model.Genre;
import com.popular_movies.model.Movie;
import com.popular_movies.model.Trailer;
import com.popular_movies.parser.GenreJSONParser;
import com.popular_movies.parser.MovieJSONParser;
import com.popular_movies.parser.TrailerJSONParser;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Provides the main activity
 */
public class MainActivity extends AppCompatActivity {

    private SharedPreferences mPrefs;
    private DataSourceMovie mDataSourceMovie;
    private DataSourceTrailer mDataSourceTrailer;
    private ProgressDialog mLoadMoviesProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if the details container is not null
        if (findViewById(R.id.movie_detail_container) != null) {
            // Check the saved instance state and replace the movie detail contain
            // with details fragment
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new DetailFragment(),
                                GlobalConstant.DETAIL_FRAGMENT_TAG)
                        .commit();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Initialize the shared preference object
        mPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        // Check if the genre names have ever been persisted before.
        // If so then the app is not being ran for the first time
        // therefore there are movies stored in the database.
        if (!mPrefs.contains(GlobalConstant.GENRES)) {
            if (Utility.isOnline(MainActivity.this)) {
                // Set the action bar
                setupActionBar();

                mDataSourceMovie = new DataSourceMovie(MainActivity.this);
                mDataSourceTrailer = new DataSourceTrailer(MainActivity.this);

                mDataSourceMovie.open();
                mDataSourceTrailer.open();

                // Syncs the most popular movies with the local SQLite database
                new SyncDatabase().execute(GlobalConstant.MOST_POPULAR);
                // Syncs the top rated movies with the local SQLite database
                new SyncDatabase().execute(GlobalConstant.TOP_RATED);

                // Persist genre names from the API
                new PersistGenreNames().execute();
            } else {
                // Set the view layout
                setContentView(R.layout.activity_error_no_network);

                // Initialize a retry button in case there is no network
                Button button_retry = (Button) findViewById(R.id.button_retry);
                // Set the on click listener for the retry button
                button_retry.setOnClickListener(mButtonRetryOnClickListener);
            }
        } else {
            // Set the action bar
            setupActionBar();
        }
    }

    /**
     * Initialize a retry button on click listener
     * to refresh the the activity in case there is no internet connection
     */
    private final View.OnClickListener mButtonRetryOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(MainActivity.this, MainActivity.class));
            MainActivity.this.finish();
        }
    };

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Resize the action bar title
            actionBar.setTitle(Html.fromHtml("<small>Popular Movies</small>"));
            actionBar.setSubtitle(Html.fromHtml("<small>" + Utility.getSortOrderPref(this) + "</small>"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // Start the settings activity
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class PersistGenreNames extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            StringRequest request = new StringRequest(com.android.volley.Request.Method.GET,
                    GlobalConstant.QUERY_GENRE, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    // Parse the response to a movie objects array list
                    ArrayList<Genre> genres = GenreJSONParser.parseFeed(response);
                    Gson gson = new Gson();

                    /**
                     * This code persists the names of the movie genres onto a shared preference.
                     * The genre preference is always overwritten, in case a new genre is added
                     **/
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    String favouriteMovieListJson = gson.toJson(genres);
                    prefsEditor.putString(GlobalConstant.GENRES, favouriteMovieListJson);
                    prefsEditor.commit();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.getMessage();
                }
            });
            Volley.newRequestQueue(MainActivity.this).add(request);

            return null;
        }
    }

    class SyncDatabase extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String[] params) {

            // Check the query to request
            final String query = (params[0].equals(GlobalConstant.MOST_POPULAR))
                    ? GlobalConstant.QUERY_POPULAR_MOVIES : GlobalConstant.QUERY_TOP_RATED_MOVIES;

            final String movieCategory = params[0];

            // Initialize the request
            StringRequest request = new StringRequest(com.android.volley.Request.Method.GET,
                    query, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    // Parse the response to a movie objects array list
                    ArrayList<Movie> movieArrayList = MovieJSONParser.parseFeed(response);

                    try {
                        for (int i = 0; i < movieArrayList.size(); i++) {

                            // Get the names of the movie genres
                            String genreNames = Utility.getGenreNames(MainActivity.this, movieArrayList.get(i).getGenre());
                            // Change the names of the movie genres
                            movieArrayList.get(i).setGenre(genreNames);
                            // Set the movie category
                            movieArrayList.get(i).setCategory(movieCategory);
                        }
                    } catch (JSONException e) {
                        e.getMessage();
                    }

                    if (movieArrayList != null) {
                        for (int i = 0; i < movieArrayList.size(); i++) {
                            mDataSourceMovie.addMovie(movieArrayList.get(i));

                            new SynceTrailer().execute(movieArrayList.get(i).getId());
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.getMessage();
                }
            });
            // Add the request to a volley request queue
            Volley.newRequestQueue(MainActivity.this).add(request);

            return null;
        }
    }

    class SynceTrailer extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(final String[] params) {

            final String query = GlobalConstant.QUERY_TRAILERS + params[0] + "/videos"
                    + GlobalConstant.API_KEY_PARAMETER;

            // Initialize the request
            StringRequest request = new StringRequest(com.android.volley.Request.Method.GET,
                    query, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    // Parse the response to a movie objects array list
                    ArrayList<Trailer> trailerArrayList = TrailerJSONParser.parseFeed(response);

                    if (trailerArrayList != null) {
                        for (int i = 0; i < trailerArrayList.size(); i++) {
                            // Add the trailer to the local SQLite database
                            mDataSourceTrailer.addTrailer(trailerArrayList.get(i));
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.getMessage();
                }
            });
            // Add the request to a volley request queue
            Volley.newRequestQueue(MainActivity.this).add(request);

            return null;
        }
    }
}
