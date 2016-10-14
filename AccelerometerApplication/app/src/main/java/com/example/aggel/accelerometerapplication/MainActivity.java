package com.example.aggel.accelerometerapplication;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView xText, yText, zText;

    private Sensor mySensor;

    private SensorManager SM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create our Sensor Manager
        SM = (SensorManager)getSystemService(SENSOR_SERVICE);

        //Accelerometer Sensor
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //Register sensor listener

        SM.registerListener(this,mySensor,SensorManager.SENSOR_DELAY_NORMAL);


        //Assign TextView
        xText = (TextView)findViewById(R.id.xText);
        yText = (TextView)findViewById(R.id.yText);
        zText = (TextView)findViewById(R.id.zText);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        xText.setText("X: " + event.values[0]);
        yText.setText("Y: " + event.values[1]);
        zText.setText("Z: " + event.values[2]);

        float max = 0;
        for (int i = 0; i < event.values.length; i++) {
                if (max == 0 || event.values[i] > max)
                    max = event.values[i];
        }
        if(max == event.values[0]) {
            xText.setTextColor(Color.parseColor("#4dbf42"));
            yText.setTextColor(Color.parseColor("#383232"));
            zText.setTextColor(Color.parseColor("#383232"));
        }
        if(max == event.values[1]) {
            xText.setTextColor(Color.parseColor("#383232"));
            yText.setTextColor(Color.parseColor("#4dbf42"));
            zText.setTextColor(Color.parseColor("#383232"));
        }
        if(max == event.values[2]) {
            xText.setTextColor(Color.parseColor("#383232"));
            yText.setTextColor(Color.parseColor("#383232"));
            zText.setTextColor(Color.parseColor("#4dbf42"));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Not in use
    }
}
