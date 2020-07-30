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
            primaryStage.setResizable(false);
            primaryStage.setScene(new Scene(root, 1000, 800));
            primaryStage.show();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
