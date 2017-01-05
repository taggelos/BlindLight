package com.example.aggel.blindlight.Listeners;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

import com.example.aggel.blindlight.Activities.SettingsActivity;
import com.example.aggel.blindlight.util.SoundEvent;

/**
 * Created by aggel on 15/10/2016.
 */

public class LightEventListener extends SettingsActivity implements SensorEventListener {

    private TextView textTable;
    public int threshold_max_light;
    public int threshold_min_light;
    private SoundEvent se;
    private Handler lhandler;
    private int soundId;
    public String sensor_name;
    public String sensor_value;

    private int streamId;

    private Context context;

    public LightEventListener(SensorManager SM,  TextView textTable , int threshold_max_light , int threshold_min_light ,  Context context) {
        //Light Sensor
        Sensor mySensor = SM.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensor_name = mySensor.getName().replaceAll("\\s+" , "");


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
            sensor_value = Float.toString(event.values[0]);


            if (event.values[0] > threshold_max_light ) {
                CharSequence text = "Too much light around you!!";
                final Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 1500);
            }
            if (event.values[0] < threshold_min_light ) {
                CharSequence text = "Little light around you!!";
                final Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 1500);
            }
        }
    }


    public void unregister(SensorManager SM){
        SM.unregisterListener(this);
    }

    public String getSensorName() {
        return sensor_name;
    }

    public String getSensorValue() {
        return sensor_value;
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Not in use
    }

}
