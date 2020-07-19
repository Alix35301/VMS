package controller;

import components.Auth;
import components.Customer;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author ali
 * @created_on 4/14/20
 */
public class CustomerController {

@FXML
    private Label lblcustomer;
@FXML
    private TableView tStatus;
    private ObservableList<ObservableList> data;
@FXML
    private Button btnrefresh;
@FXML
    private Button btnlogout;



    @FXML
    public void initialize() throws SQLException {
        Customer customer = new Customer();
        lblcustomer.setText(lblcustomer.getText()+" "+ Auth.getEmail());
//        buildData();
    }

    public void onclickRefresh(ActionEvent event) throws SQLException {

            tStatus.getItems().clear();
            tStatus.getColumns().clear();

//        buildData();
    }

    public void onClickLogout(ActionEvent event) throws IOException {
        FXMLLoader loader =new FXMLLoader(new File("src/resources/Login.fxml").toURI().toURL());

        Label label = (Label) loader.getNamespace().get("kk");

//        FXMLLoader loader = FXMLLoader.load(new File("src/resources/Login.fxml").toURI().toURL());
        AnchorPane pane = loader.load();
        System.out.println("Done here");
        Scene scene = new Scene(pane);


        Stage stage= (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);

    }

    public void buildData() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false", "root", "1234");
        data= FXCollections.observableArrayList();

        String sql = "SELECT `vehicle_status`.`vehicle_type`,\n" +
            "    `vehicle_status`.`handover_date`,\n" +
            "    `vehicle_status`.`collection_date`,\n" +
            "    `staffs`.`first_name`,\n" +
            "    `status`.`status`,\n" +
            "    `vehicle_status`.`remarks`\n"+
            "FROM `VMS`.`vehicle_status`\n" +
            "INNER JOIN `VMS`.`staffs` ON `vehicle_status`.`service_adviser_id`=`staffs`.`id`\n" +
            "INNER JOIN `VMS`.`status` ON `vehicle_status`.`status`=`status`.`id`\n" +
            "INNER JOIN VMS.customers ON vehicle_status.customer_id=customers.id;";
        ResultSet resultSet = con.createStatement().executeQuery(sql);
        System.out.println(countRows(resultSet));
        System.out.println("Success!");

        for(int i=0 ; i<resultSet.getMetaData().getColumnCount(); i++){
            //We are using non property style for making dynamic table
            final int j = i;
            TableColumn col = new TableColumn(resultSet.getMetaData().getColumnName(i+1));
            col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                }
            });

            tStatus.getColumns().addAll(col);
            System.out.println("Column ["+i+"] ");
        }

        while(resultSet.next()){
            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();
            for(int i=1 ; i<=resultSet.getMetaData().getColumnCount(); i++){
                //Iterate Column
                row.add(resultSet.getString(i));
            }
            System.out.println("Row [1] added "+row );
            data.add(row);

        }

        tStatus.setItems(data);
    }

    public int countRows(ResultSet res){
        int totalRows = 0;
        try {
            res.last();
            totalRows = res.getRow();
            res.beforeFirst();
        }
        catch(Exception ex)  {
            return 0;
        }
        return totalRows ;
    }


}
