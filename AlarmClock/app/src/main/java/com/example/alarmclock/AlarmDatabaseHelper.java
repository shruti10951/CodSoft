package com.example.alarmclock;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AlarmDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "alarm_database";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_ALARM = "alarms";
    private static final String COLUMN_ID = "alarm_id";
    private static final String COLUMN_HR = "alarm_hour";
    private static final String COLUMN_MIN = "alarm_minute";
    private static final String COLUMN_LABEL = "alarm_label";
    private static final String COLUMN_RINGTONE_URI = "alarm_ringtone_uri";
    private static final String COLUMN_SNOOZE_TIME = "alarm_snooze_time";
    private static final String COLUMN_MON = "mon";
    private static final String COLUMN_TUE = "tue";
    private static final String COLUMN_WED = "wed";
    private static final String COLUMN_THU = "thu";
    private static final String COLUMN_FRI = "fri";
    private static final String COLUMN_SAT = "sat";
    private static final String COLUMN_SUN = "sun";
    private static final String IS_ACTIVE = "isActive";
    private static final String REQUEST_CODE = "request_code";

    public AlarmDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_ALARM + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_HR + " INTEGER, " +
                COLUMN_MIN + " INTEGER, " +
                COLUMN_LABEL + " TEXT, " +
                COLUMN_RINGTONE_URI + " TEXT, " +
                COLUMN_SNOOZE_TIME + " TEXT, " +
                COLUMN_MON + " INTEGER, " +
                COLUMN_TUE + " INTEGER, " +
                COLUMN_WED + " INTEGER, " +
                COLUMN_THU + " INTEGER, " +
                COLUMN_FRI + " INTEGER, " +
                COLUMN_SAT + " INTEGER, " +
                COLUMN_SUN + " INTEGER, " +
                IS_ACTIVE + " INTEGER, " +
                REQUEST_CODE + " INTEGER " +
                ")";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
