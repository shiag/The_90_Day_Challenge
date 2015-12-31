package com.sgmasterappsgmail.The90DayChallenge.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;

/**
 * Created by shia on 11/2/2015.
 */
public class MySqlHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "maurice_todo_daily.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TODO_DAILY = "todo_daily";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TITTLE = "tittle";
    public static final String COLUMN_GOAL = "goal";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DONE = "done";
    public static final String COLUMN_NOT_DONE = "notDone";
    public static final String COLUMN_HELP = "help";


    private static final String DATABASE_CREATE =
            "CREATE TABLE " + TABLE_TODO_DAILY
                    + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_DATE + " INTEGER , "
                    + COLUMN_TITTLE + " TEXT , "
                    + COLUMN_GOAL + " TEXT , "
                    + COLUMN_DONE + " INTEGER, "
                    + COLUMN_NOT_DONE + " INTEGER, "
                    + COLUMN_HELP + " INTEGER, "
                    + COLUMN_DESCRIPTION + " TEXT " +
                    ");";
    // private static final String TAG = "MySqlHelper";

    public MySqlHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Log.d(TAG, "DB created");
        db.execSQL(DATABASE_CREATE);
    }

    public int getAllDay() {
        String selectQuiry = "SELECT * FROM " + TABLE_TODO_DAILY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuiry, null);
        int count = cursor.getCount();
        db.close();

        return count / 3;
    }

    public int getAllToDo() {
        String selectQuiry = "SELECT * FROM " + TABLE_TODO_DAILY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuiry, null);
        int count = cursor.getCount();
        db.close();

        return count;
    }

    public int getAllTodoHelp() {
        String selectQuiry = "SELECT * FROM " + TABLE_TODO_DAILY + " WHERE " + COLUMN_HELP + " =1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuiry, null);
        int count = cursor.getCount();
        db.close();
        return count;
    }

    public int getAllTodoNotDone() {
        String selectQuiry = "SELECT * FROM " + TABLE_TODO_DAILY + " WHERE " + COLUMN_NOT_DONE + " =1 ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuiry, null);
        int count = cursor.getCount();
        db.close();
        return count;
    }

    public int getAllDone() {
        String selectQuiry = "SELECT * FROM " + TABLE_TODO_DAILY + " WHERE " + COLUMN_DONE + " =1 ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuiry, null);
        int count = cursor.getCount();
        db.close();
        return count;
    }

    public int getAllEmpty() {
        String selectQuiry = "SELECT * FROM " + TABLE_TODO_DAILY + " WHERE " + COLUMN_DONE + " =0 AND " + COLUMN_HELP + " =0 AND " + COLUMN_NOT_DONE + "=0";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuiry, null);
        int count = cursor.getCount();
        db.close();
        return count;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Log.d(TAG, "DB upgrading. Dropping old table...");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO_DAILY);
        onCreate(db);
    }

}
