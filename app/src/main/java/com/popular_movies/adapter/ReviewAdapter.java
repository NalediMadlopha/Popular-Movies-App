/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.popular_movies.app.R;
import com.popular_movies.model.Review;

import java.util.List;

/**
 * Created by Naledi Madlopha on 2016/08/14.
 */
public class ReviewAdapter extends ArrayAdapter<Review> {
    private static final String LOG_TAG = ReviewAdapter.class.getSimpleName();

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param reviews A List of Review objects to display in a list
     */
    public ReviewAdapter(Activity context, List<Review> reviews) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, reviews);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     *                    (search online for "android view recycling" to learn more)
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the Review object from the ArrayAdapter at the appropriate position
        Review review = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.review_item, parent, false);
        }

        TextView reviewName = (TextView) convertView.findViewById(R.id.review_author);
        reviewName.setText(review.getAuthor());

        TextView reviewMessage = (TextView) convertView.findViewById(R.id.review_content);
        reviewMessage.setText(review.getContent());

        return convertView;
    }
}
