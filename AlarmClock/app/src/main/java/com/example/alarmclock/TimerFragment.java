package com.example.alarmclock;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class TimerFragment extends Fragment {

    NumberPicker hourPicker, minutePicker, secondPicker;
    TextView timerTxt;
    ImageView playImg, resetImg;

    boolean timerRunning;
    long remainingTimeInMillis;

    CountDownTimer countDownTimer;

    public TimerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer, container, false);

        hourPicker = view.findViewById(R.id.hour_picker);
        minutePicker = view.findViewById(R.id.minute_picker);
        secondPicker = view.findViewById(R.id.second_picker);

        timerTxt = view.findViewById(R.id.timer);
        playImg = view.findViewById(R.id.play_btn);
        resetImg = view.findViewById(R.id.reset_btn);

        timerRunning = false;

        int minValueForHour = 0;
        int maxValueForHour = 23;
        hourPicker.setMinValue(minValueForHour);
        hourPicker.setMaxValue(maxValueForHour);

        String[] displayedValues = new String[maxValueForHour - minValueForHour + 1];
        for (int i = minValueForHour; i <= maxValueForHour; i++) {
            displayedValues[i - minValueForHour] = String.format("%02d", i);
        }
        hourPicker.setDisplayedValues(displayedValues);

        int minValueForMinute = 0;
        int maxValueForMinute = 59;
        minutePicker.setMinValue(minValueForMinute);
        minutePicker.setMaxValue(maxValueForMinute);

        String[] displayedValuesForMinute = new String[maxValueForMinute - minValueForMinute + 1];
        for (int i = minValueForMinute; i <= maxValueForMinute; i++) {
            displayedValuesForMinute[i - minValueForMinute] = String.format("%02d", i);
        }
        minutePicker.setDisplayedValues(displayedValuesForMinute);

        int minValueForSecond = 0;
        int maxValueForSecond = 59;
        secondPicker.setMinValue(minValueForSecond);
        secondPicker.setMaxValue(maxValueForSecond);

        String[] displayedValuesForSecond = new String[maxValueForSecond - minValueForSecond + 1];
        for (int i = minValueForSecond; i <= maxValueForSecond; i++) {
            displayedValuesForSecond[i - minValueForSecond] = String.format("%02d", i);
        }
        secondPicker.setDisplayedValues(displayedValuesForSecond);

        playImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hr = hourPicker.getValue();
                int min = minutePicker.getValue();
                int sec = secondPicker.getValue();

                if (hr == 0 && min == 0 && sec == 0) {
                    Toast.makeText(getContext(), "Please set the timer", Toast.LENGTH_SHORT).show();
                } else {
                    if (timerRunning) {
                        stopTimer();
                    } else {
                        int totalTimeInMillis = (hr * 3600 + min * 60 + sec) * 1000;
                        startTimer(totalTimeInMillis);
                    }
                }
            }
        });

        resetImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        return view;
    }

    private void startTimer(int totalTimeInMillis) {

        if (timerRunning) {
            return;
        }

        countDownTimer = new CountDownTimer(remainingTimeInMillis > 0 ? remainingTimeInMillis : totalTimeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTimeInMillis = millisUntilFinished;
                int hours = (int) (millisUntilFinished / 3600000);
                int minutes = (int) ((millisUntilFinished % 3600000) / 60000);
                int seconds = (int) ((millisUntilFinished % 60000) / 1000);

                String timeLeftFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                timerTxt.setText(timeLeftFormatted);
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(getContext(), MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), totalTimeInMillis, intent, PendingIntent.FLAG_IMMUTABLE);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "androidclock")
                        .setSmallIcon(R.drawable.stopwatch_icon)
                        .setContentTitle("Timer")
                        .setContentText("Timer has stopped")
                        .setAutoCancel(true)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getContext());
                if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(
                            (Activity) getContext(),
                            new String[]{Manifest.permission.POST_NOTIFICATIONS},
                            111
                    );
                    return;
                }
                notificationManagerCompat.notify(123, builder.build());

                timerRunning = false;
                remainingTimeInMillis = 0;
                timerTxt.setText("00:00:00");
                playImg.setImageResource(R.drawable.play_btn);
            }
        };

        countDownTimer.start();
        timerRunning = true;
        playImg.setImageResource(R.drawable.pause_btn);
    }

    private void stopTimer() {
        if (timerRunning) {
            countDownTimer.cancel();
            timerRunning = false;
            playImg.setImageResource(R.drawable.play_btn);
        }
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timerRunning = false;
        remainingTimeInMillis = 0;
        timerTxt.setText("00:00:00");
        playImg.setImageResource(R.drawable.play_btn);
    }

}