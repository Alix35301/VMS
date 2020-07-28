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

/**
 * @author ali
 * @created_on 7/18/20
 */
public class PaymentsController {
    @FXML
    private ChoiceBox<String> paymentOptionCb;
    @FXML
    private TableView paymentsTable;
    @FXML
    private TextField productName;
    @FXML
    private TableView jobsheetTable;
    @FXML
    private TextField searchTF;
    @FXML
    private TextField amountGiven;
    @FXML
    private Text totalTxt;

    @FXML
    private CheckBox labourCheckBox;
    @FXML
    private Text modelTxt, emailTxt, contactTxt, cust_nameTxt, chassisTxt, reg_dateTxt;
    public String jobsheetid = null;

    Invoice invoice = new Invoice();
    Customer customer = new Customer();
    Vehicle vehicle = new Vehicle();
    Jobsheet jobsheet = new Jobsheet();

    ObservableList<String> paymentOption = FXCollections.observableArrayList("Cash", "Card");

    enum InvoiceStatus{
        PENDING,
        PAID
    }

    @FXML
    public void initialize() {

        TableColumn<String, Product> column1 = new TableColumn<>("Product Id");
        column1.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<String, Product> column2 = new TableColumn<>("Product/Service Name");
        column2.setCellValueFactory(new PropertyValueFactory<>("productCode"));

        TableColumn<String, Product> column3 = new TableColumn<>("Price");
        column3.setCellValueFactory(new PropertyValueFactory<>("price"));


        initilizeJobsheetTable();


        paymentsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        jobsheetTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        paymentOptionCb.setItems(paymentOption);

        paymentsTable.getColumns().addAll(column1, column2, column3);
        paymentsTable.setPlaceholder(new Label("You have not added any products yet.."));
        jobsheetTable.setPlaceholder(new Label("No jobs found for this vehicle"));


    }

    public void initilizeJobsheetTable() {

        TableColumn<String, Jobsheet.Job> column1 = new TableColumn<>("Job Summery");
        column1.setCellValueFactory(new PropertyValueFactory<>("jobSummmery"));

        TableColumn<String, Jobsheet.Job> column2 = new TableColumn<>("Job Comments");
        column2.setCellValueFactory(new PropertyValueFactory<>("jobDesc"));

        TableColumn<String, Jobsheet.Job> column3 = new TableColumn<>("Status");
        column3.setCellValueFactory(new PropertyValueFactory<>("jobStatus"));

        jobsheetTable.getColumns().addAll(column1, column2, column3);
    }


