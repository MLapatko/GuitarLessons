package com.example.user.guitarlessons.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

/**
 * Created by user on 26.03.2018.
 */

public class SettingsHelper {


    public final static String CURRENT_LANGUAGE = "language";
    public static final String APP_SETTINGS = "app settings";

    public static void setLanguage(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    public static void saveLanguage(Context context, String mLanguage) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_SETTINGS,
                Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(CURRENT_LANGUAGE, mLanguage).apply();
    }

    public static String getLanguage(Context context) {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
                .getString(CURRENT_LANGUAGE, Locale.getDefault().toLanguageTag());
    }
}
