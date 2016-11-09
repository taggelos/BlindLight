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
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;




public class SettingsActivity extends AppCompatActivity  {

    private TextView[] TextViewSettings = new TextView[3];
    private SeekBar[] SeekBars = new SeekBar[3];
    private String[] TextViewCovered = new String[3];
    private int[] My_Progress = new int[3];
    private int[] My_Light_Thresholds = new int[2];
    private NumberPicker[] Light_np=new NumberPicker[2];
    private TextView[] TextViewLight = new TextView[2];
    private Button save_changes;
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


        save_changes = (Button)findViewById(R.id.buttonSave);
        save_changes.setOnClickListener(new View.OnClickListener(){

            @Override
            //On click function
            public void onClick(View v1) {

                SeekBars[0] = (SeekBar) findViewById(R.id.seekBar);
                SeekBars[1] = (SeekBar) findViewById(R.id.seekBar2);
                SeekBars[2] = (SeekBar) findViewById(R.id.seekBar3);

                My_Progress[0] = SeekBars[0].getProgress();
                My_Progress[1] = SeekBars[1].getProgress();
                My_Progress[2] = SeekBars[2].getProgress();


                    savePrefs("PROGRESS_X" , My_Progress[0]);
                    savePrefs("PROGRESS_Y" , My_Progress[1]);
                    savePrefs("PROGRESS_Z" , My_Progress[2]);



                Intent toy2 = new Intent(SettingsActivity.this,MainActivity.class);
                    toy2.putExtra( MY_KEY_X,My_Progress[0]);
                    toy2.putExtra( MY_KEY_Y,My_Progress[1]);
                    toy2.putExtra( MY_KEY_Z,My_Progress[2]);

                startActivity(toy2);

            }
        });

        //Number_Pickers
        numberPickerr();


        //Seekbars
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
        //boolean cbValue = sp.getBoolean("CHECKBOX",false);
        int prog_x = sp.getInt("PROGRESS_X" , My_Progress[0]);
        int prog_y = sp.getInt("PROGRESS_Y" , My_Progress[1]);
        int prog_z = sp.getInt("PROGRESS_Z" , My_Progress[2]);



        SeekBars[0] = (SeekBar) findViewById(R.id.seekBar);
        SeekBars[1] = (SeekBar) findViewById(R.id.seekBar2);
        SeekBars[2] = (SeekBar) findViewById(R.id.seekBar3);

        Light_np[0] = (NumberPicker) findViewById(R.id.LightMaxNumberPicker);
        Light_np[1] = (NumberPicker) findViewById(R.id.LightMinNumberPicker);


        TextViewSettings[0] = (TextView)findViewById(R.id.seekbarView);
        TextViewSettings[1] = (TextView)findViewById(R.id.seekbarView2);
        TextViewSettings[2] = (TextView)findViewById(R.id.seekbarView3);

        //Updating the values on screen

        TextViewSettings[0].setText("Covered_X_axis : " + prog_x + "/" + SeekBars[0].getMax());
        TextViewSettings[1].setText("Covered_Y_axis : " + prog_y + "/" + SeekBars[1].getMax());
        TextViewSettings[2].setText("Covered_Z_axis : " + prog_z + "/" + SeekBars[2].getMax());

        SeekBars[0].setProgress(prog_x);
        SeekBars[1].setProgress(prog_y);
        SeekBars[2].setProgress(prog_z);

        My_Progress[0] = prog_x;
        My_Progress[1] = prog_y;
        My_Progress[2] = prog_z;

    }


    private void savePrefs(String key , int value_prog){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key,value_prog);
        edit.commit();

    }


    //Seekbar Function

    public void seekbarr(){

        SeekBars[0] = (SeekBar) findViewById(R.id.seekBar);
        SeekBars[1] = (SeekBar) findViewById(R.id.seekBar2);
        SeekBars[2] = (SeekBar) findViewById(R.id.seekBar3);

        TextViewSettings[0] = (TextView)findViewById(R.id.seekbarView);
        TextViewSettings[1] = (TextView)findViewById(R.id.seekbarView2);
        TextViewSettings[2] = (TextView)findViewById(R.id.seekbarView3);

        TextViewCovered[0] = "Covered_X_axis : ";
        TextViewCovered[1] = "Covered_Y_axis : ";
        TextViewCovered[2] = "Covered_Z_axis : ";

        //3 seekbars in 1

        for (int i=0; i<3; i++) {
            My_Progress[i]=SeekBars[i].getProgress();
            seekbarUpdate(SeekBars[i], TextViewSettings[i], TextViewCovered[i], My_Progress[i]);

        }
    }


    public void  seekbarUpdate(SeekBar seekbarr , final TextView Text , final String TextViewCovered , int MyProgress){
        Text.setText(TextViewCovered + MyProgress + "/" + seekbarr.getMax());
        loadPrefs();
        seekbarr.setOnSeekBarChangeListener(

                new SeekBar.OnSeekBarChangeListener(){
                    int progress_value;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progress_value = progress;
                        Text.setText(TextViewCovered + progress + "/" + seekBar.getMax());
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        Text.setText(TextViewCovered + progress_value + "/" + seekBar.getMax());
                    }
                }
        );

    }


    //Number_Picker Function

    public void numberPickerr() {
        String[] Max_mins = new String[2];
        Light_np[0] = (NumberPicker) findViewById(R.id.LightMaxNumberPicker);
        Light_np[1] = (NumberPicker) findViewById(R.id.LightMinNumberPicker);
        TextViewLight[0] = (TextView) findViewById(R.id.LightTextViewMax);
        TextViewLight[1] = (TextView) findViewById(R.id.LightTextViewMin);
        Max_mins[0] ="Selected MAX threshold for light is: ";
        Max_mins[1] ="Selected MIN threshold for light is: ";

        for (int i=0; i<2; i++) {
            numberPickerUpdate(Light_np[i] ,TextViewLight[i] , Max_mins[i] );

        }

    }

    public void numberPickerUpdate(NumberPicker Light_np , final TextView TextViewLight , final String Label) {
            Light_np.setMaxValue(10000);
            Light_np.setMinValue(0);
            Light_np.setWrapSelectorWheel(true);
            Light_np.setOnValueChangedListener(new NumberPicker.
                    OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int
                        oldVal, int newVal) {
                    TextViewLight.setText(Label + newVal);
                }

            });

    }

}

