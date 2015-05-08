package com.example.ronald.sfparking;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;
import android.view.View;

import java.util.Stack;

import static com.example.ronald.sfparking.SqliteSchema.*;

/**
 * Created by Dylan on 4/26/2015.
 * Interacts with the database on the device. All CRUD methods are defined here for tables in the
 * database.
 */
public class Park_LocationDataSource {
        private static int oldestID =1;
        private SQLiteDatabase database;
        private MySQLiteHelper dbHelper;
        private String [] allColumns = getColumns();
        public Park_LocationDataSource(Context context){dbHelper = MySQLiteHelper.getInstance(context);}

        public void read() {database = dbHelper.getReadableDatabase();}

        public void write() {database = dbHelper.getWritableDatabase();}

        public void open() throws SQLException {
            database = dbHelper.getWritableDatabase();
        }

        public void close() {dbHelper.close();}

    /**
     * takes information from a LocationInfo object and stores it as a new row in the table.
     * maximum number of stored entries:5
     * @param locationinfo the LocationInfo object to be added to the table in a new row.
     */
        public void createLocationInfo(LocationInfo locationinfo){
            ContentValues values = new ContentValues();
            values.put(SqlEntry.COLUMN_LONGITUDE, locationinfo.getLongitude());
            values.put(SqlEntry.COLUMN_LATITUDE, locationinfo.getLatitude());
            values.put(SqlEntry.COLUMN_STREET, locationinfo.getStreetname());
            values.put(SqlEntry.COLUMN_ON_OFF, locationinfo.getOn_off_street());
            values.put(SqlEntry.COLUMN_TIME, locationinfo.getTime());
            Cursor cursor = database.query(
                    SqlEntry.TABLE_NAME,
                    allColumns,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            if(cursor.getCount()>4) {
                database.update(SqlEntry.TABLE_NAME, values, SqlEntry.COLUMN_ID + " = " + oldestID, null);
                oldestID=(oldestID+1)%6;
                System.out.print(oldestID);
                if(oldestID==0)
                    oldestID =1;
                cursor.close();
            }else{
                long insertId = database.insert(SqlEntry.TABLE_NAME, null, values);
                cursor.close();
            }


        }


    /**
     * Updates a specified row of the SQLite table with a new LocationInfo object.
     * use database.update if you want to update by using the id to find the value to replace.
     * @param newLoc The new LocationInfo object to update the row.
     */
    public void updateLocationInfoRow( LocationInfo newLoc, int row){
        //UPDATE TABLE_NAME SET longitude=newLoc.longitude WHERE id=
        ContentValues values = new ContentValues();
        values.put(SqlEntry.COLUMN_LONGITUDE, newLoc.getLongitude());
        values.put(SqlEntry.COLUMN_LATITUDE, newLoc.getLatitude());
        values.put(SqlEntry.COLUMN_STREET, newLoc.getStreetname());
        values.put(SqlEntry.COLUMN_ON_OFF, newLoc.getOn_off_street());
        values.put(SqlEntry.COLUMN_TIME, newLoc.getTime());
        database.update(SqlEntry.TABLE_NAME, values, SqlEntry.COLUMN_ID + " = " + row, null);
    }

    /**
     * Retrieves a LocationInfo object from the history table.
     * @param id the id of the LocationInfo object requested
     * @return the LocationInfo Object requested from the table.
     */
        public LocationInfo getLocationInfo(int id){
            Cursor cursor = database.query(
                    SqlEntry.TABLE_NAME,
                    allColumns,
                    SqlEntry.COLUMN_ID + "=" + id,
                    null,
                    null,
                    null,
                    null

            );
            cursor.moveToFirst();
            LocationInfo location = new LocationInfo();
            location = cursorToLocationInfo(cursor);
            cursor.close();
            return location;
        }

    /**
     * deletes the LocationInfo object from the history table
     * @param locationinfo the locationInfo Object to be deleted.
     */
        public void deleteLocationInfo(LocationInfo locationinfo){
            long id = locationinfo.getId();
            database.delete(SqlEntry.TABLE_NAME, SqlEntry.COLUMN_ID + " = " + id, null);
            database.close();
        }

    /**
     * gets all locations in the database table as a stack with the newest entry on top.
     * @return a stack of all of the locations in the database history table.
     */
        public Stack<LocationInfo> getAllLocations() {
            /*
            starts by pointing at oldest entry and adding it to stack.
            then loops, adding the LocationInfo objects, checking id and breaking if it encounters
            the oldest entry's id again.
             */
            LocationInfo location;
            Stack<LocationInfo> locations = new Stack<LocationInfo>();
            String seeker = SqlEntry.COLUMN_ID + " = " + oldestID;
            Cursor cursor = database.query(
                    SqlEntry.TABLE_NAME,
                    allColumns,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            cursor.moveToFirst();
            if (cursor.isNull(0)) {
               return locations;
            }
            if(oldestID==1)
            {
                while(!cursor.isAfterLast())
                {
                    location = cursorToLocationInfo(cursor);
                    locations.push(location);
                    cursor.moveToNext();
                }
                cursor.close();
                return locations;
            }
            cursor.move((oldestID-1));
            location = cursorToLocationInfo(cursor);
            locations.push(location);
            cursor.moveToNext();
            if(cursor.isAfterLast())
                cursor.moveToFirst();
            while (cursor.getInt(cursor.getColumnIndex(SqlEntry.COLUMN_ID))!=oldestID) {
                location = cursorToLocationInfo(cursor);
                locations.push(location);
                cursor.moveToNext();
                if(cursor.isAfterLast()){
                    cursor.moveToFirst();
                }
            }
            cursor.close();
            return locations;
        }

        /**
         * Takes a Cursor object pointing to a row of the locations database and parses the data into
         * a new LocationInfo object, which is returned on completion.
         * @param cursor Cursor object pointing to row containing LocationInfo data
         * @return LocationInfo object containing row data pointed to by cursor
         */
        private LocationInfo cursorToLocationInfo(Cursor cursor) {
            LocationInfo location = new LocationInfo();

            location.setId( cursor.getInt(cursor.getColumnIndex(SqlEntry.COLUMN_ID)) );
            location.setLongitude( cursor.getDouble(cursor.getColumnIndex(SqlEntry.COLUMN_LONGITUDE)) );
            location.setLatitude( cursor.getDouble(cursor.getColumnIndex(SqlEntry.COLUMN_LATITUDE)) );
            location.setStreet_name( cursor.getString(cursor.getColumnIndex(SqlEntry.COLUMN_STREET)) );
            location.setOn_off_street( cursor.getString(cursor.getColumnIndex(SqlEntry.COLUMN_ON_OFF)) );
            location.setTime( cursor.getString(cursor.getColumnIndex(SqlEntry.COLUMN_TIME)) );

            return location;
        }


}
