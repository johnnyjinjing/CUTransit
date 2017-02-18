package com.example.cutransit.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cutransit.data.DataContract.StopEntry;


/**
 * Created by JingJin on 2/13/17.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "cutransit.db";
    private static final int DATABASE_VERSION = 1;

    DBHelper(Context context) {
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
                        StopEntry.COLUMN_FAVORITE   + " INTEGER DEFAULT 0 NOT NULL, "        +
                        " UNIQUE (" + StopEntry.COLUMN_ID + ") ON CONFLICT IGNORE);";

        db.execSQL(SQL_CREATE_STOP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + StopEntry.TABLE_NAME);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
