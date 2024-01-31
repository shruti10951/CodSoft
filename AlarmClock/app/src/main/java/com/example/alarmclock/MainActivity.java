package com.example.alarmclock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageView addImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        addImage = findViewById(R.id.add_alarm_img);

        replaceFragment(new ClockFragment());
        createNotificationChannel();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_alarm) {
                    replaceFragment(new AlarmFragment());
                    return true;
                } else if (item.getItemId() == R.id.navigation_clock) {
                    replaceFragment(new ClockFragment());
                    return true;
                } else if (item.getItemId() == R.id.navigation_timer) {
                    replaceFragment(new TimerFragment());
                    return true;
                } else if (item.getItemId() == R.id.navigation_stopwatch) {
                    replaceFragment(new StopwatchFragment());
                    return true;
                } else {
                    return false;
                }
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddAlarmActivity.class));
            }
        });

    }

    private void createNotificationChannel() {
        CharSequence name = "androidclock";
        String desc= "Channel for Alarm manager";
        int imp= NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel notificationChannel = new NotificationChannel("androidclock", name, imp);
        notificationChannel.setDescription(desc);

        NotificationManager notificationManager= getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(notificationChannel);

    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

}