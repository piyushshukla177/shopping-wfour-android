package com.wfour.onlinestoreapp.datastore;

import android.content.Context;

public class MySharedPreferences {
    private String TAG = getClass().getSimpleName();

    private static final String FRUITY_DROID_PREFERENCES = "MY_PREFERENCES";

    private Context mContext;

    private MySharedPreferences() {
    }

    public MySharedPreferences(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * Save a long integer to MySharedPreferences
     *
     * @param key
     * @param n
     */
    public void putLongValue(String key, long n) {
        // SmartLog.log(TAG, "Set long integer value");
        android.content.SharedPreferences pref = mContext.getSharedPreferences(
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
    public  long getLongValue(String key) {
        // SmartLog.log(TAG, "Get long integer value");
        android.content.SharedPreferences pref = mContext.getSharedPreferences(
                FRUITY_DROID_PREFERENCES, 0);
        return pref.getLong(key, 0);
    }

    /**
     * Save an integer to MySharedPreferences
     *
     * @param key
     * @param n
     */
    public  void putIntValue(String key, int n) {
        // SmartLog.log(TAG, "Set integer value");
        android.content.SharedPreferences pref = mContext.getSharedPreferences(
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
        android.content.SharedPreferences pref = mContext.getSharedPreferences(
                FRUITY_DROID_PREFERENCES, 0);
        return pref.getInt(key, 0);
    }

    /**
     * Save an string to MySharedPreferences
     *
     * @param key
     * @param s
     */
    public  void putStringValue(String key, String s) {
        // SmartLog.log(TAG, "Set string value");
        android.content.SharedPreferences pref = mContext.getSharedPreferences(
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
        android.content.SharedPreferences pref = mContext.getSharedPreferences(
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
        android.content.SharedPreferences pref = mContext.getSharedPreferences(
                FRUITY_DROID_PREFERENCES, 0);
        return pref.getString(key, defaultValue);
    }

    /**
     * Save an boolean to MySharedPreferences
     *
     * @param key
     * @params
     */
    public  void putBooleanValue(String key, Boolean b) {
        // SmartLog.log(TAG, "Set boolean value");
        android.content.SharedPreferences pref = mContext.getSharedPreferences(
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
    public  boolean getBooleanValue(String key) {
        // SmartLog.log(TAG, "Get boolean value");
        android.content.SharedPreferences pref = mContext.getSharedPreferences(
                FRUITY_DROID_PREFERENCES, 0);
        return pref.getBoolean(key, false);
    }

    /**
     * Save an float to MySharedPreferences
     *
     * @param key
     * @params
     */
    public  void putFloatValue(String key, float f) {
        android.content.SharedPreferences pref = mContext.getSharedPreferences(
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
        android.content.SharedPreferences pref = mContext.getSharedPreferences(
                FRUITY_DROID_PREFERENCES, 0);
        return pref.getFloat(key, 0.0f);
    }
}
