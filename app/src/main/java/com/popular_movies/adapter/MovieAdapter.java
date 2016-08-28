package com.popular_movies.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.popular_movies.model.Movie;

import java.util.List;

/**
 * Created by Naledi Madlopha on 2016/07/28.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param movies A List of Movie objects to display in a list
     */
    public MovieAdapter(Activity context, List<Movie> movies) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, movies);
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
        // Gets the Movie object from the ArrayAdapter at the appropriate position
        Movie movie = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(
//                    R.layout.movie_item, parent, false);
//        }
//
//        ImageView poster = (ImageView) convertView.findViewById(R.id.movie_poster_thumbnail);
//        Picasso.with(getContext()).load(movie.getPosterPath()).into(poster);
//
//        TextView title = (TextView) convertView.findViewById(R.id.movie_title);
//        title.setText(movie.getTitle());
//
//        TextView genre = (TextView) convertView.findViewById(R.id.movie_genre);
//        genre.setText(movie.getGenre());
//
//        TextView releaseDate = (TextView) convertView.findViewById(R.id.release_date);
//        releaseDate.setText(movie.getReleaseDate());

        return convertView;
    }
}
