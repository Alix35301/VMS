package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.IOException;

/**
 * @author ali
 * @created_on 7/28/20
 */
public class AnalystController {
    @FXML
    private AnchorPane rootPane;

    public void analystMenu(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(new File("src/resources/searchTwitter.fxml").toURI().toURL());
        AnchorPane pane = loader.load();
        rootPane.getChildren().setAll(pane);

    }
}
