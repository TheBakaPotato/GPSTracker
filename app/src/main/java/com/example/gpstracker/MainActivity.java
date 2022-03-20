package com.example.gpstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int DEFAUL_INTERVAL = 30;
    public static final int FASTEST_INTERVAL = 5;
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    TextView tv_lat, tv_lon, tv_altitude, tv_accuracy, tv_speed, tv_sensor, tv_updates, tv_address, tv_waypointCounts;
    Switch sw_locationsupdates, sw_gps;
    FusedLocationProviderClient fusedLocationProviderClient;
    Button btn_newWayPoint, btn_showWayPointList;
    boolean updateOn = false;
    LocationRequest locationRequest;
    LocationCallback locationCallBack;

    Location currentLocation;
    List<Location> savedLocations;

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
        tv_waypointCounts = findViewById(R.id.tv_countofCrumbs);
        sw_locationsupdates = findViewById(R.id.sw_locationsupdates);
        sw_gps = findViewById(R.id.sw_gps);
        btn_newWayPoint = findViewById(R.id.btn_newWayPoint);
        btn_showWayPointList = findViewById(R.id.btn_showWaypointList);

        locationRequest = new LocationRequest();

        locationRequest.setInterval(1000 * DEFAUL_INTERVAL);
        locationRequest.setFastestInterval(1000 * FASTEST_INTERVAL);
        locationRequest.setInterval(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        locationCallBack = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                updateUIValues(location);
            }
        };
        btn_newWayPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAplication myAplication = (MyAplication)getApplicationContext();
                savedLocations = myAplication.getMyLocations();
                savedLocations.add(currentLocation);

            }
        });

        btn_showWayPointList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sw_gps.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (sw_gps.isChecked()) {
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    tv_sensor.setText("Using GPS sensors");
                } else {
                    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                    tv_sensor.setText("Using tower signal + wifi");

                }
            }
        });
        sw_locationsupdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw_locationsupdates.isChecked()) {
                    startLocationUpdates();
                } else {
                    stopLocationUpdates();
                }
            }
        });
        updateGPS();
    }

    private void startLocationUpdates() {
        try {
        tv_updates.setText("Location is being tracked.");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);
        updateGPS();
        }catch (Exception e){

        }
    }

    private void stopLocationUpdates() {
        tv_updates.setText("Location isn't being tracked");
        tv_speed.setText("Location isn't being tracked");
        tv_altitude.setText("Location isn't being tracked");
        tv_accuracy.setText("Location isn't being tracked");
        tv_lon.setText("Location isn't being tracked");
        tv_lat.setText("Location isn't being tracked");
        tv_sensor.setText("Location isn't being tracked");
        tv_address.setText("Location isn't being tracked");
        fusedLocationProviderClient.removeLocationUpdates(locationCallBack);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSIONS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    try {
                        updateGPS();

                    }catch (Exception e){

                    }
                }else{
                    Toast.makeText(this, "This app needs permission to run", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    private void updateGPS(){
        try {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        try {
                            updateUIValues(location);
                            currentLocation = location;
                        }catch (Exception e){

                        }

                    }
                });
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
                }

            }
        }catch (Exception e){

        }
    }

    private void updateUIValues(Location location) {
        tv_lat.setText(String.valueOf(location.getLatitude()));
        tv_lon.setText(String.valueOf(location.getLongitude()));
        tv_accuracy.setText(String.valueOf(location.getAccuracy()));
        if (location.hasAccuracy()==true){
            tv_altitude.setText(String.valueOf(location.getAltitude()));
        }else{
            tv_altitude.setText("Not available");
        }
        if (location.hasSpeed()==true){
            tv_speed.setText(String.valueOf(location.getSpeed()));
        }else{
            tv_speed.setText("Not available");
        }
        Geocoder geocoder = new Geocoder(MainActivity.this);
        try {
            List<Address> addresses =geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1 );
            tv_address.setText(addresses.get(0).getAddressLine(0));
        }catch (Exception e){
            tv_address.setText("Unable to get street address");
        }

        MyAplication myAplication = (MyAplication)getApplicationContext();
        savedLocations = myAplication.getMyLocations();

        tv_waypointCounts.setText(" "+Integer.toString(savedLocations.size()));
    }
}