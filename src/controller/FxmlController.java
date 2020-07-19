package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * @author ali
 * @created_on 4/13/20
 */
public class FxmlController {
    @FXML
    AnchorPane rootPane;


    @FXML
    public void load(String fxml) throws IOException {
        if(fxml.contains("customer")){
            AnchorPane customerPane =FXMLLoader.load(getClass().getResource("resources/customer.fxml"));
            rootPane.getChildren().setAll(customerPane);

        }

    }

}
