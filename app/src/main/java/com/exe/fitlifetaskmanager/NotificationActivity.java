package com.exe.fitlifetaskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        TextView dataTextView = findViewById(R.id.dataTextView);
        String data = getIntent().getStringExtra("data");
        dataTextView.setText(data);
    }
}