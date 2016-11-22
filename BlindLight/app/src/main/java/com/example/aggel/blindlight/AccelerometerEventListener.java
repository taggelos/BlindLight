package com.example.aggel.blindlight;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by aggel on 15/10/2016.
 */

public class AccelerometerEventListener extends SettingsActivity implements SensorEventListener {

    private TextView[] textTable;
    public int threshold_x_axis;
    public int threshold_y_axis;
    public int threshold_z_axis;
    private Context context;
    private Handler handler;
    private double[] gravity = new double[3];
    private double[] linear_acceleration  = new double[3];
    private SoundEvent se;
    private int soundId;
    private int streamId;





    public AccelerometerEventListener(SensorManager SM, TextView[] textTable ,  Context context ) {
        //Accelerometer Sensor

        Sensor mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);



        //Register sensor listener
        SM.registerListener(this,mySensor,SensorManager.SENSOR_DELAY_NORMAL);

        //mysound
        se = new SoundEvent();
        soundId = se.getSoundId(context);

        //Assign TextView
        this.textTable = textTable;
        //Assign thresholds

        //this.threshold_x_axis = threshold_x_axis;
        //this.threshold_y_axis = threshold_y_axis;
        //this.threshold_z_axis = threshold_z_axis;
        this.context = context;

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        final double alpha = 0.8;

        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];

        textTable[0].setText("X: " + linear_acceleration[0]);
        textTable[1].setText("Y: " + linear_acceleration[1]);
        textTable[2].setText("Z: " + linear_acceleration[2]);

        int max = 0;
        for (int i = 0; i < event.values.length; i++) {
            textTable[i].setTextColor(Color.BLACK);
            if (event.values[i] > event.values[max])
                max = i;
        }
        textTable[max].setTextColor(Color.BLUE);


        threshold_x_axis=this.My_Progress[0];
        threshold_y_axis=this.My_Progress[1];
        threshold_z_axis=this.My_Progress[2];

        System.out.println(threshold_x_axis);

        if ((linear_acceleration[0] > threshold_x_axis) || (linear_acceleration[1] > threshold_y_axis) || (linear_acceleration[2] > threshold_z_axis)){
            CharSequence text = "Be carefull: You're moving too fast!!";
            final Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.cancel();
                }
            }, 1500);
            //streamId = se.playNonStop(soundId);
            //return;

        }
        //se.stopSound(streamId);

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Not in use
    }
}
