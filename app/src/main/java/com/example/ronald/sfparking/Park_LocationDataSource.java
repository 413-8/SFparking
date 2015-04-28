package com.example.ronald.sfparking;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

/**
 * Created by KingRat on 4/26/2015.
 */
public class Park_LocationDataSource {

        private SQLiteDatabase database;
        private MySQLiteHelper dbHelper;
        private String [] allColumns = {
                SqliteSchema.SqlEntry.COLUMN_ID,
                SqliteSchema.SqlEntry.COLUMN_LONGITUDE,
                SqliteSchema.SqlEntry.COLUMN_LATITUDE,
                SqliteSchema.SqlEntry.COLUMN_STREET,
                SqliteSchema.SqlEntry.COLUMN_ON_OFF,
                SqliteSchema.SqlEntry.COLUMN_TIME
        };

        public Park_LocationDataSource(Context context){dbHelper = new MySQLiteHelper(context);}

        public void read() {database = dbHelper.getReadableDatabase();}

        public void write() {database = dbHelper.getWritableDatabase();}

        public void close() {dbHelper.close();}

        public void createLocationInfo(LocationInfo locationinfo){

            ContentValues values = new ContentValues();
            values.put(SqliteSchema.SqlEntry.COLUMN_LONGITUDE, locationinfo.getLongitude());
            values.put(SqliteSchema.SqlEntry.COLUMN_LATITUDE, locationinfo.getLatitude());
            values.put(SqliteSchema.SqlEntry.COLUMN_STREET, locationinfo.getStreetname());
            values.put(SqliteSchema.SqlEntry.COLUMN_ON_OFF, locationinfo.getOn_off_street());
            values.put(SqliteSchema.SqlEntry.COLUMN_TIME, locationinfo.getTime());

            database.insert(SqliteSchema.SqlEntry.TABLE_NAME,null,values);
            long insertId = database.insert(SqliteSchema.SqlEntry.TABLE_NAME, null, values);
            Cursor cursor = database.query(SqliteSchema.SqlEntry.TABLE_NAME, allColumns, SqliteSchema.SqlEntry.COLUMN_ID + " = " + insertId, null, null, null, null );
            cursor.moveToNext();

        }
        public LocationInfo getLocationInfo(int id){
            Cursor cursor = database.query(SqliteSchema.SqlEntry.TABLE_NAME, allColumns, SqliteSchema.SqlEntry.COLUMN_ID + " = " + new String []{String.valueOf(id)}, null, null, null, null );
            cursor.moveToPosition(id);
            LocationInfo locationInfo = new LocationInfo(cursor.getInt(0), cursor.getDouble(1), cursor.getDouble(2), cursor.getString(3), cursor.getString(4),cursor.getString(5));
            return locationInfo;
        }
        public void deleteLocationInfo(LocationInfo locationinfo){
            long id = locationinfo.getId();
            database.delete(SqliteSchema.SqlEntry.TABLE_NAME, SqliteSchema.SqlEntry.COLUMN_ID + " = " + id, null);
            database.close();
        }

}
