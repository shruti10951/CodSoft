package com.example.alarmclock;

public class AlarmModel {

    private int alarm_id;
    private int alarm_hour;
    private int alarm_minute;
    private String alarm_label;
    private String alarm_ringtone_uri;
    private String alarm_snooze_time;
    private boolean mon;
    private boolean tue;
    private boolean wed;
    private boolean thu;
    private boolean fri;
    private boolean sat;
    private boolean sun;
    private boolean isActive;

    private int request_code;

    public AlarmModel(int alarm_id, int alarm_hour, int alarm_minute, String alarm_label, String alarm_ringtone_uri, String alarm_snooze_time, boolean mon, boolean tue, boolean wed, boolean thu, boolean fri, boolean sat, boolean sun, boolean isActive, int request_code) {
        this.alarm_id = alarm_id;
        this.alarm_hour = alarm_hour;
        this.alarm_minute = alarm_minute;
        this.alarm_label = alarm_label;
        this.alarm_ringtone_uri = alarm_ringtone_uri;
        this.alarm_snooze_time = alarm_snooze_time;
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
        this.sat = sat;
        this.sun = sun;
        this.isActive = isActive;
        this.request_code = request_code;
    }

    public AlarmModel() {
    }

    public int getRequest_code() {
        return request_code;
    }

    public void setRequest_code(int request_code) {
        this.request_code = request_code;
    }

    public int getAlarm_id() {
        return alarm_id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setAlarm_id(int alarm_id) {
        this.alarm_id = alarm_id;
    }

    public int getAlarm_hour() {
        return alarm_hour;
    }

    public void setAlarm_hour(int alarm_hour) {
        this.alarm_hour = alarm_hour;
    }

    public int getAlarm_minute() {
        return alarm_minute;
    }

    public void setAlarm_minute(int alarm_minute) {
        this.alarm_minute = alarm_minute;
    }

    public String getAlarm_label() {
        return alarm_label;
    }

    public void setAlarm_label(String alarm_label) {
        this.alarm_label = alarm_label;
    }

    public String getAlarm_ringtone_uri() {
        return alarm_ringtone_uri;
    }

    public void setAlarm_ringtone_uri(String alarm_ringtone_uri) {
        this.alarm_ringtone_uri = alarm_ringtone_uri;
    }

    public String getAlarm_snooze_time() {
        return alarm_snooze_time;
    }

    public void setAlarm_snooze_time(String alarm_snooze_time) {
        this.alarm_snooze_time = alarm_snooze_time;
    }

    public boolean isMon() {
        return mon;
    }

    public void setMon(boolean mon) {
        this.mon = mon;
    }

    public boolean isTue() {
        return tue;
    }

    public void setTue(boolean tue) {
        this.tue = tue;
    }

    public boolean isWed() {
        return wed;
    }

    public void setWed(boolean wed) {
        this.wed = wed;
    }

    public boolean isThu() {
        return thu;
    }

    public void setThu(boolean thu) {
        this.thu = thu;
    }

    public boolean isFri() {
        return fri;
    }

    public void setFri(boolean fri) {
        this.fri = fri;
    }

    public boolean isSat() {
        return sat;
    }

    public void setSat(boolean sat) {
        this.sat = sat;
    }

    public boolean isSun() {
        return sun;
    }

    public void setSun(boolean sun) {
        this.sun = sun;
    }
}
