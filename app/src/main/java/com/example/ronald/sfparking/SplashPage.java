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
 * shows splash for brief period while background tasks are started, then starts app
 */
public class SplashPage extends Activity {

    private ProgressDialog progressDialog;
    final Context sp = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this, "Gps is enabled on your device", Toast.LENGTH_SHORT);
        } else{
            showGPSDisabledAlert();
        }

        new LoadViewTask().execute();
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

    /*Background loading*/
    private class LoadViewTask extends AsyncTask<Void, Integer, Void>{
        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(SplashPage.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setTitle("Loading...");
            progressDialog.setMessage("Loading application View, please wait...");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            //progressDialog.setProgress(0);
            //progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params){
            try {
                synchronized (this){
                    //int counter = 0;

                    //while(counter <=4 ){
                    wait(850);
                    ///  counter ++;
                    //publishProgress(counter * 25);
                    //}
                }
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                Intent open = new Intent(sp, MapsActivity.class);
                startActivity(open);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values){
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result){
            progressDialog.dismiss();
            setContentView(R.layout.activity_splash);
        }
    }
        /*final Context sp = this;
        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 1 seconds
                    sleep(1000);

                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    Intent open = new Intent(sp, MapsActivity.class);
                    startActivity(open);
                }
            }
        };
        background.start();
    }
    protected void onPause(){
        super.onPause();
        finish();
    }*/
}