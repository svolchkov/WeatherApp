package com.example.sergeyv.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sergeyv.weatherapp.model.Settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PreferencesActivity extends AppCompatActivity {

    private Toolbar toolbar;
    Spinner spCities;
    //Spinner spTextColors;
    private AutoCompleteTextView countryEdit;
    private ArrayAdapter<String> adapter = null;
    private ArrayAdapter<String> countryAdapter = null;

    public static String MY_PREFS_NAME = "com.example.sergeyv.weatherapp.settings_"; // name of preferences file
    SharedPreferences prefs;

    HashMap<String,String> countryNamesToCodes = new HashMap<String,String>();
    HashMap<String,String> countryCodesToNames = new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PreferencesActivity.this,MainActivity.class);
                startActivity(i);
            }
        });

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

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

        String[] stringArray = getResources().getStringArray(R.array.countryCodes_array);
        ArrayList<String> countryNames = new ArrayList<String>();
        for(String cc : stringArray){
            if (cc.split("\\|").length > 1){
                String[] data = cc.split("\\|");
                String countryCode = data[0];
                String country = data[1];
                countryNames.add(country);
                countryNamesToCodes.put(country, countryCode);
                countryCodesToNames.put(countryCode,country);
            }

        }

        countryAdapter
                = new AutoSuggestAdapter(this, R.layout.country_flag, countryNames);

        countryEdit = (AutoCompleteTextView) findViewById(R.id.search_box);

        //countryEdit.setAdapter(countryAdapter);

        countryEdit.addTextChangedListener(filterTextWatcher);

        //countryEdit.setTokenizer(new SpaceTokenizer());

        countryEdit.setAdapter(countryAdapter);

        countryEdit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String t = ((TextView) v).getText().toString();
                String f = countryEdit.getText().toString();

                int s = countryEdit.getSelectionStart();
                int i = s;

                while (i > 0 && f.charAt(i - 1) != ' ') {
                    i--;
                }

                countryAdapter.getFilter().filter(t.substring(i, s));
            }
        });

//        spTextColors = (Spinner) findViewById(R.id.spTextColor);
//        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
//                R.array.colors_array, R.layout.spinner_item);
//        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spTextColors.setAdapter(adapter2);

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
//        int spinnerPosition2 = adapter2.getPosition(selectedColor);
//        spTextColors.setSelection(spinnerPosition2);


        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

//        spCities.setOnItemSelectedListener(new SpinnerActivity());
//        spTextColors.setOnItemSelectedListener(new SpinnerActivity());
        countryEdit.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT
                        || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
                        || keyCode == KeyEvent.KEYCODE_DPAD_UP
                        || keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {

                    String t = ((TextView) v).getText().toString();
                    String f = countryEdit.getText().toString();

                    int s = countryEdit.getSelectionStart();
                    int i = s;

                    while (i > 0 && f.charAt(i - 1) != ' ') {
                        i--;
                    }

                    countryAdapter.getFilter().filter(t.substring(i, s));

                    return false;
                }

                return false;
            }
        });

//        getListView().setOnItemClickListener(
//                new ListView.OnItemClickListener() {
//                    public void onItemClick(AdapterView<?> parent, View view,
//                                            int position, long id) {
//
//                        String t = countryAdapter.getItem(position);
//                        String f = countryEdit.getText().toString();
//
//                        int s = countryEdit.getSelectionStart();
//                        int i = s;
//
//                        while (i > 0 && f.charAt(i - 1) != ' ') {
//                            i--;
//                        }
//
//                        countryEdit.getText().insert(s, t.substring(s - i));
//                    }
//                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countryEdit.removeTextChangedListener(filterTextWatcher);
    }


    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            //okButton.setEnabled(countryEdit.getText().toString().trim()
            //        .length() > 0);
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            countryAdapter.getFilter().filter(s);
        }

    };



    public class SpaceTokenizer implements MultiAutoCompleteTextView.Tokenizer {

        public int findTokenStart(CharSequence text, int cursor) {
            int i = cursor;

            while (i > 0 && text.charAt(i - 1) != ' ') {
                i--;
            }
            while (i < cursor && text.charAt(i) == ' ') {
                i++;
            }

            return i;
        }

        public int findTokenEnd(CharSequence text, int cursor) {
            int i = cursor;
            int len = text.length();

            while (i < len) {
                if (text.charAt(i) == ' ') {
                    return i;
                } else {
                    i++;
                }
            }

            return len;
        }

        public CharSequence terminateToken(CharSequence text) {
            int i = text.length();

            while (i > 0 && text.charAt(i - 1) == ' ') {
                i--;
            }

            if (i > 0 && text.charAt(i - 1) == ' ') {
                return text;
            } else {
                if (text instanceof Spanned) {
                    SpannableString sp = new SpannableString(text + " ");
                    TextUtils.copySpansFrom((Spanned) text, 0, text.length(),
                            Object.class, sp, 0);
                    return sp;
                } else {
                    return text + " ";
                }
            }
        }
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // TODO Auto-generated method stub
//        switch (item.getItemId()) {
//            case android.R.id.:
//
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {


        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            SharedPreferences.Editor editor = prefs.edit();
//            String selectedColor = spTextColors.getSelectedItem().toString();
//            switch (selectedColor){
//                case "White":
//                    Settings.textColour = Color.WHITE;
//                    break;
//                case "Blue":
//                    Settings.textColour = Color.BLUE;
//                    break;
//                case "Red":
//                    Settings.textColour = Color.RED;
//                    break;
//                default:
//                    break;
//            }

            Settings.city = spCities.getSelectedItem().toString();

            editor.putInt("textColor", Settings.textColour);
            editor.putString("city", Settings.city);
            editor.apply();


        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }

//        @Override
//        public boolean onCreateOptionsMenu(Menu menu) {
//            // Inflate the menu; this adds items to the action bar if it is present.
////        getMenuInflater().inflate(R.menu.main, menu);
//            MenuInflater inflater = getMenuInflater();
//            inflater.inflate(R.menu.menu_preferences, menu);
//
//            return super.onCreateOptionsMenu(menu);
//        }
    }

    public class AutoSuggestAdapter extends ArrayAdapter {
        private Context context;
        private int resource;
        private List<String> items;
        private List<String> tempItems;
        private List<String> suggestions;

        public AutoSuggestAdapter(Context context, int resource, List<String> items) {
            super(context, resource, 0, items);

            this.context = context;
            this.resource = resource;
            this.items = items;
            tempItems = new ArrayList<String>(items);
            suggestions = new ArrayList<String>();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(resource, parent, false);
            }

            String item = items.get(position);

            ImageView weatherIcon = (ImageView) view
                    .findViewById(R.id.flag_icon);
            String code = countryNamesToCodes.get(item);
            if (code != null){
                code = code.toLowerCase();
                String mDrawableName = "flags_" + code;
                int resID = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());
                weatherIcon.setImageResource(resID);
            }


            if (item != null) {
                TextView countryName = (TextView) view
                        .findViewById(R.id.country_name);
                countryName.setText(item);
            }

            return view;
        }

        @Override
        public Filter getFilter() {
            return nameFilter;
        }

        Filter nameFilter = new Filter() {
            @Override
            public CharSequence convertResultToString(Object resultValue) {
                String str = (String) resultValue;
                return str;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    suggestions.clear();
                    for (String names : tempItems) {
                        if (names.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            suggestions.add(names);
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                List<String> filterList = (ArrayList<String>) results.values;
                if (results != null && results.count > 0) {
                    clear();
                    for (String item : filterList) {
                        add(item);
                        notifyDataSetChanged();
                    }
                }
            }
        };

    }
}
