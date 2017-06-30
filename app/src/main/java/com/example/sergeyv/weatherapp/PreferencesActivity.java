package com.example.sergeyv.weatherapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.sergeyv.weatherapp.model.Settings;

public class PreferencesActivity extends AppCompatActivity {

    private Toolbar toolbar;
    Spinner spCities;
    Spinner spTextColors;
    public static String MY_PREFS_NAME = "com.example.sergeyv.weatherapp.settings_"; // name of preferences file
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.preferences);

        spCities = (Spinner) findViewById(R.id.spCities);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cities_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCities.setAdapter(adapter);

        ArrayAdapter myAdap = (ArrayAdapter) spCities.getAdapter(); //cast to an ArrayAdapter
        int spinnerPosition = myAdap.getPosition(Settings.city);
        spCities.setSelection(spinnerPosition);

        spTextColors = (Spinner) findViewById(R.id.spTextColor);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.colors_array, R.layout.spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTextColors.setAdapter(adapter2);

        String selectedColor = "White";
        switch (Settings.textColour){
            case Color.WHITE:
                selectedColor = "White";
                break;
            case Color.BLUE:
                selectedColor = "Blue";
                break;
            case Color.RED:
                selectedColor = "Red";
                break;
            default:
                break;
        }
         //the value you want the position for
        int spinnerPosition2 = adapter2.getPosition(selectedColor);
        spTextColors.setSelection(spinnerPosition2);


        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        spCities.setOnItemSelectedListener(new SpinnerActivity());
        spTextColors.setOnItemSelectedListener(new SpinnerActivity());
    }

    public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {


        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            SharedPreferences.Editor editor = prefs.edit();
            String selectedColor = spTextColors.getSelectedItem().toString();
            switch (selectedColor){
                case "White":
                    Settings.textColour = Color.WHITE;
                    break;
                case "Blue":
                    Settings.textColour = Color.BLUE;
                    break;
                case "Red":
                    Settings.textColour = Color.RED;
                    break;
                default:
                    break;
            }

            Settings.city = spCities.getSelectedItem().toString();

            editor.putInt("textColor", Settings.textColour);
            editor.putString("city", Settings.city);
            editor.apply();


        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    }
}
