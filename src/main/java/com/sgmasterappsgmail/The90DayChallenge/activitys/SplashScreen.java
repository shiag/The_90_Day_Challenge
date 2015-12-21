package com.sgmasterappsgmail.The90DayChallenge.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
