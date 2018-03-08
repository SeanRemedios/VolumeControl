package com.volutime;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;
import android.content.res.ColorStateList;
import android.content.Intent;

import com.devadvance.circularseekbar.CircularSeekBar;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private TextView textViewvolumeProgress;
    private TextView textViewvolumeDeci;
    private TextView textViewProgress;
    private SeekBar seekBar;
    CircularSeekBar Cseekbar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    mTextMessage.setText("Profile");
                    launchProfileActivity();
                    return true;
                case R.id.navigation_time:
                    mTextMessage.setText("Time");
                    return true;
                case R.id.navigation_stats:
                    mTextMessage.setText("Statistics");
                    return true;
            }
            return false;
        }
    };

    private void launchProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewvolumeProgress = (TextView) findViewById(R.id.volumeProgress);
        textViewvolumeDeci = (TextView) findViewById(R.id.volumeDeci);
        textViewProgress = (TextView) findViewById(R.id.textViewProgress);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.getIndeterminateDrawable().setColorFilter(0x32CD32,android.graphics.PorterDuff.Mode.MULTIPLY);
        Cseekbar = (CircularSeekBar) findViewById(R.id.circularSeekBar1);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_time);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Cseekbar.setProgress(50);
        int CProgress = Cseekbar.getProgress();
        textViewProgress.setText("" + CProgress + "%");

        int[][] states = new int[][] {
                new int[] { android.R.attr.state_enabled}, // enabled
                new int[] {-android.R.attr.state_enabled}, // disabled
                new int[] {-android.R.attr.state_checked}, // unchecked
                new int[] { android.R.attr.state_pressed}  // pressed
        };

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

        final ColorStateList cslAbove = new ColorStateList(states, colorsAbove);
        final ColorStateList cslBelow = new ColorStateList(states, colorsBelow);

        Cseekbar.setOnSeekBarChangeListener(new CircleSeekBarListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
                Cseekbar.setProgress(progress);
                textViewProgress.setText("" + progress + "%");
                if (progress > 75) {
                    int redColorValue = Color.parseColor("#FF3030");
                    circularSeekBar.setCircleProgressColor(redColorValue);
                } else {
                    int greenColorValue = Color.parseColor("#32CD32");
                    circularSeekBar.setCircleProgressColor(greenColorValue);
                }
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
                textViewvolumeProgress.setText(""+ progress + "%");
                textViewvolumeDeci.setText("" + (progress * 0.5) + "db" );
                if (progress > 75) {
                    seekBar.setProgressTintList(cslAbove);
                    seekBar.setThumbTintList(cslAbove);
                } else {
                    seekBar.setProgressTintList(cslBelow);
                    seekBar.setThumbTintList(cslBelow);
                }
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


class CircleSeekBarListener implements CircularSeekBar.OnCircularSeekBarChangeListener {
    @Override
    public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {

    }

    public void onStartTrackingTouch(CircularSeekBar CirclularseekBar) {

    }
    public void onStopTrackingTouch(CircularSeekBar CirclularseekBar) {

    }
}
