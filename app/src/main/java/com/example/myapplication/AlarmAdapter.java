package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class AlarmAdapter extends ArrayAdapter<Alarm> {

    public AlarmAdapter(Context context, List<Alarm> alarms) {
        super(context, 0, alarms);
    }

    @NonNull
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Alarm alarm = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        TextView txtTime = convertView.findViewById(android.R.id.text1);
        TextView txtLocation = convertView.findViewById(android.R.id.text2);

        assert alarm != null;
        txtTime.setText("Time: " + alarm.getTime());
        txtLocation.setText("Location: " + alarm.getLocation());

        // Set long click listener for deleting alarm
        convertView.setOnLongClickListener(v -> {
            if (getContext() instanceof ShowAlarmsActivity) {
                ((ShowAlarmsActivity) getContext()).showDeleteConfirmationDialog(alarm.getId());
            }
            return true; // Return true to indicate the long click was handled
        });

        return convertView;
    }
}
