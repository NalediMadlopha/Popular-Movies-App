package com.popular_movies.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.popular_movies.database.ContractTrailer.TrailerEntry;
import com.popular_movies.model.Trailer;

import java.util.ArrayList;

/**
 * Created by root on 2016/09/21.
 */
public class DataSourceTrailer {

    private SQLiteDatabase db;
    private MovieDbHelper movieDbHelper;
    private String[] allColumns = {
        TrailerEntry._ID,
        TrailerEntry.COLUMN_MOVIE_ID,
        TrailerEntry.COLUMN_ISO_639_1,
        TrailerEntry.COLUMN_ISO_3166_1,
        TrailerEntry.COLUMN_KEY,
        TrailerEntry.COLUMN_NAME,
        TrailerEntry.COLUMN_SITE,
        TrailerEntry.COLUMN_SIZE,
        TrailerEntry.COLUMN_TYPE
    };

    public DataSourceTrailer(Context context) {
        movieDbHelper = new MovieDbHelper(context);
    }

    public void open() throws SQLException {
        db = movieDbHelper.getWritableDatabase();
    }

    public void close() {
        movieDbHelper.close();
    }

    public Trailer addTrailer(Trailer trailer) {
        ContentValues values = new ContentValues();

//        values.put(TrailerEntry.COLUMN_MOVIE_ID, trailer.getMovieId());
        values.put(TrailerEntry.COLUMN_ISO_639_1, trailer.getIso_639_1());
        values.put(TrailerEntry.COLUMN_ISO_3166_1, trailer.getIso_3166_1());
        values.put(TrailerEntry.COLUMN_KEY, trailer.getKey());
        values.put(TrailerEntry.COLUMN_NAME, trailer.getName());
        values.put(TrailerEntry.COLUMN_SITE, trailer.getSite());
        values.put(TrailerEntry.COLUMN_SIZE, trailer.getSize());
        values.put(TrailerEntry.COLUMN_TYPE, trailer.getType());

        long insertId = db.insert(TrailerEntry.TABLE_NAME, null,
                values);
        Cursor cursor = db.query(TrailerEntry.TABLE_NAME,
                allColumns, TrailerEntry._ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Trailer newTrailer = cursorToTrailer(cursor);
        cursor.close();

        return newTrailer;
    }

    public ArrayList<Trailer> getTrailers(String selection, String[] selectionArgs) {
        ArrayList<Trailer> trailerArrayList = new ArrayList<>();

        Cursor cursor = db.query(TrailerEntry.TABLE_NAME,
                allColumns, selection, selectionArgs, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Trailer trailer = cursorToTrailer(cursor);

            trailerArrayList.add(trailer);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return trailerArrayList;
    }

    public void truncate() {
        db.execSQL("DELETE FROM " + TrailerEntry.TABLE_NAME);
    }

    private Trailer cursorToTrailer(Cursor cursor) {
        Trailer trailer = new Trailer();

//        trailer.setId(cursor.getString(
//                cursor.getColumnIndexOrThrow(TrailerEntry._ID))
//        );
//        trailer.setMovieId(cursor.getString(
//                cursor.getColumnIndexOrThrow(TrailerEntry.COLUMN_MOVIE_ID))
//        );
        trailer.setIso_639_1(cursor.getString(
                cursor.getColumnIndexOrThrow(TrailerEntry.COLUMN_ISO_639_1))
        );
        trailer.setIso_3166_1(cursor.getString(
                cursor.getColumnIndexOrThrow(TrailerEntry.COLUMN_ISO_3166_1))
        );
        trailer.setKey(cursor.getString(
                cursor.getColumnIndexOrThrow(TrailerEntry.COLUMN_KEY))
        );
        trailer.setName(cursor.getString(
                cursor.getColumnIndexOrThrow(TrailerEntry.COLUMN_NAME))
        );
        trailer.setSite(cursor.getString(
                cursor.getColumnIndexOrThrow(TrailerEntry.COLUMN_SITE))
        );
        trailer.setSize(cursor.getString(
                cursor.getColumnIndexOrThrow(TrailerEntry.COLUMN_SIZE))
        );
        trailer.setType(cursor.getString(
                cursor.getColumnIndexOrThrow(TrailerEntry.COLUMN_TYPE))
        );

        return trailer;
    }
}
