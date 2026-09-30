package com.example.tripbuddy_eduv4909634;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BudgetActivity extends AppCompatActivity {

    TextView txtSubtotal, txtDiscount, txtFinalTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_budget);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtSubtotal = findViewById(R.id.txtSubtotal);
        txtDiscount = findViewById(R.id.txtDiscount);
        txtFinalTotal = findViewById(R.id.txtFinalTotal);

        double subtotal = getIntent().getDoubleExtra("subtotal", 0);
        double discount = getIntent().getDoubleExtra("discount", 0);
        double finalTotal = getIntent().getDoubleExtra("finalTotal", 0);

        txtSubtotal.setText("Subtotal: R" + String.format("%.2f", subtotal));
        txtDiscount.setText("Discount: R" + String.format("%.2f", discount));
        txtFinalTotal.setText("Final Total: R" + String.format("%.2f", finalTotal));



    }
}