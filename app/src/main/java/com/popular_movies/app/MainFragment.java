package com.popular_movies.app;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
 * Created by Naledi Madlopha on 2016/07/28.
 */
public class MainFragment extends Fragment {

    private static final String LOG_TAG = MainFragment.class.getSimpleName();

    private MovieAdapter mMovieAdapter;
    private GridView mMovieGridView;
    private ArrayList<FetchMovies> tasks = new ArrayList<>();
    private View mRootView;
    private LinearLayout mLoadMoviesProgressIndicator;

    boolean mDualPane;
    int mCurCheckPosition = 0;

    public MainFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Check if there is internet connection
        if (isOnline()) {

            // Inflate the main fragment
            mRootView = inflater.inflate(R.layout.fragment_main, container, false);

            // Initialize the progress bar
            mLoadMoviesProgressIndicator =
                    (LinearLayout) mRootView.findViewById(R.id.load_movies_progress_indicator);

            // Get a reference to the grid view
            mMovieGridView = (GridView) mRootView.findViewById(R.id.movie_grid);

            // Set on item click listener
            mMovieGridView.setOnItemClickListener(mMovieOnItemClickListener);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Execute a background task, to fetch the movies from The Movie Database
        new FetchMovies().execute();

        // Check if there frame to embed the movie details fragment is there
        View detailsFrame = getActivity().findViewById(R.id.details_container);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {
            // Restore last state for checked position
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }

        if (mDualPane) {
            mMovieGridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
            showDetails(mCurCheckPosition);
        }
    }

    final View.OnClickListener mButtonRetryOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    };

    final AdapterView.OnItemClickListener mMovieOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            showDetails(position);
        }
    };

    /**
     * Helper function to show the details of a selected item, either by
     * displaying a fragment in-place in the current UI, or starting a
     * whole new activity in which it is displayed.
     */
    void showDetails(int index) {
        mCurCheckPosition = index;

        if (mDualPane) {
            mMovieGridView.setItemChecked(index, true);

            DetailsFragment detailsFragment = (DetailsFragment) getFragmentManager().findFragmentById(R.id.details_container);

            if (detailsFragment == null || detailsFragment.getShownIndex() != index) {
                detailsFragment = DetailsFragment.newInstance(index);

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                if (index == 0) {
                    fragmentTransaction.replace(R.id.details_container, detailsFragment);
                } else {
                    fragmentTransaction.replace(R.id.details_container, detailsFragment);
                }

                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();
            }

            Log.v(LOG_TAG, "dual pane");
        } else {
            Movie movie = mMovieAdapter.getItem(index); // Get the clicked movie
            Intent intent = new Intent(getActivity(), DetailsActivity.class)
                    .putExtra("movie", movie); // Attach the clicked movie onto an intent
            startActivity(intent); // Start the details activity
        }

    }

    protected boolean isOnline() {

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public class FetchMovies extends AsyncTask<Void, Void, List<Movie>> {
        /*
         * The LOG_TAG stores the name of the class, it is used for
         * debugging purposes.
         */
        private final String LOG_TAG = FetchMovies.class.getSimpleName();

        @Override
        protected void onPreExecute() {
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
            String sortOrder = Utils.getSortOrderPref(getActivity());

            if (sortOrder != null) {
                switch (sortOrder) {
                    case "Most Popular": // In case the sort order is set to popular movies
                        moviesJsonString = TMDBHandler.fetchPopularMovies();
                        break;
                    case "Top Rated": // In case the sort order is set to top rated movies
                        moviesJsonString = TMDBHandler.fetchTopRatedMovies();
                        break;
                    case "Favourites":  // In case the sort order is set to favourite movies
                        // Instantiate the favourite movies handler
                        FavouriteMoviesHandler favouriteMoviesHandler = new FavouriteMoviesHandler(getActivity());

                        List<String> favouriteMovies = favouriteMoviesHandler.getMovieList();
                        List<Movie> favouriteMovieList = new ArrayList<>();

                        for (int i = 0; i < favouriteMovies.size(); i++) {
                            moviesJsonString = TMDBHandler.fetchFavouriteMovies(favouriteMovies.get(i));
                            Movie movie = MovieJSONParser.parseSingleFeed(moviesJsonString);
                            favouriteMovieList.add(movie);
                        }
                        return favouriteMovieList;
                }
            } else {
                // If the sort order has not been set, fetch most popular movies
                moviesJsonString = TMDBHandler.fetchPopularMovies();
            }


            // Return a list of movies
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
                // Set the grid view adapter
                mMovieGridView.setAdapter(mMovieAdapter);
            }

            super.onPostExecute(movies);
        }
    }
}
