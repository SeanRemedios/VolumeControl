package com.volutime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.Toast;

import com.devadvance.circularseekbar.CircularSeekBar;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class MainActivity extends AppCompatActivity {

    // Declare the activity components for later
    private TextView mTextMessage;
    private TextView textViewvolumeProgress;
    private TextView textViewProgress;
    private SeekBar seekBar;
    private CircularSeekBar Cseekbar;
    private Toolbar topToolBar;
    private ImageButton settingsButton;
    private ImageButton musicButton;
    private BottomNavigationView navigation;

    // These are our color state lists for the seekbar
    private ColorStateList cslAbove;
    private ColorStateList cslBelow;

    private boolean music_state = false; // For button use only not the session active counter
    // To allow us to reset and not change both bars - lazy and stupid, will change later
    private boolean reset_crutch = false;
    private int time_listened = 0;
    private int[] volumeArray = new int[100]; // Max we can have
    private final int RATIO = 5; // ratio of volume to time


    // This is for our thread stuff to make sure they don't get created again later
    public MainActivity() {
        // Declaring Mutex and Semaphores for threads
        Synch.mutex_session_active = new Semaphore(1, true);
        Synch.mutex_seekbar_lock = new Semaphore(1, true);
        Synch.session_update = new Semaphore(0, true);
        Synch.session = new Semaphore(0, true);
        Synch.time = new Semaphore(0, true);

        Session sessionThread;
        TimeKeeper timeKeeperThread;
        // Start the session thread
        sessionThread = new Session(this);
        sessionThread.start();
        // Start the time keeper thread
        timeKeeperThread = new TimeKeeper();
        timeKeeperThread.start();
    }

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
                    return true;
                // Tab in the middle
                case R.id.navigation_time: // This is current, do nothing
                    mTextMessage.setText("Time");
                    return true;
                // Tab on the right
                case R.id.navigation_stats:
                    mTextMessage.setText("Statistics");
                    launchActivity(StatsActivity.class); // Launch the tab's activity
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
       int seekbarProgress = seekBar.getProgress();

        if (progress > 70 && Synch.seekbar_lock && !Synch.session_active) {
            // Just a warning since listening to music for a long time is dangerous at any level
            changeCircSeekBarColor(R.color.colorOrange, circularSeekBar);
        // Eventually will change to properly check when it's at a dangerous level
        } else if (progress-RATIO > seekbarProgress) {
            changeCircSeekBarColor(R.color.colorRed, circularSeekBar);
        } else {
            changeCircSeekBarColor(R.color.colorProgress, circularSeekBar);
        }
    }

    private void checkProgressSeekbar(int progress, SeekBar seekBar) {
        int cSeekBarProgress = Cseekbar.getProgress();

        // It's dangerous if the volume is above 80%
        if (progress > 80) {
            changeSeekBarColor(cslAbove);
        // Eventually will change to properly check when it's at a dangerous level
        } else if (progress-RATIO > cSeekBarProgress) {
            changeSeekBarColor(cslAbove);
        } else {
            changeSeekBarColor(cslBelow);
        }
    }

    private void changeSeekBarColor(ColorStateList csl) {
        // Change the progress tint to red
        seekBar.setProgressTintList(csl);
        // Change the thumb as well
        seekBar.setThumbTintList(csl);
    }

    private void changeCircSeekBarColor(int id, CircularSeekBar circularSeekBar) {
        // Get the color of the resource we have
        int color = ContextCompat.getColor(getBaseContext(), id);
        // Change the circle's color
        circularSeekBar.setCircleProgressColor(color);
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
                ContextCompat.getColor(getBaseContext(),
                        R.color.colorProgress),
                Color.WHITE,
                Color.WHITE,
                Color.WHITE
        };
        int[] colorsAbove = new int[] {
                ContextCompat.getColor(getBaseContext(),
                        R.color.colorRed),
                Color.WHITE,
                Color.WHITE,
                Color.WHITE
        };

        cslAbove = new ColorStateList(states, colorsAbove);
        cslBelow = new ColorStateList(states, colorsBelow);
    }

    private float averageVolume(int[] volumeArray) {
        int sum = 0;
        float avgVol = 0;
        int i;
        for (i = 0; i < volumeArray.length; i++) {
            if (volumeArray[i] == -1) {
                break;
            }
            sum += volumeArray[i];
        }
        avgVol = sum/i;
        return avgVol;
    }

    private boolean checkVolumeArray(int[] volumeArray) {
        boolean result = false;
        for (int i = 0; i < volumeArray.length; i++) {
            if (volumeArray[i] != -1) {
                result = true;
            }
        }
        return result;
    }


    private void addPastData(float volume, int time_listened) {
        try {
            Synch.mutex_statistics.acquire();
            Synch.volumes.add(volume);
            Synch.times_listened.add(time_listened);
            Synch.mutex_statistics.release();
        } catch (Exception e) {}
    }

    private void sessionEnded() {
        music_state = false;
        musicButton.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_play));
        try {
            Synch.mutex_session_active.acquire();
            Synch.session_active = false;
            Synch.mutex_session_active.release();
        } catch (Exception e) {}
        Arrays.fill(volumeArray, -1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get all IDs from activity
        // Don't need to cast, just do it for info
        textViewvolumeProgress  = (TextView) findViewById(R.id.volumeProgress);
        textViewProgress        = (TextView) findViewById(R.id.textViewProgress);
        seekBar                 = (SeekBar) findViewById(R.id.seekBar);
        Cseekbar                = (CircularSeekBar) findViewById(R.id.circularSeekBar1);
        settingsButton          = (ImageButton) findViewById(R.id.settings_button);
        musicButton             = (ImageButton) findViewById(R.id.musicbutton);
        mTextMessage            = (TextView) findViewById(R.id.message);
        navigation              = (BottomNavigationView) findViewById(R.id.navigation);
        topToolBar              = (Toolbar) findViewById(R.id.toolbar);

        Arrays.fill(volumeArray, -1); // Fill the volume array with negative values

        // Set up the color state lists
        setupColorStateLists();

        // Call all our stuff
        seekBarStuff(seekBar);
        navigationStuff(navigation);
        circularSeekBarStuff(Cseekbar);
        toolbarStuff();

        //Checking Linking Setting Value
        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);
        Boolean switchPref = sharedPref.getBoolean
                (SettingActivity.KEY_PREF_LINK_SWITCH, false);
        Synch.seekbar_lock = switchPref;
        //Toast.makeText(this, switchPref.toString(), Toast.LENGTH_SHORT).show();


        // Button listener for settings (top right)
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the settings activity
                launchActivity(SettingActivity.class);
                finish();
            }
        });

         //Button listener for pause and unpause
        musicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (music_state == false) { // Music is playing now so start our session
                    music_state = true;
                    Thread sessionupdatethread = new Thread(SessionUpdate);
                    sessionupdatethread.start();
                    musicButton.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_pause));
                    Synch.session.release(); // Release our music semaphore so the session can start

                    try {
                        Synch.mutex_statistics.acquire();
                        Synch.dateTimeStarted.add(LocalDateTime.now());
                        Synch.mutex_statistics.release();
                    } catch (Exception e) {}

                } else { // Music is on pause
                    sessionEnded();
                }
            }
        });

        // Reset button in top left
        topToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result = checkVolumeArray(volumeArray);
                if (result) {
                    float avgVol = averageVolume(volumeArray);
                    addPastData(avgVol, time_listened);
                }
                sessionEnded();
                // This allows us to only change the seekbar progress without changing the volume
                reset_crutch = true;
                Cseekbar.setProgress(0);
                reset_crutch = false;
            }
        });


        // Listener for circular seek bar
        Cseekbar.setOnSeekBarChangeListener(new CircleSeekBarListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
                // Check to see if the two seek bars are tied together
                if (Synch.seekbar_lock && !reset_crutch) {
                    // Check if a session is active
                    if (!Synch.session_active) {
                        // Make sure the ratio is not above our max
                        int newProgress = progress;
                        if (progress+RATIO <= 100) {
                            newProgress += RATIO;
                        }
                        seekBar.setProgress(newProgress,true);
                    }
                }
                Cseekbar.setProgress(progress);
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
                // Check to see if the two seek bars are tied together
                if (Synch.seekbar_lock && !reset_crutch) {
                    // Check if a session is active
                    if (!Synch.session_active) {
                        // Make sure the ratio is not above our max
                        int newProgress = progress;
                        if (progress-RATIO >= 0) {
                            newProgress -= RATIO;
                        }
                        Cseekbar.setProgress((newProgress));
                    }
                }
                seekBar.setProgress(progress);
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

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences settings = getSharedPreferences(getString(R.string.SharedPrefs_Time),0);
        SharedPreferences.Editor editor = settings.edit();
        // Necessary to clear first if we save preferences onPause.
        editor.clear();
        editor.putInt("CSeekBarProgress", Cseekbar.getProgress());
        editor.putInt("SeekbarProgress", seekBar.getProgress());
        editor.putBoolean("MusicState", music_state);
        editor.commit();

    }

    @Override
    public void onStart() {
        super.onStart();

//        Cseekbar.setProgress(0);
//        seekBar.setProgress(0);
        music_state = false;
        musicButton.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_play));
        try {
            Synch.mutex_session_active.acquire();
            Synch.session_active = false;
            Synch.mutex_session_active.release();
        } catch (Exception e) {}
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.SharedPrefs_Time), this.MODE_PRIVATE);

        int CSeekBarProgress = sharedPref.getInt("CSeekBarProgress", 0);
        int SeekBarProgress = sharedPref.getInt("SeekBarProgress", 0);
        music_state = sharedPref.getBoolean("MusicState", false);

        Cseekbar.setProgress(CSeekBarProgress);
        seekBar.setProgress(SeekBarProgress);
        textViewvolumeProgress.setText("" + SeekBarProgress + "%");
        setTimeText(CSeekBarProgress);

        if (music_state) { // Music is playing now so start our session
            musicButton.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_pause));
        } else { // Music is on pause
            musicButton.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_play));
            try {
                Synch.mutex_session_active.acquire();
                Synch.session_active = false;
                Synch.mutex_session_active.release();
            } catch (Exception e) {}
        }
    }

    // Don't touch this fuckery, except for the numbers
    Runnable SessionUpdate = new Runnable() {
        //private int timeMaxWait = 432000; // 7.2 minutes = 1% of 12 hours
        //private int time = 10000; // Time to wait until we check again
        boolean s_active = false;
        int count = 0;

        @Override
        public void run() {

            try {
                Synch.session_update.acquire();
            } catch (Exception e) {}

            while (Synch.session_active) {
                try {
                    Synch.mutex_session_active.acquire();
                    s_active = Synch.session_active;
                    Synch.mutex_session_active.release();

                    //System.out.println("Sleeping for: " + time/1000);

                    System.out.println("Waiting");

                    // Wait for it to be released from TimeKeeper class
                    Synch.time.acquire(); // Already in a try, catch

                    // Add the time listened on every release
                    time_listened += TimeKeeper.TIMEMAXWAIT;
                    // Get the volume and add it to the array to average it later
                    int volume = seekBar.getProgress();
                    volumeArray[count] = volume;
                    count++;

                    System.out.println("Released");

                    // This allows us to change UI stuff
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Get it before we do stuff so we can check if progress = 0
                            int progress = Cseekbar.getProgress();

                            if (s_active) {
                                // 1% of 12 hours = 7.2 minutes
                                progress = progress - 1;
                                Cseekbar.setProgress(progress);
                                Cseekbar.setEnabled(false);

                            } else {
                                Cseekbar.setEnabled(true);
                            }

                            // Get it before we do stuff so we can check if progress = 0
                            progress = Cseekbar.getProgress();
                            // Get an instance of the class so we can get the notification funciton
                            Session session = new Session(getBaseContext());

                            if (progress == 0) {
                                session.sendNotification("Session Ended",
                                        "Time up! Turn off your music to prevent damage!");
                                sessionEnded();
                            } else if (progress <= 2) {
                                session.sendNotification("Session Almost Done",
                                        "Less than 15 minutes left in the current session!");
                            }
                        }

                    });
                } catch (Exception e) {}

            }
        }
    };


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
