package com.yahoo.shopping.epoch.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.yahoo.shopping.epoch.constants.AppConstants;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by jamesyan on 9/1/15.
 */
public class FavoriteSpots {
    private static FavoriteSpots sInstance;

    private Context mContext;
    private Set<String> mFavorites;

    public static FavoriteSpots getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new FavoriteSpots(context);
        }

        return sInstance;
    }

    private FavoriteSpots(Context context) {
        mContext = context;
    }

    public void addFavorite(String id) {
        mFavorites.add(id);
        save();
    }

    public void removeFavorite(String id) {
        mFavorites.remove(id);
        save();
    }

    public Set<String> getFavorites() {
        SharedPreferences preferences = mContext.getSharedPreferences(AppConstants.PREFERENCE_STORAGE_NAME, Context.MODE_PRIVATE);
        return preferences.getStringSet(AppConstants.PREFERENCE_FAVORITE_KEY, new LinkedHashSet<String>());
    }

    private void save() {
        SharedPreferences preferences = mContext.getSharedPreferences(AppConstants.PREFERENCE_STORAGE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(AppConstants.PREFERENCE_FAVORITE_KEY, mFavorites);

        editor.commit();
    }
}
