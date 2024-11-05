package com.example.myapplication;

import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.util.Log;

public class AlarmRingActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ring);

        // Make the activity visible on top of other apps and during screen off
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Initialize vibrator service
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null) {
            long[] vibrationPattern = {0, 1000, 1000}; // Start immediately, vibrate 1 second, pause 1 second
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(vibrationPattern, 0)); // 0 repeats indefinitely
            } else {
                vibrator.vibrate(vibrationPattern, 0);
            }
        }

        // Set up alarm sound
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        try {
            mediaPlayer = MediaPlayer.create(this, alarmSound);
            if (mediaPlayer != null) {
                mediaPlayer.setLooping(true); // Loop sound until dismissed
                mediaPlayer.start();
            } else {
                Log.e("AlarmRingActivity", "MediaPlayer creation failed");
            }
        } catch (Exception e) {
            Log.e("AlarmRingActivity", "Error initializing media player: " + e.getMessage());
        }

        // Set up the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Dismiss button action
        Button dismissButton = findViewById(R.id.dismissButton);
        dismissButton.setOnClickListener(v -> {
            Log.d("AlarmRingActivity", "Dismiss button clicked");
            stopAlarm();
        });

        // Optional: Set custom text for the TextView
        TextView txtAlarmRinging = findViewById(R.id.txtAlarmRinging);
        txtAlarmRinging.setText("Your Alarm is Ringing!");
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in a specified location and move the camera
        LatLng alarmLocation = new LatLng(-34, 151); // Example coordinates (Sydney)
        mMap.addMarker(new MarkerOptions().position(alarmLocation).title("Alarm Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(alarmLocation, 10)); // Zoom level
    }

    private void stopAlarm() {
        // Stop media player if playing
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        // Cancel vibration
        if (vibrator != null) {
            vibrator.cancel();
        }

        finish(); // Close the activity
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAlarm(); // Ensure resources are released when the activity is destroyed
    }
}
