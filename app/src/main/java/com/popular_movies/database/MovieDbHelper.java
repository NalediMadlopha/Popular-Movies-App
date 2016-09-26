package com.popular_movies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.popular_movies.database.MovieContract.MovieEntry;
import com.popular_movies.database.TrailerContract.TrailerEntry
        ;

/**
 * Created by root on 2016/09/14.
 */
public class MovieDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PopularMovie.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMM_SEP = ", ";

    private static final String SQL_CREATE_MOVIE_TABLE =
            "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
            MovieEntry._ID + " INTEGER PRIMARY KEY, " +
            MovieEntry.COLUMN_MOVIE_ID + TEXT_TYPE + COMM_SEP +
            MovieEntry.COLUMN_POSTER_PATH + TEXT_TYPE + COMM_SEP +
            MovieEntry.COLUMN_BACKDROP_PATH + TEXT_TYPE + COMM_SEP +
            MovieEntry.COLUMN_ORIGINAL_TITLE + TEXT_TYPE + COMM_SEP +
            MovieEntry.COLUMN_TITLE + TEXT_TYPE + COMM_SEP +
            MovieEntry.COLUMN_OVERALL + TEXT_TYPE + COMM_SEP +
            MovieEntry.COLUMN_GENRE + TEXT_TYPE + COMM_SEP +
            MovieEntry.COLUMN_RELEASE_DATE + TEXT_TYPE + COMM_SEP +
            MovieEntry.COLUMN_POPULARITY + TEXT_TYPE + COMM_SEP +
            MovieEntry.COLUMN_VOTE_COUNT + TEXT_TYPE + COMM_SEP +
            MovieEntry.COLUMN_VOTE_AVERAGE + TEXT_TYPE + COMM_SEP +
            MovieEntry.COLUMN_MOVIE_CATEGORY + TEXT_TYPE + " )";

    private static final String SQL_CREATE_TRAILER_TABLE =
            "CREATE TABLE " + TrailerEntry.TABLE_NAME + " (" +
                    TrailerEntry._ID + " INTEGER PRIMARY KEY, " +
                    TrailerEntry.COLUMN_MOVIE_ID + TEXT_TYPE + COMM_SEP +
                    TrailerEntry.COLUMN_ISO_639_1 + TEXT_TYPE + COMM_SEP +
                    TrailerEntry.COLUMN_ISO_3166_1 + TEXT_TYPE + COMM_SEP +
                    TrailerEntry.COLUMN_KEY + TEXT_TYPE + COMM_SEP +
                    TrailerEntry.COLUMN_NAME + TEXT_TYPE + COMM_SEP +
                    TrailerEntry.COLUMN_SITE + TEXT_TYPE + COMM_SEP +
                    TrailerEntry.COLUMN_SIZE + TEXT_TYPE + COMM_SEP +
                    TrailerEntry.COLUMN_TYPE + TEXT_TYPE + " )";

    private static final String SQL_DELETE_MOVIE_TABLE =
            "DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME;

    private static final String SQL_DELETE_TRAILER_TABLE =
            "DROP TABLE IF EXISTS " + TrailerEntry.TABLE_NAME;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_TRAILER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        // This database is only a cache for online data, so its upgrade policy
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_MOVIE_TABLE);
        db.execSQL(SQL_DELETE_TRAILER_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}
