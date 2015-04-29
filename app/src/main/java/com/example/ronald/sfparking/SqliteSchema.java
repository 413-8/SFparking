package com.example.ronald.sfparking;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Schema for table columns of database. Includes string constructors for SQL commands to
 * create and drop the table defined by the schema (implemented by the MySQLiteHelper class).
 */
public final class SqliteSchema {

    public SqliteSchema () {}

    public static abstract class SqlEntry implements BaseColumns {
        public static final String TABLE_NAME = "parking_info";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_STREET = "street";
        public static final String COLUMN_ON_OFF = "on_off";
        public static final String COLUMN_TIME = "time";
    }

    protected static final String CREATE_TABLE = "CREATE TABLE " + SqlEntry.TABLE_NAME + "(" +
            SqlEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            SqlEntry.COLUMN_LONGITUDE + " REAL, " +
            SqlEntry.COLUMN_LATITUDE + " REAL, " +
            SqlEntry.COLUMN_STREET + " TEXT, " +
            SqlEntry.COLUMN_ON_OFF + " TEXT, " +
            SqlEntry.COLUMN_TIME + " TEXT" +
            ");";

    protected static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + SqlEntry.TABLE_NAME;

    protected static String [] getColumns () {
        String [] col = {
                SqlEntry.COLUMN_ID,
                SqlEntry.COLUMN_LONGITUDE,
                SqlEntry.COLUMN_LATITUDE,
                SqlEntry.COLUMN_STREET,
                SqlEntry.COLUMN_ON_OFF,
                SqlEntry.COLUMN_TIME
        };

        return col;
    }

}
