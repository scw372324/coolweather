package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/8/27.
 */

public class AQI {
    public AQICity city;
    public class AQICity
    {
        public String aqi;
        public String pm25;
    }
}
