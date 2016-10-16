package com.example.aggel.accelerometerapplication;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.hardware.SensorManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

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

        final ProximityEventListener proxy = new ProximityEventListener(SM, proxText,context);

        Button button  = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            //On click function
            public void onClick(View view) {

                proxy.playSound();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Exit");
        ad.setMessage("Are you sure that you to EXIT this App?");
        ad.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        ad.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ad.show();
    }
}
