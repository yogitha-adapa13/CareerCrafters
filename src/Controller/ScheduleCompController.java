/**
 * Sample Skeleton for 'ScheduleComponent.fxml' Controller Class
 */

package Controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.mongodb.client.MongoDatabase;

import Models.Schedule;
import Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ScheduleCompController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="Scheduletype"
    private Label Scheduletype; // Value injected by FXMLLoader

    @FXML // fx:id="scheduleDT"
    private Label scheduleDT; // Value injected by FXMLLoader

    @FXML // fx:id="scheduleName"
    private Label scheduleName; // Value injected by FXMLLoader

    @FXML // fx:id="schedule_delete_btn"
    private Button schedule_delete_btn; // Value injected by FXMLLoader

    @FXML // fx:id="schedule_edit_btn"
    private Button schedule_edit_btn; // Value injected by FXMLLoader
    
    private Schedule schedule;
    private SchedListener deleteSchedListener;
    private SchedListener editSchedListener;
    
    
    
    public void setData(Schedule schedule, SchedListener deleteSched, SchedListener editSched) {
    	this.deleteSchedListener = deleteSched;
    	this.editSchedListener = editSched;
    	this.schedule = schedule;
    	
    	scheduleName.setText(schedule.getScheduleName());
    	Scheduletype.setText(schedule.getScheduleType());
    	scheduleDT.setText(schedule.getScheduleDate());
    }
    @FXML
    void deleteSchedule(ActionEvent event) {
    	deleteSchedListener.OnActionListener(schedule);
    }

    @FXML
    void editSchedule(ActionEvent event) {
    	editSchedListener.OnActionListener(schedule);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert Scheduletype != null : "fx:id=\"Scheduletype\" was not injected: check your FXML file 'ScheduleComponent.fxml'.";
        assert scheduleDT != null : "fx:id=\"scheduleDT\" was not injected: check your FXML file 'ScheduleComponent.fxml'.";
        assert scheduleName != null : "fx:id=\"scheduleName\" was not injected: check your FXML file 'ScheduleComponent.fxml'.";
        assert schedule_delete_btn != null : "fx:id=\"schedule_delete_btn\" was not injected: check your FXML file 'ScheduleComponent.fxml'.";
        assert schedule_edit_btn != null : "fx:id=\"schedule_edit_btn\" was not injected: check your FXML file 'ScheduleComponent.fxml'.";

    }

}
