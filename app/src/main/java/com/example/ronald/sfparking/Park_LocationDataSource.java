package com.example.ronald.sfparking;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import java.util.Stack;

import static com.example.ronald.sfparking.SqliteSchema.*;

/**
 * Created by KingRat on 4/26/2015.
 */
public class Park_LocationDataSource {

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

        public void createLocationInfo(LocationInfo locationinfo){

            ContentValues values = new ContentValues();
            values.put(SqlEntry.COLUMN_LONGITUDE, locationinfo.getLongitude());
            values.put(SqlEntry.COLUMN_LATITUDE, locationinfo.getLatitude());
            values.put(SqlEntry.COLUMN_STREET, locationinfo.getStreetname());
            values.put(SqlEntry.COLUMN_ON_OFF, locationinfo.getOn_off_street());
            values.put(SqlEntry.COLUMN_TIME, locationinfo.getTime());

            long insertId = database.insert(SqlEntry.TABLE_NAME, null, values);
            Cursor cursor = database.query(
                    SqlEntry.TABLE_NAME,
                    allColumns,
                    SqlEntry.COLUMN_ID + " = " + insertId,
                    null,
                    null,
                    null,
                    null
            );
            cursor.moveToNext();

        }
        public LocationInfo getLocationInfo(int id){
            Cursor cursor = database.query(
                    SqlEntry.TABLE_NAME,
                    allColumns,
                    SqlEntry.COLUMN_ID + " = " + id,
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
        public void deleteLocationInfo(LocationInfo locationinfo){
            long id = locationinfo.getId();
            database.delete(SqlEntry.TABLE_NAME, SqlEntry.COLUMN_ID + " = " + id, null);
            database.close();
        }

        public Stack<LocationInfo> getAllLocations() {
            Stack<LocationInfo> locations = new Stack<LocationInfo>();

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
            while (!cursor.isAfterLast()) {
                LocationInfo location = cursorToLocationInfo(cursor);
                locations.push(location);
                cursor.moveToNext();
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