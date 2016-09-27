/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.app;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;

/**
 * Provides the details activity
 */
public class ActivityDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setupActionBar();

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            // Get the parcelable movie and put it in a bundle
            arguments.putParcelable(GlobalConstant.MOVIE,
                    getIntent().getParcelableExtra(GlobalConstant.MOVIE));

            // Create a new details fragment
            FragmentDetail fragmentDetail = new FragmentDetail();
            // Set the fragment arguments
            fragmentDetail.setArguments(arguments);

            // Add the fragment to the movie details container (Framelayout)
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragmentDetail)
                    .commit();
        }
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Resize the action bar title
            actionBar.setTitle(Html.fromHtml("<small>Movie Details</small>"));
        }
    }
}
