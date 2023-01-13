package com.example.weatherapp;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.weatherapp.HourlyApi.HourlyApi;
import com.example.weatherapp.HourlyApi.Main;
import com.example.weatherapp.HourlyApi.NextDaysModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkLogic {

    private static NetworkLogic instance;
    private final String API_KEY = "1900640a513cd3fbfced420f9f498ae3";
    NextDaysModel nextDaysModel;

    private NetworkLogic(){}

    public static NetworkLogic getInstance(){
        if (instance==null){
            instance = new NetworkLogic();
        }
        return instance;
    }

    public void startRetrofit(double lat, double lon,MyCallback myCallback) {

        //TODO vorrei separare l'ottenimento di Retrofit dalla main activity, forse posso usare
        //TODO intent per passare i dati alla MainActivity, tanto una volta finito voglio andare li


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HourlyApi hourlyApi = retrofit.create(HourlyApi.class);
        Call<NextDaysModel> call = hourlyApi.getHourlyWeather(lat, lon,API_KEY,"metric");
        call.enqueue(new Callback<NextDaysModel>() {
            @Override
            public void onResponse(Call<NextDaysModel> call, Response<NextDaysModel> response) {
                if (response.isSuccessful()){
                     NextDaysModel weather = response.body();
                    if (weather !=null){
                        myCallback.onDataGot(weather);
                        Log.d("DEBUG", "all ok with onResponse");
                    }else{
                        Log.d("DEBUG", "weather (dentro onResponse) is null");
                    }
                }else {
                    Log.d("DEBUG", response.message());
                }
            }

            @Override
            public void onFailure(Call<NextDaysModel> call, Throwable t) {
                Log.d("FAILURE", t.getMessage());
            }
        });
    }
    public interface MyCallback {
        void onDataGot(NextDaysModel model);
    }

}
