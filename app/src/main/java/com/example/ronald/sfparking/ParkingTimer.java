package com.example.ronald.sfparking;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * The countdown timer that gets set when a user presses the park button.
 */
public class ParkingTimer extends Service {

    CountDownTimer countDown;
    long millis;

    public static final int NOTIFICATION_ID = 1;

    @Override
    public void onCreate(){
        super.onCreate();

        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, "Timer Set", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {

        millis = intent.getLongExtra("millis", 0);
        countDown = new CountDownTimer(millis, 1000) {
            @Override
            public void onTick(long l) {}

            @Override
            public void onFinish() {
                Log.d("mytag", "finished");
                sendNotification();
                stopSelf();

                    }
                };

        countDown.start();

        return START_NOT_STICKY;
    }

    /**
     *
     */
    public void sendNotification(){
        Intent intent = new Intent(this, ParkingTimer.class);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle("Time's Up");
        builder.setContentText("Move your car!!!");
        builder.setDefaults(Notification.DEFAULT_ALL);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}