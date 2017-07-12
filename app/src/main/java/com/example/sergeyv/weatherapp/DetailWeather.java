package com.example.sergeyv.weatherapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

/**
 * Created by sergeyv on 11/07/2017.
 */

public class DetailWeather extends AppCompatActivity {

    private Toolbar toolbar;
    ListView weatherDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_weather);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailWeather.this,MainActivity.class);
                startActivity(i);
            }
        });

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.preferences);
        }
}
