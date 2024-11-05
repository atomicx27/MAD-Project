package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class AlarmManagerHelper {

    private final Context context;
    private final AlarmManager alarmManager;

    public AlarmManagerHelper(Context context) {
        this.context = context.getApplicationContext(); // Use application context
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    // Method to set an alarm
    @SuppressLint("ScheduleExactAlarm")
    public void setAlarm(int hour, int minute, String location) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // If the time is in the past, add a day to the alarm
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        // Create an intent to trigger the AlarmReceiver
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("location", location); // Pass location to the receiver

        // Create PendingIntent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE // Always use the immutable flag for security
        );

        // Set the alarm
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Log.d("AlarmManagerHelper", "Alarm set for " + hour + ":" + minute + " at location: " + location);
    }

    // Method to cancel an existing alarm
    public void cancelAlarm() {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE // Ensure consistent usage of flags
        );

        alarmManager.cancel(pendingIntent);
        Log.d("AlarmManagerHelper", "Alarm canceled for intent with location: " + intent.getStringExtra("location")); // More context in log
    }
}
