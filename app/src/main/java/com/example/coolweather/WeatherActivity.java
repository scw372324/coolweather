package com.example.coolweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.view.ScrollingView;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coolweather.gson.Forecast;
import com.example.coolweather.gson.Weather;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/8/27.
 */

public class WeatherActivity extends AppCompatActivity {
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        weatherLayout=(ScrollView)findViewById(R.id.weather_layout);
        titleCity=(TextView)findViewById(R.id.title_city);
        titleUpdateTime=(TextView)findViewById(R.id.title_update_time);
        degreeText=(TextView)findViewById(R.id.degree_text);
        weatherInfoText=(TextView)findViewById(R.id.weather_info_text);
        forecastLayout=(LinearLayout)findViewById(R.id.forecast_layout);
        aqiText=(TextView)findViewById(R.id.aqi_text);
        pm25Text=(TextView)findViewById(R.id.pm25_text);
        comfortText=(TextView)findViewById(R.id.comfort_text);
        carWashText=(TextView)findViewById(R.id.car_wash_text);
        sportText=(TextView)findViewById(R.id.sport_text);
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString=prefs.getString("weather",null);
        if(weatherString!=null)
        {
            Weather weather= Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        }else{
            //String weatherId=getIntent().getStringExtra("weather_id");
            //weatherLayout.setVisibility(View.GONE);
            String weatherId="CN101121101";
            requestWeather(weatherId);
        }
    }
    public void requestWeather(String weatherId)
    {
        String weatherUrl="http://guolin.tech/api/weather?cityid="+weatherId+"&key=d5653ab8c431481baa7f79c4bfb6842a";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                final Weather weather=Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather!=null && "ok".equals(weather.status))
                        {
                            SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        }
                    }
                });

            }
        });
    }
    private void showWeatherInfo(Weather weather)
    {
        titleCity.setText(weather.basic.CityName);
        titleUpdateTime.setText(weather.basic.update.updateTime);
        degreeText.setText(weather.now.temperature);
        weatherInfoText.setText(weather.now.more.info);
        aqiText.setText(weather.aqi.city.aqi);
        pm25Text.setText(weather.aqi.city.pm25);
        comfortText.setText("舒适度："+weather.suggestion.comfort.info);
        carWashText.setText("洗车指数："+weather.suggestion.carWash.info);
        sportText.setText("运动建议："+weather.suggestion.sport.info);
        forecastLayout.removeAllViews();
        for(Forecast forecast:weather.forecastList)
        {
            View view= LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dateText=(TextView)view.findViewById(R.id.date_text);
            TextView infoText=(TextView)view.findViewById(R.id.info_text);
            TextView maxText=(TextView)view.findViewById(R.id.max_text);
            TextView minText=(TextView)view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        weatherLayout.setVisibility(View.VISIBLE);
    }
}
