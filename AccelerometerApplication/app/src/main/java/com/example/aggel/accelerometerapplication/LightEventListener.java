package com.example.aggel.accelerometerapplication;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by aggel on 15/10/2016.
 */

public class LightEventListener implements SensorEventListener {

    private TextView textTable;
    private int threshold_max_light;
    private int threshold_min_light;
    private SoundEvent se;
    private int soundId;
    private int streamId;

    private Context context;

    public LightEventListener(SensorManager SM, TextView textTable , int threshold_max_light ,int threshold_min_light ,Context context) {
        //Light Sensor
        Sensor mySensor = SM.getDefaultSensor(Sensor.TYPE_LIGHT);

        //Register sensor listener
        SM.registerListener(this,mySensor,SensorManager.SENSOR_DELAY_NORMAL);

        se = new SoundEvent();
        soundId = se.getSoundId(context);

        //Assign TextView
        this.threshold_max_light = threshold_max_light;
        this.threshold_min_light = threshold_min_light;
        this.textTable = textTable;
        this.context=context;

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if( event.sensor.getType() == Sensor.TYPE_LIGHT)
        {
            textTable.setText(String.valueOf(event.values[0]));
            if (event.values[0] > threshold_max_light ) {
                CharSequence text = "Too much light around you!!";
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                toast.show();
            }
            if (event.values[0]<threshold_min_light ) {
                CharSequence text = "Little light around you!!";
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Not in use
    }

}
