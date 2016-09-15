package com.popular_movies.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.popular_movies.model.Movie;

import java.util.ArrayList;

/**
 * Created by root on 2016/09/14.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "popularMovieDB.db";
    private static final String TABLE_POPULAR_MOVIE = "popular_movie";
    private static final String TABLE_TOP_RATED_MOVIE = "top_rated_movie";
    private static final String TABLE_FAVOURITE_MOVIE = "favourite_movie";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_POSTER_PATH = "poster_path";
    public static final String COLUMN_BACKDROP_DATE = "backdrop_date";
    public static final String COLUMN_ORIGINAL_TITLE = "original_title";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_OVERALL = "overall";
    public static final String COLUMN_GENRE = "genre";
    public static final String COLUMN_RELEASE_DATE = "release_date";
    public static final String COLUMN_POPULARITY = "popularity";
    public static final String COLUMN_VOTE_COUNT = "vote_count";
    public static final String COLUMN_VOTE_AVERAGE = "vote_average";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_POPULAR_MOVIE_TABLE = "CREATE TABLE " +
                TABLE_POPULAR_MOVIE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_POSTER_PATH + " TEXT, "
                + COLUMN_BACKDROP_DATE + " TEXT, "
                + COLUMN_ORIGINAL_TITLE + " TEXT, "
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_OVERALL + " TEXT, "
                + COLUMN_GENRE + " TEXT, "
                + COLUMN_RELEASE_DATE + " TEXT, "
                + COLUMN_POPULARITY + " TEXT, "
                + COLUMN_VOTE_COUNT + " TEXT, "
                + COLUMN_VOTE_AVERAGE + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_POPULAR_MOVIE_TABLE);

        String CREATE_TOP_RATED_MOVIE_TABLE = "CREATE TABLE " +
                TABLE_TOP_RATED_MOVIE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_POSTER_PATH + " TEXT, "
                + COLUMN_BACKDROP_DATE + " TEXT, "
                + COLUMN_ORIGINAL_TITLE + " TEXT, "
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_OVERALL + " TEXT, "
                + COLUMN_GENRE + " TEXT, "
                + COLUMN_RELEASE_DATE + " TEXT, "
                + COLUMN_POPULARITY + " TEXT, "
                + COLUMN_VOTE_COUNT + " TEXT, "
                + COLUMN_VOTE_AVERAGE + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_TOP_RATED_MOVIE_TABLE);

        String CREATE_FAVOURITE_MOVIE_TABLE = "CREATE TABLE " +
                TABLE_FAVOURITE_MOVIE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_POSTER_PATH + " TEXT, "
                + COLUMN_BACKDROP_DATE + " TEXT, "
                + COLUMN_ORIGINAL_TITLE + " TEXT, "
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_OVERALL + " TEXT, "
                + COLUMN_GENRE + " TEXT, "
                + COLUMN_RELEASE_DATE + " TEXT, "
                + COLUMN_POPULARITY + " TEXT, "
                + COLUMN_VOTE_COUNT + " TEXT, "
                + COLUMN_VOTE_AVERAGE + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_FAVOURITE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POPULAR_MOVIE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOP_RATED_MOVIE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITE_MOVIE);
        onCreate(db);
    }

    public void addMovie(String movieCategory, Movie movie) {

        ContentValues values = new ContentValues();

        values.put(COLUMN_POSTER_PATH, movie.getPosterPath());
        values.put(COLUMN_BACKDROP_DATE, movie.getBackDropDate());
        values.put(COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(COLUMN_TITLE, movie.getTitle());
        values.put(COLUMN_OVERALL, movie.getOverall());
        values.put(COLUMN_GENRE, movie.getGenre());
        values.put(COLUMN_RELEASE_DATE, movie.getReleaseDate());
        values.put(COLUMN_POPULARITY, movie.getPopularity());
        values.put(COLUMN_VOTE_COUNT, movie.getVoteCount());
        values.put(COLUMN_VOTE_AVERAGE, movie.getVoteAverage());

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        switch (movieCategory) {
            case "Most Popular":
                sqLiteDatabase.insert(TABLE_POPULAR_MOVIE, null, values);
                break;
            case "Top Rated":
                sqLiteDatabase.insert(TABLE_TOP_RATED_MOVIE, null, values);
                break;
            case "Favourite":
                sqLiteDatabase.insert(TABLE_FAVOURITE_MOVIE, null, values);
                break;
        }

        sqLiteDatabase.close();
    }

    public Movie getMovie(String movieCategory, String movieId) {
        String query = null;

        switch (movieCategory) {
            case "Most Popular":
                query = "Select * FROM " + TABLE_POPULAR_MOVIE + " WHERE " + COLUMN_ID + " =  \"" + movieId + "\"";
                break;
            case "Top Rated":
                query = "Select * FROM " + TABLE_TOP_RATED_MOVIE + " WHERE " + COLUMN_ID + " =  \"" + movieId + "\"";
                break;
            case "Favourite":
                query = "Select * FROM " + TABLE_FAVOURITE_MOVIE + " WHERE " + COLUMN_ID + " =  \"" + movieId + "\"";
                break;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Movie movie = new Movie();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            movie.setId(cursor.getString(0));
            movie.setPosterPath(cursor.getString(1));
            movie.setBackDropDate(cursor.getString(2));
            movie.setOriginalTitle(cursor.getString(3));
            movie.setTitle(cursor.getString(4));
            movie.setOverall(cursor.getString(5));
            movie.setGenre(cursor.getString(6));
            movie.setReleaseDate(cursor.getString(7));
            movie.setPopularity(cursor.getString(8));
            movie.setVoteCount(cursor.getString(9));
            movie.setVoteAverage(cursor.getString(10));
            cursor.close();
        } else {
            movie = null;
        }

        db.close();
        return movie;
    }

    public ArrayList<Movie> getMovies(String movieCategory) {
        ArrayList<Movie> movies = new ArrayList<>();
        String query = null;

        switch (movieCategory) {
            case "Most Popular":
                query = "Select * FROM " + TABLE_POPULAR_MOVIE;
                break;
            case "Top Rated":
                query = "Select * FROM " + TABLE_TOP_RATED_MOVIE;
                break;
            case "Favourite":
                query = "Select * FROM " + TABLE_FAVOURITE_MOVIE;
                break;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();

                movie.setId(cursor.getString(0));
                movie.setPosterPath(cursor.getString(1));
                movie.setBackDropDate(cursor.getString(2));
                movie.setOriginalTitle(cursor.getString(3));
                movie.setTitle(cursor.getString(4));
                movie.setOverall(cursor.getString(5));
                movie.setGenre(cursor.getString(6));
                movie.setReleaseDate(cursor.getString(7));
                movie.setPopularity(cursor.getString(8));
                movie.setVoteCount(cursor.getString(9));
                movie.setVoteAverage(cursor.getString(10));

                movies.add(movie);
            } while (cursor.moveToNext());

            cursor.close();
        } else {
            movies = null;
        }

        db.close();
        return movies;
    }

    public boolean clearTable(String movieCategory) {
        boolean result = false;

        SQLiteDatabase db = this.getWritableDatabase();

        switch (movieCategory) {
            case "Most Popular":
                db.execSQL("delete from "+ TABLE_POPULAR_MOVIE);
                result = true;
                break;
            case "Top Rated":
                db.execSQL("delete from "+ TABLE_TOP_RATED_MOVIE);
                result = true;
                break;
            case "Favourite":
                db.execSQL("delete from "+ TABLE_FAVOURITE_MOVIE);
                result = true;
                break;
        }

        return result;
    }

    public boolean removeFavouriteMovie(int movieId) {
        boolean result = false;

        String query = "SELECT * FROM " + TABLE_FAVOURITE_MOVIE + " WHERE " + COLUMN_ID + " = \"" + movieId + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Movie movie = new Movie();

        if (cursor.moveToFirst()) {
            movie.setId(cursor.getString(0));
            db.delete(TABLE_FAVOURITE_MOVIE, COLUMN_ID + " = ?",
                    new String[] { movie.getId() });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
}
