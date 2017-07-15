package com.example.sergeyv.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sergeyv.weatherapp.model.DailyForecast;
import com.example.sergeyv.weatherapp.model.Settings;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by sergeyv on 11/07/2017.
 */

public class DetailWeather extends AppCompatActivity {

    private Toolbar toolbar;
    Handler handler;
    ListView weatherDetail;
    private static final String[] items={"lorem", "ipsum", "dolor",
            "sit", "amet",
            "consectetuer", "adipiscing", "elit", "morbi", "vel",
            "ligula", "vitae", "arcu", "aliquet", "mollis",
            "etiam", "vel", "erat", "placerat", "ante",
            "porttitor", "sodales", "pellentesque", "augue", "purus"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_weather);

        //displayListView();

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
        actionBar.setTitle(String.format(getString(R.string.detailTitle),Settings.city));

        handler = new Handler();
        }

    private void updateWeatherData(String city){
        final String cityName = city.split(",")[0] + ",au";
        final Context context = this;
        new Thread(){
            public void run(){
                final JSONObject currentWeather = FetchWeather.getJSON(context, cityName, "daily");

                if(currentWeather == null){
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
                            DailyForecast fourteenDayWeather = new DailyForecast(currentWeather);
                            displayListView(fourteenDayWeather);
                        }
                    });
                }
            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Settings.city != null){
            updateWeatherData(Settings.city);
        }else{
            updateWeatherData(getString(R.string.defaultCity));
        }
    }

    private void displayListView(DailyForecast weather) {
        //assign controls
        final ListView listView = (ListView) findViewById(R.id.detailWeather);
        //imgView_mail = (ImageView) findViewById(R.id.imgView_mail);

        //Test data
        //ArrayList<String> inviteNew = new ArrayList<String>();
        //final ArrayList<ArrayList<String>> inviteList = new ArrayList<ArrayList<String>>();

        //emailAdapter = new ListViewAdapter(this, inviteList);
        listView.setAdapter(new IconicAdapter(this,weather));

        // Assign adapter to ListView
        //listView.setTextFilterEnabled(true);

//        imgView_mail.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                //variables
//                final String enteredMail = "testListViewEntry";
//                final ArrayList<ArrayList<String>> inviteList = new ArrayList<ArrayList<String>>();
//                ArrayList<String> invite = new ArrayList<String>();
//                invite.add(0, enteredMail);//add first email
//                invite.add(1, "icon_invitestatussent.png"); //add first status icon
//                inviteList.add(invite);
//                emailAdapter.notifyDataSetChanged();
//                listView.setAdapter(emailAdapter);
//            }
//        });
//        }
    }

    class IconicAdapter extends ArrayAdapter<String> {

        DailyForecast forecast;
        String[] icons;
        String[] rains;
        String[] dates;
        String[] maxTemps;
        String[] minTemps;
        private Context context;

        IconicAdapter(Context context, DailyForecast currentWeather) {
            super(DetailWeather.this, R.layout.list_row, R.id.date, currentWeather.dates);
            this.forecast = currentWeather;
            this.context = context;
            this.icons = forecast.icons.toArray(new String[0]);
            this.rains = forecast.rains.toArray(new String[0]);
            this.maxTemps = forecast.maxTemps.toArray(new String[0]);
            this.minTemps = forecast.minTemps.toArray(new String[0]);
            this.dates = forecast.dates.toArray(new String[0]);
        }
        @Override
        public View getView(int position, View convertView,
                            ViewGroup parent) {
            View row=super.getView(position, convertView, parent);
            ImageView icon=(ImageView)row.findViewById(R.id.icon);
            new DownloadImageTask(icon, this.context)
                    .execute(this.icons[position]);
            TextView date =(TextView)row.findViewById(R.id.date);
            TextView temp =(TextView)row.findViewById(R.id.temp);
            TextView rain =(TextView)row.findViewById(R.id.rain);
            String r = getString(R.string.rain);
            rain.setText(String.format(r,this.rains[position]));
            String tmp = getString(R.string.maxMinTemp);
            temp.setText(String.format(tmp,this.maxTemps[position],this.minTemps[position]));
            ArrayList<TextView> textViews = new ArrayList<TextView>
                    (Arrays.asList(rain,date,
                    temp));


            if (Settings.textColour != 0){
                for (TextView t: textViews){
                    t.setTextColor(Settings.textColour);
                }
            }
            //date.setText(String.format(getString(R.string.size_template), items[position]));
            return(row);
        }
    }
}
