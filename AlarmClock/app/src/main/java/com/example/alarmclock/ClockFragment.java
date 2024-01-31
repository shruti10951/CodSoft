package com.example.alarmclock;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ClockFragment extends Fragment {

    TextView timeTxt, amPmTxt, dayTxt;

    public ClockFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clock, container, false);

        timeTxt = view.findViewById(R.id.time);
        amPmTxt = view.findViewById(R.id.am_pm);
        dayTxt = view.findViewById(R.id.day);

        updateClockPeriodically();

        return view;
    }

    private void updateClockPeriodically() {
        updateClock();

        timeTxt.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateClockPeriodically();
            }
        }, 1000);
    }

    private void updateClock() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.getDefault());
        String formattedTime = sdf.format(calendar.getTime());

        SimpleDateFormat amPmFormat = new SimpleDateFormat("a", Locale.getDefault());
        String amPmIndicator = amPmFormat.format(calendar.getTime());

        String formattedDayOfWeek = new SimpleDateFormat("E", Locale.getDefault()).format(calendar.getTime());
        String formattedMonth = new SimpleDateFormat("MMM", Locale.getDefault()).format(calendar.getTime());
        String formattedDate = String.valueOf(dayOfMonth);

        timeTxt.setText(formattedTime);
        amPmTxt.setText(amPmIndicator);
        dayTxt.setText(formattedDayOfWeek + ", " + formattedMonth + " " + formattedDate);
    }
}
