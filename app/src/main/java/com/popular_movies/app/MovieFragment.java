/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.app;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.popular_movies.adapter.MovieAdapter;
import com.popular_movies.model.FavouriteMoviesHandler;
import com.popular_movies.model.Movie;
import com.popular_movies.model.TMDBHandler;
import com.popular_movies.parser.MovieJSONParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides the movie fragment
 */
public class MovieFragment extends Fragment {


    private MovieAdapter mMovieAdapter;
    private GridView mMovieGridView;
    private ArrayList<Movie> mMovieList = new ArrayList<>();
    private ArrayList<FetchMovies> tasks = new ArrayList<>();
    private View mRootView;
    private LinearLayout mLoadMoviesProgressIndicator;

    public MovieFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(GlobalConstant.MOVIES, mMovieList);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Check if there is internet connection
        if (isOnline()) {
            // Inflate the main fragment
            mRootView = inflater.inflate(R.layout.fragment_main, container, false);
            // Fetch movies from the API
            new FetchMovies().execute();
        } else {
            // Inflate an error fragment if there is no network connection
            mRootView = inflater.inflate(R.layout.activity_error_no_network, container, false);
            // Initialize a retry button in case there is no network
            Button button_retry = (Button) mRootView.findViewById(R.id.button_retry);
            // Set the on click listener for the retry button
            button_retry.setOnClickListener(mButtonRetryOnClickListener);
        }

        return mRootView;
    }

    /**
     * Initialize a retry button on click listener
     * to refresh the the activity in case there is no internet connection
     */
    private final View.OnClickListener mButtonRetryOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    };

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

    /** Checks if there is internet connection */
    protected boolean isOnline() {

        // Initialize the connectivity manager
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // Check if the network is connected or connecting
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    /** Fetches the movies from the API */
    public class FetchMovies extends AsyncTask<Void, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            // Initialize the progress bar
            mLoadMoviesProgressIndicator =
                    (LinearLayout) mRootView.findViewById(R.id.load_movies_progress_indicator);

            // Check if there are any tasks pending
            // If so, display the progress indicator
            if (tasks.size() == 0) {
                mLoadMoviesProgressIndicator.setVisibility(View.VISIBLE);
            }
            tasks.add(this); // Add the task to the list

            super.onPreExecute();
        }

        @Override
        protected List<Movie> doInBackground(Void... voids) {
            String moviesJsonString = null;
            // Gets the sort order preference
            String sortOrder = Utils.getSortOrderPref(getActivity());

            if (sortOrder != null) {
                switch (sortOrder) {
                    case "Most Popular": // In case the sort order is set to popular mMovieList
                        moviesJsonString = TMDBHandler.fetchPopularMovies();
                        break;
                    case "Top Rated": // In case the sort order is set to top rated mMovieList
                        moviesJsonString = TMDBHandler.fetchTopRatedMovies();
                        break;
                    case "Favourites":  // In case the sort order is set to favourite mMovieList
                        // Instantiate the favourite mMovieList handler
                        FavouriteMoviesHandler favouriteMoviesHandler = new FavouriteMoviesHandler(getActivity());

                        // Gets the movies json list
                        List<String> favouriteMovies = favouriteMoviesHandler.getMovieList();
                        List<Movie> favouriteMovieList = new ArrayList<>();

                        // Gets the movie objects from the movies json string
                        for (int i = 0; i < favouriteMovies.size(); i++) {
                            moviesJsonString = TMDBHandler.
                                    fetchFavouriteMovies(favouriteMovies.get(i));
                            Movie movie = MovieJSONParser.parseSingleFeed(moviesJsonString);
                            favouriteMovieList.add(movie);
                        }
                        return favouriteMovieList;
                }
            } else {
                // If the sort order has not been set, fetch most popular mMovieList
                moviesJsonString = TMDBHandler.fetchPopularMovies();
            }

            // Return a list of mMovieList
            return MovieJSONParser.parseFeed(moviesJsonString);
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            // Remove this task as it has ended
            tasks.remove(this);

            // Check if there are no tasks pending
            // If so, hide the progress bar
            if (tasks.size() == 0) {
                mLoadMoviesProgressIndicator.setVisibility(View.INVISIBLE);
            }

            if (movies != null) {
                // Initialize the movie adapter, passing the movie list
                mMovieAdapter = new MovieAdapter(getActivity(), movies);

                // Get a reference to the grid view
                mMovieGridView = (GridView) mRootView.findViewById(R.id.movie_grid);
                // Set on item click listener
                mMovieGridView.setOnItemClickListener(mMovieOnItemClickListener);
                // Set the grid view adapter
                mMovieGridView.setAdapter(mMovieAdapter);
            }

            super.onPostExecute(movies);
        }
    }
}
