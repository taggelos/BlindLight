package com.example.aggel.blindlight;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.example.aggel.accelerometerapplication.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Go to Second Screen
        Button StartButton;
        StartButton = (Button) findViewById(R.id.button2);
        StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toy1 = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(toy1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Exit");
        ad.setMessage("Are you sure that you want to EXIT this App?");
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
