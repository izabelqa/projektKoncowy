package com.exe.fitlifetaskmanager.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.exe.fitlifetaskmanager.DayDetailsActivity;
import com.exe.fitlifetaskmanager.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        CalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                String selectedDate = String.format(Locale.getDefault(), "%02d", dayOfMonth);
                String selectedDateToKey = String.format(Locale.getDefault(), "%04d %02d %02d", year, month, dayOfMonth);

                // Pobierz dzień tygodnia
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                String selectedDayOfWeek = new SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.getTime());
                String selectedMonth = new SimpleDateFormat("MMMM", Locale.getDefault()).format(calendar.getTime());

                // Przygotuj Intent i przekaż dane
                Intent intent = new Intent(getActivity(), DayDetailsActivity.class);
                intent.putExtra("selectedDate", selectedDate);
                intent.putExtra("selectedDayOfWeek", selectedDayOfWeek);
                intent.putExtra("selectedMonth", selectedMonth);
                intent.putExtra("selectedDateToKey", selectedDateToKey);

                // Uruchom nową aktywność
                startActivity(intent);
            }
        });

        return view;
    }
}