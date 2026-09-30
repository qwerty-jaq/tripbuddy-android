package com.example.tripbuddy_eduv4909634;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Context;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    public Button btnMemories, btnGallery, btnTripPlanner, btnBudget, btLogout;
    public TextView tvWelcome, tvTES;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btLogout = findViewById(R.id.btLogout);
        tvWelcome = findViewById(R.id.tvWelcome);
        tvTES = findViewById(R.id.tvTES);
        btnMemories = findViewById(R.id.btnMemories);
        btnGallery = findViewById(R.id.btnGallery);
        btnTripPlanner = findViewById(R.id.btnTripPlanner);
        btnBudget = findViewById(R.id.btnBudget);

        SessionManager session = new SessionManager(this);

        String username = getIntent().getStringExtra("username");
        if (username == null || username.isEmpty()){
            username = session.getUsername();
        }
        if (username != null && !username.isEmpty()) {
            tvWelcome.setText("Welcome, " + username + "!");
        }

        tvTES.setText("TES: " + session.getTES());

        btnMemories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MemoriesActivity.class));
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, GalleryActivity.class));
            }
        });

        btnTripPlanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, TripPlannerActivity.class));
            }
        });

        btnBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, BudgetActivity.class));
            }
        });

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager session = new SessionManager(HomeActivity.this);
                session.logout();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}