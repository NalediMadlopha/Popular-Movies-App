package com.popular_movies.database;

import android.provider.BaseColumns;

/**
 * Created by root on 2016/09/19.
 */
public final class TrailerContract {
    /*
     * To prevent someone from accidentally instantiating the contract class,
     * make the constructor private
     */
    private TrailerContract() {}

    /* Inner class that defines the table contents */
    public static class TrailerEntry implements BaseColumns {
        public static final String TABLE_NAME = "trailer";
        public static final String _ID = "_id";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_ISO_639_1 = "iso_639_1";
        public static final String COLUMN_ISO_3166_1 = "iso_3166_1";
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SITE = "site";
        public static final String COLUMN_SIZE = "size";
        public static final String COLUMN_TYPE = "type";
    }
}
