
package Controller;

import java.awt.desktop.OpenFilesEvent;
import java.net.URL;
import java.util.ResourceBundle;

import Models.JobDocument;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class FileCompController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="fileNameLabel"
    private Label fileNameLabel; // Value injected by FXMLLoader

    
    private JobDocument doc;
    private openFIleListener openListener;
    @FXML
    void openFile(MouseEvent event) {
    	openListener.onClickListener(doc);
    }

    public void setData(JobDocument doc, openFIleListener openListener) {
    	this.doc = doc;
    	this.openListener = openListener;
    	fileNameLabel.setText(doc.getDocumentName());
    }
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert fileNameLabel != null : "fx:id=\"fileNameLabel\" was not injected: check your FXML file 'fileComponent.fxml'.";

    }

}
