package com.sgmasterappsgmail.The90DayChallenge.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.sgmasterappsgmail.The90DayChallenge.data.MySqlHelper;

/**
 * Created by shia on 11/2/2015.
 */
public class TodoDaily implements Comparable<TodoDaily> {
    private int id = -1;
    private String tittle;
    private String description;
    private boolean done;
    private boolean notDone;
    private String goal;
    private boolean help;
    private long date;
    private boolean isHide = true;

    public TodoDaily(int i) {
        goal = "Goal " + i;
        date = System.currentTimeMillis();

    }

    public TodoDaily() {

    }

    public boolean isHide() {
        return isHide;
    }

    public void setIsHide(boolean isHide) {
        this.isHide = isHide;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public boolean isNotDone() {
        return notDone;
    }

    public void setNotDone(boolean notDone) {
        this.notDone = notDone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isHelp() {
        return help;
    }

    public void setHelp(boolean help) {
        this.help = help;
    }

    public int getId() {
        return id;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        if (id != -1) contentValues.put(MySqlHelper.COLUMN_ID, id);
        contentValues.put(MySqlHelper.COLUMN_DATE, date);
        contentValues.put(MySqlHelper.COLUMN_TITTLE, tittle);
        contentValues.put(MySqlHelper.COLUMN_GOAL, goal);
        contentValues.put(MySqlHelper.COLUMN_DESCRIPTION, description);
        contentValues.put(MySqlHelper.COLUMN_DONE, done ? 1 : 0);
        contentValues.put(MySqlHelper.COLUMN_HELP, help ? 1 : 0);
        contentValues.put(MySqlHelper.COLUMN_NOT_DONE, notDone ? 1 : 0);
        return contentValues;
    }

    public static TodoDaily buildFromCursor(Cursor cursor) {
        int idColumn = cursor.getColumnIndex(MySqlHelper.COLUMN_ID);
        int dateColumn = cursor.getColumnIndex(MySqlHelper.COLUMN_DATE);
        int tittleColumn = cursor.getColumnIndex(MySqlHelper.COLUMN_TITTLE);
        int goalColumn = cursor.getColumnIndex(MySqlHelper.COLUMN_GOAL);
        int notDoneColumn = cursor.getColumnIndex(MySqlHelper.COLUMN_NOT_DONE);
        int descriptionColumn = cursor.getColumnIndex(MySqlHelper.COLUMN_DESCRIPTION);
        int doneColumn = cursor.getColumnIndex(MySqlHelper.COLUMN_DONE);
        int helpColumn = cursor.getColumnIndex(MySqlHelper.COLUMN_HELP);


        if (idColumn == -1 || dateColumn == -1 || tittleColumn == -1 || goalColumn == -1 || descriptionColumn == -1
                || doneColumn == -1 || helpColumn == -1 || notDoneColumn == -1) {
            throw new RuntimeException("Database must have all the columns for the todo item!");
        }

        TodoDaily todoDaily = new TodoDaily();
        todoDaily.id = cursor.getInt(idColumn);
        todoDaily.date = cursor.getLong(dateColumn);
        todoDaily.tittle = cursor.getString(tittleColumn);
        todoDaily.goal = cursor.getString(goalColumn);
        todoDaily.description = cursor.getString(descriptionColumn);
        todoDaily.done = cursor.getInt(doneColumn) == 1;
        todoDaily.help = cursor.getInt(helpColumn) == 1;
        todoDaily.notDone = cursor.getInt(notDoneColumn) == 1;

        return todoDaily;
    }

    @Override
    public int compareTo(TodoDaily another) {
        if (this.getId() > another.getId()) {
            return 1;
        } else if (this.getId() < another.getId()) {
            return -1;
        } else {
            return 0;
        }
    }

}
