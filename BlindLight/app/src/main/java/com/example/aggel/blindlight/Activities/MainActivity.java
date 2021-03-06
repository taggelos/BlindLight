package com.example.aggel.blindlight.Activities;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.example.aggel.blindlight.Listeners.MyLocationListener;


import com.example.aggel.blindlight.util.NetworkStateReceiver;
import com.example.aggel.blindlight.Listeners.AccelerometerEventListener;
import com.example.aggel.blindlight.Listeners.LightEventListener;
import com.example.aggel.blindlight.Listeners.ProximityEventListener;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {

    private boolean CheckProx;
    public static Boolean  broker_run_flag = false;
    public static Boolean flag_for_switch = false;



    public static String Port_Ip="tcp://192.168.1.2:1883"; //by default

    public static String macAddress;


    //offline-online mode
    private NetworkStateReceiver networkStateReceiver;
    private MenuItem item;
    private MenuItem item2;
    public static boolean offline_mode=true;
    private Switch connectivity_Mode;

    //Location
    private LocationManager locationManager;
    public static MyLocationListener locationListener;

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
        macAddress=getMacAddr();
    }


    Intent intent1;
    @Override
    protected void onResume() {
        super.onResume();
        //----------------Listener for the GPS Location-----------------------
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
                }, 10);
            }

            return;
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);


        //-------------------Listener for Internet Connectivity-------------------
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
        //offline_mode=true;

        //--------------Create our Sensor Manager----------------
        SM = (SensorManager) getSystemService(SENSOR_SERVICE);


        //-----------------------Internet Connection------------------------
        connectivity_Mode = (Switch) findViewById(R.id.connectivity);

        connectivity_Mode.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {


                if (isChecked) {
                    offline_mode = false;
                    invalidateOptionsMenu();
                    connectivity_Mode.setEnabled(true);


                } else {
                    offline_mode = true;
                    invalidateOptionsMenu();
                    connectivity_Mode.setChecked(false);
                    connectivity_Mode.setEnabled(true);

                }


            }
        });


        //-------------------------CAMERA----------------------------

        Switch camera_Mode = (Switch) findViewById(R.id.camera);
        camera_Mode.setChecked(false);
        camera_Mode.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked ) {
                    flag_for_switch=true;

                    //Intent intent = new Intent(MainActivity.this, GoogleActivity.class);
                    //startActivity(intent);

                } else {
                    flag_for_switch=false;
                    System.out.println("ELSE CASE");
                }

            }
        });







        //-----------Assign TextView-----------
        TextView[] textTable = new TextView[3];
        textTable[0] = (TextView) findViewById(R.id.xText);
        textTable[1] = (TextView) findViewById(R.id.yText);
        textTable[2] = (TextView) findViewById(R.id.zText);

        //--------------Initialization of thresholds from seekbars->settings-------------

        Intent toy2 = getIntent();
        threshold_x_axis = toy2.getIntExtra("intVariableName1", 3);
        threshold_y_axis = toy2.getIntExtra("intVariableName2", 7);
        threshold_z_axis = toy2.getIntExtra("intVariableName3", 8);
        threshold_frequency = toy2.getIntExtra("intVariableName7", 1);
        threshold_max_light = toy2.getIntExtra("intVariableName4", 900);
        threshold_min_light = toy2.getIntExtra("intVariableName5", 1);
        CheckProx = toy2.getBooleanExtra("intVariableName6", true);


        Context context = getApplicationContext();

        //-----------------Accelerometer Sensor-----------------
        accelero = new AccelerometerEventListener(SM, threshold_frequency, threshold_x_axis, threshold_y_axis, threshold_z_axis, textTable, context);

        //-------------------Proximity Sensor-----------------
        TextView proxText = (TextView) findViewById(R.id.proxText);

        proxy = new ProximityEventListener(SM, CheckProx, proxText, context);

        //-------------------Light Sensor----------------------
        TextView sensText = (TextView) findViewById(R.id.sensText);
        lightsens = new LightEventListener(SM, sensText, threshold_max_light, threshold_min_light, context);


    }



    @Override
    protected void onPause() {
        super.onPause();
        accelero.unregister(SM);
        proxy.unregister(SM);
        lightsens.unregister(SM);
        unregisterReceiver(networkStateReceiver);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
                }, 10);
            }

            return;
        }
        locationManager.removeUpdates(locationListener);


    }




    //----------------Creating Options_menu--------------

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
        item2 = menu.findItem(R.id.menu_mqtt_settings);
        item.setEnabled(offline_mode);
        item2.setEnabled(!offline_mode);
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
                    break;
            case R.id.menu_mqtt_settings:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Connection Settings");
                alertDialog.setMessage("Please enter Ip/PortQ");

                final EditText input = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setText("tcp://192.168.1.2:1883");
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setNeutralButton("Save", new DialogInterface.OnClickListener() {

                    // click listener on the alert box
                    public void onClick(DialogInterface dialog, int which) {
                        Port_Ip = input.getText().toString();
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



    //--------------This function is used in order to find the mac address of the device--------------
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
                    res1.append(String.format("%02X:", b));
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



    //-----------This function is used in order to Find out if the GPS of an Android device is enabled------------
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    //-----------------Network state---------------

    @Override
    public void networkAvailable() {
        Context  context = getApplicationContext();
        CharSequence text = "Mode: ONLINE";
        final Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
        offline_mode = false;
        invalidateOptionsMenu();
        connectivity_Mode = (Switch) findViewById(R.id.connectivity);
        connectivity_Mode.setEnabled(true);
        connectivity_Mode.setChecked(true);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            CharSequence text2 = "GPS : ENABLED";
            Toast.makeText(context, text2, Toast.LENGTH_SHORT).show();

        } else {
            buildAlertMessageNoGps();

        }


    }

    @Override
    public void networkUnavailable() {
        Context context = getApplicationContext();
        CharSequence text = "Mode: OFFLINE";
        final Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
        offline_mode =true;
        invalidateOptionsMenu(); //  we call this function to update options_menu
        connectivity_Mode = (Switch) findViewById(R.id.connectivity);
        connectivity_Mode.setChecked(false);
        connectivity_Mode.setEnabled(false);
    }


}

