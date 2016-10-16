package com.example.aggel.accelerometerapplication;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.hardware.SensorManager;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class MainActivity extends AppCompatActivity {

    private static SeekBar seek_bar;
    private static TextView text_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create our Sensor Manager
        SensorManager SM = (SensorManager)getSystemService(SENSOR_SERVICE);

        //Assign TextView
        TextView[] textTable = new TextView[3];
        textTable[0] = (TextView)findViewById(R.id.xText);
        textTable[1] = (TextView)findViewById(R.id.yText);
        textTable[2] = (TextView)findViewById(R.id.zText);

        //Accelerometer Sensor
        AccelerometerEventListener accelero = new AccelerometerEventListener(SM, textTable);

        //Proximity Sensor
        TextView proxText = (TextView) findViewById(R.id.proxText);
        Context context = getApplicationContext();

        final ProximityEventListener proxy = new ProximityEventListener(SM, proxText,context);

        //Sound Button
        Button button  = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            //On click function
            public void onClick(View view) {

                proxy.playSound();
            }
        });

        //Seekbar
        seekbarr();

        //Light Sensor

        TextView sensText = (TextView) findViewById(R.id.sensText);
        System.out.println("axneeeeeeeeeeeeeeeeee");
        LightEventListener lightsens = new LightEventListener(SM, sensText);
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Exit");
        ad.setMessage("Are you sure that you to EXIT this App?");
        ad.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        ad.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ad.show();
    }
}
