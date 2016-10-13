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
public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setupActionBar();

        if (savedInstanceState == null) {
            // Add the fragment to the movie details container (Framelayout)
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_container, new DetailFragment())
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
