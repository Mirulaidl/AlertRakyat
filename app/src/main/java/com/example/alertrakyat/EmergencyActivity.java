package com.example.alertrakyat;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.content.Intent;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.alertrakyat.databinding.ActivityEmergencyBinding;

import java.util.ArrayList;

public class EmergencyActivity extends AppCompatActivity {

    ActivityEmergencyBinding binding;
    mergencyAdapter mergencyAdapter;
    ArrayList<mergency> mergencyArrayList = new ArrayList<>();
    mergency mergencyData;

    private String[] numberList;
    private int lastSelectedPosition = -1;
    private static final int REQUEST_CALL_PERMISSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmergencyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int[] imageList = {R.mipmap.foreground_kkm, R.mipmap.jpam2_foreground, R.mipmap.nadma_foreground, R.mipmap.bomba_foreground, R.mipmap.polis_foreground};
        String[] nameList = {"Kementerian Kesihatan Malaysia", "Jabatan Pertahanan Awam Malaysia", "Agensi Pengurusan Bencana Negara", "Jabatan Bomba dan Penyelamat Malaysia", "Kementerian Kesihatan Malaysia"};
        String[] numberList = {"1234", "1234", "1234", "1234", "1234"};

        for (int i = 0; i < imageList.length; i++){
            mergencyData = new mergency(nameList[i],numberList[i],imageList[i]);
            mergencyArrayList.add(mergencyData);
        }
        mergencyAdapter = new mergencyAdapter(EmergencyActivity.this, mergencyArrayList);
        binding.listView.setAdapter(mergencyAdapter);
        binding.listView.setClickable(true);

        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lastSelectedPosition = position; // Save the position of the selected item
                if (ContextCompat.checkSelfPermission(EmergencyActivity.this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    makeEmergencyCall(numberList, position);
                } else {
                    ActivityCompat.requestPermissions(EmergencyActivity.this, new String[]{android.Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
                }
            }
        });

    }

    private void makeEmergencyCall(String[] num, int position) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + num[position]));
            startActivity(callIntent);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                if (lastSelectedPosition != -1) {
                    makeEmergencyCall(numberList, lastSelectedPosition);
                }
            } else {
                Toast.makeText(this, "Permission Denied! Cannot make the call.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}