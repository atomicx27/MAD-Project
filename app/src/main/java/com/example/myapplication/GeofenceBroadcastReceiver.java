package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        assert geofencingEvent != null;
        if (geofencingEvent.hasError()) {
            Log.e("GeofenceReceiver", "Error in geofence event: " + geofencingEvent.getErrorCode());
            return;
        }

        // Get the geofence transition type
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Handle the transition (enter or exit)
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            Log.d("GeofenceReceiver", "Entered geofence: " + geofencingEvent.getTriggeringGeofences());
            // Here, you can trigger your alarm or show a notification
        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            Log.d("GeofenceReceiver", "Exited geofence: " + geofencingEvent.getTriggeringGeofences());
        }
    }
}
