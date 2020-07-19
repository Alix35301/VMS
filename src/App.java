import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Separator;
import javafx.stage.Stage;

import java.io.File;

public class App extends Application {

    @Override
    public void start(Stage primaryStage){
        try {

            FXMLLoader loader =new FXMLLoader(new File("src/resources/Login.fxml").toURI().toURL());

            Parent root =loader.load();
            root.setStyle("-fx-background-color: linear-gradient(#4848ff,rgba(58,139,121,0.04))");
            primaryStage.setResizable(false);
            primaryStage.setScene(new Scene(root, 827, 631));

            primaryStage.show();

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        launch(args);
    }
}
