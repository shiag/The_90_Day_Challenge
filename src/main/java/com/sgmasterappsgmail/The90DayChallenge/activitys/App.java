package com.sgmasterappsgmail.The90DayChallenge.activitys;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.sgmasterappsgmail.The90DayChallenge.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by shia on 11/4/2015.
 */
public class App extends Application {
    public enum TrackerName {
        APP_TRACKER
    }

    private Tracker tracker = null;

    public App() {
        super();
    }

    public synchronized Tracker getTracker() {
        if (tracker == null) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = analytics.newTracker(R.xml.my_tracker);
            t.enableAutoActivityTracking(true);
            t.enableExceptionReporting(true);
            tracker = t;
        }
        return tracker;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-Condensed.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }
}
