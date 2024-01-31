package com.example.alarmclock;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;

public class StopwatchFragment extends Fragment {

    Chronometer chronometer;
    ImageView playImg, resetImg;

    boolean isRunning = false;
    long pausedTime = 0;

    public StopwatchFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_stopwatch, container, false);
        chronometer= view.findViewById(R.id.chronometer);
        playImg= view.findViewById(R.id.play_btn);
        resetImg= view.findViewById(R.id.reset_btn);

        playImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    chronometer.stop();
                    pausedTime = SystemClock.elapsedRealtime() - chronometer.getBase();
                    isRunning = false;
                    playImg.setImageResource(R.drawable.pause_btn);
                } else {
                    chronometer.setBase(SystemClock.elapsedRealtime() - pausedTime);
                    chronometer.start();
                    isRunning = true;
                    playImg.setImageResource(R.drawable.play_btn);
                }
            }
        });

        resetImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.stop();
                pausedTime = 0;
                isRunning = false;
                playImg.setImageResource(R.drawable.play_btn);
            }
        });

        return view;
    }
}