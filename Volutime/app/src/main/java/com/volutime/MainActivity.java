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
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.devadvance.circularseekbar.CircularSeekBar;

public class MainActivity extends AppCompatActivity {

    // Declare the activity components for later
    private TextView mTextMessage;
    private TextView textViewvolumeProgress;
    private TextView textViewProgress;
    private SeekBar seekBar;
    private CircularSeekBar Cseekbar;
    private Toolbar topToolBar;
    private ImageButton settingsButton;

    // These are our color state lists for the seekbar
    private ColorStateList cslAbove;
    private ColorStateList cslBelow;

    // The lock that ties the two seek bars together
    public boolean seekbar_lock = true; // true = locked


    // Bottom Navigation view listener
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        // When an item is selected (called from onCreate) it calls this function
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                // Tab on the left
                case R.id.navigation_profile:
                    mTextMessage.setText("Profile");
                    launchActivity(ProfileActivity.class); // Launch the tab's activity
                    finish(); // Stop this activity
                    return true;
                // Tab in the middle
                case R.id.navigation_time: // This is current, do nothing
                    mTextMessage.setText("Time");
                    return true;
                // Tab on the right
                case R.id.navigation_stats:
                    mTextMessage.setText("Statistics");
                    launchActivity(StatsActivity.class); // Launch the tab's activity
                    finish(); // Stop this activity
                    return true;
            }
            return false; // If we didn't find a case, WTF
        }
    };

    // Activity intents to start new activity based on intent
    private void launchActivity(Class cl) {
        Intent intent = new Intent(this, cl);
        startActivity(intent);
    }

    private void seekBarStuff(SeekBar seekBar) {
        // Set the color of the seek bar
        seekBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getBaseContext(),
                R.color.colorProgress),android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    private void navigationStuff(BottomNavigationView navigation) {
        // Set the bottom navigation item that is selected
        navigation.setSelectedItemId(R.id.navigation_time);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void circularSeekBarStuff(CircularSeekBar Cseekbar) {
        Cseekbar.setProgress(0); // Make sure our progress is 0
        int CProgress = Cseekbar.getProgress(); // Get the progress
        textViewProgress.setText("" + CProgress + "%"); // Display the current progress

    }

    private void toolbarStuff() {
        // Set up the tool bar as an action bar
        setSupportActionBar(topToolBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // This is the back button in the top left

    }

    private void setTimeText(int progress) {
        double time = ((double)progress/100); // Get the progress out of %
        time = Math.round(time * 100.00) / 100.00; // Only take 2 decimal places
        double timeTotal = time * 12; // Get the time in 12 hours
        double timeHrs = Math.floor(timeTotal * 1); // Get rid of all decimals for hours
        double timeMins = timeTotal - Math.floor(timeTotal); // Get only the decimals for minutes
        timeMins = timeMins * 60; // Get the minutes
        timeMins = Math.round(timeMins * 100) / 100; // Get rid of decimals, we don't like them
        timeMins = 5*(Math.round(timeMins/5)); // Calibrate to 5 min intervals so it's not sensitive
        textViewProgress.setText("" + timeHrs + " Hrs " + timeMins + " Mins"); // Display, yay!
    }

    private void checkProgressCircle(int progress, CircularSeekBar circularSeekBar) {
        // Eventually will change to properly check when it's at a dangerous level
        if (progress > 75) {
            // Get the color of the resource we have
            int redColorValue = ContextCompat.getColor(getBaseContext(),
                    R.color.colorRed);
            // Change the circle's color
            circularSeekBar.setCircleProgressColor(redColorValue);
        } else {
            // Get the color of the resource we have
            int greenColorValue = ContextCompat.getColor(getBaseContext(),
                    R.color.colorProgress);
            // Change the circle's color
            circularSeekBar.setCircleProgressColor(greenColorValue);
        }
    }

    private void checkProgressSeekbar(int progress, SeekBar seekBar) {
        // Eventually will change to properly check when it's at a dangerous level
        if (progress > 75) {
            // Change the progress tint to red
            seekBar.setProgressTintList(cslAbove);
            // Change the thumb as well
            seekBar.setThumbTintList(cslAbove);
        } else {
            // Change the progress tint to red
            seekBar.setProgressTintList(cslBelow);
            // Change the thumb as well
            seekBar.setThumbTintList(cslBelow);
        }
    }

    private void setupColorStateLists() {
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_enabled}, // enabled
                new int[] {-android.R.attr.state_enabled}, // disabled
                new int[] {-android.R.attr.state_checked}, // unchecked
                new int[] { android.R.attr.state_pressed}  // pressed
        };

        // See states for order
        int[] colorsBelow = new int[] {
                Color.GREEN,
                Color.WHITE,
                Color.WHITE,
                Color.WHITE
        };
        int[] colorsAbove = new int[] {
                Color.RED,
                Color.WHITE,
                Color.WHITE,
                Color.WHITE
        };

        cslAbove = new ColorStateList(states, colorsAbove);
        cslBelow = new ColorStateList(states, colorsBelow);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get all IDs from activity
        textViewvolumeProgress = (TextView) findViewById(R.id.volumeProgress);
        textViewProgress = (TextView) findViewById(R.id.textViewProgress);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        Cseekbar = (CircularSeekBar) findViewById(R.id.circularSeekBar1);
        settingsButton = (ImageButton) findViewById(R.id.settings_button);
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        topToolBar = (Toolbar) findViewById(R.id.toolbar);

        // Set up the color state lists
        setupColorStateLists();

        // Call all our stuff
        seekBarStuff(seekBar);
        navigationStuff(navigation);
        circularSeekBarStuff(Cseekbar);
        toolbarStuff();

        // Button listener for settings (top right
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the settings activity
                launchActivity(SettingsActivity.class);
                finish();
            }
        });

        // Listener for circular seek bar
        Cseekbar.setOnSeekBarChangeListener(new CircleSeekBarListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
                Cseekbar.setProgress(progress); // Set the progress
                // Check to see if the two seek bars are tied together
                if (seekbar_lock == true) {
                    // Set the progress of the seek bar
                    seekBar.setProgress(progress, true);
                }
                setTimeText(progress);
                checkProgressCircle(progress, circularSeekBar); // Get the progress of the circle
            }

            public void onStartTrackingTouch(CircularSeekBar CirclularseekBar) {

            }
            public void onStopTrackingTouch(CircularSeekBar CirclularseekBar) {

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                seekBar.setProgress(progress);
                // Check to see if the two seek bars are tied together
                if (seekbar_lock == true) {
                    Cseekbar.setProgress(progress);
                }
                textViewvolumeProgress.setText(""+ progress + "%");
                checkProgressSeekbar(progress, seekBar);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

}


// Class for circular seek bar, we need it so don't you dare remove it
class CircleSeekBarListener implements CircularSeekBar.OnCircularSeekBarChangeListener {
    @Override
    public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {

    }

    public void onStartTrackingTouch(CircularSeekBar CirclularseekBar) {

    }
    public void onStopTrackingTouch(CircularSeekBar CirclularseekBar) {

    }
}
