package com.example.alertrakyat;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;


import androidx.annotation.NonNull;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap myMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_map);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    // Navigate to HomeActivity
                    startActivity(new Intent(MapActivity.this, HomeActivity.class));
                    overridePendingTransition(0, 0); // Optional
                    finish(); // Optional
                    return true;

                case R.id.nav_map:
                    // Stay in MapActivity (no action needed)
                    return true;

                case R.id.nav_report:
                    // Navigate to ReportActivity
                    startActivity(new Intent(MapActivity.this, ReportAct.class));
                    overridePendingTransition(0, 0); // Optional
                    finish(); // Optional
                    return true;

                case R.id.nav_profile:
                    // Navigate to SettingsActivity
                    startActivity(new Intent(MapActivity.this, ProfileActivity.class));
                    overridePendingTransition(0, 0); // Optional: No animation
                    return true;
                default:
                    return false;
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;

        // Latitude and Longitude of Malaysia
        LatLng malaysia = new LatLng(4.2105, 101.9758);

        // Add a marker at Malaysia
        myMap.addMarker(new MarkerOptions().position(malaysia).title("Malaysia").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

        // Move the camera to Malaysia with a zoom level of 6 (adjustable)
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(malaysia, 6));

        // Enable user's current location
        enableMyLocation();

        addFloodZones();
    }

    private void addFloodZones() {
        LatLng floodArea1 = new LatLng(3.1390, 101.6869); // Example flood location
        LatLng floodArea2 = new LatLng(5.4112, 100.3354); // Example flood location
        LatLng earthquakearea = new LatLng(2.022882, 103.311456);


        myMap.addMarker(new MarkerOptions()
                .position(earthquakearea)
                .title("Earthquake")
                .snippet("Severity: High")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.gempa)));

        // Marker for flood area with high severity (Red color)
        myMap.addMarker(new MarkerOptions()
                .position(floodArea1)
                .title("Flood Zone")
                .snippet("Severity: High")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.banjir1)));

// Marker for flood area with medium severity (Yellow color)
        myMap.addMarker(new MarkerOptions()
                .position(floodArea2)
                .title("Flood Zone")
                .snippet("Severity: Medium")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.banjir2)));

// Circle for flood area 1 (High severity)
        myMap.addCircle(new CircleOptions()
                .center(floodArea1)
                .radius(1000) // in meters
                .strokeColor(Color.RED) // Circle border color
                .fillColor(Color.argb(50, 255, 0, 0)) // Semi-transparent red fill
                .strokeWidth(5)); // Border thickness

// Circle for flood area 2 (Medium severity) - Yellow circle
        myMap.addCircle(new CircleOptions()
                .center(floodArea2)
                .radius(1000) // in meters
                .strokeColor(Color.YELLOW) // Circle border color
                .fillColor(Color.argb(50, 255, 255, 0)) // Semi-transparent yellow fill
                .strokeWidth(5)); // Border thickness

// Polygon for earthquake area
        PolygonOptions earthquakePolygon = new PolygonOptions()
                .add(
                        new LatLng(2.031533, 103.294659),
                        new LatLng(2.002555, 103.305988),
                        new LatLng(2.029475, 103.339462),
                        new LatLng(2.045764, 103.317833)
                )
                .strokeColor(Color.MAGENTA) // Polygon border color
                .fillColor(Color.argb(50, 255, 0, 255)) // Semi-transparent magenta fill
                .strokeWidth(5);

        myMap.addPolygon(earthquakePolygon);

    }

    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        myMap.setMyLocationEnabled(true);
        showCurrentLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
            } else {
                // Permission denied
            }
        }
    }

    private void showCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                    // Add a marker at the user's current location
                    myMap.addMarker(new MarkerOptions()
                            .position(currentLocation)
                            .title("You are here")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                    // Move the camera to the user's current location
                    myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 14));
                }
            });
        }
    }




}