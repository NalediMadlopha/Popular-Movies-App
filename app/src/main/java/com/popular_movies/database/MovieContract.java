package com.popular_movies.database;

import android.provider.BaseColumns;

/**
 * Created by root on 2016/09/19.
 */
public final class MovieContract {
    /*
     * To prevent someone from accidentally instantiating the contract class,
     * make the constructor private
     */
    private MovieContract() {}

    /* Inner class that defines the table contents */
    public static class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movie";
        public static final String _ID = "_id";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_date";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_OVERALL = "overall";
        public static final String COLUMN_GENRE = "genre";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_MOVIE_CATEGORY = "movie_category";
    }
}
