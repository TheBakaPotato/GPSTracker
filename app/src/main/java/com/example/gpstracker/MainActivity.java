package com.example.gpstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;

public class MainActivity extends AppCompatActivity {
    public static final int DEFAUL_INTERVAL = 30;
    public static final int FASTEST_INTERVAL = 5;
    TextView tv_lat, tv_lon, tv_altitude, tv_accuracy, tv_speed, tv_sensor, tv_updates, tv_address;
    Switch sw_locationsupdates, sw_gps;
    FusedLocationProviderClient fusedLocationProviderClient;
    boolean updateOn=false;
    LocationRequest locationReqest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_lat = findViewById(R.id.tv_lat);
        tv_lon = findViewById(R.id.tv_lon);
        tv_altitude = findViewById(R.id.tv_altitude);
        tv_accuracy = findViewById(R.id.tv_accuracy);
        tv_speed = findViewById(R.id.tv_speed);
        tv_sensor = findViewById(R.id.tv_sensor);
        tv_updates = findViewById(R.id.tv_updates);
        tv_address = findViewById(R.id.tv_address);
        sw_locationsupdates = findViewById(R.id.sw_locationsupdates);
        sw_gps = findViewById(R.id.sw_gps);

        locationReqest = new LocationRequest();

        locationReqest.setInterval(1000 * DEFAUL_INTERVAL);
        locationReqest.setFastestInterval(1000 * FASTEST_INTERVAL);
        locationReqest.setInterval(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }
}