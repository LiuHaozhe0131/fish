package com.example.myporject;


import android.content.Context;
import android.content.SharedPreferences;
public class SharedPreferencesUtils {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public SharedPreferencesUtils(Context context, String fileName) {
        preferences = context.getSharedPreferences(fileName, context.MODE_PRIVATE);
        editor = preferences.edit();
    }
    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }
    public int getInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }



}