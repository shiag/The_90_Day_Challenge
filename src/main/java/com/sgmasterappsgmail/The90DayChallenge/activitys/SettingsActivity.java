package com.sgmasterappsgmail.The90DayChallenge.activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.sgmasterappsgmail.The90DayChallenge.R;
import com.sgmasterappsgmail.The90DayChallenge.Tools.Alarm;
import com.sgmasterappsgmail.The90DayChallenge.Tools.MySharedPref;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class SettingsActivity extends AppCompatActivity {
    //private static final String TAG = SettingsActivity.class.getSimpleName();
    private TextView notifyN1, notifyN2, notifyD1, notifyD2, notifyW1, notifyW2, status, setN, setD, setW;
    private CheckBox checkBoxN, checkBoxD, checkBoxW;
    private long nightHour, dayHour;
    private int weekDayNum;
    private static final int lightGrayColor = Color.parseColor("#FFCECECE");
    private static final int darkGrayColor = Color.parseColor("#FF6C6C6C");
    private static final int primaryColor = Color.parseColor("#8f3e97");
    private static final int darkGrayColorTittle = Color.parseColor("#FF595959");
    private TimePicker timePicker;
    private Calendar calendar;
    private String todayDate, dayPeriod;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        initReminderTime();
        setUpGui();
        initCheckBox();
        //Log.d(TAG, "hour is " + nightHour);
        //Log.d(TAG, "CHECK is " + checkD);
    }

    private void initCheckBox() {
        checkBoxN.setChecked(MySharedPref.getBoolSharedPref(this, MySharedPref.NIGHT_CHECK, true));
        checkBoxD.setChecked(MySharedPref.getBoolSharedPref(this, MySharedPref.DAY_CHECK, true));
        checkBoxW.setChecked(MySharedPref.getBoolSharedPref(this, MySharedPref.WEEK_CHECK, true));
        setNonClickListener(1);
        setDonClickListener(2);
        setWonClickListener();
    }

    private void initReminderTime() {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 0);
        nightHour = MySharedPref.getLongSharedPref(this, MySharedPref.NIGHT_TIME, calendar.getTimeInMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 13);
        calendar.set(Calendar.MINUTE, 0);
        dayHour = MySharedPref.getLongSharedPref(this, MySharedPref.DAY_TIME, calendar.getTimeInMillis());
        weekDayNum = MySharedPref.getIntSharedPref(this, MySharedPref.WEEK_DAY_NUM, 10);
    }

    private void setUpGui() {
        notifyN1 = (TextView) findViewById(R.id.notfy_n_1);
        notifyN2 = (TextView) findViewById(R.id.notfy_n_2);
        notifyD1 = (TextView) findViewById(R.id.notfy_d_1);
        notifyD2 = (TextView) findViewById(R.id.notfy_d_2);
        notifyW1 = (TextView) findViewById(R.id.notfy_w_1);
        notifyW2 = (TextView) findViewById(R.id.notfy_w_2);
        status = (TextView) findViewById(R.id.new_cycle);
        setN = (TextView) findViewById(R.id.notfy_n_set);
        setD = (TextView) findViewById(R.id.notfy_d_set);
        setW = (TextView) findViewById(R.id.notfy_w_set);
        checkBoxN = (CheckBox) findViewById(R.id.checkBox_n);
        checkBoxD = (CheckBox) findViewById(R.id.checkBox_d);
        checkBoxW = (CheckBox) findViewById(R.id.checkBox_w);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        todayDate = getResources().getString(R.string.notify_n) + new SimpleDateFormat(" h:mm a.", Locale.ENGLISH).format(nightHour);
        notifyN2.setText(todayDate);
        todayDate = getResources().getString(R.string.notify_d) + new SimpleDateFormat(" h:mm a.", Locale.ENGLISH).format(dayHour);
        notifyD2.setText(todayDate);
        dayPeriod = getResources().getString(R.string.notify_w1) + " " + weekDayNum + " " + getResources().getString(R.string.notify_w2);
        notifyW2.setText(dayPeriod);
        checkIfNightChecked();
        checkIfWChecked();
        checkIfDayChecked();
        statusClick();
    }

    private void statusClick() {
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, StatusActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkIfDayChecked() {

        checkBoxD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MySharedPref.putBoolSharedPref(SettingsActivity.this, MySharedPref.DAY_CHECK, isChecked);
                if (MySharedPref.getBoolSharedPref(SettingsActivity.this, MySharedPref.DAY_CHECK, true)) {
                    notifyD1.setTextColor(darkGrayColorTittle);
                    notifyD2.setTextColor(darkGrayColor);
                    setD.setTextColor(primaryColor);
                    setD.setClickable(true);
                    setD.setClickable(true);
                    Alarm.createAlarmForDay(SettingsActivity.this);
                } else {
                    Alarm.createAlarmForDay(SettingsActivity.this).cancel();
                    notifyD1.setTextColor(lightGrayColor);
                    notifyD2.setTextColor(lightGrayColor);
                    setD.setTextColor(lightGrayColor);
                    setD.setClickable(false);
                }
            }
        });

    }

    private void checkIfNightChecked() {
        checkBoxN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MySharedPref.putBoolSharedPref(SettingsActivity.this, MySharedPref.NIGHT_CHECK, isChecked);
                if (MySharedPref.getBoolSharedPref(SettingsActivity.this, MySharedPref.NIGHT_CHECK, true)) {
                    notifyN1.setTextColor(darkGrayColorTittle);
                    notifyN2.setTextColor(darkGrayColor);
                    setN.setTextColor(primaryColor);
                    setN.setClickable(true);
                    setN.setClickable(true);
                    Alarm.createAlarmForNight(SettingsActivity.this);

                } else {
                    Alarm.createAlarmForNight(SettingsActivity.this).cancel();
                    notifyN1.setTextColor(lightGrayColor);
                    notifyN2.setTextColor(lightGrayColor);
                    setN.setTextColor(lightGrayColor);
                    setN.setClickable(false);
                }
            }
        });
    }

    private void checkIfWChecked() {
        checkBoxW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MySharedPref.putBoolSharedPref(SettingsActivity.this, MySharedPref.WEEK_CHECK, isChecked);
                if (MySharedPref.getBoolSharedPref(SettingsActivity.this, MySharedPref.WEEK_CHECK, true)) {
                    notifyW1.setTextColor(darkGrayColorTittle);
                    notifyW2.setTextColor(darkGrayColor);
                    setW.setTextColor(primaryColor);
                    setW.setClickable(true);
                    Alarm.createAlarmForWeek(SettingsActivity.this);

                } else {
                    Alarm.createAlarmForWeek(SettingsActivity.this).cancel();
                    notifyW1.setTextColor(lightGrayColor);
                    notifyW2.setTextColor(lightGrayColor);
                    setW.setTextColor(lightGrayColor);
                    setW.setClickable(false);
                }
            }
        });

    }

    private void setDonClickListener(final int i) {
        setD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerPopUp(i);
            }
        });
    }

    private void setNonClickListener(final int i) {
        setN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerPopUp(i);
            }
        });

    }

    private void setWonClickListener() {
        setW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp();
            }
        });
    }


    private void showTimePickerPopUp(final int i) {
        final Calendar cal = Calendar.getInstance();
        LayoutInflater inflater = SettingsActivity.this.getLayoutInflater();
        View timeDialogView = inflater.inflate(R.layout.time_picker, null);
        timePicker = (TimePicker) timeDialogView.findViewById(R.id.timePicker);
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setTitle("Set Reminder Time");
        builder.setView(timeDialogView);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cal.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                cal.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                if (i == 1) {
                    // Log.d(TAG, "NIGHT");
                    MySharedPref.putIntSharedPref(SettingsActivity.this, MySharedPref.NIGHT_HOUR, timePicker.getCurrentHour());
                    MySharedPref.putIntSharedPref(SettingsActivity.this, MySharedPref.NIGHT_MIN, timePicker.getCurrentMinute());
                    Alarm.createAlarmForNight(SettingsActivity.this);
                    MySharedPref.putLongSharedPref(SettingsActivity.this, MySharedPref.NIGHT_TIME, cal.getTimeInMillis());
                    nightHour = MySharedPref.getLongSharedPref(SettingsActivity.this, MySharedPref.NIGHT_TIME, (long) 0);
                    todayDate = getResources().getString(R.string.notify_n) + new SimpleDateFormat(" h:mm a.", Locale.ENGLISH).format(nightHour);
                    notifyN2.setText(todayDate);
                } else {
                    // Log.d(TAG, "DAY");
                    MySharedPref.putIntSharedPref(SettingsActivity.this, MySharedPref.DAY_HOUR, timePicker.getCurrentHour());
                    MySharedPref.putIntSharedPref(SettingsActivity.this, MySharedPref.DAY_MIN, timePicker.getCurrentMinute());
                    Alarm.createAlarmForDay(SettingsActivity.this);
                    MySharedPref.putLongSharedPref(SettingsActivity.this, MySharedPref.DAY_TIME, cal.getTimeInMillis());
                    dayHour = MySharedPref.getLongSharedPref(SettingsActivity.this, MySharedPref.DAY_TIME, (long) 0);
                    todayDate = getResources().getString(R.string.notify_d) + new SimpleDateFormat(" h:mm a.", Locale.ENGLISH).format(dayHour);
                    notifyD2.setText(todayDate);
                }
            }
        });

        builder.setNegativeButton("Cancel", null);

        builder.show();
    }

    private void showPopUp() {
        LayoutInflater inflater = this.getLayoutInflater();
        View dayDialogView = inflater.inflate(R.layout.set_days, null);
        final EditText days = (EditText) dayDialogView.findViewById(R.id.day_edit);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Days");
        builder.setView(dayDialogView);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!days.getText().toString().equals("")) {
                    MySharedPref.putIntSharedPref(SettingsActivity.this, MySharedPref.WEEK_DAY_NUM, Integer.valueOf(days.getText().toString()));
                    Alarm.createAlarmForWeek(SettingsActivity.this);
                    weekDayNum = MySharedPref.getIntSharedPref(SettingsActivity.this, MySharedPref.WEEK_DAY_NUM, 0);
                    dayPeriod = getResources().getString(R.string.notify_w1) + " " + weekDayNum + " " + getResources().getString(R.string.notify_w2);
                    notifyW2.setText(dayPeriod);
                }
            }
        });

        builder.setNegativeButton("Cancel", null);

        builder.show();
    }


}


