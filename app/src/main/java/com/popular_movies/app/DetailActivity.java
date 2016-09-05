/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Provides the details activity
 */
public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            // Get the parcelable movie and put it in a bundle
            arguments.putParcelable(GlobalConstant.sMOVIE,
                    getIntent().getParcelableExtra(GlobalConstant.sMOVIE));

            // Create a new details fragment
            DetailFragment detailFragment = new DetailFragment();
            // Set the fragment arguments
            detailFragment.setArguments(arguments);

            // Add the fragment to the movie details container (Framelayout)
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, detailFragment)
                    .commit();
        }
    }
}
