package com.wfour.onlinestoreapp;

import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;

public class MySharedPreferences {
    private String TAG = getClass().getSimpleName();

    // public static MySharedPreferences prefs.
    private static final String FRUITY_DROID_PREFERENCES = "FRUITY_DROID_PREFERENCES";

    public static final String APPLICATION_INSTALL_FIRST_TIME = "APPLICATION_INSTALL_FIRST_TIME";

    public static final String PREF_SETTING_USER_ID = "PREF_SETTING_USER_ID";

    public static Uri PREF_SETTING_URI_REMINDER = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    public static Uri PREF_SETTING_URI_NOTIFI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    public static final String PREF_URI = "";
    public static String CHECKNOTIFY = "CHECKNOTIFY";
    public static final String VOLUME_REMINDER = "VOLUME_REMINDER";
    public static final String VOLUME_NOTIFY = "VOLUME_NOTIFY";
    public static final String PREF_SETTING_USER_ACCESS_TOKEN = "PREF_SETTING_USER_ACCESS_TOKEN";

    public static final String USE_ACTIVE_MAP = "USE_ACTIVE_MAP";

    public static final String PREF_LAST_LOCATION_LONG = "PREF_LAST_LOCATION_LONG";
    public static final String PREF_LAST_LOCATION_LAT = "PREF_LAST_LOCATION_LAT";

    public static final String PREF_DEFAULT_LANGUAGE = "PREF_DEFAULT_LANGUAGE";

    public static final String PREF_USER_IS_KICK_OUT = "PREF_USER_IS_KICK_OUT";
    public static final String JSON_SCHEDULE = "JSON_SCHEDULE";
    public static final String JSON_GROUPS = "JSON_GROUPS";
    public static final String JSON_TREE_MAP = "JSON_TREE_MAP";

    // ================================================================

    private static MySharedPreferences instance;

    private Context context;

    private MySharedPreferences() {
    }

    /**
     * Constructor
     *
     * @param context
     * @return
     */
    public static MySharedPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new MySharedPreferences();
            instance.context = context;
        }
        return instance;
    }

    public MySharedPreferences(Context context) {
        this.context = context;
    }
    // ======================== UTILITY FUNCTIONS ========================

    // ======================== CORE FUNCTIONS ========================

    /**
     * Save a long integer to MySharedPreferences
     *
     * @param key
     * @param n
     */
    public void putLongValue(String key, long n) {
        // SmartLog.log(TAG, "Set long integer value");
        android.content.SharedPreferences pref = context.getSharedPreferences(
                FRUITY_DROID_PREFERENCES, 0);
        android.content.SharedPreferences.Editor editor = pref.edit();
        editor.putLong(key, n);
        editor.commit();
    }

    /**
     * Read a long integer to MySharedPreferences
     *
     * @param key
     * @return
     */
    public long getLongValue(String key) {
        // SmartLog.log(TAG, "Get long integer value");
        android.content.SharedPreferences pref = context.getSharedPreferences(
                FRUITY_DROID_PREFERENCES, 0);
        return pref.getLong(key, 0);
    }

    /**
     * Save an integer to MySharedPreferences
     *
     * @param key
     * @param n
     */
    public void putIntValue(String key, int n) {
        // SmartLog.log(TAG, "Set integer value");
        android.content.SharedPreferences pref = context.getSharedPreferences(
                FRUITY_DROID_PREFERENCES, 0);
        android.content.SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, n);
        editor.commit();
    }

    /**
     * Read an integer to MySharedPreferences
     *
     * @param key
     * @return
     */
    public int getIntValue(String key) {
        // SmartLog.log(TAG, "Get integer value");
        android.content.SharedPreferences pref = context.getSharedPreferences(
                FRUITY_DROID_PREFERENCES, 0);
        return pref.getInt(key, 0);
    }

    /**
     * Save an string to MySharedPreferences
     *
     * @param key
     * @param s
     */
    public void putStringValue(String key, String s) {
        // SmartLog.log(TAG, "Set string value");
        android.content.SharedPreferences pref = context.getSharedPreferences(
                FRUITY_DROID_PREFERENCES, 0);
        android.content.SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, s);
        editor.commit();
    }

    /**
     * Read an string to MySharedPreferences
     *
     * @param key
     * @return
     */
    public String getStringValue(String key) {
        // SmartLog.log(TAG, "Get string value");
        android.content.SharedPreferences pref = context.getSharedPreferences(
                FRUITY_DROID_PREFERENCES, 0);
        return pref.getString(key, "");
    }

    /**
     * Read an string to MySharedPreferences
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getStringValue(String key, String defaultValue) {
        // SmartLog.log(TAG, "Get string value");
        android.content.SharedPreferences pref = context.getSharedPreferences(
                FRUITY_DROID_PREFERENCES, 0);
        return pref.getString(key, defaultValue);
    }

    /**
     * Save an boolean to MySharedPreferences
     *
     * @param key
     * @params
     */
    public void putBooleanValue(String key, Boolean b) {
        // SmartLog.log(TAG, "Set boolean value");
        android.content.SharedPreferences pref = context.getSharedPreferences(
                FRUITY_DROID_PREFERENCES, 0);
        android.content.SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, b);
        editor.commit();
    }

    /**
     * Read an boolean to MySharedPreferences
     *
     * @param key
     * @return
     */
    public boolean getBooleanValue(String key) {
        // SmartLog.log(TAG, "Get boolean value");
        android.content.SharedPreferences pref = context.getSharedPreferences(
                FRUITY_DROID_PREFERENCES, 0);
        return pref.getBoolean(key, false);
    }

    /**
     * Save an float to MySharedPreferences
     *
     * @param key
     * @params
     */
    public void putFloatValue(String key, float f) {
        android.content.SharedPreferences pref = context.getSharedPreferences(
                FRUITY_DROID_PREFERENCES, 0);
        android.content.SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(key, f);
        editor.commit();
    }

    /**
     * Read an float to MySharedPreferences
     *
     * @param key
     * @return
     */
    public float getFloatValue(String key) {
        android.content.SharedPreferences pref = context.getSharedPreferences(
                FRUITY_DROID_PREFERENCES, 0);
        return pref.getFloat(key, 0.0f);
    }
}
