/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.popular_movies.app.ActivityDetail;
import com.popular_movies.app.DetailFragment;
import com.popular_movies.app.GlobalConstant;
import com.popular_movies.app.R;
import com.popular_movies.app.Utility;
import com.popular_movies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Naledi Madlopha on 2016/07/28.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private List<Movie> mMovies;
    private Context mContext;

    public MoviesAdapter(Context context, List<Movie> movies) {
        this.mMovies = movies;
        this.mContext = context;
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.movie_poster_thumbnail) ImageView poster;
        @BindView(R.id.movie_title) TextView title;
        @BindView(R.id.movie_genre) TextView genre;
        @BindView(R.id.movie_release_date) TextView releaseDate;

        public MovieViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public MoviesAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        Picasso.with(this.mContext)
                .load(mMovies.get(position).getPosterPath())
                .placeholder(R.drawable.movie_icon)
                .into(holder.poster);
        holder.title.setText(mMovies.get(position).getTitle());
        holder.genre.setText(Utility.getGenreNames(this.mContext, mMovies.get(position).getGenreIds()));
        holder.releaseDate.setText(mMovies.get(position).getReleaseDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isTablet = mContext.getResources().getBoolean(R.bool.isTablet);

                if (isTablet) {
                    FragmentManager fragmentManager =
                            ((AppCompatActivity) mContext).getSupportFragmentManager();

                    DetailFragment detailFragment = new DetailFragment();

                    // Supply index input as an argument.
                    Bundle args = new Bundle();
                    args.putParcelable(GlobalConstant.MOVIE, mMovies.get(position));
                    detailFragment.setArguments(args);

                    // Add the fragment to the movie details container (Framelayout)
                    fragmentManager.beginTransaction()
                            .replace(R.id.detail_container, detailFragment)
                            .commit();
                } else {

                    // Start the details activity and pass the movie to it
                    Intent detailIntent = new Intent(mContext, ActivityDetail.class);
                    detailIntent.putExtra(GlobalConstant.MOVIE, mMovies.get(position));

                    mContext.startActivity(detailIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public List<Movie> getMovies() {
        return mMovies;
    }

    public void swap (ArrayList<Movie> movies) {
        if (mMovies != null) {
            mMovies.clear();
            mMovies.addAll(movies);
        } else {
            mMovies = movies;
        }
        notifyDataSetChanged();
    }
}
