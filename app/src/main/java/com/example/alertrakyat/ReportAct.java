package com.example.alertrakyat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReportAct extends AppCompatActivity {
    private Button addReportButton;
    private RecyclerView recyclerView;
    private ReportAdapter reportAdapter;
    private ArrayList<Report> reportList;
    private FirebaseAuth mAuth;
    private String userId; // Declare userId variable


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Initialize userId

        // Set up UI components
        recyclerView = findViewById(R.id.reports_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reportList = new ArrayList<>();
        reportAdapter = new ReportAdapter(reportList, userId);
        recyclerView.setAdapter(reportAdapter);

        // Button to add a new report
        addReportButton = findViewById(R.id.add_report);
        addReportButton.setOnClickListener(v -> {
            // Navigate to CreateReport activity
            startActivity(new Intent(ReportAct.this, CReportActivity.class));
        });

        // Load reports for the current user from Firebase
        loadUserReportsFromFirebase();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_report);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    // Navigate to HomeActivity
                    startActivity(new Intent(ReportAct.this, HomeActivity.class));
                    overridePendingTransition(0, 0); // Optional
                    finish(); // Optional
                    return true;

                case R.id.nav_map:
                    // Navigate to MapActivity
                    startActivity(new Intent(ReportAct.this, MapActivity.class));
                    overridePendingTransition(0, 0); // Optional
                    finish(); // Optional
                    return true;

                case R.id.nav_report:
                    // Stay in ReportAct (no action needed)
                    return true;

                case R.id.nav_profile:
                    // Navigate to SettingsActivity
                    startActivity(new Intent(ReportAct.this, ProfileActivity.class));
                    overridePendingTransition(0, 0); // Optional: No animation
                    return true;
                default:
                    return false;
            }
        });
    }

    private void loadUserReportsFromFirebase() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get current user's UID
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("reports").child(userId); // Reference to reports of current user

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reportList.clear();  // Clear the list before adding new data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Report report = snapshot.getValue(Report.class); // Get the report object
                    report.setId(snapshot.getKey()); // Set the report ID (key from Firebase)
                    if (report != null) {
                        reportList.add(report); // Add report to the list
                    }
                }
                reportAdapter = new ReportAdapter(reportList, userId);
                recyclerView.setAdapter(reportAdapter); // Set adapter after reports are loaded
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ReportAct.this, "Failed to load reports", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


