package controller;

import components.AlertDiaglog;
import components.Auth;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.util.Callback;

import javax.swing.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ali
 * @created_on 7/18/20
 */
public class PaymentsController {
    @FXML
    private TableView paymentsTable;
    @FXML
    private TextField productName;
    @FXML
    private TableView jobsheet;
    @FXML
    private TextField searchTF;
    @FXML
    private Text modelTxt, emailTxt, contactTxt, cust_nameTxt, chassisTxt, reg_dateTxt;
    public String vehicle_reg = null;

    public void searchVehicle(ActionEvent event) throws IOException, SQLException {
        String customer_id = null, model = null, chassis_num = null,
            reg_date = null, email = null, phone = null, address = null, customer_name = null;
        boolean registered = false;

        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false", "root", "1234");
        String sql = "SELECT * FROM VMS.VEHICLES WHERE vehicle_reg = ?";
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        preparedStatement.setString(1, searchTF.getText());
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            registered = true;
            customer_id = resultSet.getString(2);
            model = resultSet.getString(3);
            vehicle_reg = resultSet.getString(4);
            chassis_num = resultSet.getString(5);
            reg_date = resultSet.getString(6);
        }
        if (registered) {
            String sql2 = "SELECT cust_name, email, phone, address FROM VMS.CUSTOMERS WHERE customer_id=?";
            PreparedStatement statement = con.prepareStatement(sql2);
            statement.setString(1, customer_id);
            ResultSet resultSet1 = statement.executeQuery();
            while (resultSet1.next()) {
                customer_name = resultSet1.getString(1);
                email = resultSet1.getString(2);
                phone = resultSet1.getString(3);
                address = resultSet1.getString(4);
            }
            contactTxt.setVisible(true);
            emailTxt.setVisible(true);
            modelTxt.setVisible(true);
            cust_nameTxt.setVisible(true);
            chassisTxt.setVisible(true);
            reg_dateTxt.setVisible(true);

            modelTxt.setText(model);
            cust_nameTxt.setText(customer_name);
            chassisTxt.setText(chassis_num);
            reg_dateTxt.setText(reg_date);
            emailTxt.setText(email);
            contactTxt.setText(phone);

            buildData();


        } else if (searchTF.getText().trim().isEmpty()) {

            AlertDiaglog.infoBox("Looks like you forgot to type the vehicle registraion number!", "Empty search", Alert.AlertType.WARNING);


        } else {
            AlertDiaglog.infoBox("Could not locate the vehicle in our system. Please check if its registered", "Vehicle not registered", Alert.AlertType.WARNING);

        }


    }


    public void buildData() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false", "root", "1234");
        ObservableList<ObservableList> data = FXCollections.observableArrayList();

        String sql = "SELECT job_summery, job_comments, update_date from VMS.JOBS where job_status = 'COMPLETED' and vehicle_reg=? ";
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        preparedStatement.setString(1, vehicle_reg);
        ResultSet resultSet = preparedStatement.executeQuery();

        TableColumn col = null;
        for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
            //We are using non property style for making dynamic table
            final int j = i;
            col = new TableColumn(resultSet.getMetaData().getColumnName(i + 1));
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                }
            });
            jobsheet.getColumns().addAll(col);
        }

        while (resultSet.next()) {
            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                Text text = new Text(resultSet.getString(i));
                text.wrappingWidthProperty().bind(col.widthProperty());
                row.add(text.getText());
            }
            data.add(row);

        }
//        jobsheet.setFixedCellSize(Region.USE_COMPUTED_SIZE);
        System.out.println(jobsheet.getFixedCellSize());
        jobsheet.setItems(data);


    }

    public void addService(ActionEvent event) throws SQLException {
        boolean productFound = false;
        String productCode = null, price = null;
        ObservableList<ObservableList> data = FXCollections.observableArrayList();

        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false", "root", "1234");

        String sql = "SELECT product_code, price from VMS.PRODUCTS where product_code = ?";
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        preparedStatement.setString(1, productName.getText());
        ResultSet resultSet = preparedStatement.executeQuery();

        TableColumn col = null;
        paymentsTable.getColumns().clear();
        for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
            //We are using non property style for making dynamic table
            final int j = i;
            col = new TableColumn(resultSet.getMetaData().getColumnName(i + 1));
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                }
            });
            paymentsTable.getColumns().addAll(col);
        }

        while (resultSet.next()) {
            productFound = true;
            productCode = resultSet.getString(1);
            price = resultSet.getString(2);



        }

        ObservableList<String> row = FXCollections.observableArrayList();
        for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
            Text text = new Text(resultSet.getString(i));
            text.wrappingWidthProperty().bind(col.widthProperty());
            row.add(text.getText());
        }
        data.add(row);
        paymentsTable.setItems(data);


    }


}
