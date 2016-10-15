package com.example.aggel.accelerometerapplication;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

/**
 * Created by aggel on 15/10/2016.
 */

public class AccelerometerEventListener implements SensorEventListener {

    private TextView[] textTable;

    public AccelerometerEventListener(SensorManager SM, TextView[] textTable) {
        //Accelerometer Sensor
        Sensor mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //Register sensor listener
        SM.registerListener(this,mySensor,SensorManager.SENSOR_DELAY_NORMAL);

        //Assign TextView
        this.textTable = textTable;

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        textTable[0].setText("X: " + event.values[0]);
        textTable[1].setText("Y: " + event.values[1]);
        textTable[2].setText("Z: " + event.values[2]);

        int max = 0;
        for (int i = 0; i < event.values.length; i++) {
            textTable[i].setTextColor(Color.BLACK);
            if (event.values[i] > event.values[max])
                max = i;
        }

        textTable[max].setTextColor(Color.BLUE);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Not in use
    }
}
