package com.abg.wordfinder.datasource;

import android.content.Context;
import android.content.SharedPreferences;

import com.abg.wordfinder.LocaleChange;

public class Configuration {
    private final Context context;
    private SharedPreferences pref;
    String namePref = "word_finder_v";

    public Configuration(Context context) {
        this.context = context;
    }

    public void saveLocale(String locale) {
        pref = context.getSharedPreferences(namePref, Context.MODE_PRIVATE);
        pref.edit().putString("locale", locale).apply();
    }

    public String getLocale() {
        pref = context.getSharedPreferences(namePref, Context.MODE_PRIVATE);
        return pref.getString("locale", LocaleChange.getLocale(context));
    }

    public void saveGrid(boolean isGrid) {
        pref = context.getSharedPreferences(namePref, Context.MODE_PRIVATE);
        pref.edit().putBoolean("grid", isGrid).apply();
    }

    public boolean getGrid() {
        pref = context.getSharedPreferences(namePref, Context.MODE_PRIVATE);
        return pref.getBoolean("grid", true);
    }

    public void saveContrast(boolean isContrast) {
        pref = context.getSharedPreferences(namePref, Context.MODE_PRIVATE);
        pref.edit().putBoolean("contrast", isContrast).apply();
    }

    public boolean getContrast() {
        pref = context.getSharedPreferences(namePref, Context.MODE_PRIVATE);
        return pref.getBoolean("contrast", false);
    }


}
