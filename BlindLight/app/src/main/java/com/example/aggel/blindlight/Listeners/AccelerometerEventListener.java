package com.example.aggel.blindlight.Listeners;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

import com.example.aggel.blindlight.Activities.SettingsActivity;
import com.example.aggel.blindlight.util.MyAsyncTask;
import com.example.aggel.blindlight.util.SoundEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.aggel.blindlight.Activities.MainActivity.macAddress;
import static com.example.aggel.blindlight.Activities.MainActivity.offine_mode;
import static com.example.aggel.blindlight.Activities.MainActivity.Port_Ip;
//import static com.example.aggel.blindlight.Activities.MainActivity.date;
import static com.example.aggel.blindlight.Activities.MainActivity.locationListener;

/**
 * Created by aggel on 15/10/2016.
 */

public class AccelerometerEventListener extends SettingsActivity implements SensorEventListener {

    private TextView[] textTable;
    public int threshold_x_axis;
    public int threshold_y_axis;
    public int threshold_z_axis;
    public int threshold_frequency;
    private Context context;
    private Handler handlerFrequency;
    private double[] gravity = new double[3];
    private double[] linear_acceleration  = new double[3];
    private SoundEvent se;
    public String sensor_name;
    public String  sensor_values="";
    private int soundId;
    private int streamId;
    public MyAsyncTask tt;
    private String date;





    public AccelerometerEventListener(SensorManager SM,int threshold_frequency, int threshold_x_axis , int threshold_y_axis , int threshold_z_axis , TextView[] textTable ,  Context context ) {
        //Accelerometer Sensor

        Sensor mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensor_name = ("Accelerometer Sensor");



        //Register sensor listener
        SM.registerListener(this,mySensor,SensorManager.SENSOR_DELAY_NORMAL);

        //mysound
        se = new SoundEvent();
        soundId = se.getSoundId(context);

        //Assign TextView
        this.textTable = textTable;
        //Assign thresholds

        this.threshold_x_axis = threshold_x_axis;
        this.threshold_y_axis = threshold_y_axis;
        this.threshold_z_axis = threshold_z_axis;
        this.threshold_frequency = threshold_frequency;
        this.context = context;

    }

    private Handler mHandler = new Handler();
    Runnable run=new Runnable() {

        @Override
        public void run() {

            textTable[0].setText("X: " + linear_acceleration[0]);
            textTable[1].setText("Y: " + linear_acceleration[1]);
            textTable[2].setText("Z: " + linear_acceleration[2]);

            //Kill runnable before a new one starts
            mHandler.removeCallbacks(run);

        }
    };


    @Override
    public void onSensorChanged(final SensorEvent event) {
        final int freq=threshold_frequency*1000;

        final double alpha = 0.8;
        sensor_values="";
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];

        sensor_values=sensor_values+Float.toString(event.values[0])+","+Float.toString(event.values[1])+","+Float.toString(event.values[2]);

        //---------------Calling Async Task Function---------------

        if(offine_mode ==false){
            Calendar c = Calendar.getInstance();
            int seconds = c.get(Calendar.SECOND);
            SimpleDateFormat format = new SimpleDateFormat("EEEE, MMMM d, yyyy 'at' h:mm:ss a");
            date = format.format(Calendar.getInstance().getTimeInMillis());
            System.out.println(date);
            //System.out.println(seconds);
            //final String c = sensor_value;
            String topic = macAddress + "/" + getSensorName() + "/" + getSensorValue() + "/" + date + "/" + locationListener.getDevLatitude() + "/" + locationListener.getDevLongtitude();
            tt = new MyAsyncTask(topic, Port_Ip);
            tt.execute();

        }

        else {
            int max = 0;
            for (int i = 0; i < event.values.length; i++) {
                textTable[i].setTextColor(Color.BLACK);
                if (event.values[i] > event.values[max])
                    max = i;
            }
            textTable[max].setTextColor(Color.BLUE);


            if ((linear_acceleration[0] > threshold_x_axis) || (linear_acceleration[1] > threshold_y_axis) || (linear_acceleration[2] > threshold_z_axis)) {
                CharSequence text = "Be carefull: You're moving too fast!!";
                final Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                toast.show();
                Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 1500);
                mHandler.postDelayed(run, freq);
                //streamId = se.playNonStop(soundId);
                //return;

            }
        }



    }


    public void unregister(SensorManager SM){
        SM.unregisterListener(this);
    }

    public String getSensorName(){
        return sensor_name;
    }

    public String getSensorValue() {

        return sensor_values;

    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Not in use
    }
}
