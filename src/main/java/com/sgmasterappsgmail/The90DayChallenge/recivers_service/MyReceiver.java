package com.sgmasterappsgmail.The90DayChallenge.recivers_service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
//import android.util.Log;

import com.sgmasterappsgmail.The90DayChallenge.Tools.Alarm;
import com.sgmasterappsgmail.The90DayChallenge.Tools.MySharedPref;

public class MyReceiver extends BroadcastReceiver {
    //  private static final String TAG = MyReceiver.class.getSimpleName();

    public MyReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            //Log.d(TAG, "Phone booted");
            Alarm.checkForNewDay(context);
            if (MySharedPref.getBoolSharedPref(context, MySharedPref.NIGHT_CHECK, true))
                Alarm.createAlarmForNight(context);
            if (MySharedPref.getBoolSharedPref(context, MySharedPref.DAY_CHECK, true))
                Alarm.createAlarmForDay(context);
            if (MySharedPref.getBoolSharedPref(context, MySharedPref.WEEK_CHECK, true))
                Alarm.createAlarmForWeek(context);
        } else if (intent.getAction().equals("alarm")) {
            String myTittle = intent.getStringExtra(Alarm.TITTLE);
            int myId = intent.getIntExtra(Alarm.ID, 0);
            int not = intent.getIntExtra("not", 0);

            Intent hitService = new Intent(context, MyIntentService.class);
            if (myTittle != null) {
                hitService.putExtra(Alarm.TITTLE, myTittle);
                hitService.putExtra(Alarm.ID, myId);
                hitService.putExtra("not", not);
            }
            context.startService(hitService);
        } else if (intent.getAction().equals("new_item")) {
            Intent newDay = new Intent(context, NewDayService.class);
            context.startService(newDay);
        } else {
            Alarm.checkForNewDay(context);
            if (MySharedPref.getBoolSharedPref(context, MySharedPref.NIGHT_CHECK, false))
                Alarm.createAlarmForNight(context);
            if (MySharedPref.getBoolSharedPref(context, MySharedPref.DAY_CHECK, true))
                Alarm.createAlarmForDay(context);
            if (MySharedPref.getBoolSharedPref(context, MySharedPref.WEEK_CHECK, true))
                Alarm.createAlarmForWeek(context);
        }
    }
}

