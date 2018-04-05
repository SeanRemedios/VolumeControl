package com.volutime;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;

import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {

//    private TextView mTextMessage;
    Toolbar topToolBar;
    private ImageButton settingsButton;
    BottomNavigationView navigation;
    GraphView graph;
    LinearLayout data;
    ScrollView scrollview;

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
                    launchActivity(ProfileActivity.class);
                    return true;
                case R.id.navigation_time:
                    launchActivity(MainActivity.class);
                    return true;
                case R.id.navigation_stats:
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
        assert actionBar != null;
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        settingsButton = (ImageButton) findViewById(R.id.settings_button);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        topToolBar = (Toolbar) findViewById(R.id.toolbar);
        graph = (GraphView) findViewById(R.id.graph);
        data = (LinearLayout) findViewById(R.id.dataLL);
        scrollview = (ScrollView) findViewById(R.id.scroll);

        toolbarStuff();
        navigationStuff();

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), SettingActivity.class);
                i.putExtra("FromClass", "Stats");
                startActivity(i);
                finish();
            }
        });

        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Volume");
        graph.getGridLabelRenderer().setHorizontalAxisTitleTextSize(24);
        graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(24);
        graph.getGridLabelRenderer().setGridColor(ContextCompat.getColor(this, R.color.colorText));
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(ContextCompat.getColor(this, R.color.colorText));
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(ContextCompat.getColor(this, R.color.colorText));
        graph.getGridLabelRenderer().setHorizontalLabelsColor(ContextCompat.getColor(this, R.color.colorText));
        graph.getGridLabelRenderer().setVerticalLabelsColor(ContextCompat.getColor(this, R.color.colorText));

        // set manual X bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(100);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(12);

        graph.getViewport().setScrollableY(true); // enables vertical scrolling
        graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling

        String[] dataDate = {   " Date: 09/01/2018 - ",
                                " Date: 19/01/2018 - ",
                                " Date: 14/02/2018 - ",
                                " Date: 17/02/2018 - ",
                                " Date: 21/02/2018 - ",
                                " Date: 13/03/2018 - ",
                                " Date: 24/03/2018 - ",
                                " Date: 26/03/2018 - ",
                                " Date: 30/03/2018 - ",
                                " Date: 03/04/2018 - " };

        String[] dataVol = {    "Avg Volume: 50%",
                                "Avg Volume: 35%",
                                "Avg Volume: 40%",
                                "Avg Volume: 45%",
                                "Avg Volume: 17%",
                                "Avg Volume: 40%",
                                "Avg Volume: 65%",
                                "Avg Volume: 80%",
                                "Avg Volume: 70%",
                                "Avg Volume: 50%" };

        String[] dataTime = {   " Time: 3:00pm - ",
                                " Time: 7:00pm - ",
                                " Time: 8:30am - ",
                                " Time: 11:45am - ",
                                " Time: 1:15pm - ",
                                " Time: 4:23pm - ",
                                " Time: 8:15pm - ",
                                " Time: 3:34pm - ",
                                " Time: 10:01am - ",
                                " Time: 11:53pm - " };

        String[] dataDur = {    "Duration: 1 hour",
                                "Duration: 2 hours",
                                "Duration: 3.5 hours",
                                "Duration: 8 hours",
                                "Duration: 6 hours",
                                "Duration: 6 hours",
                                "Duration: 4 hours",
                                "Duration: 3 hours",
                                "Duration: 2.5 hours",
                                "Duration: 2.25 hours" };

        for (int i=dataDate.length-1; i >= 0; i--) {
            TextView datetime = new TextView(this);
            TextView voldur = new TextView(this);

            View v = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    5
            );
            params.setMargins(0,20, 0,20);
            v.setLayoutParams(params);
            v.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));

            datetime.setId(i);
            voldur.setId(i);
            datetime.setText(dataDate[i] + dataVol[i]);
            voldur.setText(dataTime[i] + dataDur[i]);
            datetime.setTextSize(20);
            voldur.setTextSize(20);
            datetime.setTextColor(ContextCompat.getColor(this, R.color.colorText));
            voldur.setTextColor(ContextCompat.getColor(this, R.color.colorText));

            this.data.addView(datetime);
            this.data.addView(voldur);
            this.data.addView(v);
        }
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
        } catch (Exception ignored) {}
    }
}
