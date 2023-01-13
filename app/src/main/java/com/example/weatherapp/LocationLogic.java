package com.example.weatherapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class LocationLogic {

    FusedLocationProviderClient fusedLocationProviderClient;
    public static final int REQUEST_CODE = 99;
    double lat = 0.0;
    double lon = 0.0;

    public void getLastLocation(Activity activity, LocationCallback locationCallback) {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){ // vedo se ho i permessi

            Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
            locationTask.addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if( location!= null){
                        locationCallback.onGeoGot(new double[]{location.getLatitude(),
                        location.getLongitude()});

                    }else{
                        new Handler().postDelayed(
                                () -> {
                                    getLastLocation(activity, locationCallback);
                                },2000);
                    }

                }
            });

            locationTask.addOnFailureListener(activity, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(activity, " non sono riuscito a prendere la posizione",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // altrimenti li chiedo
                activity.requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            }
        }
    }

    public interface LocationCallback{
        public void onGeoGot(double[] latloc);
    }


}
