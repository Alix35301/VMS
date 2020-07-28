package controller;

import components.AlertDiaglog;
import components.Customer;
import components.Helpers;
import components.Vehicle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.*;

/**
 * @author ali
 * @created_on 7/16/20
 */
public class RegisterController {

    @FXML
    Button btnSave, btnReset;
    @FXML
    CheckBox commercial;
    @FXML
    TextField cust_name, cust_id, cust_contact, cust_email, cust_address,
        model, veh_reg, chass_num, reg_date;

    public Customer fetchCustomerInfo() {
//        if(!registered(cust_id.getText())) {
        String customer_type = "1";
        if (commercial.isSelected()) {
            customer_type = "2";
        }

        return new Customer(
            cust_id.getText(),
            customer_type,
            cust_name.getText(),
            cust_email.getText(),
            "Welcome123",
            cust_contact.getText(),
            cust_address.getText()
        );
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

    public void saveCustomer(ActionEvent event) throws IOException, SQLException {
        Customer customer = fetchCustomerInfo();
        Vehicle vehicle = fetchVehicleInfo();

        if (!Customer.isRegistered(customer.getId())) {
            Customer.addCustomerToDb(customer, Helpers.getConnection());
            Vehicle.addVehicleToDb(vehicle, Helpers.getConnection());
            AlertDiaglog.infoBox("Customer and vehicle has been added to the system", "Success", Alert.AlertType.INFORMATION);

        } else {
            AlertDiaglog.infoBox("Looks like this customer is already registered with us.. If you would like to register new vehicle select the option from the main screen", "Customer registered", Alert.AlertType.WARNING);

        }

    }

    public void resetForm(ActionEvent event) throws IOException {
        cust_name.clear();
        cust_id.clear();
        cust_contact.clear();
        cust_email.clear();
        cust_address.clear();
        model.clear();
        veh_reg.clear();
        chass_num.clear();
        reg_date.clear();
    }


}
