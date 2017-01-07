package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


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
                String SQL = "SELECT * FROM blind_light_data  ";
                PreparedStatement prepared_state= connection.prepareStatement(SQL);;

                if ( !user_id.getText().equals("") ||  !longtitude.getText().equals("") || !latitude.getText().equals("") ||  Sensor_Type.getValue()!=null || Date.getValue()!=null ){
                    SQL += "where ";
                }
                int cur=0;
                boolean flag=false;
                if ( !user_id.getText().equals("") ){
                    if (flag==true){
                        SQL += " and ";
                    }
                    else {
                        flag=true;
                    }
                    SQL += " name=?";
                }
                if( !longtitude.getText().equals("")   ){
                    if (flag==true){
                        SQL += " and ";
                    }
                    else {
                        flag=true;
                    }
                    SQL += " location_latitude=?";
                }
                if( !latitude.getText().equals("") ){
                    if (flag==true){
                        SQL += " and ";
                    }
                    else {
                        flag=true;
                    }
                    SQL += " location_longitude=?";
                }
                if(Sensor_Type.getValue()!=null){
                    if (flag==true){
                        SQL += " and ";
                    }
                    else {
                        flag=true;
                    }
                    SQL += "  sensorType=?";
                }
                if(Date.getValue()!=null){
                    if (flag==true){
                        SQL += " and ";
                    }
                    else {
                        flag=true;
                    }
                    SQL += "  date_time=?";
                }
                prepared_state = connection.prepareStatement(SQL);

                if ( !user_id.getText().equals("") ){
                    cur++;
                    prepared_state.setString(cur,user_id.getText() );
                }
                if( !longtitude.getText().equals("")   ){
                    cur++;
                    prepared_state.setFloat(cur, Float.parseFloat( longtitude.getText() ) );
                }
                if( !latitude.getText().equals("") ){
                    cur++;
                    prepared_state.setFloat(cur,Float.parseFloat( latitude.getText() ) );
                }
                if(Sensor_Type.getValue()!=null){
                    cur++;
                    prepared_state.setString(cur,Sensor_Type.getValue().toString() );
                }

                if(Date.getValue()!=null){
                    cur++;
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm" );
                    String date_string= Date.getValue().getYear()+"-"+ Date.getValue().getMonthValue()+"-"+Date.getValue().getDayOfMonth()+" "+ cbox_hours.getValue().toString()+":"+ cbox_mins.getValue().toString()+":"+ cbox_secs.getValue().toString();
                    java.util.Date ends_date = df.parse(date_string);
                    Timestamp mytmp = new java.sql.Timestamp(ends_date.getTime());
                    prepared_state.setTimestamp(cur,mytmp);
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
            } catch (ParseException e) {
                e.printStackTrace();
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
