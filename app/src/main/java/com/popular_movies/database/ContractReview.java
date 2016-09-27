package com.popular_movies.database;

import android.provider.BaseColumns;

/**
 * Created by root on 2016/09/19.
 */
public final class ContractReview {
    /*
     * To prevent someone from accidentally instantiating the contract class,
     * make the constructor private
     */
    private ContractReview() {}

    /* Inner class that defines the table contents */
    public static class ReviewEntry implements BaseColumns {
        public static final String TABLE_NAME = "review";
        public static final String _ID = "_id";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT= "content";
    }
}
