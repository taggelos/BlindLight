package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import java.util.Date;
import java.sql.*;
//import org.eclipse.paho.client.mqttv3.MqttClient;
import javafx.stage.Stage;
import sample.Pub_Sub.MqttPublisher;
import sample.Pub_Sub.MqttSubscriber;
import java.util.Date;
import java.sql.*;
import static sample.Controller.*;



public class Main extends Application {

    private static Stage pStage;
    public static Stage getPrimaryStage() {
        return pStage;
    }
    private void setPrimaryStage(Stage pStage) {
        this.pStage = pStage;
    }

    private static MqttSubscriber subscriber;
    private static MqttPublisher publisher;



    /*public class Records{
        int id_name;
        Float latitude ;
        Float longitude;
        String sensor_type;
        Float records;
        // @Temporal(TemporalType.TIMESTAMP)
        Date datetime ;
    }*/

    @Override
    public void start(Stage primaryStage) throws Exception{

        setPrimaryStage(primaryStage);

        ObservableList items =
                FXCollections.observableArrayList();

        Parent root = FXMLLoader.load(getClass().getResource("Settings.fxml"));
        primaryStage.setTitle("BlindLight(Beta)");
        primaryStage.setScene(new Scene(root, 800, 400));
        primaryStage.getIcons().add(new Image("sample/icon.png"));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
        subscriber = new MqttSubscriber();
        subscriber.main();


        //publisher = new MqttPublisher();
        //publisher.main();
    }
}
