package com.example.nandita.practiceapp1;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by nandita on 2/22/2015.
 */
public class AlarmNotficationService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AlarmNotficationService(String name) {
        super(name);
    }
    public AlarmNotficationService() {
        super("AlarmNotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("HERE", "OnHandleIntent");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this);
        mBuilder.setSmallIcon(R.drawable.ic_launcher);
        mBuilder.setContentTitle("You got notification");
        mBuilder.setContentText("Alarm from practice app : " + intent.getExtras().getString("ALARM DESC"));
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());


    }
}
