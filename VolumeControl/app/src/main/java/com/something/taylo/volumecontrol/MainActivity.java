package com.something.taylo.volumecontrol;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.devadvance.circularseekbar.CircularSeekBar;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private TextView textView2;
    private TextView textView3;
    private SeekBar seekBar;
    private ProgressBar progressBar;
    CircularSeekBar Cseekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textViewProgress);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        Cseekbar = (CircularSeekBar) findViewById(R.id.circularSeekBar1);

        Cseekbar.setProgress(50);
        int CProgress = Cseekbar.getProgress();
        textView3.setText("" + CProgress + "%");

        Cseekbar.setOnSeekBarChangeListener(new CircleSeekBarListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
                Cseekbar.setProgress(progress);
                textView3.setText("" + progress + "%");
            }

            public void onStartTrackingTouch(CircularSeekBar CirclularseekBar) {

            }
            public void onStopTrackingTouch(CircularSeekBar CirclularseekBar) {

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                progressBar.setProgress(progress);
                textView.setText(""+ progress + "%");
                textView2.setText("" + (progress * 0.5) + "db" );
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
