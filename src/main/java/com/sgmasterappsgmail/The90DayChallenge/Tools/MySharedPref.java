
package com.sgmasterappsgmail.The90DayChallenge.Tools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Set;

/**
 * Created by shia on 11/5/2015.
 */
public class MySharedPref {
    public static final String CHECK_IF_NOT_DONE_EMPTY = "notDone";
    public static final String NIGHT_TIME = "nightTime";
    public static final String DAY_TIME = "dayTime";
    public static final String NIGHT_HOUR = "night_hour";
    public static final String NIGHT_MIN = "night_min";
    public static final String DAY_HOUR = "day_hour";
    public static final String DAY_MIN = "day_min";
    public static final String WEEK_DAY_NUM = "weekTime";
    public static final String NIGHT_CHECK = "nightCheck";
    public static final String DAY_CHECK = "dayCheck";
    public static final String WEEK_CHECK = "weekCheck";
    public static final String CHECK_APPS_FIRST_LAUNCH = "firstLaunch";
    public static final String SET_OF_SHIFT = "setForShift";

    public static void putIntSharedPref(Context context, String key, int value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt(key, value);
        edit.apply();
    }

    public static int getIntSharedPref(Context context, String key, int _default) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(key, _default);
    }

    public static void putSetSharedPref(Context context, String key, Set<String> value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putStringSet(key, value);
        edit.apply();
    }

    public static Set<String> getSetSharedPref(Context context, String key, Set<String> _default) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getStringSet(key, _default);
    }

    public static void putLongSharedPref(Context context, String key, long value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putLong(key, value);
        edit.apply();
    }

    public static long getLongSharedPref(Context context, String key, Long _default) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(key, _default);
    }

    public static void putBoolSharedPref(Context context, String key, boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean(key, value);
        edit.apply();
    }

    public static boolean getBoolSharedPref(Context context, String key, boolean _default) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, _default);
    }


    public static void sendEmail(Context context) {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("message/rfc822");
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"mauricestein@gmail.com"});
        email.putExtra(Intent.EXTRA_SUBJECT, "Appointment");
        email.putExtra(Intent.EXTRA_TEXT, "I would like to make a appointment ");
        context.startActivity(email);
       /* try {
            context.startActivity(Intent.createChooser(i, "Choose an email client from..."));

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "No email client installed.",
                    Toast.LENGTH_LONG).show();
        }*/
    }

}