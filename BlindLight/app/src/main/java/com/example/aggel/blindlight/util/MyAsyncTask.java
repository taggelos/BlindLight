package com.example.aggel.blindlight.util;

/**
 * Created by Maria on 07/01/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.aggel.blindlight.Services.MySubService;
import com.example.aggel.blindlight.util.MqttPublisher;
import com.example.aggel.blindlight.util.MqttSubscriber;


import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import static com.example.aggel.blindlight.Activities.MainActivity.broker_run_flag;
import static com.example.aggel.blindlight.Activities.MainActivity.offline_mode;


import java.util.Comparator;


public class MyAsyncTask extends AsyncTask<Void ,Void , Void> {

    private String topic;
    private Context context;
    private final Comparator<Float> comparator;
    private String ip_port;
    private MqttSubscriber subscriber;
    private MqttPublisher publisher;

    public MyAsyncTask(String topic, String ip_port , Context context) {
        this.topic=topic;
        this.ip_port=ip_port;
        this.context=context;
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

        try {
                int time = 1000;
                // Sleeping for given time period
                Thread.sleep(time);
                //subscriber = new MqttSubscriber();
                //subscriber.main("20:2D:07:B3:E1:81" ,ip_port);
                //publisher = new MqttPublisher();
                if (offline_mode==false) {
                    publisher = new MqttPublisher();
                    publisher.main(topic, ip_port);
                    if(!broker_run_flag){
                        subscriber = new MqttSubscriber();
                        subscriber.main("20:2D:07:B3:E1:81" ,ip_port  , context);
                        broker_run_flag=true;
                    }

                }
                else {
                    return null;
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


        return null;
    }
}