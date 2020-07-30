package controller;

import components.AlertDiaglog;
import components.Auth;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * @author ali
 * @created_on 7/15/20
 */
public class ServiceAdvisorController {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label welcome;

    @FXML
    public void initialize(){
        welcome.setText("Welcome, "+ Auth.getUser());
    }
    public void register(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/register.fxml"));
        AnchorPane pane = fxmlLoader.load();
        rootPane.getChildren().setAll(pane);

    }

    public void register_vehicle(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/addvehicle.fxml"));
        AnchorPane pane = fxmlLoader.load();
        rootPane.getChildren().setAll(pane);

    }

    public void createJobSheet(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/jobsheet.fxml"));
        AnchorPane pane = fxmlLoader.load();
        rootPane.getChildren().setAll(pane);

    }

    public void payment(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/payment.fxml"));
        AnchorPane pane = fxmlLoader.load();
        rootPane.getChildren().setAll(pane);

    }

    public void logout(ActionEvent event) throws IOException {

        AlertDiaglog.infoBox("You have successfully logged out.","Loggout", Alert.AlertType.INFORMATION);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/Login.fxml"));
        AnchorPane pane = fxmlLoader.load();
        rootPane.getChildren().setAll(pane);

    }
}
