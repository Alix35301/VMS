package controller;

import components.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

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
    private ChoiceBox<String> paymentOptionCb;
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

    String jobSheetId=null;
    boolean dataadded = false;

    public void initialize(){
        TableColumn<String, Jobsheet.Job> column1 = new TableColumn<>("Job Summery");
        column1.setCellValueFactory(new PropertyValueFactory<>("jobSummmery"));

        TableColumn<String, Jobsheet.Job> column2 = new TableColumn<>("Job Comments");
        column2.setCellValueFactory(new PropertyValueFactory<>("jobDesc"));

        TableColumn<String, Jobsheet.Job> column3 = new TableColumn<>("Status");
        column3.setCellValueFactory(new PropertyValueFactory<>("jobStatus"));


        jobstatus.getColumns().addAll(column1,column2,column3);
        jobstatus.setPlaceholder(new Label("No jobs found for this vehicle"));
        jobstatus.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY );


    }


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
//            jobstatus.getColumns().clear();
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
        String sql2 = "SELECT id FROM VMS.JOBSHEET where mech_assigned = ? and status = 'PENDING' ";

        PreparedStatement p1 = con.prepareStatement(sql2);
        p1.setString(1,JobsheetController.getMechanicID(Auth.getUser()));
            System.out.println(p1.toString());
        ResultSet resultSet=p1.executeQuery();

        jobSheetId=null;
            while (resultSet.next()){
                jobSheetId=resultSet.getString(1);
                System.out.println(jobSheetId);
            }

            String sql = "SELECT job_summery FROM VMS.JOBS where job_status = 'PENDING' and jobsheetId = ?";
            PreparedStatement p3 = con.prepareStatement(sql);
            p3.setString(1, jobSheetId);
            System.out.println(p3.toString());
            ResultSet rs=p3.executeQuery();


            while (rs.next()){
            jobs.add(rs.getString(1));
                System.out.println(rs.getString(1));

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
        p1.setString(1,updateJob.getJobDesc());
        p1.setString(2, "COMPLETED");
        p1.setString(3, Helpers.getTime());
        p1.setString(4, selectJob.getValue());
        p1.executeUpdate();

        jobstatus.getItems().clear();
        buildData();
        AlertDiaglog.infoBox("Jobsheet has been updated!","Success", Alert.AlertType.INFORMATION);

    }


        public void buildData() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false", "root", "1234");
        ObservableList<ObservableList> data= FXCollections.observableArrayList();
        String sql = "SELECT job_summery, job_comments, job_status from VMS.JOBS where jobsheetId=?";
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        preparedStatement.setString(1, jobSheetId);
            System.out.println(preparedStatement.toString());
        ResultSet resultSet = preparedStatement.executeQuery();


        Jobsheet.Job job = new Jobsheet.Job();
        while(resultSet.next()){

            job.setJobSummmery(resultSet.getString(1));
            job.setJobDesc(resultSet.getString(2));
            job.setJobStatus(resultSet.getString(3));
            jobstatus.getItems().add(job);
            }



        }

    }

