package com.example.pc.flickr.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by deepn on 9/13/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "movieDb.db";
    public static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_MOVIEDB = " CREATE TABLE " + MovieDbApiContract.ApiData.TABLE_NAME +
                "("+ MovieDbApiContract.ApiData._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieDbApiContract.ApiData.COLUMN_ID + " TEXT NOT NULL, " +
                MovieDbApiContract.ApiData.COLUMN_NAME + " TEXT NOT NULL, " +
                MovieDbApiContract.ApiData.COLUMN_POPULARITY + " TEXT NOT NULL," +
                MovieDbApiContract.ApiData.COLUMN_TYPE + " TEXT NOT NULL, " +
                MovieDbApiContract.ApiData.COLUMN_TYPE_SUB +  " TEXT NOT NULL, " +
                MovieDbApiContract.ApiData.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                MovieDbApiContract.ApiData.COLUMN_WISH_LIST +  " BOOLEAN NOT NULL, " +
                MovieDbApiContract.ApiData.COLUMN_IMG_URL +  " TEXT NOT NULL, " +
                " UNIQUE ("+ MovieDbApiContract.ApiData.COLUMN_ID +","+ MovieDbApiContract.ApiData.COLUMN_TYPE_SUB +
                ") ON CONFLICT IGNORE"
                +")";
        db.execSQL(SQL_MOVIEDB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieDbApiContract.ApiData.TABLE_NAME);
            onCreate(db);
    }
}
