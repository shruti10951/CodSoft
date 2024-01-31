package com.example.alarmclock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddAlarmActivity extends AppCompatActivity {

    TimePicker timePicker;
    TextView monTxt, tueTxt, wedTxt, thuTxt, friTxt, satTxt, sunTxt;
    TextView ringtoneTxt, snoozeTxt, labelTxt;
    TextView cancelTxt, doneTxt;

    Boolean monSelected = false, tueSelected = false, wedSelected = false, thuSelected = false, friSelected = false, satSelected = false, sunSelected = false;

    AlarmModel alarmModel = new AlarmModel();
    SQLiteDatabase sqLiteDatabase;
    AlarmDatabaseHelper databaseHelper= new AlarmDatabaseHelper(this);

    AlarmManager alarmManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        timePicker = findViewById(R.id.timepicker);

        monTxt = findViewById(R.id.monday_txt);
        tueTxt = findViewById(R.id.tuesday_txt);
        wedTxt = findViewById(R.id.wednesday_txt);
        thuTxt = findViewById(R.id.thursday_txt);
        friTxt = findViewById(R.id.friday_txt);
        satTxt = findViewById(R.id.saturday_txt);
        sunTxt = findViewById(R.id.sunday_txt);

        ringtoneTxt = findViewById(R.id.ringtone_name_txt);
        snoozeTxt = findViewById(R.id.snooze_time_txt);
        labelTxt = findViewById(R.id.label_value_txt);

        cancelTxt = findViewById(R.id.cancel_txt);
        doneTxt = findViewById(R.id.done_txt);

        TypedArray a = obtainStyledAttributes(new int[]{android.R.attr.textColor});
        int defaultTextColor = a.getColor(0, 0);
        a.recycle();

        Uri defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (defaultRingtoneUri != null) {
            String defaultRingtoneTitle = RingtoneManager.getRingtone(this, defaultRingtoneUri).getTitle(this);
            ringtoneTxt.setText(defaultRingtoneTitle);
            alarmModel.setAlarm_ringtone_uri(defaultRingtoneUri.toString());
        }

        ringtoneTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Ringtone");
                startActivityForResult(intent, 1);
            }
        });

        snoozeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] snoozeOptions = {"5 minutes", "10 minutes", "15 minutes", "30 minutes"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AddAlarmActivity.this);
                builder.setTitle("Select Snooze Time")
                        .setItems(snoozeOptions, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String selectedSnoozeTime = snoozeOptions[which].toString();
                                snoozeTxt.setText(selectedSnoozeTime);
                            }
                        });
                builder.create().show();
            }
        });

        labelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View alertView = getLayoutInflater().inflate(R.layout.input_alert_dialogue, null);
                EditText inputLabel = alertView.findViewById(R.id.alert_et_value);
                Button cancelBtn = alertView.findViewById(R.id.alert_cancel_btn);
                Button okBtn = alertView.findViewById(R.id.alert_ok_btn);

                AlertDialog.Builder builder = new AlertDialog.Builder(AddAlarmActivity.this);
                builder.setView(alertView);
                builder.setPositiveButton(null,null);
                builder.setNegativeButton(null,null);

                AlertDialog alertDialog = builder.create();

                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String label = inputLabel.getText().toString();
                        labelTxt.setText(label);
                        alertDialog.dismiss();
                    }
                });

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();

            }
        });

        monTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(monSelected){
                    monTxt.setTextColor(defaultTextColor);
                    monSelected = false;
                }else{
                    monTxt.setTextColor(getResources().getColor(R.color.selected_day));
                    monSelected = true;
                }
            }
        });

        tueTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tueSelected){
                    tueTxt.setTextColor(defaultTextColor);
                    tueSelected = false;
                }else{
                    tueTxt.setTextColor(getResources().getColor(R.color.selected_day));
                    tueSelected = true;
                }
            }
        });

        wedTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wedSelected){
                    wedTxt.setTextColor(defaultTextColor);
                    wedSelected = false;
                }else{
                    wedTxt.setTextColor(getResources().getColor(R.color.selected_day));
                    wedSelected = true;
                }
            }
        });

        thuTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(thuSelected){
                    thuTxt.setTextColor(defaultTextColor);
                    thuSelected = false;
                }else{
                    thuTxt.setTextColor(getResources().getColor(R.color.selected_day));
                    thuSelected = true;
                }
            }
        });

        friTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(friSelected){
                    friTxt.setTextColor(defaultTextColor);
                    friSelected = false;
                }else{
                    friTxt.setTextColor(getResources().getColor(R.color.selected_day));
                    friSelected = true;
                }
            }
        });

        satTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(satSelected){
                    satTxt.setTextColor(defaultTextColor);
                    satSelected = false;
                }else{
                    satTxt.setTextColor(getResources().getColor(R.color.selected_day));
                    satSelected = true;
                }
            }
        });

        sunTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sunSelected){
                    sunTxt.setTextColor(defaultTextColor);
                    sunSelected = false;
                }else{
                    sunTxt.setTextColor(getResources().getColor(R.color.selected_day));
                    sunSelected = true;
                }
            }
        });

        cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        doneTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSelectedDays();
                String hr = String.valueOf(timePicker.getHour());
                String min = String.valueOf(timePicker.getMinute());
                alarmModel.setAlarm_label(labelTxt.getText().toString());
                alarmModel.setAlarm_snooze_time(snoozeTxt.getText().toString().split(" ")[0]);
                alarmModel.setAlarm_hour(Integer.parseInt(hr));
                alarmModel.setAlarm_minute(Integer.parseInt(min));
                alarmModel.setActive(true);

                int code = (int) System.currentTimeMillis();
                alarmModel.setRequest_code(code);

                if(alarmModel.isMon()){
                    alarmModel.setMon(true);
                    scheduleAlarmForDay(Calendar.MONDAY, alarmModel, hr, min, code);
                }
                if(alarmModel.isTue()){
                    alarmModel.setTue(true);
                    scheduleAlarmForDay(Calendar.TUESDAY, alarmModel, hr, min, code);
                }
                if(alarmModel.isWed()){
                    alarmModel.setWed(true);
                    scheduleAlarmForDay(Calendar.WEDNESDAY, alarmModel, hr, min, code);
                }
                if(alarmModel.isThu()){
                    alarmModel.setThu(true);
                    scheduleAlarmForDay(Calendar.THURSDAY, alarmModel, hr, min, code);
                }
                if(alarmModel.isFri()){
                    alarmModel.setFri(true);
                    scheduleAlarmForDay(Calendar.FRIDAY, alarmModel, hr, min, code);
                }
                if(alarmModel.isSat()){
                    alarmModel.setSat(true);
                    scheduleAlarmForDay(Calendar.SATURDAY, alarmModel, hr, min, code);
                }
                if(alarmModel.isSun()){
                    alarmModel.setSun(true);
                    scheduleAlarmForDay(Calendar.SUNDAY, alarmModel, hr, min, code);
                }

                if(!alarmModel.isMon() && !alarmModel.isTue() && !alarmModel.isWed() && !alarmModel.isThu() && !alarmModel.isFri() && !alarmModel.isSat() && !alarmModel.isSun()){
                    scheduleAlarm(alarmModel, hr, min, code);
                }

                sqLiteDatabase = databaseHelper.getWritableDatabase();
                ContentValues contentValues= new ContentValues();
                contentValues.put("alarm_label", alarmModel.getAlarm_label());
                contentValues.put("alarm_hour", alarmModel.getAlarm_hour());
                contentValues.put("alarm_minute", alarmModel.getAlarm_minute());
                contentValues.put("alarm_ringtone_uri", alarmModel.getAlarm_ringtone_uri());
                contentValues.put("alarm_snooze_time", alarmModel.getAlarm_snooze_time());
                contentValues.put("mon", alarmModel.isMon() ? 1 : 0);
                contentValues.put("tue", alarmModel.isTue() ? 1 : 0);
                contentValues.put("wed", alarmModel.isWed() ? 1 : 0);
                contentValues.put("thu", alarmModel.isThu() ? 1 : 0);
                contentValues.put("fri", alarmModel.isFri() ? 1 : 0);
                contentValues.put("sat", alarmModel.isSat() ? 1 : 0);
                contentValues.put("sun", alarmModel.isSun() ? 1 : 0);
                contentValues.put("isActive", alarmModel.isActive() ? 1 : 0);
                contentValues.put("request_code", alarmModel.getRequest_code());

                try{
                    sqLiteDatabase.insert("alarms", null, contentValues);
                    Toast.makeText(AddAlarmActivity.this, "Alarm Set!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddAlarmActivity.this, MainActivity.class));
                    finish();
                }catch (SQLException e){
                    Toast.makeText(AddAlarmActivity.this, "Something went wrong!\nPlease try again later.", Toast.LENGTH_SHORT).show();
                }finally {
                    sqLiteDatabase.close();
                }

            }
        });

    }

    private void updateSelectedDays() {
        if(monSelected){
            alarmModel.setMon(true);
        }
        if(tueSelected){
            alarmModel.setTue(true);
        }
        if(wedSelected){
            alarmModel.setWed(true);
        }
        if(thuSelected){
            alarmModel.setThu(true);
        }
        if(friSelected){
            alarmModel.setFri(true);
        }
        if(satSelected){
            alarmModel.setSat(true);
        }if(sunSelected){
            alarmModel.setSun(true);
        }

    }

    private void scheduleAlarm(AlarmModel alarm, String hr, String min, int requestCode) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hr));
        calendar.set(Calendar.MINUTE, Integer.parseInt(min));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.getDefault());
        String formattedTime = sdf.format(calendar.getTime());

        SimpleDateFormat amPmFormat = new SimpleDateFormat("a", Locale.getDefault());
        String amPmIndicator = amPmFormat.format(calendar.getTime());

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("ALARM_LABEL", alarm.getAlarm_label());
        intent.putExtra("ALARM_ID", alarm.getAlarm_id());
        intent.putExtra("ALARM_TIME", formattedTime);
        intent.putExtra("ALARM_AM_PM", amPmIndicator);
        intent.putExtra("ALARM_SNOOZE", alarmModel.getAlarm_snooze_time());
        intent.putExtra("ALARM_RINGTONE_URI", alarm.getAlarm_ringtone_uri());
        intent.putExtra("REQUEST_CODE", requestCode);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }



    private void scheduleAlarmForDay (int dayOfWeek, AlarmModel alarm, String hr, String min, int code) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hr));
        calendar.set(Calendar.MINUTE, Integer.parseInt(min));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.getDefault());
        String formattedTime = sdf.format(calendar.getTime());

        SimpleDateFormat amPmFormat = new SimpleDateFormat("a", Locale.getDefault());
        String amPmIndicator = amPmFormat.format(calendar.getTime());

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 7);
        }

        int requestCode = code + dayOfWeek;

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("ALARM_LABEL", alarm.getAlarm_label());
        intent.putExtra("ALARM_TIME", formattedTime);
        intent.putExtra("ALARM_ID", alarm.getAlarm_id());
        intent.putExtra("ALARM_AM_PM", amPmIndicator);
        intent.putExtra("ALARM_SNOOZE", alarmModel.getAlarm_snooze_time());
        intent.putExtra("ALARM_RINGTONE_URI", alarm.getAlarm_ringtone_uri());
        intent.putExtra("REQUEST_CODE", requestCode);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode,
                intent,
                PendingIntent.FLAG_IMMUTABLE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri ringtoneUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

            if (ringtoneUri != null) {
                String ringtoneTitle = RingtoneManager.getRingtone(this, ringtoneUri).getTitle(this);
                ringtoneTxt.setText(ringtoneTitle);
                alarmModel.setAlarm_ringtone_uri(ringtoneUri.toString());
            }
        }
    }
}