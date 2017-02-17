package com.example.cutransit.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by JingJin on 2/13/17.
 */

public class DataProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static final String authority = DataContract.CONTENT_AUTHORITY;
    private DBHelper mDBHelper;

    public static final int CODE_STOPS = 100;

     static {
        sUriMatcher.addURI(authority, DataContract.PATH_STOPS, CODE_STOPS);
    }

    @Override
    public boolean onCreate() {
        mDBHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case CODE_STOPS: {
                cursor = mDBHelper.getReadableDatabase().query(
                        DataContract.StopEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        throw new RuntimeException("We are not implementing getType in CUTransit.");
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        Uri retUri;
        long rowId;

        switch (sUriMatcher.match(uri)) {
            case CODE_STOPS:
                rowId = db.insert(DataContract.StopEntry.TABLE_NAME, null, values);
                if (rowId > 0)
                    retUri = DataContract.StopEntry.buildUri(rowId);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return retUri;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int rowsInserted = 0;

        switch (sUriMatcher.match(uri)) {
            case CODE_STOPS:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.StopEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                break;
            default:
                return super.bulkInsert(uri, values);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsInserted;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new RuntimeException("We are not implementing DELETE in CUTransit.");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int rowId;

        switch (sUriMatcher.match(uri)) {
            case CODE_STOPS:
                rowId = db.update(DataContract.StopEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowId != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowId;
    }
}
