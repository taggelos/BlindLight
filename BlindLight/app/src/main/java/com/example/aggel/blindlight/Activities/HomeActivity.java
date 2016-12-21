package com.example.aggel.blindlight.Activities;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.aggel.accelerometerapplication.R;

public class HomeActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        //Go to Next Screen
        Button startbtn;
        startbtn = (Button) findViewById(R.id.startBtn);
        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toy = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(toy);
            }
        });

        //Show Info
        final Button infobtn;
        infobtn = (Button) findViewById(R.id.infoBtn);
        infobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView starttxt = (TextView) findViewById(R.id.startText);
                starttxt.setVisibility(View.VISIBLE);
                infobtn.setEnabled(false);
                //infobtn.setVisibility(View.INVISIBLE);
                //starttxt.setVisibility((starttxt.getVisibility() == View.VISIBLE)
                 //       ? View.GONE : View.VISIBLE);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();


    }

}
