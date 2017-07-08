package com.example.sergeyv.weatherapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sergeyv.weatherapp.model.Settings;

import org.w3c.dom.Text;

import java.util.HashMap;

/**
 * Created by sergeyv on 6/07/2017.
 */

    public class WeatherAdapter extends BaseAdapter {
        private Context context;
        private final String[] mTimesData;
        private final String[] mWeatherData;

        public WeatherAdapter(Context context, String[] timesData,
                              String[] weatherData) {
            this.context = context;
            this.mWeatherData = weatherData;
            this.mTimesData = timesData;
        }

        public View getView(int key, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View gridView;

            if (convertView == null) {

                gridView = new View(context);

                // get layout from mobile.xml
                gridView = inflater.inflate(R.layout.weather_view, null);

            } else {
                gridView = (View) convertView;
            }

            TextView time = (TextView) gridView.findViewById(R.id.time_field);
            time.setText(mTimesData[key]);
            if (Settings.textColour != 0){
                time.setTextColor(Settings.textColour);
            }else{
                time.setTextColor(Color.WHITE);
            }

            // set image based on selected text
            ImageView weatherIcon = (ImageView) gridView
                    .findViewById(R.id.weather_icon);


            //imageView.setImageResource(mThumbIds[position]);
            new DownloadImageTask(weatherIcon, context)
                    .execute(mWeatherData[key]);

            return gridView;
        }

        @Override
        public int getCount() {
            return this.mWeatherData.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


}
