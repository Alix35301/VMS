package controller;

import components.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ali
 * @created_on 7/16/20
 */
public class JobsheetController {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private ChoiceBox<String> cbMechAssigned;
    @FXML
    TextField searchTF, jobSumTF;
    @FXML
    TextArea addcommTA;
    @FXML
    Text modelTxt, emailTxt, contactTxt, cust_nameTxt,cust_type_txt,chassisTxt, reg_dateTxt;
    public String vehicle_reg = null;
    public String customer_id = null;
    Customer customer = new Customer();
    Vehicle vehicle = new Vehicle();


    // TODO FIX FORM ODER
    // TODO ADD ALL THE FIELDS
    // TODO WORK ON JOBSHEET\

    @FXML
    public void initialize() {
        try {
            ObservableList<String> list = getMechanics();
//            System.out.println(list.toString());
            cbMechAssigned.setItems(list);
//            cbMechAssigned.getSelectionModel().select("Not Selected");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void searchVehicle(ActionEvent event) throws IOException, SQLException {

        boolean registered = false;

        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false", "root", "1234");
        String sql = "SELECT * FROM VMS.VEHICLES WHERE vehicle_reg = ?";
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        preparedStatement.setString(1, searchTF.getText());
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            registered = true;
            vehicle.setCustomer_id(resultSet.getString(2));
            vehicle.setModel(resultSet.getString(3));
            vehicle.setVehicle_reg(resultSet.getString(4));
            vehicle.setChassis_num(resultSet.getString(5));
            vehicle.setReg_date(resultSet.getString(6));

        }
        if (registered) {
            String sql2 = "SELECT cust_name, email, phone, address, customer_type, customer_id FROM VMS.CUSTOMERS WHERE customer_id=?";
            PreparedStatement statement = con.prepareStatement(sql2);
            statement.setString(1, vehicle.getCustomer_id());
            ResultSet resultSet1 = statement.executeQuery();
            while (resultSet1.next()) {
                customer.setName(resultSet1.getString(1));
                customer.setEmail(resultSet1.getString(2));
                customer.setPhone(resultSet1.getString(3));
                customer.setAddress(resultSet1.getString(4));
                customer.setType(customer.getType(Integer.parseInt(resultSet1.getString(5))));
                customer.setId(resultSet1.getString(6));
            }
            cust_type_txt.setVisible(true);
            contactTxt.setVisible(true);
            emailTxt.setVisible(true);
            modelTxt.setVisible(true);
            cust_nameTxt.setVisible(true);
            chassisTxt.setVisible(true);
            reg_dateTxt.setVisible(true);

            cust_type_txt.setText(customer.getType());
            modelTxt.setText(vehicle.getModel());
            cust_nameTxt.setText(customer.getName());
            chassisTxt.setText(vehicle.getChassis_num());
            reg_dateTxt.setText(vehicle.getReg_date());
            emailTxt.setText(customer.getEmail());
            contactTxt.setText(customer.getPhone());


        } else if (searchTF.getText().trim().isEmpty()) {

            AlertDiaglog.infoBox("Looks like you forgot to type the vehicle registraion number!", "Empty search", Alert.AlertType.WARNING);


        } else {
            AlertDiaglog.infoBox("Could not locate the vehicle in our system. Please check if its registered", "Vehicle not registered", Alert.AlertType.WARNING);

        }


    }

    public void addJob(ActionEvent event) throws SQLException {
        if (!jobSumTF.getText().trim().isEmpty() && !addcommTA.getText().trim().isEmpty() && !cbMechAssigned.getValue().trim().isEmpty()) {
            String mechId = getMechanicID(cbMechAssigned.getValue());
            Jobsheet.Job job = new Jobsheet.Job(jobSumTF.getText(), addcommTA.getText(), mechId);
            Jobsheet.jobsheet.add(job);
            String msg = String.format("Job Summery: %s\nComments: %s\n To save the job please press save button once done!", jobSumTF.getText(), addcommTA.getText());
            AlertDiaglog.infoBox(msg, "Job added", Alert.AlertType.INFORMATION);
            jobSumTF.clear();
            addcommTA.clear();


        } else {
            AlertDiaglog.infoBox("Please fill out the job details. If you wish to cancel press cancel button on the form ", "Job details not filled", Alert.AlertType.WARNING);

        }
    }

