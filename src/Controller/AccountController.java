/**
 * Sample Skeleton for 'Account.fxml' Controller Class
 */

package Controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.mongodb.client.MongoDatabase;

import Models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class AccountController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="account_edit_button"
    private Button account_edit_button; // Value injected by FXMLLoader

    @FXML // fx:id="applicationLabel"
    private Label applicationLabel; // Value injected by FXMLLoader

    @FXML // fx:id="dashboardLabel"
    private Label dashboardLabel; // Value injected by FXMLLoader

    @FXML // fx:id="documents_tab_field"
    private Label documents_tab_field; // Value injected by FXMLLoader

    @FXML // fx:id="emailLabel"
    private Label emailLabel; // Value injected by FXMLLoader

    @FXML // fx:id="phoneLabel"
    private Label phoneLabel; // Value injected by FXMLLoader

    @FXML // fx:id="schedulesLbel"
    private Label schedulesLbel; // Value injected by FXMLLoader

    @FXML // fx:id="userFullName"
    private Label userFullName; // Value injected by FXMLLoader

    @FXML // fx:id="usernameLabel"
    private Label usernameLabel; // Value injected by FXMLLoader

    private User sessionUser;
    private MongoDatabase database;
    
    public void start(User user, MongoDatabase database) {
    	this.sessionUser = user;
    	this.database = database;
    	
    	setData();
    }
    private void setData() {
		userFullName.setText(sessionUser.getFullName());
		usernameLabel.setText(sessionUser.getUsername());
		emailLabel.setText(sessionUser.getEmail());
		phoneLabel.setText(sessionUser.getPhone());
	}
	@FXML
    void openApplication(MouseEvent event) {
    	applicationLabel.getScene().getWindow().hide();
    	Stage stage = new Stage();
    	try {
    		FXMLLoader fxmlloader = new FXMLLoader();
    		fxmlloader.setLocation(getClass().getResource("/FXML/Applications.fxml"));
    		
    		Parent root = fxmlloader.load();
    		
    		//Load Controller and initialize sessionUser and MongoDB database
    		ApplicationsController appCont = fxmlloader.getController();
    		appCont.start(sessionUser, database);
    		Scene scene = new Scene(root);
    		
    		stage.setScene(scene);
    		stage.show();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }

    @FXML
    void openDashboard(MouseEvent event) {
    	dashboardLabel.getScene().getWindow().hide();
    	Stage stage = new Stage();
    	try {
			FXMLLoader fxmlloader = new FXMLLoader();
			fxmlloader.setLocation(getClass().getResource("/FXML/Dashboard.fxml"));
			
			Parent root = fxmlloader.load();
			
			DashboardController dashboardController = fxmlloader.getController();
			dashboardController.initialize(sessionUser, database);
			
    		Scene scene = new Scene(root);
    		
    		stage.setScene(scene);
    		stage.show();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }

    @FXML
    void openDocuments(MouseEvent event) {
    	documents_tab_field.getScene().getWindow().hide();
    	Stage stage = new Stage();
    	try {
    		FXMLLoader fxmlloader = new FXMLLoader();
    		fxmlloader.setLocation(getClass().getResource("/FXML/Documents.fxml"));
    		
    		Parent root = fxmlloader.load();
    		
    		//Load Controller and initialize sessionUser and MongoDB database
    		DocumentController docController = fxmlloader.getController();
    		docController.start(sessionUser, database);
    		Scene scene = new Scene(root);
    		
    		stage.setScene(scene);
    		stage.show();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }

    @FXML
    void openSchedules(MouseEvent event) {
    	schedulesLbel.getScene().getWindow().hide();
    	Stage stage = new Stage();
    	try {
    		FXMLLoader fxmlloader = new FXMLLoader();
    		fxmlloader.setLocation(getClass().getResource("/FXML/Schedule.fxml"));
    		
    		Parent root = fxmlloader.load();
    		
    		//Load Controller and initialize sessionUser and MongoDB database
    		SchedulesController schedCont = fxmlloader.getController();
    		schedCont.start(sessionUser, database);
    		Scene scene = new Scene(root);
    		
    		stage.setScene(scene);
    		stage.show();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert account_edit_button != null : "fx:id=\"account_edit_button\" was not injected: check your FXML file 'Account.fxml'.";
        assert applicationLabel != null : "fx:id=\"applicationLabel\" was not injected: check your FXML file 'Account.fxml'.";
        assert dashboardLabel != null : "fx:id=\"dashboardLabel\" was not injected: check your FXML file 'Account.fxml'.";
        assert documents_tab_field != null : "fx:id=\"documents_tab_field\" was not injected: check your FXML file 'Account.fxml'.";
        assert emailLabel != null : "fx:id=\"emailLabel\" was not injected: check your FXML file 'Account.fxml'.";
        assert phoneLabel != null : "fx:id=\"phoneLabel\" was not injected: check your FXML file 'Account.fxml'.";
        assert schedulesLbel != null : "fx:id=\"schedulesLbel\" was not injected: check your FXML file 'Account.fxml'.";
        assert userFullName != null : "fx:id=\"userFullName\" was not injected: check your FXML file 'Account.fxml'.";
        assert usernameLabel != null : "fx:id=\"usernameLabel\" was not injected: check your FXML file 'Account.fxml'.";

    }

}
