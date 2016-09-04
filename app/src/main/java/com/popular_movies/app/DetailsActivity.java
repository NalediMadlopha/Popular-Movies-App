package com.popular_movies.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.details_container, new DetailsFragment())
                    .commit();
        }

//        if (savedInstanceState == null) {
//            // During initial setup, plug in the details fragment.
//            DetailsFragment details = new DetailsFragment();
//            details.setArguments(getIntent().getExtras());
//            getFragmentManager().beginTransaction()
//                    .add(android.R.id.content, details)
//                    .commit();
//        }
    }
}