    public void saveJobsheet(ActionEvent event) throws SQLException, IOException {
        if (!searchTF.getText().trim().isEmpty()) {
            if (Jobsheet.jobsheet.size() > 0) {
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false", "root", "1234");
                if (!checkJsPending(vehicle)) {
                    String sqlJobsheet = "INSERT INTO VMS.JOBSHEET(vehicle_reg, customer_id,mech_assigned, status) VALUES (?,?,?,?)";
                    PreparedStatement p2 = con.prepareStatement(sqlJobsheet);
                    p2.setString(1, vehicle.getVehicle_reg());
                    p2.setString(2, vehicle.getCustomer_id());
                    System.out.println(cbMechAssigned.getValue());
                    p2.setString(3,getMechanicID(cbMechAssigned.getValue()));
                    p2.setString(4, "PENDING");
                    p2.executeUpdate();
                }

                String jobSheetID = getJobSheetId(vehicle);


                String current_time_str = Helpers.getTime();
                for (int i = 0; i < Jobsheet.jobsheet.size(); i++) {

                    Jobsheet.Job job = new Jobsheet.Job();

                    job.setJobDesc(Jobsheet.jobsheet.get(i).getJobDesc());
                    job.setJobSummmery(Jobsheet.jobsheet.get(i).getJobSummmery());
                    job.setJobStatus(Jobsheet.jobsheet.get(i).getJobStatus());

                    String sql = "INSERT INTO VMS.JOBS (jobsheetid, job_summery, job_comments, job_status,date_entered, entered_by) VALUES (?,?,?,?,?,?)";
                    PreparedStatement p1 = con.prepareStatement(sql);
                    p1.setString(1, jobSheetID);
                    p1.setString(2, job.getJobSummmery());
                    p1.setString(3, job.getJobSummmery());
                    p1.setString(4, "PENDING");
                    p1.setString(5, current_time_str);
                    p1.setString(6, Auth.getUser());
                    p1.executeUpdate();

                    JobSheetGenerator.creatJobSheet(jobSheetID,customer,vehicle);
                }
                AlertDiaglog.infoBox("Jobs has been successfully saved in the system", "Job Saved", Alert.AlertType.INFORMATION);
                //        Jobsheet.toPDF(Jobsheet.jobsheet);
                Jobsheet.jobsheet.clear();

            } else {
                AlertDiaglog.infoBox("Looks like you have forgot to add jobs.. Try again after adding some jobs", "Jobs are not added", Alert.AlertType.WARNING);

            }
        } else {
            AlertDiaglog.infoBox("Please select a vehicle to register a job, You might have forgotten to click search button", "Vehicle not selected    ", Alert.AlertType.WARNING);

        }


    }

    public void reset(ActionEvent actionEvent) {
        jobSumTF.clear();
        addcommTA.clear();
        searchTF.clear();

    }

    public ObservableList<String> getMechanics() throws SQLException {
        List<String> names = new ArrayList<>();
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false", "root", "1234");
        String sql = "SELECT name FROM VMS.staffs where role = 200";
        PreparedStatement p1 = con.prepareStatement(sql);
        ResultSet resultSet = p1.executeQuery();

        while (resultSet.next()) {
            names.add(resultSet.getString(1));

        }
        ObservableList<String> ob = FXCollections.observableArrayList(names);

        return ob;
    }

    public static String getMechanicID(String name) throws SQLException {
        String id = null;
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false", "root", "1234");
        String sql = "SELECT id FROM VMS.staffs where name = ?";
        PreparedStatement p1 = con.prepareStatement(sql);
        p1.setString(1, name);
        ResultSet resultSet = p1.executeQuery();
        while (resultSet.next()) {
            id = resultSet.getString(1);
        }
        return id;
    }


    public static String getJobSheetId(Vehicle vehicle) throws SQLException {
        String id = null;
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false", "root", "1234");
        String sql = "SELECT id FROM VMS.JOBSHEET where vehicle_reg = ? and customer_id = ? and status = 'PENDING'";
        PreparedStatement p = con.prepareStatement(sql);
        p.setString(1, vehicle.getVehicle_reg());
        p.setString(2, vehicle.getCustomer_id());
        ResultSet resultSet = p.executeQuery();

        while (resultSet.next()) {
            id = resultSet.getString(1);
        }

        return id;

    }

    public static boolean checkJsPending(Vehicle vehicle) throws SQLException {
        boolean found = false;
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false", "root", "1234");
        String sql = "SELECT id FROM VMS.JOBSHEET where vehicle_reg = ? and customer_id = ? and status = 'PENDING'";
        PreparedStatement p = con.prepareStatement(sql);
        p.setString(1, vehicle.getVehicle_reg());
        p.setString(2, vehicle.getCustomer_id());
        ResultSet resultSet = p.executeQuery();

        while (resultSet.next()) {
            found=true;
        }

        return found;

    }


    public void back(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/service.fxml"));
        AnchorPane pane = fxmlLoader.load();
        rootPane.getChildren().setAll(pane);

    }


}
