package com.example.ronald.sfparking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by KingRat on 4/26/2015.
 */
public class Park_LocationDataSource {

        private SQLiteDatabase database;
        private MySQLiteHelper dbHelper;
        private String [] allColumns = {MySQLiteHelper.KEY_ID, MySQLiteHelper.KEY_LONGITUDE, MySQLiteHelper.KEY_LATITUDE,MySQLiteHelper.KEY_TIME, MySQLiteHelper.KEY_STREET_NAME, MySQLiteHelper.KEY_ON_OFF_STREET};

        public Park_LocationDataSource(Context context){dbHelper = new MySQLiteHelper(context);}

        public void read() {database = dbHelper.getReadableDatabase();}

        public void write() {database =dbHelper.getWritableDatabase();}

        public void close(){dbHelper.close();}

        public void createLocationInfo(LocationInfo locationinfo){
            ContentValues values = new ContentValues();
            values.put(MySQLiteHelper.KEY_LATITUDE, locationinfo.getLatitude());
            values.put(MySQLiteHelper.KEY_LONGITUDE, locationinfo.getLongitude());
            values.put(MySQLiteHelper.KEY_ON_OFF_STREET, locationinfo.getOn_off_street());
            values.put(MySQLiteHelper.KEY_STREET_NAME, locationinfo.getStreetname());
            values.put(MySQLiteHelper.KEY_TIME, locationinfo.getTime());

            database.insert(MySQLiteHelper.TABLE_PARKINFO,null,values);
            long insertId = database.insert(MySQLiteHelper.TABLE_PARKINFO, null, values);
            Cursor cursor = database.query(MySQLiteHelper.TABLE_PARKINFO, allColumns, MySQLiteHelper.KEY_ID + " = " + insertId, null, null, null, null );
            cursor.moveToNext();

        }
        public LocationInfo getLocationInfo(int id){
            Cursor cursor = database.query(MySQLiteHelper.TABLE_PARKINFO, allColumns, MySQLiteHelper.KEY_ID + " = " + new String []{String.valueOf(id)}, null, null, null, null );
            cursor.moveToPosition(id);
            LocationInfo locationInfo = new LocationInfo(cursor.getInt(0), cursor.getDouble(1), cursor.getDouble(2), cursor.getString(3), cursor.getString(4),cursor.getString(5));
            return locationInfo;
        }
        public void deleteLocationInfo(LocationInfo locationinfo){
            long id = locationinfo.getId();
            database.delete(MySQLiteHelper.TABLE_PARKINFO, MySQLiteHelper.KEY_ID + " = " + id, null);
            database.close();
        }

}
