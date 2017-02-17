package com.example.cutransit.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by JingJin on 2/13/17.
 */

public class DataContract {

    public static final String CONTENT_AUTHORITY = "com.example.cutransit.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_STOPS = "stops";
    public static final String PATH_FAVORITES = "favorites";

    /* Inner class that defines the table contents of the stop table */
    public static final class StopEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_STOPS)
                .build();
        public static final String TABLE_NAME = "stop";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CODE = "code";
        public static final String COLUMN_DISTANCE = "distance";
        public static final String COLUMN_FAVORITE = "favorite";

        public static Uri buildUri(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }
    }
}
