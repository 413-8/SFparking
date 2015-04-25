package com.example.ronald.sfparking;

import android.content.Context;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
/**
 * Created by saralove on 4/22/2015.
 * splash page for the application.
 * shows splash for brief period while background tasks are started, then starts app
 */
public class SplashPage extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final Context sp = this;
        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(5000);

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
    }
}