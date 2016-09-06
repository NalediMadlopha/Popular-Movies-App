/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.app;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.popular_movies.adapter.ReviewAdapter;
import com.popular_movies.adapter.TrailerAdapter;
import com.popular_movies.model.FavouriteMoviesHandler;
import com.popular_movies.model.Movie;
import com.popular_movies.model.Review;
import com.popular_movies.model.TMDBHandler;
import com.popular_movies.model.Trailer;
import com.popular_movies.parser.ReviewsJSONParser;
import com.popular_movies.parser.TrailerJSONParser;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Provides the details fragment
 */
public class DetailFragment extends Fragment {

    private Movie mMovie;
    private ListView mTrailersListView;
    private LinearLayout mReviewsLinearLayout;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private FavouriteMoviesHandler mFavouriteMoviesHandler;
    private ImageButton mFavouriteMovieIcon;

    public DetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the details fragment
        final View rootView = inflater.inflate(R.layout.fragment_details, null);

        // Get the arguments
        Bundle arguments = getArguments();
        if (arguments != null) {
            // Get the parcelable movie argument
            mMovie = arguments.getParcelable(GlobalConstant.sMOVIE);
        }

        // Set the poster of the movie
        ImageView movie_poster = (ImageView) rootView.findViewById(R.id.details_movie_poster);
        Picasso.with(getActivity()).load(mMovie.getBackDropDate()).into(movie_poster);

        // Set the favourite movie icon
        mFavouriteMovieIcon = (ImageButton) rootView.findViewById(R.id.details_favourite_movie);

        // Instantiate the favourite movies handler
        mFavouriteMoviesHandler = new FavouriteMoviesHandler(getActivity());

        // Check if the movie is already in the favourite movie list
        if (mFavouriteMoviesHandler.isFavourite(mMovie.getId())) {
            // Set the favourite icon tint to red
            mFavouriteMovieIcon.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
        }

        // Set favourite button on click listener
        mFavouriteMovieIcon.setOnClickListener(favouriteMovieButtonOnClickListener);

        // Set the title of the movie
        TextView movie_title = (TextView) rootView.findViewById(R.id.details_movie_title);
        movie_title.setText(mMovie.getOriginalTitle());

        // Set the genre(s) of the movie
        TextView movie_genres = (TextView) rootView.findViewById(R.id.details_movie_genres);
        movie_genres.setText(mMovie.getGenre());

        // Set the release date of the movie
        TextView movie_release_date = (TextView) rootView.findViewById(R.id.details_movie_release_date);
        movie_release_date.setText(mMovie.getReleaseDate());

        // Set the plot synopsis of the movie
        TextView plot_synopsis = (TextView) rootView.findViewById(R.id.details_movie_plot_synopsis);
        plot_synopsis.setText(mMovie.getOverall());

        // Find the trailer list view
        mTrailersListView = (ListView) rootView.findViewById(R.id.trailer_list_view);

        // Set the trailer on item click listener
        mTrailersListView.setOnItemClickListener(mTrailerOnItemClickListener);

        // Find the review linear layout
        mReviewsLinearLayout = (LinearLayout) rootView.findViewById(R.id.reviews_linear_layout);

        // Fetch the movie trailers using the asyncTask
        new FetchTrailers().execute();

        // Fetch the movie reviews using the asyncTask
        new FetchReviews().execute();

        return rootView;
    }

    /**
     * Create the trailer on item click lister
     * Open the trailer video on YouTube
     */
    private final AdapterView.OnItemClickListener mTrailerOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            // Get the trailer from the trailer adapter
            Trailer trailer = (Trailer) mTrailerAdapter.getItem(position);

            // Open the movie trailer video
            watchTrailer(trailer.getKey());
        }
    };

    /** Create the favourite movie button on click lister */
    private final Button.OnClickListener favouriteMovieButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            // Check if the movie is already in the favourite movie list
            if (mFavouriteMoviesHandler.isFavourite(mMovie.getId())) {

                // Remove the movie from the favourite movie list
                mFavouriteMoviesHandler.removeMovie(mMovie.getId());

                // Set the favourite icon tint to grey
                mFavouriteMovieIcon.setImageTintList(ColorStateList.valueOf(getResources()
                        .getColor(R.color.colorGrey)));

                // Notify the user with a toast
                Toast.makeText(getActivity(), "Removed from the favourite movies collection.", Toast.LENGTH_LONG).show();
            } else {

                // Add the movie to the favourite movie list
                mFavouriteMoviesHandler.addMovie(mMovie.getId());

                // Set the favourite icon tint to red
                mFavouriteMovieIcon.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));

                // Notify the user with a toast
                Toast.makeText(getActivity(), "Added to the favourite movies collection.", Toast.LENGTH_LONG).show();
            }
        }
    };

    /** Open the trailer video on YouTube */
    public void watchTrailer(String trailerKey) {
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(GlobalConstant.sYOUTUBE_VND + trailerKey));
        } catch (ActivityNotFoundException e) {
            intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(GlobalConstant.sYOUTUBE_URL + trailerKey));
        } finally {
            // Start the activity that will open the YouTube app
            // or the YouTube website
            startActivity(intent);
        }
    }

    /** Provides a background task that fetches the movie's reviews */
    public class FetchReviews extends AsyncTask<Void, Void, List<Review>> {

        @Override
        protected List<Review> doInBackground(Void... voids) {
            // Convert movie id from string to integer
            int movieId = Integer.parseInt(mMovie.getId());
            // Fetch the movie reviews from the API
            String reviewsJsonString = TMDBHandler.fetchMovieReviews(movieId);

            // Parses the review JSON string
            // Returns a list of reviews
            return ReviewsJSONParser.parseFeed(reviewsJsonString);
        }

        @Override
        protected void onPostExecute(List<Review> reviews) {

            // Check if there are trailers
            if (reviews != null) {
                // Initialize the trailer adapter, passing the trailers list
                mReviewAdapter = new ReviewAdapter(getActivity(), reviews);

                // Loop through the item in the review adapter
                for (int i = 0; i < mReviewAdapter.getCount(); i++) {
                    // Get the view of the item
                    View view = mReviewAdapter.getView(i, null, mReviewsLinearLayout);
                    // Add the view to the review linear layout
                    mReviewsLinearLayout.addView(view);
                }
            }
            super.onPostExecute(reviews);
        }
    }

    /** Provides a background task that fetches the movie's trailers */
    public class FetchTrailers extends AsyncTask<Void, Void, List<Trailer>> {

        @Override
        protected List<Trailer> doInBackground(Void... voids) {

            // Convert movie id from string to integer
            int movieId = Integer.parseInt(mMovie.getId());

            // Fetch the movie trailers from the API
            String trailerJsonString = TMDBHandler.fetchMovieTrailers(movieId);

            // Parses the trailer JSON string
            // Returns a list of trailers
            return TrailerJSONParser.parseFeed(trailerJsonString);
        }

        @Override
        protected void onPostExecute(List<Trailer> trailers) {

            // Check if there are trailers
            if (trailers != null) {

                // Initialize the trailer adapter, passing the trailers list
                mTrailerAdapter = new TrailerAdapter(getActivity(), trailers);

                // Set the list view adapter
                mTrailersListView.setAdapter(mTrailerAdapter);

                // Modify the height of the list view
                Utils.setListViewHeightBasedOnItems(mTrailersListView);
            }
            super.onPostExecute(trailers);
        }
    }
}