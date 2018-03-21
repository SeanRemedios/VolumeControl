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

import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {

    private TextView mTextMessage;
    Toolbar topToolBar;
    private ImageButton settingsButton;
    BottomNavigationView navigation;

    // Statistics are gotten from the MainActivity class through getters that are populated in
    // the onResume function when the page is clicked.
    ArrayList<Float> volumes = new ArrayList<>();
    ArrayList<Integer> times_listened = new ArrayList<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    mTextMessage.setText("Profile");
                    launchActivity(ProfileActivity.class);
                    finish();
                    return true;
                case R.id.navigation_time:
                    mTextMessage.setText("Time");
                    launchActivity(MainActivity.class);
                    finish();
                    return true;
                case R.id.navigation_stats:
                    mTextMessage.setText("Statistics");
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
        navigation.setSelectedItemId(R.id.navigation_stats);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void toolbarStuff() {
        // Top toolbar
        setSupportActionBar(topToolBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        settingsButton = (ImageButton) findViewById(R.id.settings_button);

        mTextMessage = (TextView) findViewById(R.id.message);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);

        topToolBar = (Toolbar) findViewById(R.id.toolbar);

        toolbarStuff();
        navigationStuff();

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(SettingActivity.class);
                finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        MainActivity main = new MainActivity();
        try{
            Synch.mutex_statistics.acquire();
            // Synch.volumes;
            // Synch.times_listened;
            // Synch.dateTimeStarted;
            Synch.mutex_statistics.release();
        } catch (Exception e) {}
    }
}
