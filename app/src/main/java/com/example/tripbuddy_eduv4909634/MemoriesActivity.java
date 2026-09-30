package com.example.tripbuddy_eduv4909634;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MemoriesActivity extends AppCompatActivity {

    private static final int REQ_PICK_IMAGE = 200;
    private ImageView imgMemoryPreview;
    private MediaPlayer mediaPlayer;
    private Button btPlay, btPause, btSaveMemory, btPickImage;
    private EditText etMemoryText, etLocation;
    private Spinner spinnerMood, spinnerMusic;
    private Uri selectedImageUri;
    private MemoriesDBHelper dbHelper;
    private SessionManager session;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_memories);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new MemoriesDBHelper(this);
        session = new SessionManager(this);

        imgMemoryPreview = findViewById(R.id.imgMemoryPreview);
        etMemoryText = findViewById(R.id.etMemoryText);
        etLocation = findViewById(R.id.etLocation);
        spinnerMood = findViewById(R.id.spinnerMood);
        spinnerMusic = findViewById(R.id.spinnerMusic);
        btPickImage = findViewById(R.id.btnPickImage);
        btPlay = findViewById(R.id.btPlay);
        btPause = findViewById(R.id.btPause);
        btSaveMemory = findViewById(R.id.btnSaveMemory);

        ImageView img =findViewById(R.id.imgMemoryPreview);
        Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        img.startAnimation(fade);

        mediaPlayer = MediaPlayer.create(MemoriesActivity.this, R.raw.background_music1);

        ArrayAdapter<String> moodAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"Happy", "Sad", "Excited", "Relaxed", "Other"});
        moodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMood.setAdapter(moodAdapter);

        ArrayAdapter<String> musicAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"None", "background_music1", "background_music2"});
        musicAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMusic.setAdapter(musicAdapter);

        btPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, REQ_PICK_IMAGE);
            }
        });

        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String musicName = spinnerMusic.getSelectedItem().toString();
               if (!musicName.equals("None")) {
                   playMusicByName(musicName);
               } else {
                   Toast.makeText(MemoriesActivity.this, "Select a music track", Toast.LENGTH_SHORT).show();
               }
            }
        });

        btPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            }
        });

        btSaveMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMemory();
            }
        });

    }

    private void saveMemory() {
        String text = etMemoryText.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String mood = spinnerMood.getSelectedItem().toString();
        String music = spinnerMusic.getSelectedItem().toString();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
        String imageUriString = selectedImageUri != null ? selectedImageUri.toString() : "";

        long id = dbHelper.insertMemory(text, imageUriString, location, date, mood, music);
        if (id > 0 ) {
            session.addTES(3);
            Toast.makeText(this, "Memory Saved", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to save memory", Toast.LENGTH_SHORT).show();
        }
    }

    private void playMusicByName(String name) {
        int resId = getResources().getIdentifier(name, "raw", getPackageName());
        if (resId == 0) {
            Toast.makeText(this, "Music  not found", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = MediaPlayer.create(this, resId);
        mediaPlayer.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            imgMemoryPreview.setImageURI(selectedImageUri);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }


    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}