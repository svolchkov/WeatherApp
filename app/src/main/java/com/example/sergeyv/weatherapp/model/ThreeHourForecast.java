package com.example.sergeyv.weatherapp.model;

import android.util.Log;

import com.example.sergeyv.weatherapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sergeyv on 6/07/2017.
 */

public class ThreeHourForecast {
    public String time;
    public String date;
    public double temp;
    public String icon;

    public ThreeHourForecast(JSONObject json){
        try{
            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");
            DateFormat df = new SimpleDateFormat("EE hh:mm a");
            //Format f = new SimpleDateFormat("MMM dd");
            DateFormat fullDate = DateFormat.getDateTimeInstance();
            Date d = new Date(json.getLong("dt")*1000);
            this.time = df.format(d);
            this.date = fullDate.format(d);
            this.temp = main.getDouble("temp");
            this.icon = details.getString("icon");
        } catch (JSONException e){
            Log.e("MYAPP","JSONException",e);
        }

    }
}
