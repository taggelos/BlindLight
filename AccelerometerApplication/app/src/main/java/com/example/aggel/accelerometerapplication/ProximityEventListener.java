package com.example.aggel.accelerometerapplication;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by aggel on 15/10/2016.
 */

public class ProximityEventListener implements SensorEventListener {

    private TextView textTable;

    private Context context;

    public ProximityEventListener(SensorManager SM, TextView textTable, Context context) {
        //Accelerometer Sensor
        Sensor mySensor = SM.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        //Register sensor listener
        SM.registerListener(this,mySensor,SensorManager.SENSOR_DELAY_NORMAL);

        //Assign TextView
        this.textTable = textTable;
        this.context = context;

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        textTable.setText(String.valueOf(event.values[0]));
        if (event.values[0]==0){

            CharSequence text = "Hello toast!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(this.context, text, duration);
            toast.show();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Not in use
    }
}
