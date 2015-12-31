package com.sgmasterappsgmail.The90DayChallenge.recivers_service;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationManagerCompat;
//import android.util.Log;

import com.sgmasterappsgmail.The90DayChallenge.R;
import com.sgmasterappsgmail.The90DayChallenge.Tools.Alarm;
import com.sgmasterappsgmail.The90DayChallenge.Tools.MySharedPref;
import com.sgmasterappsgmail.The90DayChallenge.activitys.MainActivity;


public class MyIntentService extends IntentService {

    //private static final String TAG = MyIntentService.class.getSimpleName();

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Log.d(TAG, "MyIntentService");
        String myData = intent.getStringExtra(Alarm.TITTLE);
        int notifyInt = intent.getIntExtra(Alarm.ID, 0);
        int not = intent.getIntExtra("not", 0);
        if (not == 1 && MySharedPref.getIntSharedPref(this, MySharedPref.CHECK_IF_NOT_DONE_EMPTY, 0) == 0) {
            return;
        }
        //Log.d(TAG, not + "service");
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentTitle("Reminder");
        if (myData != null) {
            builder.setContentText(myData);
        }
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.ic_stat_action_alarm);
        builder.setWhen(0);
        builder.setSound(sound);
        Intent notifyIntent = new Intent(this, MainActivity.class);
        if (not == 1) {
            notifyIntent.putExtra("notdone", true);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(notifyInt, notificationCompat);

    }
}
