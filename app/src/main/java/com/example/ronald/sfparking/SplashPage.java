package com.example.ronald.sfparking;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by saralove on 4/22/2015.
 * splash page for the application.
 * shows splash for a brief period while background tasks are started, then starts app
 */
public class SplashPage extends Activity {

    //A progressDialog object
    private ProgressDialog progressDialog;

    final Context sp = this;

    //Called when the app starts up
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //LocationManager object
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //Check to see if GPS is enabled, if not then prompt the user to enable it
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this, "Gps is enabled on your device", Toast.LENGTH_SHORT);
            new LoadViewTask().execute();
        } else{
            setContentView(R.layout.activity_splash);
            showGPSDisabledAlert();
        }
    }

    /*GPS Alert*/
    private void showGPSDisabledAlert(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Gps is disabled.  Would you like to enable it?")
                .setCancelable(false).setPositiveButton("Go to settings to enable GPS", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                Intent callGPS = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(callGPS);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    /**
     * Background Loading
     */
    private class LoadViewTask extends AsyncTask<Void, Integer, Void> {

        /**
         *Execute before the background starts loading
         */
        @Override
        protected void onPreExecute(){

            setContentView(R.layout.activity_splash);
            super.onPreExecute();
        }

        /**
         * Loads the database and map, then starts up the app
         */
        @Override
        protected Void doInBackground(Void... params){
            try {
                synchronized (this){
                        wait(1500);
                }
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                Intent open = new Intent(sp, MapsActivity.class);
                startActivity(open);
            }
            return null;
        }

    }

    /**
     * Pauses on the splash screen temporarily then move onto the map.
     */
    protected void onPause(){
        super.onPause();
        finish();
    }
}