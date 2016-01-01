package com.sgmasterappsgmail.The90DayChallenge.Tools;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
//import android.util.Log;

import java.util.Calendar;

/**
 * Created by shia on 12/8/2015.
 */
public class Alarm {
    public static final String TITTLE = "tittle";
    public static final String ID = "id";
    public static final int NOTIFICATION_REMINDER_NIGHT = 2;
    private static final int NOTIFICATION_REMINDER_WEEK = 4;
    private static final int NOTIFICATION_REMINDER_DAY = 3;

    public static void checkForNewDay(Context context) {
        Calendar calendar = Calendar.getInstance();
        Calendar current = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 0);
        if (calendar.compareTo(current) <= 0) {
            calendar.setTimeInMillis(System.currentTimeMillis() + 86400000);
            calendar.set(Calendar.HOUR_OF_DAY, 1);
            calendar.set(Calendar.MINUTE, 0);
        }
        Intent notifyIntent = new Intent("new_item");
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (context, NOTIFICATION_REMINDER_NIGHT, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 60 * 24, pendingIntent);
    }

    //it return a PendingIntent to be able to cancel it
    public static PendingIntent createAlarmForNight(Context context) {
        int hour = MySharedPref.getIntSharedPref(context, MySharedPref.NIGHT_HOUR, 21);
        int min = MySharedPref.getIntSharedPref(context, MySharedPref.NIGHT_MIN, 0);
        Calendar calendar = Calendar.getInstance();
        Calendar current = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        // it will set for tomorrow if the user pass in a time that passed already today
        if (calendar.compareTo(current) <= 0) {
            calendar.setTimeInMillis(System.currentTimeMillis() + 86400000);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, min);
        }

        Intent notifyIntent = new Intent("alarm");
        notifyIntent.putExtra(TITTLE, "Complete Goal's from today, and set Goals for tomorrow!");
        notifyIntent.putExtra(ID, NOTIFICATION_REMINDER_NIGHT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (context, NOTIFICATION_REMINDER_NIGHT, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 60 * 24, pendingIntent);
        return pendingIntent;
    }

    public static PendingIntent createAlarmForDay(Context context) {
        int hour = MySharedPref.getIntSharedPref(context, MySharedPref.DAY_HOUR, 13);
        int min = MySharedPref.getIntSharedPref(context, MySharedPref.DAY_MIN, 0);
        Calendar calendar = Calendar.getInstance();
        Calendar current = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        if (calendar.compareTo(current) <= 0) {
            calendar.setTimeInMillis(System.currentTimeMillis() + 86400000);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, min);
        }

        Intent notifyIntent = new Intent("alarm");
        notifyIntent.putExtra(TITTLE, "Start finishing today's Goals!");
        notifyIntent.putExtra(ID, NOTIFICATION_REMINDER_DAY);
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (context, NOTIFICATION_REMINDER_DAY, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 60 * 24, pendingIntent);
        return pendingIntent;
    }

    public static PendingIntent createAlarmForWeek(Context context) {
        int day = MySharedPref.getIntSharedPref(context, MySharedPref.WEEK_DAY_NUM, 10);
        Calendar calendar = Calendar.getInstance();
        long first = day * 86400000;
        long f = day * 24l;
        long repeat = 1000l * 60l * 60l * f;
        calendar.setTimeInMillis(System.currentTimeMillis() + first);
        Intent notifyIntent = new Intent("alarm");
        notifyIntent.putExtra(TITTLE, "You have Goals waiting to be completed. tap here to view");
        notifyIntent.putExtra(ID, NOTIFICATION_REMINDER_WEEK);
        // this is to know if its from this notification if yes to launch notDone adepter
        notifyIntent.putExtra("not", 1);

        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (context, NOTIFICATION_REMINDER_WEEK, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                repeat, pendingIntent);
        return pendingIntent;
    }
}


