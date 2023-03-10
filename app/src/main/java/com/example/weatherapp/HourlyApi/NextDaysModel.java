
package com.example.weatherapp.HourlyApi;



import com.example.weatherapp.HourlyApi.City;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class NextDaysModel implements Serializable {

    @SerializedName("cod")
    @Expose
    private String cod;
    @SerializedName("message")
    @Expose
    private int message;
    @SerializedName("cnt")
    @Expose
    private int cnt;
    @SerializedName("list")
    @Expose
    private java.util.List<com.example.weatherapp.HourlyApi.List> list = null;
    @SerializedName("city")
    @Expose
    private City city;

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public java.util.List<com.example.weatherapp.HourlyApi.List> getList() {
        return list;
    }

    public void setList(java.util.List<com.example.weatherapp.HourlyApi.List> list) {
        this.list = list;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

}
