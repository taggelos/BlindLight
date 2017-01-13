package com.example.aggel.blindlight.Services;

import android.app.Service;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.aggel.accelerometerapplication.R;
import com.example.aggel.blindlight.util.MqttSubscriber;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


/**
 * Created by Maria on 12/01/2017.
 */

public class MySubService extends Service implements MqttCallback {


    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent , int flags , int startId){
        int qos = 2;
        String broker = "tcp://192.168.1.2:1883";
        String clientId = "JavaAsyncSample";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttAsyncClient sampleClient = new MqttAsyncClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setWill("Test/clienterrors" , "crashed".getBytes(),2,false);
            connOpts.setCleanSession(true);
            sampleClient.setCallback(new MqttSubscriber());
            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            Thread.sleep(1000);
            sampleClient.subscribe("20:2D:07:B3:E1:81", qos);
            System.out.println("Subscribed");
        } catch (Exception me) {
            if (me instanceof MqttException) {
                System.out.println("reason " + ((MqttException) me).getReasonCode());
            }
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.err.println("connection lost");

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("topic: " + topic);
        String mes = new String(message.getPayload());
        System.out.println("message: " + mes);
        Toast.makeText(this , "PROSOXH" , Toast.LENGTH_SHORT).show();



    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.err.println("delivery complete");
    }
}
