package com.example.ronald.sfparking;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.location.Location;

import java.util.List;


/**
 * Created by Karl on 4/23/2015.
 */
public class histDBhandler extends SQLiteOpenHelper{


    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "location_history";

    // history table name
    private static final String TABLE_LOCATIONS = "locations";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_TIME = "time";
    private static final String KEY_STREET_NAME = "street_name";
    private static final String KEY_ON_OFF_STREET = "on/off_street";



    public histDBhandler (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOCATION_TABLE = "CREATE TABLE " + TABLE_LOCATIONS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_LONGITUDE + " TEXT,"
                + KEY_LATITUDE + " TEXT," + KEY_TIME + " TEXT," + KEY_STREET_NAME
                + " TEXT," + KEY_ON_OFF_STREET + " TEXT" + ")";
        db.execSQL(CREATE_LOCATION_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);

        // Create tables again
        onCreate(db);
    }

    /* Adding new contact
    public void addLocation(Location contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        Location values = new Location();
        values.(KEY_NAME, contact.getName()); // Contact Name
        values.put(KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone Number

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }
     
    // Getting single contact
    public Location getContact(int id) {}
     
    // Getting All Contacts
    public List<Location> getAllContacts() {}
     
    // Getting contacts Count
    public int getContactsCount() {}
    // Updating single contact
    public int updateContact(Location contact) {}
    // Deleting single contact
    public void deleteContact(Location contact) {}
*/


}
