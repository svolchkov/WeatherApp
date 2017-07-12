package com.example.sergeyv.weatherapp.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sergeyv on 12/07/2017.
 */

public class OneDayForecast {
    public String date;
    public double minTemp;
    public double maxTemp;
    public double rain;
    public String icon;

    public OneDayForecast(JSONObject json){
        try{
            this.rain = 0.0;
            JSONObject weather = json.getJSONObject("weather");
            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject temp = json.getJSONObject("temp");
            DateFormat df = new SimpleDateFormat("EE, MMM dd");
            //Format f = new SimpleDateFormat("MMM dd");
            //DateFormat fullDate = DateFormat.getDateTimeInstance();
            Date d = new Date(json.getLong("dt")*1000);
            this.date = df.format(d);
            this.maxTemp = temp.getDouble("max");
            this.minTemp = temp.getDouble("min");
            this.icon = details.getString("icon");
            this.rain = weather.getDouble("rain");

        } catch (JSONException e){
            Log.e("MYAPP","JSONException",e);
        }

    }
}
