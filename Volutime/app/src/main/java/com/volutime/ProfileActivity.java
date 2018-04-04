package com.volutime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity  {

    private Toolbar topToolBar;
    private ImageButton settingsButton;
    private Button setPrefsButton;
    private EditText volumeEdit;
    private EditText timeEdit;
    private BottomNavigationView navigation;
    private RadioGroup radiogroup;

    private final String HEADPHONE_TYPE_1 = "Earbud";
    private final String HEADPHONE_TYPE_2 = "OverEar";
    public String headphone_type;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    return true;
                case R.id.navigation_time:
                    launchActivity(MainActivity.class);
                    return true;
                case R.id.navigation_stats:
                    launchActivity(StatsActivity.class);
                    return true;
            }
            return false;
        }
    };

    // Activity intents to start new activity based on intent
    private void launchActivity(Class cl) {
        Intent intent = new Intent(this, cl);
        startActivity(intent);
    }

    private void navigationStuff() {
        navigation.setSelectedItemId(R.id.navigation_profile);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private void toolbarStuff() {
        // Top toolbar
        setSupportActionBar(topToolBar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setPrefsButton = (Button) findViewById(R.id.setprefs_button);
        volumeEdit = (EditText) findViewById(R.id.volume_edit);
        timeEdit = (EditText) findViewById(R.id.time_edit);
        settingsButton = (ImageButton) findViewById(R.id.settings_button);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        topToolBar = (Toolbar) findViewById(R.id.toolbar);
        radiogroup = (RadioGroup) findViewById(R.id.RG_HeadphoneType);

        toolbarStuff();
        navigationStuff();

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), SettingActivity.class);
                i.putExtra("FromClass", "Profile");
                startActivity(i);
                finish();
            }
        });

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.RB_Earbud:
                        headphone_type = HEADPHONE_TYPE_1;
                        break;
                    case R.id.RB_OTE:
                        headphone_type = HEADPHONE_TYPE_2;
                        break;
                }
            }
        });

        setPrefsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences(getString(R.string.SharedPrefs_Time), 0);
                SharedPreferences.Editor editor = settings.edit();
                SharedPreferences defaultsettings = getSharedPreferences(getString(R.string.SharedPrefs_Default), 0);
                SharedPreferences.Editor editordefault = defaultsettings.edit();
                // Necessary to clear first if we save preferences onPause.
//                editor.clear();
                editordefault.clear();

                int volumeInt = 0;
                double timeDoub = 0;
                int timeInt = 0;
                try {
                    volumeInt = Integer.parseInt(volumeEdit.getText().toString());
                    timeDoub = Double.parseDouble(timeEdit.getText().toString());
                    timeInt = convertToPercent(timeDoub);
                    if (volumeInt >= 100 || timeInt >= 100) {
                        volumeEdit.setText("100");
                        int temp = 100 - MainActivity.RATIO;
                        timeEdit.setText(temp + "");
                        volumeInt = 100;
                        timeInt = temp;
                    }
                } catch(NumberFormatException nfe) {
                    System.out.println("Could not parse " + nfe);
                }

                System.out.println("Time Pref: " + timeInt);
                System.out.println("Volume Pref: " + volumeInt);

                editor.putInt("CSeekBarProgress", timeInt);
                editor.putInt("SeekBarProgress", volumeInt);
                editordefault.putInt("CSeekBarProgress", timeInt);
                editordefault.putInt("SeekBarProgress", volumeInt);
                editor.apply();
                editordefault.apply();
            }
        });
    }

    private int convertToPercent(double time) {
        int timeINT = 0;
        timeINT = (int)((time/12)*100);
        return timeINT;
    }

    private void setTimeText(int progress) {
        double time = ((double)progress/100); // Get the progress out of %
        time = Math.round(time * 100.00) / 100.00; // Only take 2 decimal places
        double timeTotal = time * 12; // Get the time in 12 hours
        double timeHrs = Math.floor(timeTotal * 1); // Get rid of all decimals for hours
        timeEdit.setText(timeHrs + ""); // Display, yay!
        System.out.println("Time: " + timeHrs);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.SharedPrefs_Default), this.MODE_PRIVATE);

        int time_pref = sharedPref.getInt("CSeekBarProgress", 0);
        int volume_pref = sharedPref.getInt("SeekBarProgress", 0);
        volumeEdit.setText(Integer.toString(volume_pref), TextView.BufferType.EDITABLE);
        setTimeText(time_pref);
        System.out.println("Volume: " + volume_pref);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
