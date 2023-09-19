package com.example.apgp_puzzlebobble;

import android.content.Context;

import android.content.SharedPreferences;

public class HighScoreManager {
    public static final String PREFERENCES_NAME = "HighScore";
    private static SharedPreferences getPreferences(Context context)
    {
        return context.getSharedPreferences( PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static void setInt(Context context, String key, int value)
    {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(Context context, String key)
    {
        SharedPreferences prefs = getPreferences(context);
        int value = prefs.getInt(key, 0);
        return value;
    }

    public static void setString(Context context, String key, String value)
    {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key)
    {
        SharedPreferences prefs = getPreferences(context);
        String defaultName = "EMP";
        String value = prefs.getString(key, defaultName);
        return value;
    }

}
