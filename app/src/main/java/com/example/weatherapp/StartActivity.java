package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.weatherapp.HourlyApi.NextDaysModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class StartActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 99;
    private static final int WIFI = 1;
    private static final int GPS = 2;
    private static final int PERMISSIONS = 3;
    LocationManager locationManager;
    Button reloadButton;
    ImageView loadingGif;
    TextView errorMessage;
    String[] permissions;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        permissions = new String[] {Manifest.permission.ACCESS_FINE_LOCATION};

        reloadButton = findViewById(R.id.buttonReload);
        loadingGif = findViewById(R.id.loadingImageview);
        errorMessage = findViewById(R.id.errorMessageTextview);
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                Glide.with(this).load(R.drawable.dotsloadingblack).into(loadingGif);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                Glide.with(this).load(R.drawable.idotsloading).into(loadingGif);
                break;
        }

        if (!isInternetAvailable()){
            errorMessage.setText("Internet not available. \nPlease, try to turn on WIFI and click Reload");
            errorMessage.setVisibility(View.VISIBLE);
            loadingGif.setVisibility(View.INVISIBLE);
            showButtonReload(WIFI);
        }else {
            internetAvaible();
        }

    }

    private void internetAvaible() {

        if (isGpsAvailable()) {
            if (checkGpsPermissions()) {
                loadingGif.setVisibility(View.VISIBLE);
                new LocationLogic().getLastLocation(StartActivity.this, new LocationLogic.LocationCallback() {
                    @Override
                    public void onGeoGot(double[] latlon) {
                        saveCoordinates(latlon);
                        startNetworkThread(latlon);
                    }
                });
            }else{
                askPermissionToUser();
                }
        } else {
            if (getSharedPreferences("Location", MODE_PRIVATE).contains("lat")){
                SharedPreferences sh = getSharedPreferences("Location", MODE_PRIVATE);
                if (sh.getFloat("lat", 0) != 0 && sh.getFloat("lon", 0) != 0) {
                    double[] oldLatLon = new double[]{
                            sh.getFloat("lat", 0),
                            sh.getFloat("lon", 0)
                    };
                    loadingGif.setVisibility(View.VISIBLE);
                    startNetworkThread(oldLatLon);
                }
            } else {
                errorMessage.setText("I have no position to load. \nPlease, try to turn on GPS and click Reload");
                errorMessage.setVisibility(View.VISIBLE);
                loadingGif.setVisibility(View.INVISIBLE);
                showButtonReload(GPS);
            }
        }
    }

    private void saveCoordinates(double[] latlon) {

        BigDecimal lat = new BigDecimal(latlon[0]);
        BigDecimal lon = new BigDecimal(latlon[1]);
        SharedPreferences lastLocationUsed = getSharedPreferences("Location",MODE_PRIVATE);
        lastLocationUsed.edit()
                .putFloat("lat",lat.floatValue())
                .putFloat("lon",lon.floatValue())
                .apply();

    }

    private void showButtonReload(int purpouse) {
        reloadButton.setVisibility(View.VISIBLE);
        switch (purpouse){
            case WIFI :
                reloadButton.setText("Reload");
                reloadButton.setOnClickListener(view -> {
                    if(isInternetAvailable()){
                        errorMessage.setVisibility(View.INVISIBLE);
                        errorMessage.setText("");
                        reloadButton.setVisibility(View.INVISIBLE);
                        reloadButton.setText("");
                        internetAvaible();
                    }
                });
                break;
            case GPS :
                reloadButton.setText("Reload");
                reloadButton.setOnClickListener(view -> {
                    if (isGpsAvailable()){
                        errorMessage.setVisibility(View.INVISIBLE);
                        errorMessage.setText("");
                        loadingGif.setVisibility(View.VISIBLE);
                        reloadButton.setVisibility(View.INVISIBLE);
                        reloadButton.setText("");
                        new Handler().postDelayed(
                                () -> {
                                    internetAvaible();
                                },2000);
                    }
                });
                break;
            case PERMISSIONS :
                reloadButton.setText("Go to Settings");
                reloadButton.setOnClickListener(view -> {
                    errorMessage.setVisibility(View.INVISIBLE);
                    errorMessage.setText("");
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", getPackageName(), null));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    flag = true;
                });
                break;
        }
    }

    private boolean checkGpsPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            return true;
        }else{
            return false;
        }
    }

    private void askPermissionToUser(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // altrimenti li chiedo
            if (!shouldShowRequestPermissionRationale(permissions[0])){
                new AlertDialog.Builder(this)
                       .setMessage("This app requires Location permission to work")
                       .setPositiveButton("Ok", (dialogInterface, i) -> {
                           dialogInterface.dismiss();
                           ActivityCompat.requestPermissions(this,permissions, REQUEST_CODE);
                       }).show();
            }else{
                errorMessage.setVisibility(View.VISIBLE);
                errorMessage.setText("This app requires Location permissions.\nPlease, click the button and then go to 'Permissions'");
                loadingGif.setVisibility(View.INVISIBLE);
                showButtonReload(PERMISSIONS);
            }
        }
    }

    private void startNetworkThread(double[] latlon) {
        NetworkLogic network = NetworkLogic.getInstance();
        if (network!=null){
            network.startRetrofit(latlon[0], latlon[1], new NetworkLogic.MyCallback() {
                @Override
                public void onDataGot(NextDaysModel model) {
                    if (model != null) {
                        Intent intent = new Intent(StartActivity.this, MainActivity.class);
                        intent.putExtra("MODEL", model);
                        new Handler().postDelayed(
                                () -> {
                                    startActivity(intent);
                                    finish();
                                },1000);
                    } else {
                        Log.d("DEBUG", "nextDaysModel is null");
                    }
                }
            });

        }else{
            Log.d("DEBUG", "network is null");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_CODE:
                if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    errorMessage.setVisibility(View.INVISIBLE);
                    errorMessage.setText("");
                    reloadButton.setVisibility(View.INVISIBLE);
                    loadingGif.setVisibility(View.VISIBLE);
                    new LocationLogic().getLastLocation(StartActivity.this, new LocationLogic.LocationCallback() {
                        @Override
                        public void onGeoGot(double[] latlon) {
                            startNetworkThread(latlon);
                        }
                    });
                }else{
                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage.setText("This app requires Location permissions.\nPlease, click the button and then go to 'Permissions'");
                    loadingGif.setVisibility(View.INVISIBLE);
                    showButtonReload(PERMISSIONS);
                }
                break;
        }
    }

    //-------metodi per controllare se il GPS o il WIFI sono accesi -------------------------------

    public boolean isInternetAvailable() {
        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {

            Log.e("DEBUG",e.toString());
            return false;
        }
    }

    public boolean isGpsAvailable(){
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (flag){
            ActivityCompat.requestPermissions(this,permissions, REQUEST_CODE);
            reloadButton.setVisibility(View.INVISIBLE);
            reloadButton.setText("");
            flag=false;
        }
    }
}