package controller;

import components.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class LoginController extends Auth {

    ObservableList<String> choiceBoxLoginList = FXCollections.observableArrayList("Staff", "Customer");

    @FXML
    private Label lblStatus;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private ChoiceBox<String> choiceBoxLogin;
    @FXML
    private Label lblcustomer;
    public static String username;

    public AnchorPane getRootPane() {
        return rootPane;
    }

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label lbllogout;


    public static String getUsername() {
        return username;
    }

    @FXML
    public void initialize() {
        lbllogout.setVisible(false);
        choiceBoxLogin.setItems(choiceBoxLoginList);
        choiceBoxLogin.getSelectionModel().select("Customer");
    }

    public void login(ActionEvent event) throws SQLException, IOException {

        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false", "root", "1234");
        User user = new User(txtUsername.getText(), txtPassword.getText(), choiceBoxLogin.getValue());
        if (!user.getEmail().equals("") || !user.getPassword().equals("")) {
            if (Auth.Authenticate(user)) {
                if (choiceBoxLogin.getValue() == "Customer") {
                    FXMLLoader loader = new FXMLLoader(new File("src/resources/customer.fxml").toURI().toURL());
                    //            lblcustomer.setText(lblcustomer.getText()+" "+customer.getEmail());
                    AnchorPane pane = loader.load();
                    rootPane.getChildren().setAll(pane);
                } else {
                    // selecting role of the user based on the query
                    PreparedStatement preparedStatement;
                    preparedStatement = con.prepareStatement("SELECT * FROM VMS.staffs WHERE email=?");
                    preparedStatement.setString(1, user.getEmail());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    String role = null;
                    while (resultSet.next()) {
                        role = resultSet.getString("role");
                    }
                    // setting view based on the role from query
                    if (Integer.parseInt(role) == 200) {
                        FXMLLoader loader = new FXMLLoader(new File("src/resources/updatejobsheet.fxml").toURI().toURL());
                        AnchorPane pane = loader.load();
                        rootPane.getChildren().setAll(pane);
                    } else if (Integer.parseInt(role) == 100) {
                        FXMLLoader loader = new FXMLLoader(new File("src/resources/service.fxml").toURI().toURL());
                        AnchorPane pane = loader.load();
                        rootPane.getChildren().setAll(pane);
                    }
                }
            } else {
                // Alert diaglog if auth fails
                AlertDiaglog.infoBox("Please check the credentials and try again :)", "Incorrect username/password", Alert.AlertType.WARNING);
            }
        }else {
            // Alert dialog if both username and password is empty
            AlertDiaglog.infoBox("Please enter username and password! These cannot be empty :(", "Empty fields ", Alert.AlertType.WARNING);

        }

    }


    public static void main(String[] args) {


    }
}
