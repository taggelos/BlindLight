package com.example.aggel.accelerometerapplication;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.hardware.SensorManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create our Sensor Manager
        SensorManager SM = (SensorManager)getSystemService(SENSOR_SERVICE);

        //Assign TextView
        TextView[] textTable = new TextView[3];
        textTable[0] = (TextView)findViewById(R.id.xText);
        textTable[1] = (TextView)findViewById(R.id.yText);
        textTable[2] = (TextView)findViewById(R.id.zText);

        AccelerometerEventListener accelero = new AccelerometerEventListener(SM, textTable);

        TextView proxText = (TextView) findViewById(R.id.proxText);
        Context context = getApplicationContext();

        ProximityEventListener proxy = new ProximityEventListener(SM, proxText,context);



    }
}
