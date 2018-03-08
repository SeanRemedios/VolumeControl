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

public class StatsActivity extends AppCompatActivity  {

    private TextView mTextMessage;
    Toolbar topToolBar;
    private ImageButton settingsButton;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    mTextMessage.setText("Profile");
                    launchProfileActivity();
                    finish();
                    return true;
                case R.id.navigation_time:
                    mTextMessage.setText("Time");
                    launchMainActivity();
                    finish();
                    return true;
                case R.id.navigation_stats:
                    mTextMessage.setText("Statistics");
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
    private void launchSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        settingsButton = (ImageButton) findViewById(R.id.settings_button);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_stats);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        topToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
        ActionBar actionBar = getSupportActionBar();;
        actionBar.setDisplayHomeAsUpEnabled(true);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSettingsActivity();
                finish();
            }
        });
    }
}
