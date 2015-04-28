package com.example.ronald.sfparking;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by KingRat on 4/26/2015.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSON = 1;
    public static final String DATABASE_NAME = "Parking_Information.db";
    public static final String TABLE_PARKINFO = "parking_info";

    public static final String KEY_ID = "_id";

    public static final String KEY_LONGITUDE = "Longitude",
        KEY_LATITUDE = "Latitude",
        KEY_TIME = "Time",
        KEY_STREET_NAME = "Street_name",
        KEY_ON_OFF_STREET = "on_off_Street";

    public static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_PARKINFO + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_LONGITUDE + " TEXT,"
            + KEY_LATITUDE + " TEXT,"
            + KEY_TIME + " TEXT,"
            + KEY_STREET_NAME + " TEXT,"
            + KEY_ON_OFF_STREET + " TEXT" + ");";
    public MySQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSON);
    }

    @Override
    public void onCreate(SQLiteDatabase database){database.execSQL(DATABASE_CREATE);}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.d(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS + TABLE_PARKINFO");
        onCreate(db);
    }

}
