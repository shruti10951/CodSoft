package com.example.alarmclock;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
    private static final int PERMISSION_REQUEST_CODE = 123;

    @Override
    public void onReceive(Context context, Intent intent) {
        String alarmLabel = intent.getStringExtra("ALARM_LABEL");
        String alarmTime = intent.getStringExtra("ALARM_TIME");
        String alarmAmPm = intent.getStringExtra("ALARM_AM_PM");
        String alarmSnooze = intent.getStringExtra("ALARM_SNOOZE");
        String alarmRingtone = intent.getStringExtra("ALARM_RINGTONE_URI");
        int alarmId = intent.getIntExtra("ALARM_ID", 0);
        int requestCode = intent.getIntExtra("requestCode", (int) System.currentTimeMillis());

        if (alarmLabel != null) {

            Intent intent1 = new Intent(context, AlarmActivity.class);
            intent1.putExtra("ALARM_TIME", alarmTime);
            intent1.putExtra("ALARM_AM_PM", alarmAmPm);
            intent1.putExtra("ALARM_LABEL", alarmLabel);
            intent1.putExtra("ALARM_SNOOZE", alarmSnooze);
            intent1.putExtra("ALARM_RINGTONE_URI", alarmRingtone);
            intent1.putExtra("ALARM_ID", alarmId);

            Intent serviceIntent = new Intent(context, RingtoneService.class);
            serviceIntent.putExtra("ALARM_RINGTONE_URI", alarmRingtone);
            context.startService(serviceIntent);

            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, intent1, PendingIntent.FLAG_IMMUTABLE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "androidclock")
                    .setSmallIcon(R.drawable.stopwatch_icon)
                    .setContentTitle("Alarm")
                    .setContentText(alarmLabel)
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        (Activity) context,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        PERMISSION_REQUEST_CODE
                );
                return;
            }
            notificationManagerCompat.notify(123, builder.build());
        }
    }
}
