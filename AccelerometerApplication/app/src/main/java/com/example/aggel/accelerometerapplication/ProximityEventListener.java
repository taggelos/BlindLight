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

    private SoundPool mySound;
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
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){

            AudioAttributes aa = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();

            mySound = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .setAudioAttributes(aa)
                    .build();

        }
        else {
            mySound = new SoundPool(10, AudioManager.STREAM_ALARM,1);
        }
        soundId = mySound.load(context,R.raw.kimsound,1);

    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        textTable.setText(String.valueOf(event.values[0]));
        if (event.values[0]==0){

            CharSequence text = "Hello toast!";
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();
            streamId= mySound.play(soundId,.25f,.25f,1,-1,1);
            return;
        }
        mySound.stop(soundId);
    }

    public void playSound(){

        mySound.play(soundId,.25f,.25f,1,0,1);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Not in use
    }
}
