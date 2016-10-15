package com.example.aggel.accelerometerapplication;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

/**
 * Created by aggel on 15/10/2016.
 */

public class ProximityEventListener implements SensorEventListener {

    private TextView textTable;

    public ProximityEventListener(SensorManager SM, TextView textTable) {
        //Accelerometer Sensor
        Sensor mySensor = SM.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        //Register sensor listener
        SM.registerListener(this,mySensor,SensorManager.SENSOR_DELAY_NORMAL);

        //Assign TextView
        this.textTable = textTable;

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        textTable.setText(String.valueOf(event.values[0]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Not in use
    }
}
