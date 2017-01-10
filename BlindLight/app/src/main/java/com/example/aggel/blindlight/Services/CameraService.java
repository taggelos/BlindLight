package com.example.aggel.blindlight.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * Created by aggel on 9/1/2017.
 */

public class CameraService extends Service implements TextToSpeech.OnInitListener  {


    private TextToSpeech tts;

    @Override
    public void onCreate() {
        super.onCreate();

    }


    @Override
    public void onInit(int status) {
        Log.v(TAG, "oninit");
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.v(TAG, "Language is not available.");
            } else {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tts.speak("Obstacle ahead ",TextToSpeech.QUEUE_FLUSH,null,null);
                } else {
                    tts.speak("Obstacle ahead ", TextToSpeech.QUEUE_FLUSH, null);
                }

            }
        } else {
            Log.v(TAG, "Could not initialize TextToSpeech.");
        }
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this,"Service Started...",Toast.LENGTH_LONG).show();
        String text = "ASJKDHAS";
        if(text.isEmpty()){
            Toast.makeText(this, "Text is empty", Toast.LENGTH_SHORT).show();

        }

        //startService(new Intent(this, PhotoService.class));
        System.out.println("sdghaskjh");

        /*Intent dialogIntent = new Intent(this, GoogleService.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dialogIntent);*/

        tts = new TextToSpeech(this,this);
        //tts.setSpeechRate(0.5f);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"Service STOPPED...",Toast.LENGTH_LONG).show();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }

        //stopForeground(true);
        //stopSelf();
        Toast.makeText(this,"Service ooooooooooooooooooSTOPPED...",Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
