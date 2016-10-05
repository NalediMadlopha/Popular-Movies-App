/*
 * Copy (C) 2016 Popular Movies Udacity Project 1
 */
package com.popular_movies.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.popular_movies.app.ActivityDetail;
import com.popular_movies.app.FragmentDetail;
import com.popular_movies.app.GlobalConstant;
import com.popular_movies.app.R;
import com.popular_movies.app.Utility;
import com.popular_movies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Naledi Madlopha on 2016/07/28.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private Context context;

    public MoviesAdapter(Context context, List<Movie> movies) {
        this.movies = movies;
        this.context = context;
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        public ImageView poster;
        public TextView title;
        public TextView genre;
        public TextView releaseDate;

        public MovieViewHolder(View v) {
            super(v);

            poster = (ImageView) v.findViewById(R.id.movie_poster_thumbnail);
            title = (TextView) v.findViewById(R.id.movie_title);
            genre = (TextView) v.findViewById(R.id.movie_genre);
            releaseDate = (TextView) v.findViewById(R.id.movie_release_date);
        }
    }

    @Override
    public MoviesAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        Picasso.with(this.context).load(movies.get(position).getPosterPath()).into(holder.poster);
        holder.title.setText(movies.get(position).getTitle());
        holder.genre.setText(Utility.getGenreNames(this.context, movies.get(position).getGenreIds()));
        holder.releaseDate.setText(movies.get(position).getReleaseDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentDetail fragmentDetail = new FragmentDetail();

                // Supply index input as an argument.
                Bundle args = new Bundle();
                args.putParcelable(GlobalConstant.MOVIE, movies.get(position));
                fragmentDetail.setArguments(args);

                // Replace the movie details container framelayout with the details fragment
//                context.getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.movie_detail_container, fragmentDetail, GlobalConstant.MOVIE)
//                        .commit();

                // Start the details activity and pass the movie to it
                Intent intent = new Intent(context, ActivityDetail.class);
                intent.putExtra(GlobalConstant.MOVIE, movies.get(position));

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
