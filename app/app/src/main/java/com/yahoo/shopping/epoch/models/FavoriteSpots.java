package com.yahoo.shopping.epoch.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.yahoo.shopping.epoch.constants.AppConstants;

import java.util.LinkedHashSet;
import java.util.Set;

public class FavoriteSpots {
    private static FavoriteSpots sInstance;

    private Context mContext;

    public static FavoriteSpots getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new FavoriteSpots(context);
        }

        return sInstance;
    }

    private FavoriteSpots(Context context) {
        mContext = context;
    }

    public boolean contains(String resourceId) {
        Set<String> favorites = getFavorites();
        return favorites.contains(resourceId);
    }

    public void addFavorite(String id) {
        Set<String> favorites = getFavorites();
        favorites.add(id);
        save(favorites);
    }

    public void removeFavorite(String id) {
        Set<String> favorites = getFavorites();
        favorites.remove(id);
        save(favorites);
    }

    public Set<String> getFavorites() {
        SharedPreferences preferences = mContext.getSharedPreferences(AppConstants.PREFERENCE_STORAGE_NAME, Context.MODE_PRIVATE);
        Set<String> prefFavorite = preferences.getStringSet(AppConstants.PREFERENCE_FAVORITE_KEY, new LinkedHashSet<String>());

        Set<String> favorites = new LinkedHashSet<>();
        favorites.addAll(prefFavorite);

        return favorites;
    }

    private void save(Set<String> favorites) {
        SharedPreferences preferences = mContext.getSharedPreferences(AppConstants.PREFERENCE_STORAGE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(AppConstants.PREFERENCE_FAVORITE_KEY, favorites);

        editor.commit();
    }
}
