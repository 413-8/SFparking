package com.example.ronald.sfparking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dylan on 4/24/2015.
 */
public class DataBaseHandler extends SQLiteOpenHelper {


    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "location_information";

    // history table name
    private static final String TABLE_LOCATIONS = "locations";

    // Contacts Table Columns names
    private static final String KEY_ID = "id",
            KEY_LONGITUDE = "longitude",
            KEY_LATITUDE = "latitude",
            KEY_TIME = "time",
            KEY_STREET_NAME = "street_name",
            KEY_ON_OFF_STREET = "on/off_street";



    public DataBaseHandler (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * creates the database upon program startup.
     * @param db the database that will be created.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOCATION_TABLE = "CREATE TABLE " + TABLE_LOCATIONS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_LONGITUDE + " TEXT,"
                + KEY_LATITUDE + " TEXT," + KEY_TIME + " TEXT," + KEY_STREET_NAME
                + " TEXT," + KEY_ON_OFF_STREET + " TEXT" + ")";
        db.execSQL(CREATE_LOCATION_TABLE);
    }

    /**
     * If the app is modified by an upgrade, it will delete the old table and create a new one.
     * @param db    The database that will be cleared and re-created.
     * @param oldVersion previous app version
     * @param newVersion new app version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);

        // Create tables again
        onCreate(db);
    }

    /**
     * creates a new Contentvalues object that will hold the info to put in the table.
     * once relevant fields are set, it inserts the ContentValues object into the table as a new
     * row.
     * @param locationinfo the locationinfo object that will be put in the table.
     */
    public void createLocation(LocationInfo locationinfo){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_LONGITUDE, locationinfo.getLongitude());
        values.put(KEY_LATITUDE, locationinfo.getLatitude());
        values.put(KEY_ON_OFF_STREET, locationinfo.getOn_off_street());
        values.put(KEY_TIME, locationinfo.getTime());
        values.put(KEY_STREET_NAME, locationinfo.getStreetname());

        db.insert(TABLE_LOCATIONS, null, values);
        db.close();
    }

    /**
     * A cursor is made that points to the row associated with an id in the table.
     * a LocationInfo object is created and modified.
     * @param id
     * @return
     */
    public LocationInfo getLocationInfo(int id){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_LOCATIONS, new String[] {KEY_ID, KEY_LATITUDE, KEY_LONGITUDE, KEY_ON_OFF_STREET, KEY_STREET_NAME, KEY_TIME},KEY_ID + "=?", new String[] {String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        LocationInfo locationinfo = new LocationInfo(Integer.parseInt(cursor.getString(0)),Double.parseDouble(cursor.getString(1)),Double.parseDouble(cursor.getString(2)),cursor.getString(3),cursor.getString(4),cursor.getString(5));
        db.close();
        cursor.close();
        return locationinfo;

    }

    public void deleteLocationInfo(LocationInfo locationInfo){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_LOCATIONS,KEY_ID + "=?", new String[] {String.valueOf(locationInfo.getId())});
        db.close();
    }

    public  int getLocationInfoCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM" + TABLE_LOCATIONS,null);
        int count = cursor.getCount();
        db.close();
        cursor.close();
        return count;

    }

    public int updateLocationInfo(LocationInfo locationinfo) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_LONGITUDE, locationinfo.getLongitude());
        values.put(KEY_LATITUDE, locationinfo.getLatitude());
        values.put(KEY_ON_OFF_STREET, locationinfo.getOn_off_street());
        values.put(KEY_TIME, locationinfo.getTime());
        values.put(KEY_STREET_NAME, locationinfo.getStreetname());

        return db.update(TABLE_LOCATIONS, values, KEY_ID + "=?", new String[]{String.valueOf(locationinfo.getId())});
    }
    public List<LocationInfo> getAllLocationInfo(){
        List<LocationInfo> locationInfo  = new ArrayList<LocationInfo>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM" + TABLE_LOCATIONS, null);
        if (cursor.moveToFirst()){
            do {
                LocationInfo locationinfo = new LocationInfo(Integer.parseInt(cursor.getString(0)),Double.parseDouble(cursor.getString(1)),Double.parseDouble(cursor.getString(2)),cursor.getString(3),cursor.getString(4),cursor.getString(5));
                locationInfo.add(locationinfo);

            }
            while(cursor.moveToNext());
        }
        return locationInfo;
    }




}

