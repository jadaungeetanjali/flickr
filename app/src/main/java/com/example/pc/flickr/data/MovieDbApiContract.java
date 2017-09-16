package com.example.pc.flickr.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by deepn on 9/13/2017.
 */

public class MovieDbApiContract {

    public static final String AUTHORITY = "com.example.pc.flickr";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_API_DATA = "api_data";

    public static final class ApiData implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_API_DATA).build();

        public static final String TABLE_NAME = "api_data";
        public static final String COLUMN_ID = "data_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_TYPE_ID = "type_id";
        public static final String COLUMN_TYPE_SUB = "type_sub";
        public static final String COLUMN_WISH_LIST = "wish_list";
        public static final String COLUMN_IMG_URL = "img_url";
    }
}
