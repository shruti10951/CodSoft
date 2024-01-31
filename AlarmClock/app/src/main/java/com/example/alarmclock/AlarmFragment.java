package com.example.alarmclock;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class AlarmFragment extends Fragment {

    ArrayList<AlarmModel> list = new ArrayList<AlarmModel>();
    SQLiteDatabase sqLiteDatabase;
    AlarmDatabaseHelper databaseHelper;
    Cursor cursor;
    AlarmAdapter alarmAdapter;

    RecyclerView recyclerView;


    public AlarmFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_alarm, container, false);

        recyclerView = view.findViewById(R.id.alarm_recycler);

        databaseHelper = new AlarmDatabaseHelper(getContext());

        alarmAdapter = new AlarmAdapter(getContext(), list);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(alarmAdapter);

        list.clear();

        sqLiteDatabase = databaseHelper.getReadableDatabase();

        cursor = sqLiteDatabase.query("alarms", null, null, null, null, null, null);
        while (cursor.moveToNext()){
            AlarmModel alarmModel= new AlarmModel();

            int idIndex = cursor.getColumnIndex("alarm_id");
            alarmModel.setAlarm_id(cursor.getInt(idIndex));

            int titleIndex = cursor.getColumnIndex("alarm_label");
            alarmModel.setAlarm_label(cursor.getString(titleIndex));

            int hrIndex = cursor.getColumnIndex("alarm_hour");
            alarmModel.setAlarm_hour(cursor.getInt(hrIndex));

            int minIndex = cursor.getColumnIndex("alarm_minute");
            alarmModel.setAlarm_minute(cursor.getInt(minIndex));

            int ringtoneIndex = cursor.getColumnIndex("alarm_ringtone_uri");
            alarmModel.setAlarm_ringtone_uri(cursor.getString(ringtoneIndex));

            int snoozeIndex = cursor.getColumnIndex("alarm_snooze_time");
            alarmModel.setAlarm_snooze_time(cursor.getString(snoozeIndex));

            int monIndex = cursor.getColumnIndex("mon");
            boolean isMon = cursor.getInt(monIndex) == 1;
            alarmModel.setMon(isMon);

            int tueIndex = cursor.getColumnIndex("tue");
            boolean isTue = cursor.getInt(tueIndex) == 1;
            alarmModel.setTue(isTue);

            int wedIndex = cursor.getColumnIndex("wed");
            boolean isWed = cursor.getInt(wedIndex) == 1;
            alarmModel.setWed(isWed);

            int thuIndex = cursor.getColumnIndex("thu");
            boolean isThu = cursor.getInt(thuIndex) == 1;
            alarmModel.setThu(isThu);

            int friIndex = cursor.getColumnIndex("fri");
            boolean isFri = cursor.getInt(friIndex) == 1;
            alarmModel.setFri(isFri);

            int satIndex = cursor.getColumnIndex("sat");
            boolean isSat = cursor.getInt(satIndex) == 1;
            alarmModel.setSat(isSat);

            int sunIndex = cursor.getColumnIndex("sun");
            boolean isSun = cursor.getInt(sunIndex) == 1;
            alarmModel.setSun(isSun);

            int activeIndex = cursor.getColumnIndex("isActive");
            boolean isActive = cursor.getInt(activeIndex) == 1;
            alarmModel.setActive(isActive);

            int requestIndex = cursor.getColumnIndex("request_code");
            alarmModel.setRequest_code(cursor.getInt(requestIndex));

            list.add(alarmModel);

        }
        cursor.close();
        sqLiteDatabase.close();
        alarmAdapter.notifyDataSetChanged();
        return view;
    }
}