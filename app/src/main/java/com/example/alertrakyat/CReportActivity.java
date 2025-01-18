package com.example.alertrakyat;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CReportActivity extends AppCompatActivity {
    private EditText reportName, reportDate, reportDescription;
    private Button submitReport;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_creport);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get the current user's UID

        // Initialize views
        reportName = findViewById(R.id.report_name);
        reportDate = findViewById(R.id.report_date);
        reportDescription = findViewById(R.id.report_description);
        submitReport = findViewById(R.id.submit_report);

        // Submit report
        submitReport.setOnClickListener(v -> createReport());


        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set Home as selected by default
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        // Handle navigation item selection
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        // Stay on the current page
                        return true;

                    case R.id.nav_map:
                        // Navigate to MapActivity
                        startActivity(new Intent(CReportActivity.this, MapActivity.class));
                        overridePendingTransition(0, 0); // Optional: No animation
                        return true;

                    case R.id.nav_report:
                        // Navigate to SettingsActivity
                        startActivity(new Intent(CReportActivity.this, ReportAct.class));
                        overridePendingTransition(0, 0); // Optional: No animation
                        return true;

                    case R.id.nav_profile:
                        // Navigate to SettingsActivity
                        startActivity(new Intent(CReportActivity.this, ProfileActivity.class));
                        overridePendingTransition(0, 0); // Optional: No animation
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    private void createReport() {
        String name = reportName.getText().toString().trim();
        String date = reportDate.getText().toString().trim();
        String description = reportDescription.getText().toString().trim();

        if (name.isEmpty() || date.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
        } else {
            // Get the current user's UID
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // Save to Firebase under user's UID
            DatabaseReference database = FirebaseDatabase.getInstance().getReference("reports").child(userId);
            String reportId = database.push().getKey();  // Automatically generate unique key for the report

            Report newReport = new Report(name, date, description);

            database.child(reportId).setValue(newReport).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(CReportActivity.this, "Report Created Successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CReportActivity.this, ReportAct.class);
                    startActivity(intent);
                    finish(); // Close the CreateReport activity
                } else {
                    Toast.makeText(CReportActivity.this, "Failed to create report", Toast.LENGTH_SHORT).show();
                    Log.e("FirebaseError", "Error: ", task.getException());
                }
            });

        }
    }
}