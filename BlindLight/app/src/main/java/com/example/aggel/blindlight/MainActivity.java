package com.example.aggel.blindlight;


import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.aggel.accelerometerapplication.R;

public class MainActivity extends AppCompatActivity {

    private boolean CheckProx;

    //Thresholds

    private int threshold_x_axis;
    private int threshold_y_axis;
    private int threshold_z_axis;
    private int threshold_max_light;
    private int threshold_min_light;


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

        //Ιnitialization of thresholds from seekbars->settings

        Intent toy2 = getIntent();
        threshold_x_axis = toy2.getIntExtra("intVariableName1", 3);
        threshold_y_axis = toy2.getIntExtra("intVariableName2", 7);
        threshold_z_axis = toy2.getIntExtra("intVariableName3", 8);
        threshold_max_light = toy2.getIntExtra("intVariableName4", 25);
        threshold_min_light = toy2.getIntExtra("intVariableName5", 5);
        CheckProx = toy2.getBooleanExtra("intVariableName6" , true);

        Context context = getApplicationContext();

        //Accelerometer Sensor
        AccelerometerEventListener accelero = new AccelerometerEventListener(SM,  textTable , threshold_x_axis , threshold_y_axis, threshold_z_axis ,context);

        //Proximity Sensor
        TextView proxText = (TextView) findViewById(R.id.proxText);

        final ProximityEventListener proxy = new ProximityEventListener(SM, CheckProx , proxText,context);

        //Light Sensor
        TextView sensText = (TextView) findViewById(R.id.sensText);
        LightEventListener lightsens = new LightEventListener(SM, sensText , threshold_max_light , threshold_min_light
                , context);


    }


    //Creating Options_menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.menu_AndroidSettings:
                Intent toy = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(toy);
                break;
            case R.id.menu_Exit:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);

    }


}
