package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;


public class Controller {

    @FXML
    private Slider x_axis;
    @FXML
    private Slider y_axis;
    @FXML
    private Slider z_axis;
    @FXML
    private Slider freq;
    @FXML
    private Label max_check;
    @FXML
    private Label min_check;
    @FXML
    private TextField max_light;
    @FXML
    private TextField min_light;
    @FXML
    private TextField user_id;
    @FXML
    private TextField longtitude;
    @FXML
    private TextField latitude;
    @FXML
    private ChoiceBox Sensor_Type;
    @FXML
    private DatePicker Date;
    @FXML
    private ChoiceBox cbox_hours;
    @FXML
    private ChoiceBox cbox_mins;
    @FXML
    private ChoiceBox cbox_secs;

    public void Apply(ActionEvent mouseEvent) {

       boolean fMax = false;
       if (max_light.getText().matches("[0-9]+")){
           int max_light_int = Integer.parseInt(max_light.getText());
           if (max_light_int < 0 || max_light_int > 10000)
               fMax = true;
       }
       else
           fMax = true;

       boolean fMin = false;
       if (min_light.getText().matches("[0-9]+")){
           int min_light_int = Integer.parseInt(min_light.getText());
           if (min_light_int < 0 || min_light_int > 10000)
               fMin = true;
       }
       else
           fMin = true;

       min_check.setVisible(fMax);
       max_check.setVisible(fMin);
//       if (fMax || fMin)
//           return;

        System.out.println("clicked on "+ x_axis.getValue()+"    " + y_axis.getValue()+ "   " + z_axis.getValue()+" "+freq.getValue());
    }


//--------------------------------------

    public class Records{
        int id_name;
        Float latitude ;
        Float longitude;
        String sensor_type;
        Float records;
        // @Temporal(TemporalType.TIMESTAMP)
        java.util.Date datetime ;
    }

    public void Search(ActionEvent mouseEvent) throws IOException {
        System.out.println("maaaaa");

       // boolean fId = false;
        //if (user_id.getText().matches("[0-9]+")){
         //   String userId = user_id.getText();
        //    fId = true;
       // }
       // id_check.setVisible(fId);

            // get var

            // check var

            // Sql query
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "112358aaa");
                Statement stmt = connection.createStatement();
                String SQL = "SELECT * FROM blind_light_data where 1==1  ";
                PreparedStatement prepared_state= connection.prepareStatement(SQL);;

              //  if ( !user_id.equals("") &&  !longtitude.equals("") && !latitude.equals("") &&  !Sensor_Type.getValue().equals("") && Date.getValue()!=null && !cbox_hours.getValue().equals("") && !cbox_mins.getValue().equals("") && !cbox_secs.getValue().equals("")){
                //    SQL += "where ";
               // }
                int cur=0;
                if ( !user_id.equals("") ){
                    SQL += " and name=?";
                }
                if( !longtitude.equals("")   ){
                    SQL += " and location_latitude=?";
                }
                if( !latitude.equals("") ){
                    SQL += " and location_longitude=?";
                }
                if(!Sensor_Type.getValue().equals("")){
                    SQL += " and sensorType=?";
                }
                if(!Date.getValue().equals("")){
                    SQL += " and date_time=?";
                }
                prepared_state = connection.prepareStatement(SQL);

                if ( !user_id.equals("") ){
                    cur++;
                    prepared_state.setString(cur,user_id.getText() );
                }
                if( !longtitude.equals("")   ){
                    cur++;
                    prepared_state.setFloat(cur, Float.parseFloat( longtitude.getText() ) );
                }
                if( !latitude.equals("") ){
                    cur++;
                    prepared_state.setFloat(cur,Float.parseFloat( latitude.getText() ) );
                }
                if(!Sensor_Type.getValue().equals("")){
                    cur++;
                    prepared_state.setString(cur,Sensor_Type.getValue().toString() );
                }
                if(!Date.getValue().equals("")){
                    cur++;
                   java.sql.Date cur_date = new Date(0);
                   cur_date.setYear(Date.getValue().getYear());
                   cur_date.setMonth(Date.getValue().getMonthValue());
                   cur_date.setDate(Date.getValue().getDayOfMonth());
                   cur_date.setHours(Integer.parseInt( cbox_hours.getValue().toString()) );
                   cur_date.setMinutes( Integer.parseInt( cbox_mins.getValue().toString()) );
                   cur_date.setSeconds(Integer.parseInt( cbox_secs.getValue().toString()) );
                    prepared_state.setDate(cur, cur_date  );
                }


                System.out.println(prepared_state);

                ResultSet rs = prepared_state.executeQuery();
/*
                Records temp_record = new Records();
                while (rs.next()) {
                    temp_record.id_name = rs.getInt("name");
                    temp_record.latitude = rs.getFloat("location_latitude");
                    temp_record.longitude = rs.getFloat("location_longitude");
                    temp_record.sensor_type = rs.getString("sensorType");
                    temp_record.records = rs.getFloat("records");
                    // @Temporal(TemporalType.TIMESTAMP)
                    temp_record.datetime = rs.getDate("date_time");

                    // list_records.Add(temp_record);
                    //items.add(temp_record.id_name + " " + temp_record.latitude + " " + temp_record.longitude + " " + temp_record.sensor_type + " " + temp_record.records + " " + temp_record.datetime + "");
                    System.out.println(temp_record.id_name + " " + temp_record.latitude + " " + temp_record.longitude + " " + temp_record.sensor_type + " " + temp_record.records + " " + temp_record.datetime + "");
                }
                */
            }
            catch (SQLException err)    {
                System.out.println( err.getMessage() );
            }

            // Print results
            Stage primaryStage = Main.getPrimaryStage();
            primaryStage.setTitle("Result)");
            TabPane myPane = FXMLLoader.load(getClass().getResource("Search.fxml"));
            Scene scene = new Scene(myPane);
            primaryStage.close();
            primaryStage.setScene(scene);
            primaryStage.show();
    }


    public void Refresh(ActionEvent mouseEvent) throws IOException {
       Search(mouseEvent);
    }

    public void NewSearch(ActionEvent mouseEvent) throws IOException {
        Stage primaryStage = Main.getPrimaryStage();
        primaryStage.setTitle("BlindLight(Beta)");
        TabPane myPane = FXMLLoader.load(getClass().getResource("Settings.fxml"));
        Scene scene = new Scene(myPane);
        primaryStage.close();
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}
