package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.weatherapp.HourlyApi.HourlyApi;
import com.example.weatherapp.HourlyApi.Main;
import com.example.weatherapp.HourlyApi.NextDaysModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    private double lat;
    private double lon;
    private TextView txtTempBig;
    private TextView city, tempMin, tempMax, pressure, water, wind,sunrise,sunset;
    private RecyclerView todayRecycler;
    private View fake_cardview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtTempBig = findViewById(R.id.textview_temperatureBig);
        city = findViewById(R.id.city_textview);
        tempMax = findViewById(R.id.textView_temp_max);
        tempMin = findViewById(R.id.textView_temp_min);
        pressure = findViewById(R.id.textView_pressure);
        water = findViewById(R.id.textView_water);
        wind = findViewById(R.id.textView_wind);
        sunrise = findViewById(R.id.textView_sunrise);
        sunset = findViewById(R.id.textView_sunset);
        todayRecycler = findViewById(R.id.todayRecycler);
        fake_cardview = findViewById(R.id.cardview_info);


        // devo prendere il weather
        NextDaysModel weather = (NextDaysModel) getIntent().getSerializableExtra("MODEL");

        Main main = weather.getList().get(0).getMain();
        city.setText(weather.getCity().getName());
        txtTempBig.setText(Math.round(main.getTemp()) + "°");
        pressure.setText(String.valueOf(main.getPressure())+ "hpa");
        water.setText(String.valueOf(main.getHumidity())+ "%");
        wind.setText(String.valueOf(weather.getList().get(0).getWind().getSpeed())+"km/h");
        tempMin.setText(Math.round(main.getTempMin())+"°");
        tempMax.setText(Math.round(main.getTempMax())+"°");
        sunrise.setText(Tools.unixConverter(weather.getCity().getSunrise()));
        sunset.setText(Tools.unixConverter(weather.getCity().getSunset()));
        fake_cardview.setBackground(
                getDrawable(Tools.getWeatherIcon(
                        weather.getList().get(0).getWeather().get(0).getIcon(),true
                ))
        );
        // solo dopo aver ricevuto i dati setto il recycler
        // uso il LinearLayout orizzontale
        todayRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this,
                LinearLayoutManager.HORIZONTAL,false));
        todayRecycler.setAdapter(new TodayRecAdapter(weather,MainActivity.this));

    }


}