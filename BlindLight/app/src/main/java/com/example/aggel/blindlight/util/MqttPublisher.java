package com.example.aggel.blindlight.util;

/**
 * Created by Maria on 28/12/2016.
 */


import com.example.aggel.blindlight.Activities.MainActivity;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import static com.example.aggel.blindlight.Activities.MainActivity.offline_mode;


public class MqttPublisher  {

    public String topic;
    public void main(String topic , String port_ip ) {
        this.topic=topic;
        String content = "My Message";
        int qos = 2;
        String broker = port_ip ;
        String clientId = "Μyclientid";
        MemoryPersistence persistence = new MemoryPersistence();


        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            if (offline_mode==false) {
                System.out.println("Connected");
                System.out.println("Publishing message: " + content);
                MqttMessage message = new MqttMessage(content.getBytes());
                message.setQos(qos);

                sampleClient.publish(topic, message);
                System.out.println("Message published");
            }
            //sampleClient.disconnect();
            //System.out.println("Disconnected");
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

}

