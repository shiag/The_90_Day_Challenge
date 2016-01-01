package com.sgmasterappsgmail.The90DayChallenge.activitys;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.sgmasterappsgmail.The90DayChallenge.R;
import com.sgmasterappsgmail.The90DayChallenge.Tools.PercentView;
import com.sgmasterappsgmail.The90DayChallenge.data.MySqlHelper;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class StatusActivity extends AppCompatActivity {
    private static final String TAG = "StatusActivity";
    private int done, help, notDone, empty, all, amountOfDays;
    private TextView dayAmount, perDone, perHelp, perNotDone, perEmpty;
    private PercentView customDone, customHelp, customNotDone, customEmpty;
    private MySqlHelper sqlHelper;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_activity);
        setUpGui();
        startBackground();
    }

    private void startBackground() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                Log.d(TAG, "in doInBackground");
                all = sqlHelper.getAllToDo();
                amountOfDays = sqlHelper.getAllDay() - 1;
                // should not be divided by 0
                if (sqlHelper.getAllDone() != 0)
                    done = (100 * sqlHelper.getAllDone()) / all;
                if (sqlHelper.getAllTodoHelp() != 0)
                    help = (100 * sqlHelper.getAllTodoHelp()) / all;
                if (sqlHelper.getAllTodoNotDone() != 0)
                    notDone = (100 * sqlHelper.getAllTodoNotDone()) / all;
                if (sqlHelper.getAllEmpty() != 0)
                    empty = (100 * sqlHelper.getAllEmpty()) / all;
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.d(TAG, "in onPostExecute");
                initTextView();
                initCustomView();
            }
        }.execute();
    }

    private void initTextView() {
        dayAmount.setText(amountOfDays + " Days");
        perDone.setText(done + "% is Done!");
        perHelp.setText(help + "% you need Help!");
        perNotDone.setText(notDone + "% is NotDone!");
        perEmpty.setText(empty + "% is Empty!");
    }

    private void initCustomView() {
        customDone.initWith(done);
        customHelp.initWith(help);
        customNotDone.initWith(notDone);
        customEmpty.initWith(empty);
    }

    private void setUpGui() {
        sqlHelper = new MySqlHelper(this);
        dayAmount = (TextView) findViewById(R.id.days_amount);
        perDone = (TextView) findViewById(R.id.per_done);
        perHelp = (TextView) findViewById(R.id.per_help);
        perNotDone = (TextView) findViewById(R.id.per_not_done);
        perEmpty = (TextView) findViewById(R.id.per_empty);
        customDone = (PercentView) findViewById(R.id.custom_done);
        customHelp = (PercentView) findViewById(R.id.custom_help);
        customNotDone = (PercentView) findViewById(R.id.custom_not_done);
        customEmpty = (PercentView) findViewById(R.id.custom_empty);
    }
}