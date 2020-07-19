package controller;

import components.AlertDiaglog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.*;

/**
 * @author ali
 * @created_on 7/16/20
 */
public class RegisterVehicleController {
    @FXML
    Button btnSave, btnReset;
    @FXML
    TextField cust_id, model, veh_reg, chass_num, reg_date;

    public void saveVehicle(ActionEvent event) throws IOException, SQLException {
      if (RegisterController.checkCustomer(cust_id.getText())){
          if (!checkVehicle(veh_reg.getText())){
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false", "root", "1234");
                String sql = "INSERT INTO VMS.VEHICLES (customer_id, model, vehicle_reg, chassis_num, reg_date) VALUES (?,?,?,?,?)";
                PreparedStatement p1 = con.prepareStatement(sql);
                p1.setString(1, cust_id.getText());
                p1.setString(2, model.getText());
                p1.setString(3, veh_reg.getText());
                p1.setString(4, chass_num.getText());
                p1.setString(5, reg_date.getText());
                System.out.println(p1.toString());
                p1.executeUpdate();
            }else{
              AlertDiaglog.infoBox("Looks like this vehicle is already in the system.. Please check again", "Vehicle already registered", Alert.AlertType.WARNING);

          }
      }else {
          AlertDiaglog.infoBox("Sorry, we couldnt locate the customer, it maybe because customer is not registered yet", "Customer not found", Alert.AlertType.WARNING);

      }

    }

    public void resetForm(ActionEvent event) throws IOException {
        cust_id.clear();
        model.clear();
        veh_reg .clear();
        chass_num.clear();
        reg_date.clear();
    }

    public static boolean checkVehicle(String vehicle_reg) throws SQLException {
        boolean found = false;
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false", "root", "1234");
        String sql = "SELECT * FROM VMS.VEHICLES WHERE vehicle_reg = ?";
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        preparedStatement.setString(1, vehicle_reg);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            found = true;
        }
        return found;
    }



}
