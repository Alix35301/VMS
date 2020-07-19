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
public class RegisterController {

    @FXML
    Button btnSave, btnReset;
    @FXML
    CheckBox commercial;
    @FXML
    TextField cust_name, cust_id, cust_contact, cust_email, cust_address,
        model, veh_reg, chass_num, reg_date;


    public void saveCustomer(ActionEvent event) throws IOException, SQLException {
        if(!checkCustomer(cust_id.getText())) {
            String customer_type = "1";
            if (commercial.isSelected()) {
                customer_type = "2";
            }
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false", "root", "1234");
            String sql = "INSERT INTO VMS.CUSTOMERS (`customer_type`, `cust_name`, `email`, `password`, `phone`, `address`, `customer_id`) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement p1 = con.prepareStatement(sql);
            p1.setString(1, customer_type);
            p1.setString(2, cust_name.getText());
            p1.setString(3, cust_email.getText());
            p1.setString(6, cust_address.getText());
            p1.setString(4, "welcome123");
            p1.setString(5, cust_contact.getText());
            p1.setString(7, cust_id.getText());
            System.out.println(p1.toString());
            p1.executeUpdate();
        }else {
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
        veh_reg .clear();
        chass_num.clear();
        reg_date.clear();
    }

    public static boolean checkCustomer(String customer_id) throws SQLException {
        boolean found = false;
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false", "root", "1234");
        String sql = "SELECT * FROM VMS.CUSTOMERS WHERE customer_id = ?";
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        preparedStatement.setString(1, customer_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            found = true;
        }
        return found;
    }

}
