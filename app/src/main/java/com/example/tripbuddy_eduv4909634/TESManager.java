package com.example.tripbuddy_eduv4909634;

import android.content.Context;
import android.content.SharedPreferences;

public class TESManager {
    private static final String PREFS = "TripBuddyTES";
    private static final String KEY_TES = "TES";

    public static void addPoints(Context ctx, int points) {
        SharedPreferences p = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        int current = p.getInt(KEY_TES, 0);
        p.edit().putInt(KEY_TES, current + points).apply();
    }

    public static int getPoints(Context ctx){
        SharedPreferences p = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return p.getInt(KEY_TES, 0);
    }

    public static void setPoints(Context ctx, int value) {
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putInt(KEY_TES, value).apply();
    }

}
