package com.idimsoftware.www.androidcommon;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Daniel on 2015-11-08.
 */
public class Prefs {

    // ***** Preference methods *****

    /*
     * Reads a string-value from the default preferences.
     */
    public static String getString(Context context, String key, String defValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(key, defValue);
    }

    /*
     * Writes a string value to the default preferences.
     */
    public static void setString(Context context, String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = prefs.edit();
        try {
            editor.putString(key, value);
        } finally {
            editor.commit();
        }

    }

    /*
     * Reads a boolean value from the default preferences.
     */
    public static boolean getBool(Context context, String key, boolean defValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(key, defValue);
    }

    /*
     * Writes a boolean value to the default preferences.
     */
    public static void setBool(Context context, String key, boolean value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = prefs.edit();
        try {
            editor.putBoolean(key, value);
        } finally {
            editor.commit();
        }

    }

    public static float getFloat(Context context, String key, float defValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getFloat(key, defValue);
    }

    /*
     * Writes a float from the default preferences.
     */
    public static void setFloat(Context context, String key, float value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        try {
            editor.putFloat(key, value);
        } finally {
            editor.commit();
        }
    }


}
