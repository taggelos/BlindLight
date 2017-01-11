package com.example.aggel.blindlight.util;

/**
 * Created by Maria on 28/12/2016.
 */


import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttSubscriber implements MqttCallback {
    public  String topic;
    public Context context;

    public  void main(String topic , String port_ip , Context context ) {

        this.topic=topic;
        this.context = context;

        int qos = 2;
        String broker = port_ip;
        //String broker = "tcp://192.168.1.4:1883";
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
            sampleClient.subscribe(topic, qos);
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

        Toast.makeText(context, mes,
                Toast.LENGTH_SHORT).show();



    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.err.println("delivery complete");
    }
}
