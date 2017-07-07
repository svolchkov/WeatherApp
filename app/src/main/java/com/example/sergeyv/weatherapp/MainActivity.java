package com.example.sergeyv.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.audiofx.BassBoost;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sergeyv.weatherapp.model.Forecast;
import com.example.sergeyv.weatherapp.model.Settings;

import org.json.JSONObject;

import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.support.design.widget.Snackbar.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    TextView tvDateTime, tvTitle, tvCity, tvTemperature;
    ArrayList<TextView> textViews;
    int textColor;
    public static String MY_PREFS_NAME = "com.example.sergeyv.weatherapp.settings_"; // name of preferences file

    GridView weatherGrid;

    //TEMP
    TextView cityField;
    TextView updatedField;
    TextView detailsField;
    TextView currentTemperatureField;
    ImageView weatherIcon;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDateTime = (TextView) findViewById(R.id.tvDateTime);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvCity = (TextView) findViewById(R.id.tvCity);
        tvTemperature =  (TextView) findViewById(R.id.tvTemp);
        weatherGrid = (GridView) findViewById(R.id.weather_grid);
//        cityField = (TextView)findViewById(R.id.city_field);
//        updatedField = (TextView)findViewById(R.id.updated_field);
//        detailsField = (TextView)findViewById(R.id.details_field);
//        currentTemperatureField = (TextView)findViewById(R.id.current_temperature_field);
//        weatherIcon = (ImageView)findViewById(R.id.weather_icon);

        textViews = new ArrayList<TextView>(Arrays.asList(tvDateTime,tvTitle,
                tvCity,tvTemperature));

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        if (prefs != null) {
            Settings.textColour = prefs.getInt("textColor", 0);
            Settings.city = prefs.getString("city", null);

        }
        if (Settings.textColour != 0){
            for (TextView t: textViews){
                t.setTextColor(Settings.textColour);
            }
        }

        if (Settings.city != null){
            tvCity.setText(Settings.city);
        }
        else{
            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.constraintLayout), R.string.noSettingsSet, Snackbar.LENGTH_LONG);
            mySnackbar.show();
        }

        if (savedInstanceState != null) {
            for (TextView t: textViews){
                t.setTextColor(savedInstanceState.getInt("textColor", Color.WHITE));
                }

            }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();



        handler = new Handler();

        // Gridview was getting cut off, had to add this
        //if(Build.VERSION.SDK_INT >= 21)
        //    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

    }

    private void updateWeatherData(String city){
        final String cityName = city.split(",")[0] + ",au";
        final Context context = this;
        new Thread(){
            public void run(){
                final JSONObject json = FetchWeather.getJSON(context, cityName);
                if(json == null){
                    handler.post(new Runnable(){
                        public void run(){
                            String s = getString(R.string.unknownCity);
                            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.constraintLayout), s + ": " + cityName, Snackbar.LENGTH_LONG);
                            mySnackbar.show();
                        }
                    });
                } else {
                    handler.post(new Runnable(){
                        public void run(){
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject json){
        try {
            //cityField.setText(json.getString("name").toUpperCase(Locale.UK) +
            //       ", " +
            //       json.getJSONObject("sys").getString("country"));

            //JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            //JSONObject main = json.getJSONObject("main");
            Forecast f = new Forecast(json);
//            detailsField.setText(
//                    details.getString("description").toUpperCase(Locale.US) +
//                            "\n" + "Humidity: " + main.getString("humidity") + "%" +
//                            "\n" + "Pressure: " + main.getString("pressure") + " hPa");
//
//            currentTemperatureField.setText(
//                    String.format("%.2f", main.getDouble("temp"))+ " ℃");
//
            DateFormat df = DateFormat.getDateTimeInstance();
            //String updatedOn = df.format(new Date(json.getLong("dt")*1000));
            String updatedOn = df.format(new Date());
            //tvTemperature.setText();
            tvDateTime.setText("Last update: " + updatedOn);
            String temp = getString(R.string.weatherInfo);

            //var iconUrl = "http://openweathermap.org/img/w/" + iconCode + ".png";

            String[] times = f.times.toArray(new String[0]);
            String[] dates = f.dates.toArray(new String[0]);
            String[] weathers = f.icons.toArray(new String[0]);
            String[] temps = f.temps.toArray(new String[0]);

            tvTemperature.setText(String.format(temp, temps[0],f.maxTemp,f.minTemp ) );
            //URL url = new URL("http://openweathermap.org/img/w/" + details.getString("icon") + ".png");
            weatherGrid.setAdapter(new WeatherAdapter(this,times, weathers));

            //Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            //weatherIcon.setImageBitmap(bmp);



        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data",e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Settings.textColour != 0){
            for (TextView t: textViews){
                t.setTextColor(Settings.textColour);
            }
        }
        if (Settings.city != null){
            tvCity.setText(Settings.city);
        }

        if (Settings.city != null){
            updateWeatherData(Settings.city);
        }else{
            updateWeatherData("Perth,WA");
        }
    }

    @Override
    protected void onStop(){
        // save current user settings to preferences
        super.onStop();
//        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
//        if (Settings.textColour != 0)
//            editor.putInt("textColor", Settings.textColour);
//        if (Settings.city != null)
//            editor.putString("city", Settings.city);
//        editor.apply();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if(item.getItemId()==R.id.preferences){
            Intent i = new Intent(this,PreferencesActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("textColor", Settings.textColour);
        outState.putString("city", Settings.city);
    }
}
