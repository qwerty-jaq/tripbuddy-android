package com.example.tripbuddy_eduv4909634;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettingActivity extends AppCompatActivity {

    Switch switchDark, switchMusic;
    Spinner spinnerLanguage;
    SharedPreferences prefs;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences prefs = getSharedPreferences("TripBuddySettings", MODE_PRIVATE);
        switchDark = findViewById(R.id.switchDark);
        switchMusic = findViewById(R.id.switchMusic);
        spinnerLanguage = findViewById(R.id.spinnerLanguage);

        ArrayAdapter<String> langAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"English", "Afrikaans"});
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(langAdapter);

        boolean darkMode = prefs.getBoolean("darkMode", false);
        boolean bgMusic = prefs.getBoolean("backgroundMusic", true);
        String language = prefs.getString("language", "English");

        switchDark.setChecked(darkMode);
        switchMusic.setChecked(bgMusic);
        int langPos = language.equals("Afrikaans") ? 1 : 0;
        spinnerLanguage.setSelection(langPos);

        switchDark.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("darkMode", isChecked).apply();
            if (isChecked) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        });

        switchMusic.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("backgroundMusic", isChecked).apply();
        });

        spinnerLanguage.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(android.widget.AdapterView<?> parents, android.view.View view, int position, long id){
                String sel = position == 1 ? "Afrikaans" : "English";
                prefs.edit().putString("language", sel).apply();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}