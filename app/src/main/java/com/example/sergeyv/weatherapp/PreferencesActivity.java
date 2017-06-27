package com.example.sergeyv.weatherapp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class PreferencesActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.preferences);

        Spinner spCities = (Spinner) findViewById(R.id.spCities);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cities_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCities.setAdapter(adapter);

        String myString = "Perth,WA"; //the value you want the position for
        ArrayAdapter myAdap = (ArrayAdapter) spCities.getAdapter(); //cast to an ArrayAdapter
        int spinnerPosition = myAdap.getPosition(myString);
        spCities.setSelection(spinnerPosition);
    }
}
