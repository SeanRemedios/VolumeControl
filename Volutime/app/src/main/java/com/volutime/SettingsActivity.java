package com.volutime;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    private TextView mTextMessage;
    Toolbar topToolBar;
    private ImageButton settingsButton;
    BottomNavigationView navigation;

    private ColorStateList cslNavOff;
    private ColorStateList cslNavOn;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    setNavColor(cslNavOn);
                    launchActivity(ProfileActivity.class);
                    finish();
                    return true;
                case R.id.navigation_time:
                    setNavColor(cslNavOn);
                    launchActivity(MainActivity.class);
                    finish();
                    return true;
                case R.id.navigation_stats:
                    setNavColor(cslNavOn);
                    launchActivity(StatsActivity.class);
                    finish();
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

    private void toolbarStuff() {
        // Top toolbar
        setSupportActionBar(topToolBar);
        ActionBar actionBar = getSupportActionBar();;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void navigationStuff() {
//        navigation.setSelected(false);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void setupColorStateLists() {
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_enabled}, // enabled
                new int[] {-android.R.attr.state_enabled}, // disabled
                new int[] {-android.R.attr.state_checked}, // unchecked
                new int[] { android.R.attr.state_pressed}  // pressed
        };

        int[] colorsNavOn = new int[] {
                ContextCompat.getColor(getBaseContext(),
                        R.color.colorChecked),
                Color.WHITE,
                ContextCompat.getColor(getBaseContext(),
                        R.color.colorUnChecked),
                ContextCompat.getColor(getBaseContext(),
                        R.color.colorChecked)
        };
        int[] colorsNavOff = new int[] {
                ContextCompat.getColor(getBaseContext(),
                        R.color.colorUnChecked),
                Color.WHITE,
                ContextCompat.getColor(getBaseContext(),
                        R.color.colorUnChecked),
                ContextCompat.getColor(getBaseContext(),
                        R.color.colorUnChecked)
        };

        cslNavOn = new ColorStateList(states, colorsNavOn);
        cslNavOff = new ColorStateList(states, colorsNavOff);
    }

    private void setNavColor(ColorStateList csl) {
        navigation.setItemIconTintList(csl);
        navigation.setItemTextColor(csl);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mTextMessage = (TextView) findViewById(R.id.message);
        settingsButton = (ImageButton) findViewById(R.id.settings_button);
        mTextMessage = (TextView) findViewById(R.id.message);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        topToolBar = (Toolbar) findViewById(R.id.toolbar);

        mTextMessage.setText("Settings");
        setupColorStateLists();
        setNavColor(cslNavOff);
        toolbarStuff();
        navigationStuff();

    }
}
