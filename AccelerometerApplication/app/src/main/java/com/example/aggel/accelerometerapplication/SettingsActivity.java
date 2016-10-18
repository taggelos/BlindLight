package com.example.aggel.accelerometerapplication;

import android.content.Intent;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;



public class SettingsActivity extends AppCompatActivity {

    private static TextView text_view;
    private static SeekBar seek_bar;


    private SoundEvent se;
    private int soundId;
    private int streamId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Seekbar
        seekbarr();

        //mysound
        se = new SoundEvent();
        soundId = se.getSoundId(getApplicationContext());

        //Sound Button
        Button button  = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            //On click function
            public void onClick(View view) {
                se.playOnce(soundId);
            }
        });
    }

    public void seekbarr(){
        seek_bar = (SeekBar) findViewById(R.id.seekBar);
        text_view = (TextView)findViewById(R.id.seekbarView);
        text_view.setText("Covered : " + seek_bar.getProgress() + "/" + seek_bar.getMax());

        seek_bar.setOnSeekBarChangeListener(

                new SeekBar.OnSeekBarChangeListener(){
                    int progress_value;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progress_value = progress;
                        text_view.setText("Covered : " + progress + "/" + seek_bar.getMax());
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        text_view.setText("Covered : " + progress_value + "/" + seek_bar.getMax());
                    }
                }
        );
    }
}
