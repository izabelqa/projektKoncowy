package com.exe.fitlifetaskmanager.fragments;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.exe.fitlifetaskmanager.NotificationActivity;
import com.exe.fitlifetaskmanager.R;
import com.google.android.material.snackbar.Snackbar;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Button buttonChangeColors = view.findViewById(R.id.buttonChangeColors);
        buttonChangeColors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChangeColorsClick(v);
            }
        });

        Button buttonChangeFont = view.findViewById(R.id.buttonChangeFont);
        buttonChangeFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChangeFontClick(v);
            }
        });

        Button buttonNotifications = view.findViewById(R.id.buttonNotifications);
        buttonNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNotificationButtonClick(v);
            }
        });
        return view;
    }

    public void onChangeColorsClick(View view) {
        Snackbar.make(view, "Zmieniono kolory", Snackbar.LENGTH_SHORT).show();
    }

    public void onChangeFontClick (View view){
            Snackbar.make(view, "Zmieniono czcionkę", Snackbar.LENGTH_SHORT).show();

    }

    public void onNotificationButtonClick(View view){
        String CHANNEL_ID = "CHANNEL_ID_NOTIFICATION";
        String textTitle = "Notification Title";
        String textContent = "Notification Content";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intent = new Intent(getContext(), NotificationActivity.class); // Poprawa: getContext() zamiast getActivity()
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("data", "Some value to passed here");

        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_MUTABLE); // Poprawa: FLAG_UPDATE_CURRENT zamiast FLAG_MUTABLE
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE); // Poprawa: getContext() zamiast getSystemService()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
            if (notificationChannel == null) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(CHANNEL_ID, "Some description", importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        notificationManager.notify(0,builder.build());
    }

}

