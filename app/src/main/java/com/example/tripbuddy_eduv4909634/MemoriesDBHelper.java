package com.example.tripbuddy_eduv4909634;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import java.util.ArrayList;
import java.util.List;

public class MemoriesDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "memories.db";
    private static final int DB_VERSION = 2;

    public MemoriesDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE memories (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "text TEXT," +
                "imageUri TEXT," +
                "date TEXT," +
                "mood TEXT," +
                "music TEXT" +
                ")");

        db.execSQL("CREATE TABLE trips (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "destination TEXT," +
                "sightseeing INTEGER," +
                "hiking INTEGER," +
                "dining INTEGER," +
                "museum INTEGER," +
                "customExpense REAL," +
                "total REAL," +
                "date TEXT" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS memories");
        db.execSQL("DROP TABLE IF EXISTS trips");
        onCreate(db);
    }

    public long insertMemory(String text, String imageUri, String location, String date, String mood, String music){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("text", text);
        values.put("imageUri", imageUri);
        values.put("location", location);
        values.put("date", date);
        values.put("mood", mood);
        values.put("music", music);

        return db.insert("memories", null, values);
    }

    public Cursor getMemoriesDesc() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query("memories", null, null, null, null, null, "date DESC");
    }

    public long insertTrip(String destination, boolean sightseeing, boolean hiking, boolean dining, boolean museum, double customExpense, double total, String date) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("destination", destination);
        values.put("sightseeing", sightseeing ? 1 : 0);
        values.put("hiking", hiking ? 1 : 0);
        values.put("dining", dining ? 1 : 0);
        values.put("museum", museum ? 1 : 0);
        values.put("customExpense", customExpense);
        values.put("total", total);
        values.put("date", date);
        return db.insert("trips", null, values);
    }

    public Cursor getLastTrip() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM trips ORDER BY id DESC LIMIT 1", null);
    }

}

