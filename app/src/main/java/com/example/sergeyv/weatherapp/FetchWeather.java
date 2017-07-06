package com.example.sergeyv.weatherapp;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sergeyv on 1/07/2017.
 */

public class FetchWeather {
 //   private static final String OPEN_WEATHER_MAP_API =
//            "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&APPID=%s";
 //"http://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric&APPID=%s";
 //   getString(R.string.forecastURL);
    public static JSONObject getJSON(Context context, String city) {
        try {
            final String OPEN_WEATHER_MAP_API = context.getString(R.string.forecastURL);
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, city,context.getString(R.string.openWeatherApiKey)));
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

//            connection.addRequestProperty("x-api-key",
//                    context.getString(R.string.openWeatherApiKey));
            connection.getResponseCode();
            InputStream stream = connection.getErrorStream();
            if (stream != null) {
                return null;
            }
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp = "";
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if (data.getInt("cod") != 200) {
                return null;
            }

            return data;
        } catch (Exception e) {
            Log.e("MYAPP", "exception", e);
            return null;
        }
    }
}
