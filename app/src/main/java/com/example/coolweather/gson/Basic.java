package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/8/27.
 */

public class Basic {
    @SerializedName("city")
    public String CityName;

    @SerializedName("id")
    public String WeatherId;

    public Update update;
    public class Update
    {
        @SerializedName("loc")
        public String updateTime;
    }
}
