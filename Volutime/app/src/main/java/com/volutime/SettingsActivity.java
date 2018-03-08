package com.volutime;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    private TextView mTextMessage;
    Toolbar topToolBar;
    private ImageButton settingsButton;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    launchProfileActivity();
                    finish();
                    return true;
                case R.id.navigation_time:
                    launchMainActivity();
                    finish();
                    return true;
                case R.id.navigation_stats:
                    launchStatsActivity();
                    finish();
                    return true;
            }
            return false;
        }
    };

    private void launchMainActivity() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void launchProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
    private void launchStatsActivity() {

        Intent intent = new Intent(this, StatsActivity.class);
        startActivity(intent);
    }
    private void launchSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mTextMessage = (TextView) findViewById(R.id.message);
        settingsButton = (ImageButton) findViewById(R.id.settings_button);
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        topToolBar = (Toolbar) findViewById(R.id.toolbar);

        // Top toolbar
        setSupportActionBar(topToolBar);
        ActionBar actionBar = getSupportActionBar();;
        actionBar.setDisplayHomeAsUpEnabled(true);

        mTextMessage.setText("Settings");

        navigation.setSelectedItemId(R.id.navigation_stats);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSettingsActivity();
                finish();
            }
        });
    }
}
