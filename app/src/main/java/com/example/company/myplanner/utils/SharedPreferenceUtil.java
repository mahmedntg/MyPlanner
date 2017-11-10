package com.example.company.myplanner.utils;

import android.content.Context;
import android.content.SharedPreferences;



public class SharedPreferenceUtil {
    public static final String PREFS_NAME = "PLANNER_APP";
    public static final String periodKey = "period";
    private static SharedPreferenceUtil sharedPreferenceUtil;
    private Context context;

    public static SharedPreferenceUtil getInstance(Context context) {
        if (sharedPreferenceUtil == null) {
            sharedPreferenceUtil = new SharedPreferenceUtil(context);
        }
        return sharedPreferenceUtil;
    }

    private SharedPreferenceUtil() {
    }

    private SharedPreferenceUtil(Context context) {
        this.context = context;
    }

    public SharedPreferences getSharedPreferences() {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        return settings;
    }

    public void clearSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        if (sharedPreferences.contains(periodKey)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
        }
    }

    public boolean isSharedPreferencesExists() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        if (sharedPreferences.contains(periodKey)) {
            return true;
        } else {
            return false;
        }
    }
}
