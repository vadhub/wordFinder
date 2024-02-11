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


}
