package com.example.tripbuddy_eduv4909634;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "TripBuddySession";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_TRIP_COUNT = "tripCount";
    private static final String KEY_TES = "tes";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void login(String username) {
        editor.putString(KEY_USERNAME, username);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getUsername() {
        return prefs.getString(KEY_USERNAME, null);
    }

    public int getTripCount() { return prefs.getInt(KEY_TRIP_COUNT, 0); }

    public void setTripCount(int count) {
        editor.putInt(KEY_TRIP_COUNT, count);
        editor.apply();
    }

    public void incrementTripCount() {
        int c = getTripCount();
        editor.putInt(KEY_TRIP_COUNT, c +1);
        editor.apply();
    }

    public int getTES() {
        return prefs.getInt(KEY_TES, 0);
    }

    public void addTES(int amount) {
        int s = getTES();
        editor.putInt(KEY_TES, s + amount);
        editor.apply();
    }

    public void resetTES(){
        editor.putInt(KEY_TES, 0);
        editor.apply();
    }

}
