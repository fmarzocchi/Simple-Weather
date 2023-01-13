package com.example.weatherapp.HourlyApi;



import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HourlyApi {

    @GET("forecast")
    Call<NextDaysModel> getHourlyWeather(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("appid") String apiKey,
            @Query("units") String units
    );

}
