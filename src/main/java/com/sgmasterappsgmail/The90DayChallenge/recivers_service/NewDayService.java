
package com.sgmasterappsgmail.The90DayChallenge.recivers_service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.sgmasterappsgmail.The90DayChallenge.Tools.MySharedPref;
import com.sgmasterappsgmail.The90DayChallenge.data.DailyTodoContentProvider;
import com.sgmasterappsgmail.The90DayChallenge.models.TodoDaily;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class NewDayService extends IntentService {

    public NewDayService() {
        super("NewDayService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Set sendToTom = new HashSet<>(MySharedPref.getSetSharedPref(this,
                MySharedPref.SET_OF_SHIFT, new HashSet<String>()));
        for (int i = 0; i < 3; i++) {
            TodoDaily daily = new TodoDaily(i + 1);
            daily.setDate(System.currentTimeMillis());
            if (!sendToTom.isEmpty()) {
                Iterator<String> iter = sendToTom.iterator();
                String d = iter.next();
                String[] goal = d.split(",");
                daily.setTittle(goal[0]);
                daily.setDescription(goal[1]);
                sendToTom.remove(d);
            }
            getContentResolver().insert(DailyTodoContentProvider.CONTENT_URI, daily.toContentValues());
        }
        MySharedPref.putSetSharedPref(this, MySharedPref.SET_OF_SHIFT, sendToTom);
    }

}

