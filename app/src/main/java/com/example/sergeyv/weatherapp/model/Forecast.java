package com.example.sergeyv.weatherapp.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sergeyv on 6/07/2017.
 */

public class Forecast {

    private List<ThreeHourForecast> threeHourForecasts;
    private HashMap<String,String[]> forecastData = new HashMap<String,String[]>();
    public List<String> times = new ArrayList<String>();
    public List<String> dates = new ArrayList<String>();
    public List<String> temps = new ArrayList<String>();
    public List<String> icons = new ArrayList<String>();
    public double maxTemp = -999;
    public double minTemp = 999;


    public Forecast(JSONObject json){
        threeHourForecasts = new ArrayList<ThreeHourForecast>();
        try{
            JSONArray forecasts = json.getJSONArray("list");
            for (int i = 0; i < forecasts.length(); i++){
                JSONObject forecast = forecasts.getJSONObject(i);
                ThreeHourForecast f = new ThreeHourForecast(forecast);
                threeHourForecasts.add(f);
                times.add(f.time);
                dates.add(f.date);
                // get max and min temp for the next 24 hours at 3 hour intervals
                if (f.temp > maxTemp && i <= 7)
                    maxTemp = f.temp;
                if (f.temp < minTemp && i <= 7)
                    minTemp = f.temp;
                temps.add(String.format("%.1f",f.temp));
                icons.add(f.icon);
            }
        } catch (JSONException e){
            Log.e("MYAPP","JSONException",e);
        }
    }

//    public HashMap <String,String[]> getForecastData(){
//
//    }
}
