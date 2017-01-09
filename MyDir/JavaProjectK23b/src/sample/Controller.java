package sample;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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
    private Label try_again;
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
    private TextField Sensor_Value;
    @FXML
    private DatePicker Date;
    @FXML
    private ChoiceBox cbox_hours;
    @FXML
    private ChoiceBox cbox_mins;
    @FXML
    private ChoiceBox cbox_secs;
    @FXML
    private Label datetime_check;

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
       else fMin = true;

       min_check.setVisible(fMax);
       max_check.setVisible(fMin);

       if (fMax==true || fMin==true){
           try_again.setVisible(true);
       }else
           System.out.println("clicked on "+ max_light.getText()+"   "+ min_light.getText() +"     " + x_axis.getValue() +"    " + y_axis.getValue() + "   " + z_axis.getValue() +" "+ freq.getValue());
    }


//--------------------------------------
    class Records {
        String id_name;
        float latitude ;
        float longitude;
        String sensor_type;
        float  sensor_value;
        // @Temporal(TemporalType.TIMESTAMP)
        Date  datetime ;
    }


   public static List<Records> rec_li = new ArrayList<Records>();
    ResultSet rs;
    int resperpage  = 2;
    int numofpages;



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

                if ( !user_id.getText().equals("") ||  !longtitude.getText().equals("") || !latitude.getText().equals("") ||  Sensor_Type.getValue()!=null ||  !Sensor_Value.getText().equals("")  || Date.getValue()!=null ){
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
                    SQL += " user_id=?";
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
                if(!Sensor_Value.getText().equals("")){
                    if (flag==true){
                        SQL += " and ";
                    }
                    else {
                        flag=true;
                    }
                    SQL += "  sensorValue=?";
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
                if(!Sensor_Value.getText().equals("")){
                    cur++;
                    prepared_state.setString(cur,Sensor_Value.getText().toString() );
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

               rs = prepared_state.executeQuery();


                // Print results
               // if (cbox_hours.getValue()==null || cbox_mins.getValue()==null || cbox_secs.getValue()==null ) {
                //    datetime_check.setVisible(true);
            //        return;
             //   }

                Stage primaryStage = Main.getPrimaryStage();
                primaryStage.setTitle("Result");
                Pane myPane = FXMLLoader.load(getClass().getResource("Search.fxml"));

                int numofres=0;
                if (rs.last()) {
                    numofres = rs.getRow();
                    rs.beforeFirst();
                }

                if  (numofres%resperpage>0){
                    numofpages = numofres/resperpage+1;
                }
                else{
                    numofpages = numofres/resperpage;
                }
                if (numofres == 0) {
                    numofpages=1;
                }

                rec_li.clear();
                while (rs.next()) {
                    Records temp_record = new Records();
                    temp_record.id_name = rs.getString("user_id");
                    temp_record.latitude = rs.getFloat("location_latitude");
                    temp_record.longitude = rs.getFloat("location_longitude");
                    temp_record.sensor_type = rs.getString("sensorType");
                    temp_record.sensor_value = rs.getFloat("sensorValue");
                    // @Temporal(TemporalType.TIMESTAMP)
                    temp_record.datetime = rs.getDate("date_time");
                    rec_li.add(temp_record);
                 }


                Pagination mypg =  new Pagination();
                mypg.setPageCount(numofpages);

                mypg.setPageFactory(new Callback<Integer, Node>() {
                    @Override
                    public Node call(Integer pageIndex) {
                        try {
                            return createPage(pageIndex);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }

                });


               Pane sec_pane = (Pane) myPane.getChildren().get(1);
               sec_pane.getChildren().add(mypg);


                Scene scene = new Scene(myPane,1000, 600);
                primaryStage.close();
                primaryStage.setScene(scene);
                primaryStage.show();

            }
            catch (SQLException err)    {
                System.out.println( err.getMessage() );
            } catch (ParseException e) {
                e.printStackTrace();
            }

    }

    public VBox createPage(int pageIndex) throws SQLException {
        VBox box = new VBox(5);
        TableView<ObservableList<String>> element = new TableView<>();
        // add columns
        List<String> columnNames = new ArrayList<>();
        columnNames.add("user_id");
        columnNames.add("latitude");
        columnNames.add("longitude");
        columnNames.add("sensor_Type");
        columnNames.add("sensor_Value");
        columnNames.add("date_time");

        for (int j = 0; j < columnNames.size(); j++) {
            final int finalIdx = j;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(
                    columnNames.get(j)
            );
            column.setCellValueFactory(param ->
                    new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx))
            );
            element.getColumns().add(column);
        }


            // add data
            for (int j = 0; j < resperpage; j++) {
                if (pageIndex*resperpage+j >= rec_li.size() ){
                    break;
                }
                Records rec =rec_li.get(pageIndex*resperpage+j);
                if (rec!=null) {
                    List<String> myar = new ArrayList<>();
                    String cur_str;
                    cur_str = rec.id_name;
                    myar.add(cur_str);
                    cur_str = Float.toString(rec.latitude);
                    myar.add(cur_str);
                    cur_str = Float.toString(rec.longitude);
                    myar.add(cur_str);
                    cur_str = rec.sensor_type;
                    myar.add(cur_str);
                    cur_str = Float.toString(rec.sensor_value);
                    myar.add(cur_str);
                    if (rec.datetime!=null) {
                        cur_str =rec.datetime.toString();
                    }
                    else {
                        cur_str ="";
                    }
                    myar.add(cur_str);

                    element.getItems().add(
                            FXCollections.observableArrayList(
                                    myar
                            )
                    );
                }
            }

        box.getChildren().add(element);

        return box;
    }


    public void Refresh(ActionEvent mouseEvent) throws IOException {

        Stage primaryStage = Main.getPrimaryStage();
        primaryStage.setTitle("Result)");
        Pane myPane = FXMLLoader.load(getClass().getResource("Search.fxml"));

        Pagination mypg =  new Pagination();

        if  (rec_li.size()%resperpage>0){
            numofpages = rec_li.size()/resperpage+1;
        }
        else{
            numofpages = rec_li.size()/resperpage;
        }
        if (rec_li.size() == 0) {
            numofpages=1;
        }

        mypg.setPageCount(numofpages);

        mypg.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                try {
                    return createPage(pageIndex);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return null;
                }
            }

        });


        Pane sec_pane = (Pane) myPane.getChildren().get(1);
        sec_pane.getChildren().add(mypg);


        Scene scene = new Scene(myPane,600, 600);
        primaryStage.close();
        primaryStage.setScene(scene);
        primaryStage.show();
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
