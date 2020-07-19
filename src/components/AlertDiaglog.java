package components;
/**
 * @author ali
 * @created_on 4/19/20
 */

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.application.Platform;

public class AlertDiaglog
{

    public static void infoBox(String infoMessage, String titleBar,AlertType alertType)
    {
        /* By specifying a null headerMessage String, we cause the dialog to
           not have a header */
        infoBox(infoMessage, titleBar, null,alertType);
    }

    public static void infoBox(String infoMessage, String titleBar, String headerMessage,AlertType alertType)
    {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(alertType);
        alert.setTitle(titleBar);
        alert.setHeaderText(headerMessage);
        alert.setContentText(infoMessage);
        alert.showAndWait();
    }
}
