package sample.Pub_Sub;

/**
 * Created by Maria on 28/12/2016.
 */

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.sql.Date;

import static sample.Controller.x_threshold;
import static sample.Controller.y_threshold;
import static sample.Controller.z_threshold;
import static sample.Controller.max_light_threshold;
import static sample.Controller.min_light_threshold;



public class MqttSubscriber implements MqttCallback {

    private String[] arr;
    private static MqttPublisher publisher;


    public static void main() {

        String topic="#";
        int qos = 2;
        String broker = "tcp://localhost:1883";
        String clientId = "Μyclientid2";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttAsyncClient sampleClient = new MqttAsyncClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
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
        System.out.println("message: " + new String(message.getPayload()));

        //take the values
        String s = topic;
        arr = s.split("/");




        manager(arr);


    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.err.println("delivery complete");
    }


    public String[] getDataArray(){
        return arr;
    }

    public static void manager(String[] arr){
        Boolean possible_crash=false;
        String macAddress = arr[0];
        String sensorType= arr[1];
        String sensor_Value= arr[2];
        String date= arr[3];
        String latitude=arr[4];
        String longtitude=arr[5];

        if (sensorType.equals("Accelerometer Sensor")) {

            String[] accelero_values  = sensor_Value.split(",");

            System.out.println(macAddress);
            System.out.println(x_threshold);
            if ((Double.parseDouble(accelero_values[0]) >= x_threshold) || (Double.parseDouble(accelero_values[1]) >= y_threshold) || (Double.parseDouble(accelero_values[2]) >= z_threshold)) {
                System.out.println("-------------"+x_threshold);

                //inserInMyDataBase(macAddress , latitude , longtitude , sensorType , sensor_Value , date);
                possible_crash=true;

            }

        }
        else if(sensorType.equals("Light Sensor")){

            if (Double.parseDouble(sensor_Value) > max_light_threshold) {
                possible_crash=true;

            }
            if (Double.parseDouble(sensor_Value) < min_light_threshold) {
                possible_crash=true;

            }

        }
        else {
            if ((Double.parseDouble(sensor_Value))==0){
                possible_crash=true;

            }

        }
        if(possible_crash.equals(true)){
            inserInMyDataBase(macAddress , latitude , longtitude , sensorType , sensor_Value , date);
            publisher = new MqttPublisher();
            publisher.main(macAddress);

        }


        //if possibitity =true

        ////publisher = new MqttPublisher();
        //publisher.main();

    }

    public static void inserInMyDataBase(String macAddress ,String latitude ,String longtitude ,String sensorType ,String sensor_Value , String  date ){


        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb?autoReconnect=true&useSSL=false", "root", "MyNewPass");
            // stmt = connection.createStatement( );
            String SQL = "INSERT INTO  blind_light_data(user_id ,location_latitude , location_longitude , sensorType , sensorValue , date_time)  VALUES(? , ? , ? , ? , ? , ?)";


            PreparedStatement prepared_state= connection.prepareStatement(SQL);
            prepared_state.setString(1 , macAddress );
            prepared_state.setFloat(2 , Float.parseFloat(latitude));
            prepared_state.setFloat(3 , Float.parseFloat(longtitude));
            prepared_state.setString(4 , sensorType );
            prepared_state.setString(5 , sensor_Value );
            prepared_state.setDate(6 , null);


            System.out.println(prepared_state+"----fwefwf-------");
            //int rs = stmt.executeUpdate(String.valueOf(prepared_state));
            prepared_state.executeUpdate();


        }
        catch ( SQLException err ) {
            System.out.println( err.getMessage( ) );
        }

    }

}


