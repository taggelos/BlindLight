package com.example.aggel.accelerometerapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;




public class SettingsActivity extends AppCompatActivity  {

    private  TextView text_view_x_axis;
    private  TextView text_view_y_axis;
    private  TextView text_view_z_axis;

    private  SeekBar seekBar_x_axis ;
    private  SeekBar seekBar_y_axis ;
    private  SeekBar seekBar_z_axis ;

    private Button save_changes;
    private CheckBox cb;

    //default thresholds
    private int x_my_progress;
    private int y_my_progress;
    private int z_my_progress;


    private SoundEvent se;
    private int soundId;
    private int streamId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        //SaveChanges Button
        final String MY_KEY_X = "intVariableName1";
        final String MY_KEY_Y = "intVariableName2";
        final String MY_KEY_Z = "intVariableName3";
        cb = (CheckBox) findViewById(R.id.checkBox1);
        save_changes = (Button)findViewById(R.id.buttonSave);
        save_changes.setOnClickListener(new View.OnClickListener(){

            @Override
            //On click function
            public void onClick(View v1) {

                seekBar_x_axis = (SeekBar) findViewById(R.id.seekBar);
                seekBar_y_axis = (SeekBar) findViewById(R.id.seekBar2);
                seekBar_z_axis = (SeekBar) findViewById(R.id.seekBar3);

                x_my_progress = seekBar_x_axis.getProgress();
                y_my_progress = seekBar_y_axis.getProgress();
                z_my_progress = seekBar_z_axis.getProgress();

                savePrefs("CHECKBOX" , cb.isChecked());

                if (cb.isChecked())
                    savePrefs("PROGRESS_X" , x_my_progress);
                    savePrefs("PROGRESS_Y" , y_my_progress);
                    savePrefs("PROGRESS_Z" , z_my_progress);
                    Intent toy2 = new Intent(SettingsActivity.this,MainActivity.class);
                    toy2.putExtra( MY_KEY_X,x_my_progress);
                    toy2.putExtra( MY_KEY_Y,y_my_progress);
                    toy2.putExtra( MY_KEY_Z,z_my_progress);
                    startActivity(toy2);

            }
        });

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



    //Functions for Saving changes on Seekbars

    private void loadPrefs(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean cbValue = sp.getBoolean("CHECKBOX",false);
        int prog_x = sp.getInt("PROGRESS_X" , x_my_progress);
        int prog_y = sp.getInt("PROGRESS_Y" , y_my_progress);
        int prog_z = sp.getInt("PROGRESS_Z" , z_my_progress);

        if(cbValue){
            cb.setChecked(true);
        }else{
            cb.setChecked(false);

        }
        seekBar_x_axis = (SeekBar) findViewById(R.id.seekBar);
        seekBar_y_axis = (SeekBar) findViewById(R.id.seekBar2);
        seekBar_z_axis = (SeekBar) findViewById(R.id.seekBar3);
        text_view_x_axis = (TextView)findViewById(R.id.seekbarView);
        text_view_y_axis = (TextView)findViewById(R.id.seekbarView2);
        text_view_z_axis = (TextView)findViewById(R.id.seekbarView3);

        //Updating the values on screen

        text_view_x_axis.setText("Covered_X_axis : " + prog_x + "/" + seekBar_x_axis.getMax());
        text_view_y_axis.setText("Covered_Y_axis : " + prog_y + "/" + seekBar_y_axis.getMax());
        text_view_z_axis.setText("Covered_Z_axis : " + prog_z + "/" + seekBar_z_axis.getMax());
        seekBar_x_axis.setProgress(prog_x);
        seekBar_y_axis.setProgress(prog_y);
        seekBar_z_axis.setProgress(prog_z);

        x_my_progress = prog_x;
        y_my_progress = prog_y;
        z_my_progress = prog_z;

    }

    private void savePrefs(String key , boolean value){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key,value);
        edit.commit();
    }

    private void savePrefs(String key , int value_prog){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key,value_prog);
        edit.commit();

    }


    //Seekbar Function

    public void seekbarr(){

        seekBar_x_axis = (SeekBar) findViewById(R.id.seekBar);
        seekBar_y_axis = (SeekBar) findViewById(R.id.seekBar2);
        seekBar_z_axis = (SeekBar) findViewById(R.id.seekBar3);

        System.out.println("MPLAAAAAAAAAAAAAAAAAAAAAA");

        text_view_x_axis = (TextView)findViewById(R.id.seekbarView);
        text_view_y_axis = (TextView)findViewById(R.id.seekbarView2);
        text_view_z_axis = (TextView)findViewById(R.id.seekbarView3);

        x_my_progress = seekBar_x_axis.getProgress();

        text_view_x_axis.setText("Covered_X_axis : " + x_my_progress + "/" + seekBar_x_axis.getMax());


        loadPrefs();


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

        y_my_progress = seekBar_y_axis.getProgress();


        text_view_y_axis.setText("Covered_Y_axis : " + y_my_progress + "/" + seekBar_y_axis.getMax());


        loadPrefs();

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

        z_my_progress = seekBar_z_axis.getProgress();


        text_view_z_axis.setText("Covered_Z_axis : " + z_my_progress + "/" + seekBar_z_axis.getMax());


        loadPrefs();


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

