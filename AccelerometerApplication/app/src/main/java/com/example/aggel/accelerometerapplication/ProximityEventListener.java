package com.example.aggel.accelerometerapplication;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by aggel on 15/10/2016.
 */

public class ProximityEventListener implements SensorEventListener {

    private TextView textTable;

    private Context context;

    private SoundEvent se;
    private int soundId;
    private int streamId;

    public ProximityEventListener(SensorManager SM, TextView textTable, Context context) {
        //Accelerometer Sensor
        Sensor mySensor = SM.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        //Register sensor listener
        SM.registerListener(this,mySensor,SensorManager.SENSOR_DELAY_NORMAL);

        //Assign TextView
        this.textTable = textTable;
        this.context = context;

        //mysound
        se = new SoundEvent();
        soundId = se.getSoundId(context);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        textTable.setText(String.valueOf(event.values[0]));
        if (event.values[0]==0){

            CharSequence text = "Βe carefull!!";
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();
            streamId = se.playNonStop(soundId);
            return;
        }
        se.stopSound(streamId);
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Not in use
    }
}