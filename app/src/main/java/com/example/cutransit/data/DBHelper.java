package com.example.cutransit.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cutransit.data.DataContract.FavoriteEntry;
import com.example.cutransit.data.DataContract.StopEntry;


/**
 * Created by JingJin on 2/13/17.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "cutransit.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_STOP_TABLE =
                "CREATE TABLE " + StopEntry.TABLE_NAME + " (" +
                        StopEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        StopEntry.COLUMN_ID         + " TEXT NOT NULL, "                     +
                        StopEntry.COLUMN_NAME       + " TEXT NOT NULL,"                      +
                        StopEntry.COLUMN_CODE       + " TEXT NOT NULL, "                     +
                        StopEntry.COLUMN_DISTANCE   + " REAL NOT NULL, "                     +
                        " UNIQUE (" + StopEntry.COLUMN_ID + ") ON CONFLICT REPLACE);";

        /*
        final String SQL_CREATE_STOP_DETAIL_TABLE =
                "CREATE TABLE " + StopDetailEntry.TABLE_NAME + " (" +
                        StopDetailEntry._ID         + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        StopDetailEntry.COLUMN_CODE + " TEXT NOT NULL, "                     +
                        StopDetailEntry.COLUMN_ID   + " TEXT NOT NULL,"                      +
                        StopDetailEntry.COLUMN_LAT  + " REAL NOT NULL, "                     +
                        StopDetailEntry.COLUMN_LON  + " REAL NOT NULL, "                     +
                        " FOREIGN KEY (" + StopDetailEntry.COLUMN_CODE  + ") REFERENCES " +
                        StopEntry.TABLE_NAME + " (" + StopEntry.COLUMN_CODE + "); ";
        */

        final String SQL_CREATE_FAVORITE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + FavoriteEntry.TABLE_NAME             + "  (" +
                        FavoriteEntry._ID           + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FavoriteEntry.COLUMN_ID   + " TEXT NOT NULL, "                       +
                        " FOREIGN KEY (" + FavoriteEntry.COLUMN_ID  + ") REFERENCES " +
                        StopEntry.TABLE_NAME + " (" + StopEntry.COLUMN_ID + ")); ";

        db.execSQL(SQL_CREATE_STOP_TABLE);
//        db.execSQL(SQL_CREATE_STOP_DETAIL_TABLE);
        db.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + StopEntry.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + StopDetailEntry.TABLE_NAME);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
