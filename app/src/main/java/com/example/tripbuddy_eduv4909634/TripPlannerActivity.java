package com.example.tripbuddy_eduv4909634;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class TripPlannerActivity extends AppCompatActivity {

    Button btnPickDate, btnSaveTrip;
    TextView txtSelectedDate, txtTotal;
    EditText etCustomExpense, etDestination, etNotes;
    CheckBox chkSightSeeing, chkHiking, chkDining, chkMuseum;

    MemoriesDBHelper dbHelper;
    SessionManager session;

    private final int SIGHTSEE_COST = 500;
    private final int HIKING_COST = 300;
    private final int DINING_COST = 400;
    private final int MUSEUM_COST = 200;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trip_planner);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new MemoriesDBHelper(this);
        session = new SessionManager(this);

        etDestination = findViewById(R.id.etDestination);
        etNotes = findViewById(R.id.etNotes);
        txtTotal = findViewById(R.id.txtTotal);
        etCustomExpense = findViewById(R.id.etCustomExpense);
        btnPickDate = findViewById(R.id.btnPickDate);
        txtSelectedDate = findViewById(R.id.txtSelectDate);
        chkSightSeeing = findViewById(R.id.chkSightSeeing);
        chkHiking = findViewById(R.id.chkHiking);
        chkDining = findViewById(R.id.chkDining);
        chkMuseum = findViewById(R.id.chkMuseum);
        btnSaveTrip = findViewById(R.id.btnSaveTrip);

        etCustomExpense.addTextChangedListener(watcher);
        chkSightSeeing.setOnCheckedChangeListener((buttonView, isChecked) -> updateTotal());
        chkHiking.setOnCheckedChangeListener((buttonView, isChecked) -> updateTotal());
        chkDining.setOnCheckedChangeListener((buttonView, isChecked) -> updateTotal());
        chkMuseum.setOnCheckedChangeListener((buttonView, isChecked) -> updateTotal());

        btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date();
            }
        });

        preloadLastTrip();

        btnSaveTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTrip();
            }
        });

        updateTotal();


    }

    private void saveTrip() {
        String destination = etDestination.getText().toString().trim();
        String date = txtSelectedDate.getText().toString().trim();
        double subtotal = calculateTotal();

        int previousTrips = session.getTripCount();
        double discountAmount = 0;
        if (previousTrips >= 3 ) {
            discountAmount = subtotal *0.10;
        }

        double finalTotal = subtotal - discountAmount;
        boolean sightseeing = chkSightSeeing.isChecked();
        boolean hiking = chkHiking.isChecked();
        boolean dining = chkDining.isChecked();
        boolean museum = chkMuseum.isChecked();
        double customExpense = 0;
        try {
            String val = etCustomExpense.getText().toString().trim();
            if (!val.isEmpty()) customExpense = Double.parseDouble(val);
        } catch (NumberFormatException ignored) {}

        long id = dbHelper.insertTrip(destination, sightseeing, hiking, dining, museum, customExpense, finalTotal, date);
        if (id > 0) {
            session.incrementTripCount();
            session.addTES(5);

            getSharedPreferences("LastTrip", MODE_PRIVATE)
                    .edit()
                    .putString("last_destination", destination)
                    .putBoolean("last_sightseeing", sightseeing)
                    .putBoolean("last_hiking", hiking)
                    .putBoolean("last_dining", dining)
                    .putBoolean("last_museum", museum)
                    .putFloat("last_custom", (float) customExpense)
                    .apply();

            Intent i = new Intent(TripPlannerActivity.this, BudgetActivity.class);
            i.putExtra("subtotal", subtotal);
            i.putExtra("discount", discountAmount);
            i.putExtra("finalTotal", finalTotal);
            startActivity(i);
            finish();
        } else {
            Toast.makeText(this, "Failed to save trip, please try again!", Toast.LENGTH_SHORT).show();
        }

    }

    private void preloadLastTrip() {
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {}

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            updateTotal();
        }
    };

    private void updateTotal() {
        double total = calculateTotal();
        txtTotal.setText("Total: R" + String.format(Locale.getDefault(), "%.2f", total));
    }

    private double calculateTotal() {
        double total = 0;
        if (chkSightSeeing.isChecked()) total += SIGHTSEE_COST;
        if (chkHiking.isChecked()) total += HIKING_COST;
        if (chkDining.isChecked()) total += DINING_COST;
        if (chkMuseum.isChecked()) total += MUSEUM_COST;

        try {
            String val = etCustomExpense.getText().toString().trim();
            if (!val.isEmpty()){
                double custom = Double.parseDouble(val);
                if (custom >= 0) total += custom;
                else etCustomExpense.setError("Enter a positive number");
            }

        } catch (NumberFormatException ignored) {}

        txtTotal.setText("Total: R" + total);
        return total;
    }

    private void Expense() {
        double customExpense = 0;

        try {
            String val = etCustomExpense.getText().toString();
            if (!val.isEmpty()) {
                customExpense = Double.parseDouble(val);
                if(customExpense < 0){
                    customExpense = 0;
                    etCustomExpense.setError("Enter a postive number only!");
                }
            }
        } catch (NumberFormatException e) {
            etCustomExpense.setError("Invalid number");
        }

    }

    private void Date() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(TripPlannerActivity.this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    txtSelectedDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1);
                }, year, month, day);
        dpd.show();

    };

}