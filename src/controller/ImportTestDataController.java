package controller;

import components.AlertDiaglog;
import components.Helpers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.concurrent.TransferQueue;

/**
 * @author ali
 * @created_on 7/29/20
 */
public class ImportTestDataController {
    @FXML
    private AnchorPane rootPane;

    @FXML
    private Text status1;

    @FXML
    private Text status2;
    @FXML
    private Text status3;

    final FileChooser fileChooser = new FileChooser();

    public void importProducts(ActionEvent event) throws SQLException {
        File file = fileChooser.showOpenDialog(new Stage());

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false&allowLoadLocalInfile=true", "root", "1234");
        try {
            String loadquery = String.format("LOAD DATA LOCAL INFILE '" + "%s" + "' INTO TABLE VMS.PRODUCTS FIELDS TERMINATED BY ','" + " LINES TERMINATED BY '\n' (product_code,price)", file.getAbsoluteFile());
            System.out.println(loadquery);
            Statement statement = conn.createStatement();
            statement.execute(loadquery);
            status1.setVisible(true);
            status1.setText("File uploaded");

        }catch (Exception e){
            AlertDiaglog.infoBox(e.getMessage(),"Error happened", Alert.AlertType.ERROR);
            e.printStackTrace();

        }

    }

    public void importCustomers(ActionEvent event) throws SQLException {
        File file = fileChooser.showOpenDialog(new Stage());

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false&allowLoadLocalInfile=true", "root", "1234");
        try {
            String loadquery = String.format("LOAD DATA LOCAL INFILE '" + "%s" + "' INTO TABLE VMS.CUSTOMERS FIELDS TERMINATED BY ','" + " LINES TERMINATED BY '\n' (id,customer_type,cust_name,email,password,phone,address,customer_id)", file.getAbsoluteFile());
            System.out.println(loadquery);
            Statement statement = conn.createStatement();
            statement.execute(loadquery);
            status2.setVisible(true);
            status2.setText("File uploaded");

        }catch (Exception e){
            AlertDiaglog.infoBox(e.getMessage(),"Error happened", Alert.AlertType.ERROR);
            e.printStackTrace();

        }

    }

    public void importVehicles(ActionEvent event) throws SQLException {
        File file = fileChooser.showOpenDialog(new Stage());

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false&allowLoadLocalInfile=true", "root", "1234");
        try {
            String loadquery = String.format("LOAD DATA LOCAL INFILE '" + "%s" + "' INTO TABLE VMS.VEHICLES FIELDS TERMINATED BY ','" + " LINES TERMINATED BY '\n' (id,customer_id,model,vehicle_reg,chassis_num,reg_date)", file.getAbsoluteFile());
            System.out.println(loadquery);
            Statement statement = conn.createStatement();
            statement.execute(loadquery);
            status3.setVisible(true);
            status3.setText("File uploaded");

        }catch (Exception e){
            AlertDiaglog.infoBox(e.getMessage(),"Error happened", Alert.AlertType.ERROR);
            e.printStackTrace();

        }

    }


    public void logout(ActionEvent event) throws IOException {

        AlertDiaglog.infoBox("You have successfully logged out.","Loggout", Alert.AlertType.INFORMATION);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/Login.fxml"));
        AnchorPane pane = fxmlLoader.load();
        rootPane.getChildren().setAll(pane);

    }

    public void back(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/analyst.fxml"));
        AnchorPane pane = fxmlLoader.load();
        rootPane.getChildren().setAll(pane);

    }

    public void removeTestData(ActionEvent event) {
        String cus = "TRUNCATE VMS.CUSTOMERS;";
        String veh = "TRUNCATE VMS.VEHICLES;";
        String pro = "TRUNCATE VMS.PRODUCTS;";

        Connection conn = Helpers.getConnection();

        Statement s = null;Statement s2 = null;Statement s3 = null;
        try {
            s = conn.createStatement();
            s2 = conn.createStatement();
            s3 = conn.createStatement();
            s.executeUpdate(cus);
            s2.executeUpdate(veh);
            s3.executeUpdate(pro);
            AlertDiaglog.infoBox("All the test data has been removed from the syste","Success", Alert.AlertType.INFORMATION);
        } catch (SQLException throwables) {
            AlertDiaglog.infoBox(throwables.getMessage(),"Error", Alert.AlertType.ERROR);
            throwables.printStackTrace();
        }


    }

}
