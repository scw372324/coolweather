package com.example.coolweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Administrator on 2017/8/22.
 */

public class HttpUtil {
    public  static  void sendOkHttpRequest(String address,okhttp3.Callback calback)
    {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(calback);
    }
}
