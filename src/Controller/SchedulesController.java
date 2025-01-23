package Controller;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import com.mongodb.client.MongoDatabase;

import DataHandlers.JobApplicationHandler;
import DataHandlers.ScheduleHandler;
import DataHandlers.UserHandler;
import Models.Schedule;
import Models.User;
import MyCollections.AppPriorityQueue;
import MyCollections.MyQueue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SchedulesController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="accountsTab"
    private Label accountsTab; // Value injected by FXMLLoader

    @FXML // fx:id="addScheduleBtn"
    private Button addScheduleBtn; // Value injected by FXMLLoader

    @FXML // fx:id="applicationsTab"
    private Label applicationsTab; // Value injected by FXMLLoader

    @FXML // fx:id="dashboardTab"
    private Label dashboardTab; // Value injected by FXMLLoader

    @FXML // fx:id="documentsTab"
    private Label documentsTab; // Value injected by FXMLLoader

    @FXML // fx:id="schedulesGrid"
    private GridPane schedulesGrid; // Value injected by FXMLLoader

    @FXML // fx:id="user_display_label"
    private Label user_display_label; // Value injected by FXMLLoader
    
    private User sessionUser;
    private MongoDatabase sessionDb;
    private ScheduleHandler schedHandler;
    private UserHandler userHandler;
    private JobApplicationHandler jobHandler;
    private MyQueue<Schedule> scheduleQueue;
    
    public void start(User user, MongoDatabase database) {
    	this.sessionUser = user;
    	this.sessionDb = database;
    	
    	schedHandler = new ScheduleHandler(sessionDb);
    	userHandler = new UserHandler(sessionDb);
    	jobHandler = new JobApplicationHandler(sessionDb);
    	
    	scheduleQueue = new AppPriorityQueue<>(new ScheduleComparator());
    	assignScheduleData();
    	listSchedule();
    }
    
    private void assignScheduleData() {
    	List<Integer> scheduleIds = sessionUser.getScheduleIDs();
    	for(int i=0; i<scheduleIds.size(); i++) {
    		Schedule sched = schedHandler.getSchedule(scheduleIds.get(i));
    		scheduleQueue.enqueue(sched);
    	}
	}
    
    private void listSchedule() {
    	int row = 1;
    	int col = 0;
    	SchedListener deleteSched = new SchedListener() {
			
			@Override
			public void OnActionListener(Schedule schedule) {
				if(CommonMethods.showAlertDecision("Do you want to delete this schedule?")) {
					String deleteStatus = schedHandler.deleteSchedule(schedule.getScheduleID());
					CommonMethods.showAlert(deleteStatus);
					sessionUser.removeSchedule(schedule.getScheduleID());
					userHandler.updateUser(sessionUser.getUsername(), sessionUser);
				}
			}
		};
		
		SchedListener editSched = new SchedListener() {

			@Override
			public void OnActionListener(Schedule schedule) {
				editSchedule(schedule);
			}
			
		};
		
		Object[] scheObj = scheduleQueue.toArray();
		
		schedulesGrid.getChildren().clear();
		schedulesGrid.getRowConstraints().clear();
		schedulesGrid.getColumnConstraints().clear();
		try {
			for(int i=0; i<scheObj.length; i++) {
				Schedule sched = (Schedule) scheObj[i];
				FXMLLoader fxmlloader = new FXMLLoader();
				fxmlloader.setLocation(getClass().getResource("/FXML/ScheduleComponent.fxml"));
				AnchorPane anchorPane = fxmlloader.load();
				
				ScheduleCompController schedCompCont = fxmlloader.getController();
				schedCompCont.setData(sched, deleteSched, editSched);
				
				row++;
				schedulesGrid.add(anchorPane, col, row);
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void editSchedule(Schedule schedule) {
		CommonMethods.showAlert("Edit Schdule");
	}
	@FXML
    void addSchedule(ActionEvent event) {
    	Stage stage = new Stage();
    	try {
    		FXMLLoader fxmlloader = new FXMLLoader();
    		fxmlloader.setLocation(getClass().getResource("/FXML/AddScheduleComponent.fxml"));
    		Parent root = fxmlloader.load();
    		
    		AddScheduleController addSchedCont = fxmlloader.getController();
    		addSchedCont.start(sessionUser, sessionDb);
    		addSchedCont.setOnUserUpdate(user -> {
    			this.sessionUser = user;
    		});
    		
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.show();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }

	 @FXML
	 void openAccounts(MouseEvent event) {
		 accountsTab.getScene().getWindow().hide();
	    	Stage stage = new Stage();
	    	try {
	    		FXMLLoader fxmlloader = new FXMLLoader();
	    		fxmlloader.setLocation(getClass().getResource("/FXML/Account.fxml"));
	    		
	    		Parent root = fxmlloader.load();
	    		
	    		//Load Controller and initialize sessionUser and MongoDB database
	    		AccountController accCont = fxmlloader.getController();
	    		accCont.start(sessionUser, sessionDb);
	    		Scene scene = new Scene(root);
	    		
	    		stage.setScene(scene);
	    		stage.show();
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	    }
	
	    @FXML
	    void openApplications(MouseEvent event) {
	    	accountsTab.getScene().getWindow().hide();
	    	Stage stage = new Stage();
	    	try {
	    		FXMLLoader fxmlloader = new FXMLLoader();
	    		fxmlloader.setLocation(getClass().getResource("/FXML/Applications.fxml"));
	    		
	    		Parent root = fxmlloader.load();
	    		
	    		//Load Controller and initialize sessionUser and MongoDB database
	    		ApplicationsController appCont = fxmlloader.getController();
	    		appCont.start(sessionUser, sessionDb);
	    		Scene scene = new Scene(root);
	    		
	    		stage.setScene(scene);
	    		stage.show();
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	    }
	
	    @FXML
	    void openDashboard(MouseEvent event) {
	    	dashboardTab.getScene().getWindow().hide();
	    	Stage stage = new Stage();
	    	try {
				FXMLLoader fxmlloader = new FXMLLoader();
				fxmlloader.setLocation(getClass().getResource("/FXML/Dashboard.fxml"));
				
				Parent root = fxmlloader.load();
				
				DashboardController dashboardController = fxmlloader.getController();
				dashboardController.initialize(sessionUser, sessionDb);
				
	    		Scene scene = new Scene(root);
	    		
	    		stage.setScene(scene);
	    		stage.show();
	    	}catch(Exception e) {
	    		e.printStackTrace();
	    	}
	    }
	
	    @FXML
	    void openDocuments(MouseEvent event) {
	    	documentsTab.getScene().getWindow().hide();
	    	Stage stage = new Stage();
	    	try {
	    		FXMLLoader fxmlloader = new FXMLLoader();
	    		fxmlloader.setLocation(getClass().getResource("/FXML/Documents.fxml"));
	    		
	    		Parent root = fxmlloader.load();
	    		
	    		//Load Controller and initialize sessionUser and MongoDB database
	    		DocumentController docController = fxmlloader.getController();
	    		docController.start(sessionUser, sessionDb);
	    		Scene scene = new Scene(root);
	    		
	    		stage.setScene(scene);
	    		stage.show();
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	    }
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert accountsTab != null : "fx:id=\"accountsTab\" was not injected: check your FXML file 'Schedule.fxml'.";
        assert addScheduleBtn != null : "fx:id=\"addScheduleBtn\" was not injected: check your FXML file 'Schedule.fxml'.";
        assert applicationsTab != null : "fx:id=\"applicationsTab\" was not injected: check your FXML file 'Schedule.fxml'.";
        assert dashboardTab != null : "fx:id=\"dashboardTab\" was not injected: check your FXML file 'Schedule.fxml'.";
        assert documentsTab != null : "fx:id=\"documentsTab\" was not injected: check your FXML file 'Schedule.fxml'.";
        assert schedulesGrid != null : "fx:id=\"schedulesGrid\" was not injected: check your FXML file 'Schedule.fxml'.";
        assert user_display_label != null : "fx:id=\"user_display_label\" was not injected: check your FXML file 'Schedule.fxml'.";

    }

 // Custom comparator to compare schedules based on their dates
    private static class ScheduleComparator implements Comparator<Schedule> {
        @Override
        public int compare(Schedule s1, Schedule s2) {
            return s1.getScheduleDate().compareTo(s2.getScheduleDate());
        }
    }
}
