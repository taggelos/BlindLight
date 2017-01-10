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
/*        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "MyNewPass");
            Statement stmt = connection.createStatement( );
            String SQL = "SELECT * FROM blind_light_data";
            ResultSet rs = stmt.executeQuery( SQL );

            Records temp_record=new Records();
            while (rs.next()) {
                temp_record.id_name = rs.getInt("name");
                temp_record.latitude = rs.getFloat("location_latitude");
                temp_record.longitude = rs.getFloat("location_longitude");
                temp_record.sensor_type = rs.getString("sensorType");
                temp_record.records = rs.getFloat("records");
               // @Temporal(TemporalType.TIMESTAMP)
                temp_record.datetime = rs.getDate("date_time");

               // list_records.Add(temp_record);
                items.add(temp_record.id_name+" "+ temp_record.latitude+" "+ temp_record.longitude+" "+ temp_record.sensor_type+" "+ temp_record.records+" "+ temp_record.datetime+"");
                System.out.println( temp_record.id_name+" "+ temp_record.latitude+" "+ temp_record.longitude+" "+ temp_record.sensor_type+" "+ temp_record.records+" "+ temp_record.datetime+"");
            }
        }
        catch ( SQLException err ) {
            System.out.println( err.getMessage( ) );
        }

        ListView<String> listView = new ListView<String>();
        listView.setItems(items);
        StackPane root = new StackPane();
        root.getChildren().add(listView);
        primaryStage.setScene(new Scene(root, 200, 250));
        primaryStage.show(); */

        Parent root = FXMLLoader.load(getClass().getResource("Settings.fxml"));
        primaryStage.setTitle("BlindLight(Beta)");
        primaryStage.setScene(new Scene(root, 800, 400));
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
