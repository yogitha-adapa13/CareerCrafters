package Controller;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class CommonMethods {
    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static boolean showAlertDecision(String message) {
    	Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        ButtonType buttonTypeOK = new ButtonType("OK");
        ButtonType buttonTypeCancel = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);
        
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == buttonTypeOK;
    }
}
