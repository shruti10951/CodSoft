package com.example.alarmclock;

import static android.content.Context.ALARM_SERVICE;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {

    Context context;
    ArrayList<AlarmModel> arrayList;
    SQLiteDatabase database;
    AlarmDatabaseHelper databaseHelper;
    public AlarmAdapter(Context context, ArrayList arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.databaseHelper = new AlarmDatabaseHelper(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.alarm_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AlarmModel alarmModel = arrayList.get(position);
        holder.label.setText(alarmModel.getAlarm_label());

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, alarmModel.getAlarm_hour());
        calendar.set(Calendar.MINUTE, alarmModel.getAlarm_minute());

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.getDefault());
        String formattedTime = sdf.format(calendar.getTime());

        SimpleDateFormat amPmFormat = new SimpleDateFormat("a", Locale.getDefault());
        String amPmIndicator = amPmFormat.format(calendar.getTime());

        holder.time.setText(formattedTime);
        holder.amPm.setText(amPmIndicator);

        if (alarmModel.isMon()) {
            holder.mon.setTextColor(ContextCompat.getColor(context, R.color.selected_day));
        }
        if (alarmModel.isTue()) {
            holder.tue.setTextColor(ContextCompat.getColor(context, R.color.selected_day));
        }
        if (alarmModel.isWed()) {
            holder.wed.setTextColor(ContextCompat.getColor(context, R.color.selected_day));
        }
        if (alarmModel.isThu()) {
            holder.thu.setTextColor(ContextCompat.getColor(context, R.color.selected_day));
        }
        if (alarmModel.isFri()) {
            holder.fri.setTextColor(ContextCompat.getColor(context, R.color.selected_day));
        }
        if (alarmModel.isSat()) {
            holder.sat.setTextColor(ContextCompat.getColor(context, R.color.selected_day));
        }
        if (alarmModel.isSun()) {
            holder.sun.setTextColor(ContextCompat.getColor(context, R.color.selected_day));
        }
        holder.switchCompat.setChecked(alarmModel.isActive());

        holder.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SimpleDateFormat hrSdf = new SimpleDateFormat("HH", Locale.getDefault());
                    String hr = hrSdf.format(calendar.getTime());
                    SimpleDateFormat minSdf = new SimpleDateFormat("mm", Locale.getDefault());
                    String min = minSdf.format(calendar.getTime());

                    int code = (int) System.currentTimeMillis();
                    alarmModel.setRequest_code(code);

                    if(alarmModel.isMon()){
                        alarmModel.setMon(true);
                        scheduleAlarmForDay(holder.alarmManager, Calendar.MONDAY, alarmModel, hr, min, code);
                    }
                    if(alarmModel.isTue()){
                        alarmModel.setTue(true);
                        scheduleAlarmForDay(holder.alarmManager, Calendar.TUESDAY, alarmModel, hr, min, code);
                    }
                    if(alarmModel.isWed()){
                        alarmModel.setWed(true);
                        scheduleAlarmForDay(holder.alarmManager, Calendar.WEDNESDAY, alarmModel, hr, min, code);
                    }
                    if(alarmModel.isThu()){
                        alarmModel.setThu(true);
                        scheduleAlarmForDay(holder.alarmManager, Calendar.THURSDAY, alarmModel, hr, min, code);
                    }
                    if(alarmModel.isFri()){
                        alarmModel.setFri(true);
                        scheduleAlarmForDay(holder.alarmManager, Calendar.FRIDAY, alarmModel, hr, min, code);
                    }
                    if(alarmModel.isSat()){
                        alarmModel.setSat(true);
                        scheduleAlarmForDay(holder.alarmManager, Calendar.SATURDAY, alarmModel, hr, min, code);
                    }
                    if(alarmModel.isSun()){
                        alarmModel.setSun(true);
                        scheduleAlarmForDay(holder.alarmManager, Calendar.SUNDAY, alarmModel, hr, min, code);
                    }

                    if(!alarmModel.isMon() && !alarmModel.isTue() && !alarmModel.isWed() && !alarmModel.isThu() && !alarmModel.isFri() && !alarmModel.isSat() && !alarmModel.isSun()){
                        scheduleAlarm(holder.alarmManager, alarmModel, hr, min, code);
                    }

                    database = databaseHelper.getWritableDatabase();
                    ContentValues contentValues= new ContentValues();
                    contentValues.put("isActive", 1);
                    contentValues.put("request_code", code);

                    try{
                        database.update("alarms", contentValues, "alarm_id=?", new String[]{String.valueOf(alarmModel.getAlarm_id())});
                        Toast.makeText(context, "Alarm Set!", Toast.LENGTH_SHORT).show();
                    }catch (SQLException e){
                        Toast.makeText(context, "Something went wrong!\nPlease try again later.", Toast.LENGTH_SHORT).show();
                    }finally {
                        database.close();
                    }

                }else{
                    deActivateAlarms(alarmModel);
                    database = databaseHelper.getWritableDatabase();
                    ContentValues contentValues= new ContentValues();
                    contentValues.put("isActive", 0);
                    try{
                        database.update("alarms", contentValues, "alarm_id=?", new String[]{String.valueOf(alarmModel.getAlarm_id())});
                        Toast.makeText(context, "Alarm Disabled!", Toast.LENGTH_SHORT).show();
                    }catch (SQLException e){
                        Toast.makeText(context, "Something went wrong!\nPlease try again later.", Toast.LENGTH_SHORT).show();
                    }finally {
                        database.close();
                    }
                }
            }
        });

    }

    private void deActivateAlarms(AlarmModel alarmModel) {
        if(alarmModel.isSun()){
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    alarmModel.getRequest_code() + 1,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
            }
        }
        if(alarmModel.isMon()){
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    alarmModel.getRequest_code() + 2,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
            }
        }
        if(alarmModel.isTue()){
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    alarmModel.getRequest_code() + 3,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
            }
        }
        if(alarmModel.isWed()){
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    alarmModel.getRequest_code() + 4,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
            }
        }
        if(alarmModel.isThu()){
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    alarmModel.getRequest_code() + 5,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
            }
        }
        if(alarmModel.isFri()){
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    alarmModel.getRequest_code() + 6,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
            }
        }
        if(alarmModel.isSat()){
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    alarmModel.getRequest_code() + 7,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
            }
        }
        if(!alarmModel.isMon() && !alarmModel.isTue() && !alarmModel.isWed() && !alarmModel.isThu() && !alarmModel.isFri() && !alarmModel.isSat() && !alarmModel.isSun()){
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    alarmModel.getRequest_code(),
                    intent,
                    PendingIntent.FLAG_IMMUTABLE);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
            }
        }
    }

    private void scheduleAlarmForDay (AlarmManager alarmManager, int dayOfWeek, AlarmModel alarm, String hr, String min, int code) {
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

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("ALARM_LABEL", alarm.getAlarm_label());
        intent.putExtra("ALARM_TIME", formattedTime);
        intent.putExtra("ALARM_AM_PM", amPmIndicator);
        intent.putExtra("ALARM_SNOOZE", alarm.getAlarm_snooze_time());
        intent.putExtra("ALARM_RINGTONE_URI", alarm.getAlarm_ringtone_uri());
        intent.putExtra("REQUEST_CODE", requestCode);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_IMMUTABLE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);

    }

    private void scheduleAlarm(AlarmManager alarmManager, AlarmModel alarm, String hr, String min, int requestCode) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hr));
        calendar.set(Calendar.MINUTE, Integer.parseInt(min));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.getDefault());
        String formattedTime = sdf.format(calendar.getTime());

        SimpleDateFormat amPmFormat = new SimpleDateFormat("a", Locale.getDefault());
        String amPmIndicator = amPmFormat.format(calendar.getTime());

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("ALARM_LABEL", alarm.getAlarm_label());
        intent.putExtra("ALARM_TIME", formattedTime);
        intent.putExtra("ALARM_AM_PM", amPmIndicator);
        intent.putExtra("ALARM_SNOOZE", alarm.getAlarm_snooze_time());
        intent.putExtra("ALARM_RINGTONE_URI", alarm.getAlarm_ringtone_uri());
        intent.putExtra("REQUEST_CODE", requestCode);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView label, time, amPm, mon, tue, wed, thu, fri, sat, sun;
        SwitchCompat switchCompat;

        AlarmManager alarmManager;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

            label = itemView.findViewById(R.id.title_txt);
            time = itemView.findViewById(R.id.time_txt);
            amPm = itemView.findViewById(R.id.am_pm_txt);
            sun = itemView.findViewById(R.id.sunday_txt);
            mon = itemView.findViewById(R.id.monday_txt);
            tue = itemView.findViewById(R.id.tuesday_txt);
            wed = itemView.findViewById(R.id.wednesday_txt);
            thu = itemView.findViewById(R.id.thursday_txt);
            fri = itemView.findViewById(R.id.friday_txt);
            sat = itemView.findViewById(R.id.saturday_txt);
            switchCompat = itemView.findViewById(R.id.switchAlarm);
        }
    }

    ItemTouchHelper.Callback itemTouchHelperCallback = new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete Alarm")
                    .setMessage("Are you sure you want to delete this alarm?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        AlarmModel deletedAlarm = arrayList.get(position);
                        arrayList.remove(position);
                        notifyItemRemoved(position);
                        database = databaseHelper.getWritableDatabase();

                        try {
                            database.delete("alarms", "alarm_id=?", new String[]{String.valueOf(deletedAlarm.getAlarm_id())});
                            deActivateAlarms(deletedAlarm);
                            Toast.makeText(context, "Alarm Deleted!", Toast.LENGTH_SHORT).show();
                        } catch (SQLException e) {
                            Toast.makeText(context, "Something went wrong!\nPlease try again later.", Toast.LENGTH_SHORT).show();
                        } finally {
                            database.close();
                        }
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        notifyItemChanged(position);
                    })
                    .show();
        }
    };

    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
