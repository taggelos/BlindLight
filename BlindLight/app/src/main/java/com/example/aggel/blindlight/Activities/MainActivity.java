package com.example.aggel.blindlight.Activities;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;
import android.content.IntentFilter;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.example.aggel.accelerometerapplication.R;
import com.example.aggel.blindlight.util.MqttPublisher;
import com.example.aggel.blindlight.util.MqttSubscriber;
import com.example.aggel.blindlight.util.NetworkStateReceiver;
import com.example.aggel.blindlight.Sensors.AccelerometerEventListener;
import com.example.aggel.blindlight.Sensors.LightEventListener;
import com.example.aggel.blindlight.Sensors.ProximityEventListener;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {

    private boolean CheckProx;

    //Mqtt Broker Client
    private MqttSubscriber subscriber;
    private MqttPublisher publisher;
    public static String Port;


    //offline-online mode
    private NetworkStateReceiver networkStateReceiver;
    private MenuItem item;
    private boolean online_mode;
    private Switch connectivity_Mode;

    //Thresholds

    private int threshold_x_axis;
    private int threshold_y_axis;
    private int threshold_z_axis;
    private int threshold_frequency;
    private int threshold_max_light;
    private int threshold_min_light;
    private AccelerometerEventListener accelero;
    private ProximityEventListener proxy;
    private LightEventListener lightsens;
    private SensorManager SM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting the MAC of the device
        String macAddress = getMacAddr();
        System.out.println("---------------------------");
        System.out.println("MAC ADDRESS IS : "+macAddress);
        System.out.println("---------------------------");

    }

    //This function is used in order to find the mac address of the device
    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00"; // <Android 6.0.
    }

    protected String getIpAddress () {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("IP Address", ex.toString());
        }
        return null;
    }

    @Override
    protected void onResume(){
        super.onResume();


        //Listener for Internet Connectivity
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));


        //Create our Sensor Manager
        SM = (SensorManager)getSystemService(SENSOR_SERVICE);

        //Internet Connection

        connectivity_Mode = (Switch) findViewById(R.id.connectivity);

        connectivity_Mode.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    online_mode=false;
                    invalidateOptionsMenu();
                    connectivity_Mode.setEnabled(true);

                }else{
                    online_mode=true;
                    invalidateOptionsMenu();
                    connectivity_Mode.setChecked(false);
                    connectivity_Mode.setEnabled(true);
                }


            }
        });

        //Assign TextView
        TextView[] textTable = new TextView[3];
        textTable[0] = (TextView)findViewById(R.id.xText);
        textTable[1] = (TextView)findViewById(R.id.yText);
        textTable[2] = (TextView)findViewById(R.id.zText);

        //Initialization of thresholds from seekbars->settings

        Intent toy2 = getIntent();
        threshold_x_axis = toy2.getIntExtra("intVariableName1", 3);
        threshold_y_axis = toy2.getIntExtra("intVariableName2", 7);
        threshold_z_axis = toy2.getIntExtra("intVariableName3", 8);
        threshold_frequency = toy2.getIntExtra("intVariableName7", 1);
        threshold_max_light = toy2.getIntExtra("intVariableName4", 900);
        threshold_min_light = toy2.getIntExtra("intVariableName5", 1);
        CheckProx = toy2.getBooleanExtra("intVariableName6" , true);

        Context context = getApplicationContext();

        //Accelerometer Sensor
        accelero = new AccelerometerEventListener(SM,threshold_frequency , threshold_x_axis ,threshold_y_axis ,threshold_z_axis , textTable , context);

        //Proximity Sensor
        TextView proxText = (TextView) findViewById(R.id.proxText);

        proxy = new ProximityEventListener(SM, CheckProx, proxText,context);

        //Light Sensor
        TextView sensText = (TextView) findViewById(R.id.sensText);
        lightsens = new LightEventListener(SM, sensText ,threshold_max_light, threshold_min_light , context);


    }


    @Override
    protected void onPause(){
        super.onPause();
        accelero.unregister(SM);
        proxy.unregister(SM);
        lightsens.unregister(SM);
        unregisterReceiver(networkStateReceiver);
    }


    //Creating Options_menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);
        connectivity_Mode = (Switch) findViewById(R.id.connectivity);
        item = menu.findItem(R.id.menu_AndroidSettings);

            item.setEnabled(online_mode);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.menu_AndroidSettings:
                    Intent toy1 = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(toy1);
                    finish();
            case R.id.menu_mqtt_settings:
                    //Intent toy2 = new Intent(MainActivity.this, MqttSetings.class);
                    //startActivity(toy2);
                    //finish();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Port");
                alertDialog.setMessage("Enter Port");


                final EditText input = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setNeutralButton("Save", new DialogInterface.OnClickListener() {

                    // click listener on the alert box
                    public void onClick(DialogInterface dialog, int which) {
                        Port = input.getText().toString();
                        System.out.println("---------------------------");
                        System.out.println( " PORT :"+Port);
                        System.out.println("---------------------------");
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
                break;
            case R.id.menu_Exit:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle(getResources().getString(R.string.exitDB));
        ad.setMessage(getResources().getString(R.string.questionDB));
        ad.setPositiveButton(getResources().getString(R.string.yesDB), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        ad.setNegativeButton(getResources().getString(R.string.noDB), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ad.show();
    }


    //Network state

    @Override
    public void networkAvailable() {
        Context context = getApplicationContext();
        CharSequence text = "Mode: ONLINE";
        final Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
        online_mode = false;
        invalidateOptionsMenu();
        connectivity_Mode = (Switch) findViewById(R.id.connectivity);
        connectivity_Mode.setEnabled(true);
        connectivity_Mode.setChecked(true);
    }

    @Override
    public void networkUnavailable() {
        Context context = getApplicationContext();
        CharSequence text = "Mode: OFFLINE";
        final Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
        online_mode=true;
        invalidateOptionsMenu(); //  we call this function to update options_menu
        connectivity_Mode = (Switch) findViewById(R.id.connectivity);
        connectivity_Mode.setChecked(false);
        connectivity_Mode.setEnabled(false);
    }
}
