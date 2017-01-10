package com.example.aggel.blindlight.util;

/**
 * Created by Maria on 07/01/2017.
 */

import android.os.AsyncTask;
import com.example.aggel.blindlight.util.MqttPublisher;
import com.example.aggel.blindlight.util.MqttSubscriber;


import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import java.util.Comparator;


public class MyAsyncTask extends AsyncTask<Void ,Void , Void> {

    private String topic;
    private final Comparator<Float> comparator;
    private String ip_port;
    private MqttSubscriber subscriber;
    private MqttPublisher publisher;

    public MyAsyncTask(String topic, String ip_port) {
        this.topic=topic;
        this.ip_port=ip_port;
        this.comparator = new Comparator<Float>() {
            @Override
            public int compare(Float lhs, Float rhs) {
                return lhs.compareTo(rhs);
            }
        };

    }
    @Override
    protected Void doInBackground(Void... params) {
        //final String value = params[0];

        //subscriber = new MqttSubscriber();
        publisher = new MqttPublisher();

        try {
            int time = 1000;
            // Sleeping for given time period
            Thread.sleep(time);
            //subscriber = new MqttSubscriber();
            //subscriber.main("#" ,ip_port);
            //publisher = new MqttPublisher();
            publisher.main(topic , ip_port);
            // Do your long operations here and return the result



        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}