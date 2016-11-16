package com.example.aggel.blindlight;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aggel.accelerometerapplication.R;

/**
 * Created by aggel on 15/10/2016.
 */

public class SoundEvent  {

    private SoundPool mySound;
    private int streamId;

    public SoundEvent(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){

            AudioAttributes aa = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();

            mySound = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .setAudioAttributes(aa)
                    .build();

        }
        else {
            mySound = new SoundPool(10, AudioManager.STREAM_ALARM,1);
        }
    }

    public int getSoundId(Context context) {
        return mySound.load(context, R.raw.kimsound,1);
    }

    public int playNonStop(int soundId) {
        streamId= mySound.play(soundId,.25f,.25f,1,-1,1);
        return streamId;
    }

    public void stopSound(int streamId) {
        mySound.stop(streamId);
    }

    public void playOnce(int soundId) {
        mySound.play(soundId,.25f,.25f,1,0,1);
    }
}
