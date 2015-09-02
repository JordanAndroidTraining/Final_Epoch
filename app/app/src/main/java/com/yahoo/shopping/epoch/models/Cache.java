package com.yahoo.shopping.epoch.models;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.LinkedHashSet;
import java.util.Set;

public class Cache {

    private Context mContext;
    private SharedPreferences mPreference;

    public Cache(String cacheName, Context context) {
        mContext = context;
        mPreference = context.getSharedPreferences(cacheName, Context.MODE_PRIVATE);
    }

    public String getString(String key) {
        return mPreference.getString(key, "");
    }

    public Set<String> getStringSet(String key) {
        Set<String> prefValues = mPreference.getStringSet(key, new LinkedHashSet<String>());

        Set<String> values = new LinkedHashSet<>();
        values.addAll(prefValues);

        return values;
    }

    public void storeString(String key, String value) {
        mPreference.edit().putString(key, value).apply();
    }

    public void storeStringSet(String key, Set<String> values) {
        mPreference.edit().putStringSet(key, values).apply();
    }

    public void remove(String key) {
        mPreference.edit().remove(key).apply();
    }
}
