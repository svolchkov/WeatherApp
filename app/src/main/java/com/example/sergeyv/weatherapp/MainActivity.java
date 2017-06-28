package com.example.sergeyv.weatherapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import com.example.sergeyv.weatherapp.model.Settings;

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.support.design.widget.Snackbar.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    TextView tvDateTime, tvTitle, tvCity, tvTemperature;
    ArrayList<TextView> textViews;
    int textColor;
    public static String MY_PREFS_NAME = "com.example.sergeyv.weatherapp.settings"; // name of preferences file


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDateTime = (TextView) findViewById(R.id.tvDateTime);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvCity = (TextView) findViewById(R.id.tvCity);
        tvTemperature =  (TextView) findViewById(R.id.tvTemp);

        textViews = new ArrayList<TextView>(Arrays.asList(tvDateTime,tvTitle,tvCity,tvTemperature));

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        if (prefs != null) {
            for (TextView t: textViews){
                t.setTextColor(prefs.getInt("textColor", Color.WHITE));
                }
            tvCity.setText(prefs.getString("city", "Perth,WA"));
        }else{
            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.constraintLayout), R.string.noSettingsSet, Snackbar.LENGTH_LONG);
        }

        if (savedInstanceState != null) {
            for (TextView t: textViews){
                t.setTextColor(savedInstanceState.getInt("textColor", Color.WHITE));
                }

            }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
    }

    @Override
    protected void onStop(){
        // save current user settings to preferences
        super.onStop();
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        if (Settings.textColour != 0)
            editor.putInt("textColor", Settings.textColour);
        if (Settings.city != null)
            editor.putString("city", Settings.city);
        editor.apply();
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
        outState.putInt("textColor", tvTitle.getCurrentTextColor());
        outState.putString("city", tvCity.getText().toString());
    }
}
