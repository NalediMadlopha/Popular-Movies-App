/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.popular_movies.adapter.MovieAdapter;
import com.popular_movies.database.DatabaseHelper;
import com.popular_movies.model.Movie;
import com.popular_movies.parser.MovieJSONParser;

import java.util.ArrayList;

/**
 * Provides the movie fragment
 */
public class MovieFragment extends Fragment {

    private String mSortOrderOnStart = new String();
    private MovieAdapter mMovieAdapter;
    private GridView mMovieGridView;
    private static ArrayList<Movie> mMovies = new ArrayList<>();
    private View mRootView;



    public MovieFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if there is network connection
        if (Utility.isOnline(getActivity())) {
            StringRequest movieRequest = new StringRequest(com.android.volley.Request.Method.GET,
                    GlobalConstant.POPULAR_MOVIES_QUERY, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Parse the response to a movie objects array list
                    mMovies = MovieJSONParser.parseFeed(response);

                    // Initialize a new database helper
                    DatabaseHelper databaseHelper = new DatabaseHelper(getActivity(), null, null, 1);

                    // Add the movies fetched
                    for (int i = 0; i < mMovies.size(); i++) {
                        databaseHelper.addMovie("Most Popular", mMovies.get(i));
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

            Volley.newRequestQueue(getActivity()).add(movieRequest);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(GlobalConstant.MOVIES, mMovies);
        super.onSaveInstanceState(outState);
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
    public void onResume() {
        super.onResume();

        // Initialize a new database helper
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity(), null, null, 1);

        mMovies = databaseHelper.getMovies("Most Popular");

        updateDisplay(mMovies);

//        StringRequest stringRequest = null;
//
//        stringRequest = request(GlobalConstant.POPULAR_MOVIES_QUERY);

//        if (mSortOrderOnStart.equals(Utility.getSortOrderPref(getActivity())) && !mMovies.isEmpty()) {
//            updateDisplay(mMovies);
//        } else {
//            StringRequest stringRequest = null;
//
//        stringRequest = request(GlobalConstant.POPULAR_MOVIES_QUERY);
//        Volley.newRequestQueue(getActivity()).add(stringRequest);
//            switch (Utility.getSortOrderPref(getActivity())) {
//                case "Most Popular":
//                    stringRequest = request(GlobalConstant.POPULAR_MOVIES_QUERY);
//                    Volley.newRequestQueue(getActivity()).add(stringRequest);
//                    break;
//                case "Top Rated":
//                    stringRequest = request(GlobalConstant.TOP_RATED_MOVIES_QUERY);
//                    Volley.newRequestQueue(getActivity()).add(stringRequest);
//                    break;
//                case "Favourites":
//                    // Instantiate the favourite mMovieList handler
//                    FavouriteMoviesHandler favouriteMoviesHandler = new FavouriteMoviesHandler(getActivity());
//
//                    mMovies = favouriteMoviesHandler.getMovieList();
//
//                    Log.e("FAVOURITE", mMovies.toString());
//                    updateDisplay(mMovies);
//                    GlobalConstant.SINGLE_MOVIE_QUERY_ + "271110?"
//                            + GlobalConstant.API_KEY + "=" + GlobalConstant.C5CA40DED62975B80638B7357FD69E9
                    // Instantiate the favourite mMovieList handler
//                    FavouriteMoviesHandler favouriteMoviesHandler = new FavouriteMoviesHandler(getActivity());
//
//                    // Gets the mMovies json list
//                    List<String> favouriteMovies = favouriteMoviesHandler.getMovieList();
//                    List<Movie> favouriteMovieList = new ArrayList<>();
//
//                    // Gets the movie objects from the mMovies json string
//                    for (int i = 0; i < favouriteMovies.size(); i++) {
//                        moviesJsonString = Request.
//                                fetchFavouriteMovies(favouriteMovies.get(i));
//                        Movie movie = MovieJSONParser.parseSingleFeed(moviesJsonString);
//                        favouriteMovieList.add(movie);
//                    }
//                    return favouriteMovieList;
//                    break;
//            }
//        }
    }

    public StringRequest request (String URL) {
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET,
                URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mMovies = MovieJSONParser.parseFeed(response);
                DatabaseHelper databaseHelper = new DatabaseHelper(getActivity(), null, null, 1);

                for (int i = 0; i < mMovies.size(); i++) {
                    databaseHelper.addMovie("Most Popular", mMovies.get(i));
                }

                mMovies.clear();
                Movie _movie = databaseHelper.getMovie("Most Popular", "1");

                mMovies.add(_movie);

//                Log.e("REQUEST", response.toString());
                updateDisplay(mMovies);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return stringRequest;
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
}