    public void searchVehicle(ActionEvent event) throws IOException, SQLException {
        boolean registered = false;

        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false", "root", "1234");
        String sql = "SELECT customer_id, model, vehicle_reg, chassis_num, reg_date FROM VMS.VEHICLES WHERE vehicle_reg = ?";
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        preparedStatement.setString(1, searchTF.getText());
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            registered = true;
            vehicle.setCustomer_id(resultSet.getString(1));
            vehicle.setModel(resultSet.getString(2));
            vehicle.setVehicle_reg(resultSet.getString(3));
            vehicle.setChassis_num(resultSet.getString(4));
            vehicle.setReg_date(resultSet.getString(5));
        }
        if (registered) {
            String sql2 = "SELECT cust_name, email, phone, address FROM VMS.CUSTOMERS WHERE customer_id=?";
            PreparedStatement statement = con.prepareStatement(sql2);
            statement.setString(1, vehicle.getCustomer_id());
            System.out.println(statement.toString());
            ResultSet resultSet1 = statement.executeQuery();
            while (resultSet1.next()) {
                customer.setId(vehicle.getCustomer_id());
                customer.setName(resultSet1.getString(1));
                customer.setEmail(resultSet1.getString(2));
                customer.setPhone(resultSet1.getString(3));
                customer.setAddress(resultSet1.getString(4));
            }
            contactTxt.setVisible(true);
            emailTxt.setVisible(true);
            modelTxt.setVisible(true);
            cust_nameTxt.setVisible(true);
            chassisTxt.setVisible(true);
            reg_dateTxt.setVisible(true);
//
            modelTxt.setText(vehicle.getModel());
            cust_nameTxt.setText(customer.getName());
            chassisTxt.setText(vehicle.getChassis_num());
            reg_dateTxt.setText(vehicle.getReg_date());
            emailTxt.setText(customer.getEmail());
            contactTxt.setText(customer.getPhone());
            jobsheetTable.getItems().clear();
            buildData();


        } else if (searchTF.getText().trim().isEmpty()) {

            AlertDiaglog.infoBox("Looks like you forgot to type the vehicle registraion number!", "Empty search", Alert.AlertType.WARNING);


        } else {
            AlertDiaglog.infoBox("Could not locate the vehicle in our system. Please check if its registered", "Vehicle not registered", Alert.AlertType.WARNING);

        }


    }


    public Jobsheet getJobsheet(Vehicle vehicle) throws SQLException {
        Connection conn =Helpers.getConnection();
        String sql1 = "SELECT * from VMS.JOBSHEET where vehicle_reg=? and status = 'PENDING'";
        PreparedStatement preparedStatement = conn.prepareStatement(sql1);
        preparedStatement.setString(1, vehicle.getVehicle_reg());
        System.out.println(preparedStatement.toString());
        ResultSet r = preparedStatement.executeQuery();

        while (r.next()) {
            jobsheet.setId(r.getString(1));
            jobsheet.setVehicle_reg(r.getString(2));
            jobsheet.setCustomer_id(r.getString(3));
            jobsheet.setMech_assigned(r.getString(4));
            jobsheet.setStatus(r.getString(5));

        }
        return jobsheet;
    }

    public ResultSet getJobs() throws SQLException {
        Connection conn = Helpers.getConnection();
        String sql = "SELECT job_summery, job_comments, job_status from VMS.JOBS where jobsheetId=?";
        PreparedStatement p = conn.prepareStatement(sql);
        p.setString(1, jobsheet.getId());
        System.out.println(p.toString());
        ResultSet resultSet = p.executeQuery();
        return resultSet;
    }


    public void buildData() throws SQLException {
        Jobsheet jobsheet = getJobsheet(vehicle);
        // checks if there is acitve jobsheet to populate table
        if (jobsheet.getId()==null){
            AlertDiaglog.infoBox("There is no active Jobsheet for this" +
                "vehicle, please create a new jobsheet to coninue","No active Jobsheet",
                Alert.AlertType.WARNING);
        }else {
            ResultSet resultSet =getJobs();
            Jobsheet.Job job = new Jobsheet.Job();
            while (resultSet.next()) {

                job.setJobSummmery(resultSet.getString(1));
                System.out.println(job.getJobSummmery());
                job.setJobDesc(resultSet.getString(2));
                job.setJobStatus(resultSet.getString(3));
                jobsheetTable.getItems().add(job);
            }
        }
    }


    public void addService(ActionEvent event) throws SQLException {
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        if(jobsheet.getId()==null){
            AlertDiaglog.infoBox("There is no active Jobsheet for this" +
                    "vehicle, please create a new jobsheet to coninue","No active Jobsheet",
                Alert.AlertType.WARNING);

        }else{
            Connection con =Helpers.getConnection();
            // creating invoice if not already created

            invoice.setCustomerId(customer.getId());
            invoice.setVehicleReg(vehicle.getVehicle_reg());
            invoice.setId(createInvoice(invoice));
            // creating order under the new / old invoice id
            Product product = getProduct(productName.getText());
            if (product.getPrice() != null) {
                Order order = new Order(product, invoice);
                createOrder(invoice, order);
                paymentsTable.getItems().add(product);

                ArrayList<Product> products = getAllProducts();
                float total = InvoiceGenerator.calculateTotal(products);
                float tax = InvoiceGenerator.calTax(total,6);
                totalTxt.setText("MVR "+String.valueOf(total+tax));

            } else {
                AlertDiaglog.infoBox("Item not found", ".", Alert.AlertType.INFORMATION);

            }

        }


    }

    public String createInvoice(Invoice invoice) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false", "root", "1234");
        if (!invoiceExists(invoice)) {
            String query = "INSERT INTO INVOICE (customer_id, vehicle_reg, created_time, created_user, status)" +
                "VALUES (?,?,?,?,?)";
            PreparedStatement p = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            invoice.setCreatedDate(Helpers.getTime());
            invoice.setCustomerId(customer.getId());
            invoice.setVehicleReg(vehicle.getVehicle_reg());
            invoice.setCreatedUser(Auth.getUser());
            invoice.setStatus(InvoiceStatus.PENDING.toString());
            p.setString(1, invoice.getCustomerId());
            p.setString(2, invoice.getVehicleReg());
            p.setString(3, invoice.getCreatedDate());
            p.setString(4, invoice.getCreatedUser());
            p.setString(5, invoice.getStatus());
            p.executeUpdate();

            try (ResultSet generatedKeys = p.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    invoice.setId(String.valueOf(generatedKeys.getInt(1)));
//                    System.out.println(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating invoice failed, no ID obtained.");
                }
            }
        } else {
            invoice.setId(getInvoiceId(invoice));
        }
        return invoice.getId();
    }

    public static boolean invoiceExists(Invoice invoice) throws SQLException {
        Boolean exists = false;
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false", "root", "1234");
        String query = "SELECT id FROM VMS.INVOICE WHERE customer_id = ? AND vehicle_reg =? and status = 'PENDING'";
        PreparedStatement p = con.prepareStatement(query);
        p.setString(1, invoice.getCustomerId());
        p.setString(2, invoice.getVehicleReg());
        System.out.println(p.toString());
        ResultSet r = p.executeQuery();

        while (r.next()) {
            exists = true;
        }
        return exists;
    }

    public static String getInvoiceId(Invoice invoice) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false", "root", "1234");
        String query = "SELECT id, created_time, status FROM VMS.INVOICE WHERE customer_id = ? AND vehicle_reg =? and status = 'PENDING'";
        PreparedStatement p = con.prepareStatement(query);
        p.setString(1, invoice.getCustomerId());
        p.setString(2, invoice.getVehicleReg());
        ResultSet r = p.executeQuery();

        while (r.next()) {
            invoice.setId(String.valueOf(r.getInt(1)));
            invoice.setCreatedDate(r.getString(2));
            invoice.setStatus(r.getString(3));
        }
        return invoice.getId();
    }


    public void createOrder(Invoice invoice, Order order) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false", "root", "1234");
        String query = "INSERT INTO VMS.ORDER (invoice_id, product_id) VALUES (?, ?)";
        PreparedStatement p = con.prepareStatement(query);
        p.setString(1, invoice.getId());
        p.setString(2, order.getProductId());
        p.executeUpdate();


    }

    public Product getProduct(String productCode) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false", "root", "1234");
        String query = "SELECT id, product_code, price FROM VMS.PRODUCTS  where product_code=?";
        PreparedStatement p = con.prepareStatement(query);
        p.setString(1, productCode);
        ResultSet r = p.executeQuery();
        Product product = new Product();
        while (r.next()) {
            product = new Product(r.getString(1), r.getString(2), r.getString(3));
        }

        return product;
    }

    public ArrayList<Product> getAllProducts() throws SQLException {
        ArrayList<Product> products = new ArrayList<>();
        Connection con = Helpers.getConnection();
        String query = "SELECT product_id FROM VMS.ORDER  where invoice_id=?";
        PreparedStatement preparedStatement = con.prepareStatement(query);
        preparedStatement.setString(1, invoice.getId());
        System.out.println(preparedStatement.toString());
        ResultSet resultSet = preparedStatement.executeQuery();
        Product product = new Product();

        while (resultSet.next()) {
            product = new Product();
            product.setId(resultSet.getString(1));
            String q = "SELECT product_code, price FROM VMS.PRODUCTS  where id=?";
            PreparedStatement p = con.prepareStatement(q);
            p.setString(1, product.getId());
//            System.out.println(p.toString());
            ResultSet r = p.executeQuery();
            while (r.next()) {
                product.setId(product.getId());
                product.setProductCode(r.getString(1));
                System.out.println(r.getString(1));
                product.setPrice(r.getString(2));


            }
            products.add(product);


        }


        return products;
    }

    public void generateInvoice() {
        try {
            ArrayList<Product> products = getAllProducts();
            System.out.println(products.size());
            System.out.println(invoice.getCreatedDate());
            InvoiceGenerator.createInvoice(products, customer, vehicle, invoice);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateInvoice(ActionEvent event) throws SQLException {
        if(jobsheet.getId()==null){
            AlertDiaglog.infoBox("There is no active Jobsheet for this" +
                    "vehicle, please create a new jobsheet to coninue","No active Jobsheet",
                Alert.AlertType.WARNING);

        }else{

            float total = InvoiceGenerator.calculateTotal(getAllProducts());
            float tax = InvoiceGenerator.calTax(total, 6);
            float net = total + tax;
            invoice.setAmountGiven(amountGiven.getText());
            invoice.setPaymentMethod(paymentOptionCb.getValue());
            invoice.setCreatedUser(Auth.getUser());

//
            if (isValid(amountGiven.getText(),net)) {
                Connection conn = Helpers.getConnection();
                String sql = "UPDATE VMS.INVOICE SET status =?, total =?, amount_given =?, payment_type=? where id=?";
                try {
                    PreparedStatement p = conn.prepareStatement(sql);

                    p.setString(1, String.valueOf(InvoiceStatus.PAID));
                    p.setString(2, String.valueOf(net));
                    p.setString(3, invoice.getAmountGiven());
                    p.setString(4, invoice.getPaymentMethod());
                    p.setString(5, invoice.getId());
                    System.out.println(p.toString());
                    p.executeUpdate();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                generateInvoice();
                updateJobsheetStatus();
                AlertDiaglog.infoBox("Successfully added the payment.","Payment Success", Alert.AlertType.INFORMATION);
            }else {
                AlertDiaglog.infoBox("Please make sure the value is numeric and is greater than the total","Invalid Input", Alert.AlertType.WARNING);
            }

        }

    }

    public boolean isValid(String input, float total) {
        boolean valid = true;
        float amount = 0;
        try {
            amount = Float.parseFloat(input);
            System.out.println(amount+" "+total);
            if (amount < total){
                valid = false;
            }else if (amount == total){
                valid=true;
            }

        } catch (Exception e) {
            valid = false;
        }
        return valid;

    }

    public void updateJobsheetStatus() throws SQLException {
        Connection conn = Helpers.getConnection();
        String sql = "UPDATE VMS.JOBSHEET SET status=? where id = ?";
        PreparedStatement p = conn.prepareStatement(sql);
        p.setString(1, Jobsheet.JobSheetStatsus.UPDATED.toString());
        p.setString(2, jobsheetid);
        p.executeUpdate();
    }

    public MechRate getMechRate() throws SQLException {
        Connection conn = Helpers.getConnection();
        String sql = "SELECT * FROM MECH_RATES WHERE mech_id  =? ";
        PreparedStatement p = conn.prepareStatement(sql);
        p.setString(1, jobsheet.getMech_assigned());
        ResultSet resultSet = p.executeQuery();
        MechRate mechRate = new MechRate();
        while(resultSet.next()){
            mechRate.setId(resultSet.getString(1));
            mechRate.setMech_id(resultSet.getString(2));
            mechRate.setRate(resultSet.getString(3));
        }
        return mechRate;
    }

    public void checkbox(ActionEvent event){
        if(labourCheckBox.isSelected()){
            System.out.println(paymentsTable.);
            // add cost to table
            // add that order table sql

        }else{
            System.out.println();
            // delete if exists in the table
            // delete if exisit in the table sql
        }
    }


}
