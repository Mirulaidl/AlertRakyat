package com.example.alertrakyat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private EditText usernameEditText, emailEditText, noPhoneEditText;
    private Button editButton, deleteAccountButton , Logout_BTN;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set Home as selected by default
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        // Handle navigation item selection
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        // Navigate to MapActivity
                        startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                        overridePendingTransition(0, 0); // Optional: No animation
                        return true;

                    case R.id.nav_map:
                        // Navigate to MapActivity
                        startActivity(new Intent(ProfileActivity.this, MapActivity.class));
                        overridePendingTransition(0, 0); // Optional: No animation
                        return true;

                    case R.id.nav_report:
                        // Navigate to SettingsActivity
//                        startActivity(new Intent(ProfileActivity.this, ReportActivity.class));
//                        overridePendingTransition(0, 0); // Optional: No animation
                        return true;

                    case R.id.nav_profile:

                        return true;
                    default:
                        return false;
                }
            }
        });

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize UI components
        usernameEditText = findViewById(R.id.etUsername);
        emailEditText = findViewById(R.id.etEmail);
        noPhoneEditText = findViewById(R.id.etPhonenum);
        editButton = findViewById(R.id.btnEdit);
        deleteAccountButton = findViewById(R.id.btnDelete);

        // Set fields to display user data (fetch from Firebase)
        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        mDatabase.child("users").child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                User user = task.getResult().getValue(User.class);
                if (user != null) {
                    usernameEditText.setText(user.getUsername());
                    emailEditText.setText(user.getEmail());
                    noPhoneEditText.setText(user.getNoPhone());
                }
            } else {
                Toast.makeText(ProfileActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });

        // Enable editing when "Edit Profile" is clicked
        editButton.setOnClickListener(v -> {
            usernameEditText.setEnabled(true);
            emailEditText.setEnabled(true);
            noPhoneEditText.setEnabled(true);
            editButton.setText("Save Changes");
            editButton.setOnClickListener(v1 -> saveChanges(userId));
        });

        // Delete account
        deleteAccountButton.setOnClickListener(v -> deleteAccount(userId));


        Logout_BTN = findViewById(R.id.btnLogout);

        Logout_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button emergencyCallButton = findViewById(R.id.btnEmergencycall);

        emergencyCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EmergencyActivity.class);
                startActivity(intent);
            }
        });
    }

    private void saveChanges(String userId) {
        String username = usernameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String noPhone = noPhoneEditText.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(noPhone)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save changes to Firebase Realtime Database
        User updatedUser = new User(username, email, noPhone);
        mDatabase.child("users").child(userId).setValue(updatedUser).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                usernameEditText.setEnabled(false);
                emailEditText.setEnabled(false);
                noPhoneEditText.setEnabled(false);
                editButton.setText("Edit Profile");
            } else {
                Toast.makeText(ProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteAccount(String userId) {
        mDatabase.child("users").child(userId).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mAuth.getCurrentUser().delete().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(ProfileActivity.this, "Account deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Failed to delete account", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(ProfileActivity.this, "Failed to delete account data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}