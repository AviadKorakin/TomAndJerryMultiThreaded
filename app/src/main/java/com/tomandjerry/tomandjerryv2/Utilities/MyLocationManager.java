package com.tomandjerry.tomandjerryv2.Utilities;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MyLocationManager {

    private final FusedLocationProviderClient fusedLocationClient;
    private final LocationRequest locationRequest;
    private final LocationCallback locationCallback;
    private final Activity activity;
    private final Executor executor;
    private double lat;
    private double lng;

    public MyLocationManager(Activity activity) {
        this.activity = activity;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);

        locationRequest = new LocationRequest.Builder(5000).setMinUpdateIntervalMillis(2000).setPriority(Priority.PRIORITY_HIGH_ACCURACY).build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location mostAccurateLocation = null;
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        if (mostAccurateLocation == null || location.getAccuracy() < mostAccurateLocation.getAccuracy()) {
                            mostAccurateLocation = location;
                        }
                    }
                }
                if (mostAccurateLocation != null) {
                    lat = mostAccurateLocation.getLatitude();
                    lng = mostAccurateLocation.getLongitude();
                }
            }
        };

        executor = Executors.newSingleThreadExecutor();
    }

    public void startLocationUpdates() {
        executor.execute(() -> {
            if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                activity.runOnUiThread(() -> Toast.makeText(activity, "Location denied - setting location to Afeka College", Toast.LENGTH_SHORT).show());
                lat = 32.115514949769846;
                lng = 34.8181039;
                return;
            }
            Looper looper = Looper.getMainLooper(); // or use another appropriate Looper
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, looper);

        });
    }

    public void attachLocationListener() {
        startLocationUpdates();
    }

    public void detachLocationListener() {
        executor.execute(() -> fusedLocationClient.removeLocationUpdates(locationCallback));
    }

    public double getLatitude() {
        return lat;
    }

    public double getLongitude() {
        return lng;
    }
}