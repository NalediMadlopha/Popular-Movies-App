package com.popular_movies.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.popular_movies.database.ContractReview.ReviewEntry;
import com.popular_movies.model.Review;

import java.util.ArrayList;

/**
 * Created by root on 2016/09/21.
 */
public class DataSourceReview {

    private SQLiteDatabase db;
    private MovieDbHelper movieDbHelper;
    private String[] allColumns = {
        ReviewEntry._ID,
        ReviewEntry.COLUMN_MOVIE_ID,
        ReviewEntry.COLUMN_AUTHOR,
        ReviewEntry.COLUMN_CONTENT
    };

    public DataSourceReview(Context context) {
        movieDbHelper = new MovieDbHelper(context);
    }

    public void open() throws SQLException {
        db = movieDbHelper.getWritableDatabase();
    }

    public void close() {
        movieDbHelper.close();
    }

    public Review addReview(Review review) {
        ContentValues values = new ContentValues();

        values.put(ReviewEntry.COLUMN_MOVIE_ID, review.getMovieId());
        values.put(ReviewEntry.COLUMN_AUTHOR, review.getAuthor());
        values.put(ReviewEntry.COLUMN_CONTENT, review.getContent());

        long insertId = db.insert(ReviewEntry.TABLE_NAME, null,
                values);
        Cursor cursor = db.query(ReviewEntry.TABLE_NAME,
                allColumns, ReviewEntry._ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Review newReview = cursorToReview(cursor);
        cursor.close();

        return newReview;
    }

    public ArrayList<Review> getReviews(String selection, String[] selectionArgs) {
        ArrayList<Review> trailerArrayList = new ArrayList<>();

        Cursor cursor = db.query(ReviewEntry.TABLE_NAME,
                allColumns, selection, selectionArgs, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Review review = cursorToReview(cursor);

            trailerArrayList.add(review);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return trailerArrayList;
    }

    public void truncate() {
        db.execSQL("DELETE FROM " + ReviewEntry.TABLE_NAME);
    }

    private Review cursorToReview(Cursor cursor) {
        Review review = new Review();

        review.setId(cursor.getString(
                cursor.getColumnIndexOrThrow(ReviewEntry._ID))
        );
        review.setMovieId(cursor.getString(
                cursor.getColumnIndexOrThrow(ReviewEntry.COLUMN_MOVIE_ID))
        );
        review.setAuthor(cursor.getString(
                cursor.getColumnIndexOrThrow(ReviewEntry.COLUMN_AUTHOR))
        );
        review.setContent(cursor.getString(
                cursor.getColumnIndexOrThrow(ReviewEntry.COLUMN_CONTENT))
        );

        return review;
    }
}
