/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.popular_movies.adapter.MovieAdapter;
import com.popular_movies.database.MovieDataSource;
import com.popular_movies.model.Movie;
import com.popular_movies.parser.MovieJSONParser;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Provides the movie fragment
 */
public class MovieFragment extends Fragment {
    private MovieDataSource mDataSource;

    private ArrayList<Movie> mMovies;
    private MovieAdapter mMovieAdapter;
    private GridView mMovieGridView;
    private View mRootView;
    private ProgressDialog mLoadMoviesProgressDialog;

    public MovieFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("onCreate", "Create");


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the grid view
        mMovieGridView = (GridView) mRootView.findViewById(R.id.movie_grid);
        // Set on item click listener
        mMovieGridView.setOnItemClickListener(mMovieOnItemClickListener);

        return mRootView;
    }

    @Override
    public void onStart() {
        super.onStart();
//        getActivity().deleteDatabase("PopularMovie.db");
        mDataSource = new MovieDataSource(getActivity());
        mDataSource.open();

        // Check if there is network connection
        if (Utility.isOnline(getActivity())) {
//            String sortOrderPref = Utility.getSortOrderPref(getActivity());
//
//            new SyncDatabase().execute(sortOrderPref);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mDataSource.open();
        if (mMovies == null || mMovies.size() == 0) {
            String[] movieCategory = { Utility.getSortOrderPref(getActivity()) };
            mMovies = mDataSource.getMovies("movie_category=?", movieCategory);
        }
        if (mMovies != null && mMovies.size() > 0) {
            // Initialize the movie adapter, passing the movie list
            mMovieAdapter = new MovieAdapter(getActivity(), mMovies);
            // Set the grid view adapter
            mMovieGridView.setAdapter(mMovieAdapter);
        }

    }

    @Override
    public void onPause() {
        mDataSource.close();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(GlobalConstant.MOVIES, mMovies);
        super.onSaveInstanceState(outState);
    }

    public void updateDisplay(ArrayList<Movie> movies) {

        // Initialize the movie adapter, passing the movie list
        mMovieAdapter = new MovieAdapter(getActivity(), movies);
        // Set the grid view adapter
        mMovieGridView.setAdapter(mMovieAdapter);
    }

    /**
     * Initialize an on item click listener
     * If the screen displays a multipane layout, a movie details fragment is add
     * and the movie details are displayed on the fragment.
     * If the screen displays a single pane layout then a new activity is started
     * displaying the movie details
     */
    private final AdapterView.OnItemClickListener mMovieOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            // Check if the movie detail container is displayed or not
            if (mRootView.findViewById(R.id.movie_detail_container) != null) {
                DetailFragment detailFragment = new DetailFragment();

                // Supply index input as an argument.
                Bundle args = new Bundle();
                args.putParcelable(GlobalConstant.MOVIE, mMovieAdapter.getItem(position));
                detailFragment.setArguments(args);

                // Replace the movie details container framelayout with the details fragment
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, detailFragment, GlobalConstant.MOVIES)
                        .commit();
            } else {
                // Start the details activity and pass the movie to it
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(GlobalConstant.MOVIE, mMovieAdapter.getItem(position));
                startActivity(intent);
            }
        }
    };

    class SyncDatabase extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadMoviesProgressDialog = ProgressDialog.show(getActivity(), "Loading", "Loading movies...", true);
        }

        @Override
        protected Void doInBackground(String[] params) {

            // Check the query to request
            String query = (params[0].equals(GlobalConstant.MOST_POPULAR))
                    ? GlobalConstant.QUERY_POPULAR_MOVIES : GlobalConstant.QUERY_TOP_RATED_MOVIES;

            // Initialize the request
            StringRequest request = new StringRequest(com.android.volley.Request.Method.GET,
                    query, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    // Parse the response to a movie objects array list
                    ArrayList<Movie> movies = MovieJSONParser.parseFeed(response);

                    try {
                        for (int i = 0; i < movies.size(); i++) {

                            // Get the names of the movie genres
                            String genreNames = Utility.getGenreNames(getActivity(), movies.get(i).getGenre());
                            // Change the names of the movie genres
                            movies.get(i).setGenre(genreNames);
                        }
                    } catch (JSONException e) {
                        e.getMessage();
                    }

                    if (movies != null) {
                        for (int i = 0; i < movies.size(); i++) {
                            mDataSource.addMovie(movies.get(i));
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
            Volley.newRequestQueue(getActivity()).add(request);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mLoadMoviesProgressDialog.dismiss();
        }
    }
}
