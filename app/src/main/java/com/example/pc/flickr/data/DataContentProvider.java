package com.example.pc.flickr.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.UnsupportedSchemeException;
import android.net.Uri;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by deepn on 9/14/2017.
 */

public class DataContentProvider extends ContentProvider {

    private MovieDbHelper movieDbHelper;
    public static final int API_DATA = 100;
    public static final int API_DATA_ID = 101;
    public static final UriMatcher sUriMatcher =buildUriMatcher();
    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieDbApiContract.AUTHORITY, MovieDbApiContract.PATH_API_DATA, API_DATA);
        uriMatcher.addURI(MovieDbApiContract.AUTHORITY, MovieDbApiContract.PATH_API_DATA + "/#", API_DATA_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {

        Context context = getContext();
        movieDbHelper = new MovieDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = movieDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);


        Cursor retCursor;
        switch (match){
            case API_DATA:
                retCursor = db.query(MovieDbApiContract.ApiData.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknow Uri: " + uri);

        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        Uri returnUri ;
        switch (match){
            case API_DATA:
                long id = db.insert(MovieDbApiContract.ApiData.TABLE_NAME,null, values);
                if (id > 0){
                    returnUri = ContentUris.withAppendedId(MovieDbApiContract.ApiData.CONTENT_URI, id);
                }
                else {
                    throw  new android.database.SQLException("Falide to add celibrity") ;
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknow Uri: " + uri);

        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
