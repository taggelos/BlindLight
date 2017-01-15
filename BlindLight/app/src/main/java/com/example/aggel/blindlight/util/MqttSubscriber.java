package com.example.aggel.blindlight.util;

/**
 * Created by Maria on 28/12/2016.
 */


import android.content.Context;


import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class MqttSubscriber implements MqttCallback  {
    public  String topic;
    public Context context;
    public static boolean flag_message =false;

    public  void main(String topic , String port_ip ,Context context ) {

        this.topic=topic;
        this.context=context;


        int qos = 2;
        String broker = port_ip;
        String clientId = "ΜyClient2Android";
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
              //  sampleClient.disconnect();
                //System.out.println("Disconnected");

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
    public void messageArrived(String topic, final MqttMessage message) throws Exception {
        System.out.println("topic: " + topic);
        final String mes = new String(message.getPayload());
        System.out.println("message: " + mes);
        flag_message =true;
        System.out.println(flag_message + "--------------------");




        /* CharSequence text = mes;
        final Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 1500);
        */



    }


    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.err.println("delivery complete");
    }
}
