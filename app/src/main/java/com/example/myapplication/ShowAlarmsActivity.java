package com.example.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ShowAlarmsActivity extends AppCompatActivity {
    private ListView listViewAlarms;
    private AlarmDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_alarms);

        listViewAlarms = findViewById(R.id.listViewAlarms);
        db = new AlarmDatabase(this);
        try {
            loadAlarms();
        } catch (Exception e) {
            Log.e("ShowAlarmsActivity", "Error loading alarms", e);
            Toast.makeText(this, "Error loading alarms", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadAlarms() {
        List<Alarm> alarms = db.getAllAlarms();

        if (alarms.isEmpty()) {
            Toast.makeText(this, "No alarms found", Toast.LENGTH_SHORT).show();
            return;
        }

        // Set up the custom adapter for the ListView
        AlarmAdapter adapter = new AlarmAdapter(this, alarms);
        listViewAlarms.setAdapter(adapter);
    }

    public void showDeleteConfirmationDialog(int alarmId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Alarm");
        builder.setMessage("Are you sure you want to delete this alarm?");
        builder.setPositiveButton("Yes", (dialog, which) -> deleteAlarm(alarmId));
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void deleteAlarm(int alarmId) {
        db.deleteAlarm(alarmId); // Delete the alarm from the database
        loadAlarms(); // Reload alarms to update the list
        Toast.makeText(this, "Alarm deleted", Toast.LENGTH_SHORT).show();
    }
}
