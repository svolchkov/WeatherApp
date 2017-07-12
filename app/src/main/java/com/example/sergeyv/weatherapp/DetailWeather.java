package com.example.sergeyv.weatherapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sergeyv.weatherapp.model.Settings;

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

    private void displayListView() {
        //assign controls
        final ListView listView = (ListView) findViewById(R.id.detailWeather);
        //imgView_mail = (ImageView) findViewById(R.id.imgView_mail);

        //Test data
        //ArrayList<String> inviteNew = new ArrayList<String>();
        //final ArrayList<ArrayList<String>> inviteList = new ArrayList<ArrayList<String>>();

        //emailAdapter = new ListViewAdapter(this, inviteList);
        listView.setAdapter(new IconicAdapter());

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
        IconicAdapter() {
            super(DetailWeather.this, R.layout.list_row, R.id.temp, items);
        }
        @Override
        public View getView(int position, View convertView,
                            ViewGroup parent) {
            View row=super.getView(position, convertView, parent);
            ImageView icon=(ImageView)row.findViewById(R.id.icon);
            //icon.setImageResource(R.drawable.ok);
            TextView date =(TextView)row.findViewById(R.id.date);
            TextView temp =(TextView)row.findViewById(R.id.temp);
            TextView rain =(TextView)row.findViewById(R.id.rain);
            date.setText("Date");
            rain.setText("Rain");
            ArrayList<TextView> textViews = new ArrayList<TextView>
                    (Arrays.asList(rain,date,
                    temp));

            SharedPreferences prefs = getSharedPreferences(MainActivity.MY_PREFS_NAME, MODE_PRIVATE);
            if (prefs != null) {
                Settings.textColour = prefs.getInt("textColor", Color.WHITE);
                Settings.city = prefs.getString("city", null);

            }
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
