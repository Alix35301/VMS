package controller;

import components.AlertDiaglog;
import components.Customer;
import components.Helpers;
import components.Vehicle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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
        Vehicle vehicle= fetchVehicleInfo();
      if (Customer.isRegistered(cust_id.getText())){
          if (!Vehicle.registered(veh_reg.getText())){
              Vehicle.addVehicleToDb(vehicle,Helpers.getConnection());
              AlertDiaglog.infoBox("Vehicle has been registered to the system", "Success", Alert.AlertType.INFORMATION);
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


    public Vehicle fetchVehicleInfo() {
        return new Vehicle(
            cust_id.getText(),
            model.getText(),
            veh_reg.getText(),
            chass_num.getText(),
            reg_date.getText()
        );
    }



}
