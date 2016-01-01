package com.sgmasterappsgmail.The90DayChallenge.activitys;

import android.app.Application;

import com.sgmasterappsgmail.The90DayChallenge.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by shia on 11/4/2015.
 */
public class App extends Application {
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
