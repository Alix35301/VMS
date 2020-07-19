package controller;

import components.AlertDiaglog;
import components.Auth;
import components.Helpers;
import components.Jobsheet;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ali
 * @created_on 7/17/20
 */
public class UpdateJobsheetController {
    @FXML
    private TableView jobstatus;
    @FXML
    private TextField searchTF;
    @FXML
    private TextArea remarks;
    @FXML
    private ChoiceBox<String> selectJob;
    @FXML
    Text modelTxt, emailTxt, contactTxt, cust_nameTxt, chassisTxt, reg_dateTxt;
    public String vehicle_reg = null;

    boolean dataadded = false;


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

            try {
                ObservableList<String> list  = getJobs(vehicle_reg);
                System.out.println(list.toString());
                selectJob.setItems(list);
//                selectJob.getSelectionModel().select("Not Selected");
            }catch (Exception e){
                e.printStackTrace();
            }
            jobstatus.getColumns().clear();
            jobstatus.getItems().clear();
            buildData();
        } else if (searchTF.getText().trim().isEmpty()) {

            AlertDiaglog.infoBox("Looks like you forgot to type the vehicle registraion number!", "Empty search", Alert.AlertType.WARNING);


        } else {
            AlertDiaglog.infoBox("Could not locate the vehicle in our system. Please check if its registered", "Vehicle not registered", Alert.AlertType.WARNING);

        }

    }

        public ObservableList<String> getJobs(String vehicle_reg) throws SQLException {

        List<String> jobs= new ArrayList<>();
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false", "root", "1234");
        String sql = "SELECT job_summery FROM VMS.JOBS where vehicle_reg = ? and job_status = 'PENDING' and mech_assigned=?";
        PreparedStatement p1 = con.prepareStatement(sql);
        p1.setString(1, vehicle_reg);
        p1.setString(2,JobsheetController.getMechanicID(Auth.getUser()));
        ResultSet resultSet=p1.executeQuery();

        while (resultSet.next()){
            jobs.add(resultSet.getString(1));

        }
        ObservableList<String> ob = FXCollections.observableArrayList(jobs);

        return  ob;

    }

    public void update(ActionEvent event) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false", "root", "1234");
        String sql = "UPDATE VMS.JOBS SET job_comments=?, job_status=?, update_date=?  where job_summery = ?";

        Jobsheet.Job updateJob = new Jobsheet.Job(remarks.getText());
        PreparedStatement p1 = con.prepareStatement(sql);
        System.out.println(p1.toString());
        p1.setString(1,updateJob.getJob_desc());
        p1.setString(2, "COMPLETED");
        p1.setString(3, Helpers.getTime());
        p1.setString(4, selectJob.getValue());
        p1.executeUpdate();

        jobstatus.getColumns().clear();
        jobstatus.getItems().clear();
        buildData();

    }


        public void buildData() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false", "root", "1234");
        ObservableList<ObservableList> data= FXCollections.observableArrayList();

        String sql = "SELECT job_summery, job_comments, job_status from VMS.JOBS where job_status = 'PENDING' and mech_assigned=? and vehicle_reg=? ";
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        preparedStatement.setString(1,JobsheetController.getMechanicID(Auth.getUser()));
        preparedStatement.setString(2, vehicle_reg);
        ResultSet resultSet = preparedStatement.executeQuery();


        for(int i=0 ; i<resultSet.getMetaData().getColumnCount(); i++){
            //We are using non property style for making dynamic table
            final int j = i;
            TableColumn col = new TableColumn(resultSet.getMetaData().getColumnName(i+1));
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>, ObservableValue<String>>(){
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                }
            });
            if (i==0){
                col.prefWidthProperty().bind(jobstatus.widthProperty().multiply(0.3));
            }else if (i==1){
                col.prefWidthProperty().bind(jobstatus.widthProperty().multiply(0.5));

            }else if (i==2){
                col.prefWidthProperty().bind(jobstatus.widthProperty().multiply(0.3));
            }
            col.setResizable(false);
            jobstatus.getColumns().addAll(col);
        }

        while(resultSet.next()){
            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();
            for(int i=1 ; i<=resultSet.getMetaData().getColumnCount(); i++){
                //Iterate Column
                row.add(resultSet.getString(i));
            }
            data.add(row);

        }

        jobstatus.setItems(data);
    }
}
