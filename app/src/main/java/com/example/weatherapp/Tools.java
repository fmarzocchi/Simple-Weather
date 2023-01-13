package com.example.weatherapp;

import android.graphics.drawable.Drawable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Tools {

    public static String unixConverter(int unix) {
        Date date = new Date(unix * 1000L);
        SimpleDateFormat jdf = new SimpleDateFormat("h:mm a");
        jdf.setTimeZone(TimeZone.getTimeZone("GMT+1"));

        return jdf.format(date);
    }

    public static Integer getWeatherIcon(String code,boolean isForCardview) {

        switch (code) {
            case "01n":
                if (isForCardview){
                    return R.drawable.clearnightskymd;
                }
                return R.drawable.newmoon;
            case "01d":
                if (isForCardview){
                    return R.drawable.sunraymodified;
                }
                return R.drawable.sun;
            case "02n":
                if (isForCardview){
                    return R.drawable.clearnightskymd;
                }
                return R.drawable.newmooncloud;
            case "02d":
                if (isForCardview){
                    return R.drawable.sunfcloudhd;
                }
                return R.drawable.sunbehindsmallcloud;
            case "03n":
            case "03d":
                if (isForCardview){
                    return R.drawable.cloudcardviewmodified;
                }
                return R.drawable.cloud;
            case "04n":
            case "04d":
                if (isForCardview){
                    return R.drawable.clouddestcardviewmodified;
                }
                return R.drawable.clouddest;
            case "09n":
            case "09d":
                if (isForCardview){
                    return R.drawable.rain2cardviewmodified;
                }
                return R.drawable.cloudwithrain;
            case "10n":
                if (isForCardview){
                    return R.drawable.rain2cardviewmodified;
                }
                return R.drawable.newmoonrain;
            case "10d":
                if (isForCardview){
                    return R.drawable.rain2cardviewmodified;
                }
                return R.drawable.sunbehindraincloud;
            case "11n":
            case "11d":
                if (isForCardview){
                    return R.drawable.thunderstormmodified;
                }
                return R.drawable.cloudwithlightningandrain;
            case "13n":
            case "13d":
                return R.drawable.snowflake;
            case "50n":
            case "50d":
                return R.drawable.fog;
        }

        return R.drawable.cloudwithlightningandrain;

    }


}
