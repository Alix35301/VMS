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

import java.io.File;
import java.io.IOException;

/**
 * @author ali
 * @created_on 7/28/20
 */
public class AnalystController {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label welcome;

    @FXML
    public void initialize(){
        welcome.setText("Welcome, "+ Auth.getUser());
    }

    public void analystMenu(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/searchTwitter.fxml"));                        FXMLLoader loader = new FXMLLoader(new File("src/resources/service.fxml").toURI().toURL());
        AnchorPane pane = fxmlLoader.load();
        rootPane.getChildren().setAll(pane);

    }

    public void importTestData(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/importTestData.fxml"));
        AnchorPane pane = fxmlLoader.load();
        rootPane.getChildren().setAll(pane);

    }



    public void logout(ActionEvent event) throws IOException {

        AlertDiaglog.infoBox("You have successfully logged out.","Loggout", Alert.AlertType.INFORMATION);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/Login.fxml"));
        AnchorPane pane = fxmlLoader.load();
        rootPane.getChildren().setAll(pane);

    }


    // TODO add graph to show word count
}
