package com.example.aggel.accelerometerapplication;


import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int threshold_x_axis;
    private int threshold_y_axis;
    private int threshold_z_axis;
    private SettingsActivity SA;


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
        SA = new SettingsActivity();

        threshold_x_axis = SA.getSeekBarPr_X();
        threshold_y_axis = SA.getSeekBarPr_Y();
        threshold_z_axis = SA.getSeekBarPr_Z();

        Context context = getApplicationContext();

        //Accelerometer Sensor
        AccelerometerEventListener accelero = new AccelerometerEventListener(SM, textTable , threshold_x_axis , threshold_y_axis, threshold_z_axis ,context);

        //Proximity Sensor
        TextView proxText = (TextView) findViewById(R.id.proxText);

        final ProximityEventListener proxy = new ProximityEventListener(SM, proxText,context);

        //Light Sensor
        TextView sensText = (TextView) findViewById(R.id.sensText);
        LightEventListener lightsens = new LightEventListener(SM, sensText);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }



    //Creating Menu

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
                //onBackPressed();
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);

    }


}