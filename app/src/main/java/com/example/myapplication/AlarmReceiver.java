package com.example.myapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {

    private static Ringtone ringtone; // Static to maintain reference across invocations

    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context);
        ringBell(context);
        vibrate(context);
    }

    private void showNotification(Context context) {
        String channelId = "alarm_notifications"; // Ensure this matches your channel ID
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background) // Notification icon
                .setContentTitle("Alarm")
                .setContentText("Your alarm time has been reached!") // Notification content
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Priority for the notification
                .setAutoCancel(true) // Automatically remove the notification when clicked
                .setContentIntent(getPendingIntent(context)); // Intent to launch when the notification is tapped

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return; // Exit if notification permission is not granted
        }
        notificationManager.notify(1, builder.build()); // Notify with a unique ID
    }

    private PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, AlarmRingActivity.class); // Start AlarmRingActivity
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    private void ringBell(Context context) {
        // Play the default alarm sound
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (ringtone == null) {
            ringtone = RingtoneManager.getRingtone(context, alarmUri);
        }
        if (ringtone != null && !ringtone.isPlaying()) {
            ringtone.play(); // Start playing the ringtone
        }
    }

    private void vibrate(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            long[] pattern = {0, 1000, 1000}; // Vibration pattern (start, vibrate, sleep)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0)); // 0 = Repeat indefinitely
            } else {
                vibrator.vibrate(pattern, 0); // For older versions
            }
        }
    }

    public static void stopAlarm() {
        if (ringtone != null && ringtone.isPlaying()) {
            ringtone.stop(); // Stop ringtone
            ringtone = null; // Reset the reference
        }
    }
}
