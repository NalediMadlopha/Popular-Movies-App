/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.app;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.popular_movies.model.ResponseReviews;
import com.popular_movies.model.ResponseTrailers;
import com.popular_movies.model.Review;
import com.popular_movies.model.Trailer;
import com.popular_movies.rest.ApiClient;
import com.popular_movies.rest.ApiInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Provides the details fragment
 */
public class DetailFragment extends Fragment {

    @BindView(R.id.details_movie_poster) ImageView movie_poster;
    @BindView(R.id.ic_votes_rating_average) ImageView vote_rating;
    @BindView(R.id.details_favourite_movie) ImageButton favouriteMovieIcon;
    @BindView(R.id.details_movie_title) TextView movie_title;
    @BindView(R.id.details_movie_genres) TextView movie_genres;
    @BindView(R.id.details_movie_release_date) TextView movie_release_date;
    @BindView(R.id.details_average_votes) TextView votes_average;
    @BindView(R.id.details_movie_plot_synopsis) TextView plot_synopsis;
    @BindView(R.id.trailer_list_view) ListView trailersListView;
    @BindView(R.id.reviews_linear_layout) LinearLayout reviewsLinearLayout;

    private Unbinder unbinder;
    private Movie mMovie;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private Activity mActivity;

    private ApiInterface mApiService =
            ApiClient.getClient().create(ApiInterface.class);

    public DetailFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mMovie = savedInstanceState.getParcelable(GlobalConstant.MOVIE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(GlobalConstant.MOVIE, mMovie);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = getActivity();

        // Get the arguments
        Bundle args = getArguments();
        Intent intent = mActivity.getIntent();

        if (savedInstanceState != null) {
            mMovie = savedInstanceState.getParcelable(GlobalConstant.MOVIE);
        } else if (args != null) {
            // Get the parcelable movie argument
            mMovie = args.getParcelable(GlobalConstant.MOVIE);
        } else if (intent != null) {
            mMovie = intent.getParcelableExtra(GlobalConstant.MOVIE);
        } else {
            mMovie = null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the details fragment
        final View view = inflater.inflate(R.layout.fragment_detail, null);
        unbinder = ButterKnife.bind(this, view);

        if (mMovie != null) {
            // Set the poster of the movie
            Picasso.with(mActivity)
                    .load(mMovie.getBackDropPath())
                    .placeholder(R.drawable.movie_icon)
                    .into(movie_poster);

            // Set the favourite icon color
            vote_rating.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorYellowOrange)));
            // Set the title of the movie
            movie_title.setText(mMovie.getOriginalTitle());
            // Set the genre(s) of the movie
            movie_genres.setText(Utility.getGenreNames(mActivity, mMovie.getGenreIds()));
            // Set the release date of the movie
            movie_release_date.setText(mMovie.getReleaseDate());
            // Set the votes average
            votes_average.setText(mMovie.getVoteAverage().toString());
            // Set the plot synopsis of the movie
            plot_synopsis.setText(mMovie.getOverall());

            // Set the favourite movie icon
            favouriteMovieIcon = (ImageButton) view.findViewById(R.id.details_favourite_movie);

            // Check if the movie is already in the favourite movie list
            if (FavouriteMoviesHandler.isFavourite(mActivity, mMovie)) {
                // Set the favourite icon tint to red
                favouriteMovieIcon.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
            }

