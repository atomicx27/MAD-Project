package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AlarmDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "alarms.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_ALARMS = "alarms";

    public AlarmDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_ALARMS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, time TEXT, location TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARMS);
        onCreate(db);
    }

    // Method to add an alarm to the database
    public void addAlarm(String time, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("time", time);
        values.put("location", location);
        db.insert(TABLE_ALARMS, null, values);
        db.close();
    }

    public List<Alarm> getAllAlarms() {
        List<Alarm> alarms = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_ALARMS, null);
            if (cursor.moveToFirst()) {
                do {
                    Alarm alarm = new Alarm();
                    alarm.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    alarm.setTime(cursor.getString(cursor.getColumnIndexOrThrow("time")));
                    alarm.setLocation(cursor.getString(cursor.getColumnIndexOrThrow("location")));
                    alarms.add(alarm);
                    Log.d("AlarmDatabase", "Loaded alarm with ID: " + alarm.getId()); // Log each alarm ID
                } while (cursor.moveToNext());
            } else {
                Log.d("AlarmDatabase", "No alarms found in database"); // Log if no data is found
            }
        } catch (Exception e) {
            Log.e("AlarmDatabase", "Error retrieving alarms", e); // Log any exception
        } finally {
            if (cursor != null) cursor.close();
        }
        return alarms;
    }


    // Method to delete an alarm
    public void deleteAlarm(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ALARMS, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing table and recreate it
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARMS);
        onCreate(db);
    }

}
