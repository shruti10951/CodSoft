package com.example.alarmclock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AlarmActivity extends AppCompatActivity {

    String label, amPm, time, snooze, ringtone;
    int id;
    TextView alarmLabel, alarmTime, alarmAmPm;
    Button dismissBtn, snoozeTime;

    SQLiteDatabase sqLiteDatabase;
    AlarmDatabaseHelper databaseHelper= new AlarmDatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        Intent intent= getIntent();
        label = intent.getStringExtra("ALARM_LABEL");
        time = intent.getStringExtra("ALARM_TIME");
        amPm = intent.getStringExtra("ALARM_AM_PM");
        snooze = intent.getStringExtra("ALARM_SNOOZE");
        ringtone = intent.getStringExtra("ALARM_RINGTONE_URI");
        id = intent.getIntExtra("ALARM_ID", 0);

        alarmLabel = findViewById(R.id.alarm_label);
        alarmTime = findViewById(R.id.alarm_time);
        alarmAmPm = findViewById(R.id.alarm_am_pm);
        dismissBtn = findViewById(R.id.dismiss_btn);
        snoozeTime = findViewById(R.id.snooze_btn);

        alarmAmPm.setText(amPm);
        alarmTime.setText(time);
        alarmLabel.setText(label);

        dismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stopIntent = new Intent(AlarmActivity.this, RingtoneService.class);
                stopService(stopIntent);
                sqLiteDatabase = databaseHelper.getWritableDatabase();
                ContentValues contentValues= new ContentValues();
                contentValues.put("isActive", 0);
                try{
                    sqLiteDatabase.update("alarms", contentValues, "alarm_id=?", new String[]{String.valueOf(id)});
                }catch (SQLException e){
                    Toast.makeText(AlarmActivity.this, "Something went wrong!\nPlease try again later.", Toast.LENGTH_SHORT).show();
                }finally {
                    sqLiteDatabase.close();
                }
                finish();
            }
        });

        snoozeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar snoozeTime = Calendar.getInstance();
                snoozeTime.add(Calendar.MINUTE, Integer.parseInt(snooze));
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                int requestCode = (int) System.currentTimeMillis();

                Intent intent = new Intent(AlarmActivity.this, AlarmReceiver.class);
                intent.putExtra("ALARM_LABEL", label);
                intent.putExtra("ALARM_TIME", time);
                intent.putExtra("ALARM_AM_PM", amPm);
                intent.putExtra("ALARM_SNOOZE", snooze);
                intent.putExtra("ALARM_RINGTONE_URI", ringtone);
                intent.putExtra("REQUEST_CODE", requestCode);
                intent.putExtra("ALARM_ID", id);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        AlarmActivity.this,
                        requestCode,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE);

                alarmManager.set(AlarmManager.RTC_WAKEUP, snoozeTime.getTimeInMillis(), pendingIntent);

                Intent stopIntent = new Intent(AlarmActivity.this, RingtoneService.class);
                stopService(stopIntent);
                finish();
            }
        });

    }
}