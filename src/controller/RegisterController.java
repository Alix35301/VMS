package controller;

import components.AlertDiaglog;
import components.Customer;
import components.Helpers;
import components.Vehicle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

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
    @FXML
    private AnchorPane rootPane;

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
        if(isOk()) {
            Customer customer = fetchCustomerInfo();
            Vehicle vehicle = fetchVehicleInfo();

            if (!Customer.isRegistered(customer.getId())) {
                Customer.addCustomerToDb(customer, Helpers.getConnection());
                Vehicle.addVehicleToDb(vehicle, Helpers.getConnection());
                AlertDiaglog.infoBox("Customer and vehicle has been added to the system", "Success", Alert.AlertType.INFORMATION);

            } else {
                AlertDiaglog.infoBox("Looks like this customer is already registered with us.. If you would like to register new vehicle select the option from the main screen", "Customer registered", Alert.AlertType.WARNING);

            }
        }else {
            AlertDiaglog.infoBox("You missed some fields. Please try again after entering missing fields",
                "Missing field data", Alert.AlertType.WARNING);
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


    public void back(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/service.fxml"));
        AnchorPane pane = fxmlLoader.load();
        rootPane.getChildren().setAll(pane);


    }

    public boolean isOk(){

        return !cust_name.getText().trim().isEmpty() &&
            !cust_id.getText().trim().isEmpty() &&
            !cust_contact.getText().trim().isEmpty() &&
            !cust_email.getText().trim().isEmpty() &&
            !cust_address.getText().trim().isEmpty() &&
            !model.getText().trim().isEmpty() &&
            !veh_reg.getText().trim().isEmpty() &&
            !chass_num.getText().trim().isEmpty() &&
            !reg_date.getText().trim().isEmpty();
    }

}
