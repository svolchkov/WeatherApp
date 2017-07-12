package com.example.sergeyv.weatherapp.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergeyv on 12/07/2017.
 */

public class DailyForecast {
    private List<OneDayForecast> threeHourForecasts;
    public List<String> rains = new ArrayList<String>();
    public List<String> dates = new ArrayList<String>();
    public List<String> maxTemps = new ArrayList<String>();
    public List<String> minTemps = new ArrayList<String>();
    public List<String> icons = new ArrayList<String>();


    public DailyForecast(JSONObject json) {
        threeHourForecasts = new ArrayList<OneDayForecast>();
        try {
            JSONArray forecasts = json.getJSONArray("list");
            for (int i = 0; i < forecasts.length(); i++) {
                JSONObject forecast = forecasts.getJSONObject(i);
                OneDayForecast f = new OneDayForecast(forecast);
                threeHourForecasts.add(f);
                rains.add(String.format("%.1f", f.rain));
                dates.add(f.date);
                // get max and min temp for the next 24 hours at 3 hour intervals
                maxTemps.add(String.format("%.1f", f.maxTemp));
                minTemps.add(String.format("%.1f", f.minTemp));
                icons.add(f.icon);
            }
        } catch (JSONException e) {
            Log.e("MYAPP", "JSONException", e);
        }
    }
}
