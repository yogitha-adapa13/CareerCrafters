/**
 * Sample Skeleton for 'AddScheduleComponent.fxml' Controller Class
 */

package Controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.mongodb.client.MongoDatabase;

import Models.Schedule;
import Models.User;

import DataHandlers.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddScheduleController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="ScheduleName"
    private TextField ScheduleName; // Value injected by FXMLLoader

    @FXML // fx:id="scheduleDate"
    private DatePicker scheduleDate; // Value injected by FXMLLoader

    @FXML // fx:id="scheduleDescTF"
    private TextField scheduleDescTF; // Value injected by FXMLLoader

    @FXML // fx:id="scheduleType"
    private ComboBox<String> scheduleType; // Value injected by FXMLLoader

    @FXML // fx:id="submitBtn"
    private Button submitBtn; // Value injected by FXMLLoader
    
    private Schedule newSchedule;
    private User sessionUser;
    private MongoDatabase sessionDb;
    private ScheduleHandler schedHandler;
    private UserHandler userHandler;
    
    private Consumer<User> userUpdateListener;

    // Method to set the listener for user updates
    public void setOnUserUpdate(Consumer<User> listener) {
        this.userUpdateListener = listener;
    }

    // Method to notify the listener about user updates
    private void notifyUserUpdate(User user) {
        if (userUpdateListener != null) {
            userUpdateListener.accept(user);
        }
    }
    
    public void start(User user, MongoDatabase database) {
    	this.sessionUser = user;
    	this.sessionDb = database;
    	
    	schedHandler = new ScheduleHandler(database);
    	userHandler = new UserHandler(database);
    	newSchedule = new Schedule();
    }

    @FXML
    void handleSubmit(ActionEvent event) {
    	if(checkFields()) {
    		addData();
    		String insertStatus = schedHandler.postSchedule(newSchedule);
    		sessionUser.addSchedule(newSchedule.getScheduleID());
    		userHandler.updateUser(sessionUser.getUsername(), sessionUser);
    		CommonMethods.showAlert(insertStatus);
    		
    		Stage stage = (Stage) submitBtn.getScene().getWindow();
    		stage.close();
    		
    	}else {
    		CommonMethods.showAlert("Enter all the fields");
    	}
    }

    private void addData() {
    	Random rand = new Random();
		newSchedule.setScheduleID(rand.nextInt(100)+sessionUser.getScheduleIDs().size()+301);
		newSchedule.setScheduleName(ScheduleName.getText());
		newSchedule.setScheduleType( scheduleType.getSelectionModel().getSelectedItem().toString());
		newSchedule.setScheduleDescription(scheduleDescTF.getText());
		newSchedule.setScheduleDate(getFormattedDate());
	}

	private boolean checkFields() {
		if(ScheduleName.getText().isEmpty() || scheduleDescTF.getText().isEmpty() || scheduleDate.getValue() == null || scheduleType.getSelectionModel().getSelectedItem() == null) {
			return false;
		}else {
			return true;
		}
	}

	private String getFormattedDate() {
		LocalDate selectedDate = scheduleDate.getValue();

        // Format the selected date using a DateTimeFormatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return selectedDate.format(formatter);
	}
	@FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert ScheduleName != null : "fx:id=\"ScheduleName\" was not injected: check your FXML file 'AddScheduleComponent.fxml'.";
        assert scheduleDate != null : "fx:id=\"scheduleDate\" was not injected: check your FXML file 'AddScheduleComponent.fxml'.";
        assert scheduleDescTF != null : "fx:id=\"scheduleDescTF\" was not injected: check your FXML file 'AddScheduleComponent.fxml'.";
        assert scheduleType != null : "fx:id=\"scheduleType\" was not injected: check your FXML file 'AddScheduleComponent.fxml'.";
        assert submitBtn != null : "fx:id=\"submitBtn\" was not injected: check your FXML file 'AddScheduleComponent.fxml'.";
        
        scheduleType.getItems().addAll("Interview", "Follow-ups", "Email", "General Task");
    }

}
