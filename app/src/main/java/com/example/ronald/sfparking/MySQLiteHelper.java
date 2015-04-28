package com.example.ronald.sfparking;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Helper for creating and upgrading local SQLite database "Parking_Information.db". Uses string
 * constructors from SqliteSchema to create and upgrade (clear and recreate) the database.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSON = 1;
    public static final String DATABASE_NAME = "Parking_Information.db";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSON);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(SqliteSchema.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVer, int newVer){
        Log.d(MySQLiteHelper.class.getName(), "Upgrading database from version " + oldVer + " to " +
                newVer + ", which will destroy all old data");
        database.execSQL(SqliteSchema.DELETE_TABLE);
        onCreate(database);
    }

}
