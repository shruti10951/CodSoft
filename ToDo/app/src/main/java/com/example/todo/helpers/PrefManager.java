package com.example.todo.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    private static final String PREF_NAME = "ToDoPreferences";
    private static final String KEY_FIRST_TIME = "isFirstTime";
    private static final String KEY_USERNAME = "username";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    public PrefManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public boolean isFirstTime() {
        return pref.getBoolean(KEY_FIRST_TIME, true);
    }

    public void setNotFirstTime() {
        editor.putBoolean(KEY_FIRST_TIME, false);
        editor.apply();
    }

    public String getUsername() {
        return pref.getString(KEY_USERNAME, null);
    }

    public void setUsername(String username) {
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }
}
