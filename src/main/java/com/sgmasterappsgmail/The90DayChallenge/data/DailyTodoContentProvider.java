package com.sgmasterappsgmail.The90DayChallenge.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by shia on 11/2/2015.
 */
public class DailyTodoContentProvider extends ContentProvider {
    private MySqlHelper sqlHelper;

    private static final String TAG = "DailyContentProvider";

    private static final String AUTHORITY = "com.sgmasterappsgmail.The90DayChallenge.contentprovider";
    private static final String BASE = "dailytodo";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE);

    private static final int ALL_DAYS = 25;
    private static final int DAILY = 5;

    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mUriMatcher.addURI(AUTHORITY, BASE, ALL_DAYS);
        mUriMatcher.addURI(AUTHORITY, BASE + "/#", DAILY);
    }


    @Override
    public boolean onCreate() {
        sqlHelper = new MySqlHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query");
        String sl = MySqlHelper.COLUMN_DONE + " = ?";
        String[] sla = new String[]{"1"};
        SQLiteQueryBuilder query = new SQLiteQueryBuilder();
        query.setTables(MySqlHelper.TABLE_TODO_DAILY);
        int uriType = mUriMatcher.match(uri);
        switch (uriType) {
            case ALL_DAYS:
                break;
            case DAILY:
                // adding the ID to the original query
                query.appendWhere(MySqlHelper.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        Cursor cursor = query.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "insert");
        int uriType = mUriMatcher.match(uri);
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case ALL_DAYS:
                id = db.insert(MySqlHelper.TABLE_TODO_DAILY, null, values);
                db.close();
                break;
            case DAILY:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "delete");
        int uriType = mUriMatcher.match(uri);
        SQLiteDatabase sqlDB = sqlHelper.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case ALL_DAYS:
                rowsDeleted = sqlDB.delete(MySqlHelper.TABLE_TODO_DAILY, selection,
                        selectionArgs);
                break;
            case DAILY:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(MySqlHelper.TABLE_TODO_DAILY,
                            MySqlHelper.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(MySqlHelper.TABLE_TODO_DAILY,
                            MySqlHelper.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(TAG, "update");
        int uriType = mUriMatcher.match(uri);
        SQLiteDatabase sqlDB = sqlHelper.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case ALL_DAYS:
                rowsUpdated = sqlDB.update(MySqlHelper.TABLE_TODO_DAILY,
                        values,
                        selection,
                        selectionArgs);
                break;
            case DAILY:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(MySqlHelper.TABLE_TODO_DAILY,
                            values,
                            MySqlHelper.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(MySqlHelper.TABLE_TODO_DAILY,
                            values,
                            MySqlHelper.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