            // Set favourite button on click listener
            favouriteMovieIcon.setOnClickListener(favouriteMovieButtonOnClickListener);
            // Set the trailer on item click listener
            trailersListView.setOnItemClickListener(mTrailerOnItemClickListener);
        } else {
            return null;
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.registerReceiver(broadcastReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
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

    /**
     * Create the trailer on item click lister
     * Open the trailer video on YouTube
     */
    private final AdapterView.OnItemClickListener mTrailerOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            // Get the trailer from the trailer adapter
            Trailer trailer = mTrailerAdapter.getItem(position);
            // Open the movie trailer video
            watchTrailer(trailer.getKey());
        }
    };

    /** Create the favourite movie button on click lister */
    private final Button.OnClickListener favouriteMovieButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            // Check if the movie is already in the favourite movie list
            if (FavouriteMoviesHandler.isFavourite(mActivity, mMovie)) {

                // Remove the movie from the favourite movie list
                FavouriteMoviesHandler.removeMovie(mActivity, mMovie);
                // Set the favourite icon tint to grey
                favouriteMovieIcon.setImageTintList(ColorStateList.valueOf(getResources()
                        .getColor(R.color.colorGrey)));
                // Notify the user with a toast
                Toast.makeText(mActivity, "Removed from the favourite movies collection.", Toast.LENGTH_LONG).show();
            } else {

                // Add the movie to the favourite movie list
                FavouriteMoviesHandler.addMovie(mActivity, mMovie);
                // Set the favourite icon tint to red
                favouriteMovieIcon.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                // Notify the user with a toast
                Toast.makeText(mActivity, "Added to the favourite movies collection.", Toast.LENGTH_LONG).show();
            }
        }
    };

    /** Open the trailer video on YouTube */
    public void watchTrailer(String trailerKey) {
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(GlobalConstant.VND_YOUTUBE + trailerKey));
        } catch (ActivityNotFoundException e) {
            intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(GlobalConstant.HTTP_WWW_YOUTUBE_COM_WATCH_V + trailerKey));
        } finally {
            if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
                // Start the activity that will open the YouTube app
                // or the YouTube website
                startActivity(intent);
            } else {
                Toast.makeText(mActivity, "No application on your phone can open the trailers, " +
                        "download a browser.", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Fetches the movie trailers from the api
     *
     * @param apiService is the api service interface
     * @param movieId the id of the movie
     */
    private void fetchTrailers(ApiInterface apiService, int movieId) {

        // Call the api service to get the trailers
        Call<ResponseTrailers> call = apiService.getTrailers(movieId, GlobalConstant.TMDB_API_KEY);

        call.enqueue(new Callback<ResponseTrailers>() {
            @Override
            public void onResponse(Call<ResponseTrailers>call, Response<ResponseTrailers> response) {
                // Get the response's results
                ArrayList<Trailer> trailers = (ArrayList) response.body().getResults();

                // Initialize the trailer adapter, passing the trailers list
                mTrailerAdapter = new TrailerAdapter(mActivity, trailers);
                // Set the list view adapter
                trailersListView.setAdapter(mTrailerAdapter);
                // Modify the height of the list view
                Utility.setListViewHeightBasedOnItems(trailersListView);
            }

            @Override
            public void onFailure(Call<ResponseTrailers>call, Throwable t) {
                // Log error here since request failed
                Log.e("Retrofit Error[Trailer]", t.toString());
            }
        });
    }

    /**
     * Fetches the movie reviews from the api
     *
     * @param apiService is the api service interface
     * @param movieId the id of the movie
     */
    private void fetchReviews(ApiInterface apiService, int movieId) {

        // Call the api service to get the reviews
        Call<ResponseReviews> call = apiService.getReviews(movieId, GlobalConstant.TMDB_API_KEY);

        call.enqueue(new Callback<ResponseReviews>() {
            @Override
            public void onResponse(Call<ResponseReviews>call, Response<ResponseReviews> response) {
                // Get the response's results
                ArrayList<Review> reviews = (ArrayList) response.body().getResults();

                // Initialize the trailer adapter, passing the trailers list
                mReviewAdapter = new ReviewAdapter(mActivity, reviews);

                // Loop through the item in the review adapter
                for (int i = 0; i < mReviewAdapter.getCount(); i++) {
                    // Get the view of the item
                    View view = mReviewAdapter.getView(i, null, reviewsLinearLayout);
                    reviewsLinearLayout.addView(view);
                }
            }

            @Override
            public void onFailure(Call<ResponseReviews>call, Throwable t) {
                // Log error here since request failed
                Log.e("Retrofit Error[Review]", t.toString());
            }
        });
    }

    // Initialize a connection state broadcast receiver
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Utility.isOnline(context)) {
                if (mMovie != null) {
                    fetchTrailers(mApiService, mMovie.getId());
                    fetchReviews(mApiService, mMovie.getId());
                }

            } else {
                Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        }
    };
}
