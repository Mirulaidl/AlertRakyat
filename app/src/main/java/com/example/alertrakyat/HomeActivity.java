package com.example.alertrakyat;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements SensorEventListener {

    FirebaseAuth auth;
    Button Logout_BTN, EmergencyCall_BTN;
    TextView UserEmail_TV;
    FirebaseUser user;

    private Sensor accelerometerSensor, barometerSensor;
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);


        RecyclerView articleRecyclerView = findViewById(R.id.articleRecyclerView);
        List<Article> articles = new ArrayList<>();

// Add some articles
        articles.add(new Article("The Star", "Floods: 10,000 people affected in Pitas", "KOTA KINABALU: Some 10,000 residents are estimated to be affected by floods in the rural Pitas district as of 9pm on Friday (Jan 17).", "https://www.thestar.com.my/news/nation/2025/01/18/floods-10000-people-affected-in-pitas"));
        articles.add(new Article("reliefweb", "Malaysia: Floods - Nov 2024", "Peninsular Malaysia, including Johor, Kelantan, Pahang, Perak, and Terengganu States continues to experience heavy rainfall and consequent floods that resulted in displacements and damage.", "https://reliefweb.int/disaster/fl-2024-000218-mys"));
        articles.add(new Article("The Straits Time", "Floods displace close to 139,000 people in Malaysia with death toll at 4", "KUALA LUMPUR – Almost 139,000 people have been forced out of their homes as massive floods caused by relentless rain swept through Malaysia’s northern states, disaster officials said on Nov 30.", "https://www.straitstimes.com/asia/se-asia/floods-displace-122000-people-in-malaysia"));

        ArticleAdapterActivity adapter = new ArticleAdapterActivity(this, articles);
        articleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        articleRecyclerView.setAdapter(adapter);

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
                        startActivity(new Intent(HomeActivity.this, MapActivity.class));
                        overridePendingTransition(0, 0); // Optional: No animation
                        return true;

                    case R.id.nav_report:
                        // Navigate to SettingsActivity
                        startActivity(new Intent(HomeActivity.this, ReportAct.class));
                        overridePendingTransition(0, 0); // Optional: No animation
                        return true;

                    case R.id.nav_profile:
                        // Navigate to SettingsActivity
                        startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                        overridePendingTransition(0, 0); // Optional: No animation
                        return true;



                    default:
                        return false;
                }
            }
        });

        // Initialize SensorManager and Sensors
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            barometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        }

        // Register Accelerometer Sensor
        if (accelerometerSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(this, "Accelerometer not available", Toast.LENGTH_SHORT).show();
        }

        // Register Barometer Sensor
        if (barometerSensor != null) {
            sensorManager.registerListener(this, barometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(this, "Barometer not available", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister sensors when activity is destroyed
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No action needed for now
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        TextView sensorData = findViewById(R.id.sensor_data); // Reference the TextView

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            double movement = Math.sqrt(x * x + y * y + z * z);

            // Detect tremors (example threshold = 15)
            if (movement > 15) {
                Toast.makeText(this, "Unusual movement detected!", Toast.LENGTH_SHORT).show();
            }

            // Update TextView for accelerometer data
            sensorData.setText("Movement: " + movement);
        }

//        if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
//            float pressure = event.values[0];
//
//            // Check pressure for weather/haze prediction
//            if (pressure < 1000) {
//                Toast.makeText(this, "Low pressure detected! Potential storm or haze conditions.", Toast.LENGTH_SHORT).show();
//            }
//
//            // Update TextView for pressure data
//            sensorData.append("\nPressure: " + pressure + " hPa");
//        }
    }



}