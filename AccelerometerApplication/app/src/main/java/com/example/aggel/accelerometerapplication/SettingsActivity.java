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

    private static TextView text_view_x_axis;
    private static TextView text_view_y_axis;
    private static TextView text_view_z_axis;

    private static SeekBar seekBar_x_axis;
    private static SeekBar seekBar_y_axis;
    private static SeekBar seekBar_z_axis;






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





    public int GetSeekBarPr() {
        return seekBar_x_axis.getProgress();
    }





    public void seekbarr(){
        seekBar_x_axis = (SeekBar) findViewById(R.id.seekBar);
        seekBar_y_axis = (SeekBar) findViewById(R.id.seekBar2);
        seekBar_z_axis = (SeekBar) findViewById(R.id.seekBar3);

        text_view_x_axis = (TextView)findViewById(R.id.seekbarView);
        text_view_y_axis = (TextView)findViewById(R.id.seekbarView2);
        text_view_z_axis = (TextView)findViewById(R.id.seekbarView3);


        text_view_x_axis.setText("Covered_X_axis : " + seekBar_x_axis.getProgress() + "/" + seekBar_x_axis.getMax());

        seekBar_x_axis.setOnSeekBarChangeListener(

                new SeekBar.OnSeekBarChangeListener(){
                    int progress_value;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progress_value = progress;
                        text_view_x_axis.setText("Covered_X_axis : " + progress + "/" + seekBar.getMax());
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        text_view_x_axis.setText("Covered_X_axis : " + progress_value + "/" + seekBar.getMax());
                    }
                }
        );

        text_view_y_axis.setText("Covered_Y_axis : " + seekBar_y_axis.getProgress() + "/" + seekBar_y_axis.getMax());

        seekBar_y_axis.setOnSeekBarChangeListener(

                new SeekBar.OnSeekBarChangeListener(){
                    int progress_value;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progress_value = progress;
                        text_view_y_axis.setText("Covered_Y_axis : " + progress + "/" + seekBar.getMax());
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        text_view_y_axis.setText("Covered_Y_axis : " + progress_value + "/" + seekBar.getMax());
                    }
                }
        );

        text_view_z_axis.setText("Covered_Z_axis : " + seekBar_z_axis.getProgress() + "/" + seekBar_z_axis.getMax());

        seekBar_z_axis.setOnSeekBarChangeListener(

                new SeekBar.OnSeekBarChangeListener(){
                    int progress_value;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progress_value = progress;
                        text_view_z_axis.setText("Covered_Z_axis : " + progress + "/" + seekBar.getMax());
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        text_view_z_axis.setText("Covered_Z_axis : " + progress_value + "/" + seekBar.getMax());
                    }
                }
        );


    }
}

