package com.example.notes;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.appcompat.app.AppCompatDelegate;

public class Themes {

    public static final String SHARED_PREFERENCES_NAME = "preferences";
    public static final String MODE_KEY = "mode";
    public static final String COLOR_THEME_KEY = "color_theme";

    public static void applyTheme(Context context) {
        int modeIndex = 1;   // default
        int colorThemeIndex = 1;   // default

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME,
                Activity.MODE_PRIVATE);
        if (sharedPreferences != null) {
            modeIndex = sharedPreferences.getInt(MODE_KEY, 1);
            colorThemeIndex = sharedPreferences.getInt(COLOR_THEME_KEY, 1);
        }

        applyDarkMode(context, modeIndex);
        applyColorTheme(context, colorThemeIndex);
    }

    public static void applyDarkMode(Context context, int modeIndex) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(MODE_KEY, modeIndex);
        editor.apply();

        switch (modeIndex) {
            case 1:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
                }
                break;
            case 2:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case 3:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
        }
    }

    public static void applyColorTheme(Context context, int colorThemeIndex) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(COLOR_THEME_KEY, colorThemeIndex);
        editor.apply();

        switch (colorThemeIndex) {
            case 1:
                context.setTheme(R.style.MyTheme_Maroon);
                break;
            case 2:
                context.setTheme(R.style.MyTheme_Emerald);
                break;
            case 3:
                context.setTheme(R.style.MyTheme);
                break;
            default:
                break;
        }
    }
}