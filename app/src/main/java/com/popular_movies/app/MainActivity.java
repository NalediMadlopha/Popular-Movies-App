/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Provides the main activity
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the action bar from the support library
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        // Resize the action bar title
        actionBar.setTitle(Html.fromHtml("<small>Popular Movies</small>"));

        // Gets the sort order preference
        String sortOrder = Utils.getSortOrderPref(this);

        if (sortOrder != null) {
            // Set the sub title based on the sort order preference
            switch (sortOrder) {
                case "Most Popular": // In case the sort order is set to popular mMovieList
                    actionBar.setSubtitle(Html.fromHtml("<small>Most Popular</small>"));
                    break;
                case "Top Rated": // In case the sort order is set to top rated mMovieList
                    actionBar.setSubtitle(Html.fromHtml("<small>Top Rated</small>"));
                    break;
                case "Favourites":  // In case the sort order is set to favourite mMovieList
                    actionBar.setSubtitle(Html.fromHtml("<small>Favourites</small>"));
                    break;
            }
        }

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
}
