package com.popular_movies.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.popular_movies.database.ContractMovie.MovieEntry;
import com.popular_movies.model.Movie;

import java.util.ArrayList;

/**
 * Created by root on 2016/09/21.
 */
public class DataSourceMovie {

    private SQLiteDatabase db;
    private MovieDbHelper movieDbHelper;
    private String[] allColumns = {
            MovieEntry._ID,
            MovieEntry.COLUMN_MOVIE_ID,
            MovieEntry.COLUMN_POSTER_PATH,
            MovieEntry.COLUMN_BACKDROP_PATH,
            MovieEntry.COLUMN_ORIGINAL_TITLE,
            MovieEntry.COLUMN_TITLE,
            MovieEntry.COLUMN_OVERALL,
            MovieEntry.COLUMN_GENRE,
            MovieEntry.COLUMN_RELEASE_DATE,
            MovieEntry.COLUMN_POPULARITY,
            MovieEntry.COLUMN_VOTE_COUNT,
            MovieEntry.COLUMN_VOTE_AVERAGE,
            MovieEntry.COLUMN_MOVIE_CATEGORY
    };

    public DataSourceMovie(Context context) {
        movieDbHelper = new MovieDbHelper(context);
    }

    public void open() throws SQLException {
        db = movieDbHelper.getWritableDatabase();
    }

    public void close() {
        movieDbHelper.close();
    }

    public Movie addMovie(Movie movie) {
        ContentValues values = new ContentValues();

        values.put(MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        values.put(MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        values.put(MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackDropPath());
        values.put(MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(MovieEntry.COLUMN_TITLE, movie.getTitle());
        values.put(MovieEntry.COLUMN_OVERALL, movie.getOverall());
//        values.put(MovieEntry.COLUMN_GENRE, movie.getGenre());
        values.put(MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        values.put(MovieEntry.COLUMN_POPULARITY, movie.getPopularity());
        values.put(MovieEntry.COLUMN_VOTE_COUNT, movie.getVoteCount());
        values.put(MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());

        long insertId = db.insert(MovieEntry.TABLE_NAME, null,
                values);
        Cursor cursor = db.query(MovieEntry.TABLE_NAME,
                allColumns, MovieEntry._ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Movie newMovie = cursorToMovie(cursor);
        cursor.close();

        return newMovie;
    }

    public void truncate() {
        db.execSQL("DELETE FROM " + MovieEntry.TABLE_NAME);
    }

    public ArrayList<Movie> getMovies(String selection, String[] selectionArgs) {
        ArrayList<Movie> movies = new ArrayList<>();

        Cursor cursor = db.query(MovieEntry.TABLE_NAME,
                allColumns, selection, selectionArgs, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Movie movie = cursorToMovie(cursor);

            movies.add(movie);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return movies;
    }

    private Movie cursorToMovie(Cursor cursor) {
        Movie movie = new Movie();

//        movie.setId(cursor.getString(
//                cursor.getColumnIndexOrThrow(ContractMovie.MovieEntry.COLUMN_MOVIE_ID))
//        );
//        movie.setPosterPath(cursor.getString(
//                cursor.getColumnIndexOrThrow(ContractMovie.MovieEntry.COLUMN_POSTER_PATH))
//        );
//        movie.setBackDropPath(cursor.getString(
//                cursor.getColumnIndexOrThrow(ContractMovie.MovieEntry.COLUMN_BACKDROP_PATH))
//        );
//        movie.setOriginalTitle(cursor.getString(
//                cursor.getColumnIndexOrThrow(ContractMovie.MovieEntry.COLUMN_ORIGINAL_TITLE))
//        );
//        movie.setTitle(cursor.getString(
//                cursor.getColumnIndexOrThrow(ContractMovie.MovieEntry.COLUMN_TITLE))
//        );
//        movie.setOverall(cursor.getString(
//                cursor.getColumnIndexOrThrow(ContractMovie.MovieEntry.COLUMN_OVERALL))
//        );
////        movie.setGenre(cursor.getString(
////                cursor.getColumnIndexOrThrow(ContractMovie.MovieEntry.COLUMN_GENRE))
////        );
//        movie.setReleaseDate(cursor.getString(
//                cursor.getColumnIndexOrThrow(ContractMovie.MovieEntry.COLUMN_RELEASE_DATE))
//        );
//        movie.setPopularity(cursor.getString(
//                cursor.getColumnIndexOrThrow(ContractMovie.MovieEntry.COLUMN_POPULARITY))
//        );
//        movie.setVoteCount(cursor.getString(
//                cursor.getColumnIndexOrThrow(ContractMovie.MovieEntry.COLUMN_VOTE_COUNT))
//        );
//        movie.setVoteAverage(cursor.getString(
//                cursor.getColumnIndexOrThrow(ContractMovie.MovieEntry.COLUMN_VOTE_AVERAGE))
//        );
//        movie.setCategory(cursor.getString(
//                cursor.getColumnIndexOrThrow(MovieEntry.COLUMN_MOVIE_CATEGORY))
//        );
        return movie;
    }
}
