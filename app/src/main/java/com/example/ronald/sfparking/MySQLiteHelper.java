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

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Parking_Information.db";

    private static MySQLiteHelper instanceDB;
    //constructor

    public static synchronized MySQLiteHelper getInstance(Context context)
    {
        if(instanceDB==null)
            instanceDB=new MySQLiteHelper(context);

        return instanceDB;
    }

    private MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Creates a table in the database to be used by the application.
     * @param database the Sqlite database to be created
     */
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(SqliteSchema.CREATE_TABLE);
    }

    /**
     *if the app is updated, it will delete the old table and make a new one.
     * @param database the database of the app.
     * @param oldVer the old version of the app.
     * @param newVer the new version of the app.
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVer, int newVer){
        Log.d(MySQLiteHelper.class.getName(), "Upgrading database from version " + oldVer + " to " +
                newVer + ", which will destroy all old data");
        database.execSQL(SqliteSchema.DELETE_TABLE);
        onCreate(database);
    }

}
