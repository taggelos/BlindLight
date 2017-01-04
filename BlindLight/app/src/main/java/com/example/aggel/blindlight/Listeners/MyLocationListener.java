package com.example.aggel.blindlight.Listeners;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import static android.content.ContentValues.TAG;


public class MyLocationListener implements LocationListener {

    private Context context;

    public MyLocationListener(Context context) {
        this.context= context;
    }

    @Override
    public void onLocationChanged(Location loc ) {

        //if ( loc != null)

        CharSequence text = "I AM HEREEE LOCATION LISTENER";
        final Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();

        Toast.makeText(context,
                "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                        + loc.getLongitude(), Toast.LENGTH_SHORT).show();
        String longitude = "Longitude: " + loc.getLongitude();
        Log.v(TAG, longitude);
        String latitude = "Latitude: " + loc.getLatitude();
        Log.v(TAG, latitude);


    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(context, "Gps is turned off!! ",
                Toast.LENGTH_SHORT).show();
        //context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        //Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        //context.startActivity(intent);

    }

    @Override
    public void onProviderEnabled(String provider) {

        Toast.makeText(context, "Gps is turned on!! ",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }



}
