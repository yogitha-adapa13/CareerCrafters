package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import com.mongodb.client.MongoDatabase;

import Alogrithms.AppQuickSort;
import DataHandlers.JobApplicationHandler;
import DataHandlers.UserHandler;
import Models.JobApplication;
import Models.Schedule;
import Models.User;
import MyCollections.AppArray;
import MyCollections.AppBag;
import MyCollections.JobApplicationBinaryTree;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ApplicationsController {
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="account_tab"
    private Label account_tab; // Value injected by FXMLLoader

    @FXML // fx:id="applicationGrid"
    private GridPane applicationGrid; // Value injected by FXMLLoader

    @FXML // fx:id="applications_tab"
    private Label applications_tab; // Value injected by FXMLLoader

    @FXML // fx:id="documents_tab"
    private Label documents_tab; // Value injected by FXMLLoader

    @FXML // fx:id="schedules_tab"
    private Label schedules_tab; // Value injected by FXMLLoader

    @FXML // fx:id="searchBar"
    private TextField searchBar; // Value injected by FXMLLoader

    @FXML // fx:id="searchBtn"
    private Button searchBtn; // Value injected by FXMLLoader

    @FXML // fx:id="sortComboBox"
    private ComboBox sortComboBox; // Value injected by FXMLLoader

    @FXML // fx:id="user_display_field"
    private Label user_display_field; // Value injected by FXMLLoader
    
    @FXML
    private Button addJobBtn;
    
    private AppBag<JobApplication> jobs;
    private User sessionUser;
    private MongoDatabase database;
    private JobApplicationHandler jobHandler;
    private UserHandler userHandler;
    
  //starting the controller with necessary data
    public void start(User user, MongoDatabase db) {
    	this.sessionUser = user;
    	this.database = db;
    	
    	userHandler = new UserHandler(db);
    	
    	//Change text fields
    	user_display_field.setText(sessionUser.getFirstName());
    	applications_tab.setText("Dashboard");
    	
    	List<Integer> applicationIds = sessionUser.getApplicationIds();
    	jobs = new AppArray<>();
    	jobHandler = new JobApplicationHandler(database);
    	if(!applicationIds.isEmpty()) {
    		for(int i=0; i<applicationIds.size(); i++) {
        		int jobId = applicationIds.get(i);
        		jobs.add(jobHandler.getJobApplication(jobId));
        	}
        	listApplications(jobs);
    	}
    }
    
    @FXML
    void addJobApplication(ActionEvent event) {
    	Stage stage = new Stage();
    	try {
			FXMLLoader fxmlloader = new FXMLLoader();
			fxmlloader.setLocation(getClass().getResource("/FXML/Applications_dialogbox.fxml"));
			
			Parent root = fxmlloader.load();
			
			AddApplicationController addJobController = fxmlloader.getController();
			addJobController.start(sessionUser, database);
			
    		Scene scene = new Scene(root);
    		
    		stage.setScene(scene);
    		stage.show();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }

    @FXML
    void searchApplications(ActionEvent event) {
    	String searchParams = searchBar.getText();
    	JobApplicationBinaryTree jobBinTree = new JobApplicationBinaryTree();
    	for(int i =0; i< jobs.getCurrentSize(); i++) {
    		jobBinTree.insert(jobs.get(i));
		}
    	
    	listApplications(jobBinTree.search(searchParams));
    }

    @FXML
    void sortApplications(ActionEvent event) {
    	String selectedField = sortComboBox.getSelectionModel().getSelectedItem().toString();
    	switch(selectedField) {
    	case "Company Name":
			AppQuickSort.quickSort(jobs, "companyName");
			break;
		case "Job Title":
			AppQuickSort.quickSort(jobs, "jobTitle");
			break;
		case "appliedDate":
			AppQuickSort.quickSort(jobs, "appliedDate");
			break;
    	}
    	
    	listApplications(jobs);
    }
    
    
    
    public void listApplications(AppBag<JobApplication> jobs) {
    	
    	int row = 1;
		int col = 0	;
    	MyListener deleteJob = new MyListener() {
    		@Override
    		public void onClickListener(JobApplication job) {
    			
    			if(CommonMethods.showAlertDecision("Are you sure you want to delete this applicaiton")) {
    				String deleteStatement= jobHandler.deleteJobApplication(job.getApplicationID());
    				
    				sessionUser.removeJobApplication(job.getApplicationID());
    				userHandler.updateUser(sessionUser.getUsername(), sessionUser);
    				CommonMethods.showAlert(deleteStatement);
    			}
    		}
    	};
    	MyListener openJob = new MyListener() {
    		@Override
    		public void onClickListener(JobApplication job) {
    			CommonMethods.showAlert("Open Application"+job.toString());
    		}
    	};
    	applicationGrid.getChildren().clear();
    	
    	applicationGrid.getRowConstraints().clear();
    	applicationGrid.getColumnConstraints().clear();
    	
    	try {
    		for(int i =0;i<jobs.getCurrentSize(); i++) {
    			FXMLLoader fxmlloader = new FXMLLoader();
    			fxmlloader.setLocation(getClass().getResource("/FXML/ApplicationComponent.fxml"));
				AnchorPane anchorPane = fxmlloader.load();
				
				JobCompController jobComp = fxmlloader.getController();
				jobComp.setData(jobs.get(i), deleteJob, openJob);
				
				row ++;
				applicationGrid.add(anchorPane, col, row);
    		}
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
    	
    }
    
    @FXML
    void openDashboard(MouseEvent event) {
    	applications_tab.getScene().getWindow().hide();
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
    	documents_tab.getScene().getWindow().hide();
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
    	schedules_tab.getScene().getWindow().hide();
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
    
    @FXML
    void openAccounts(MouseEvent event) {
    	account_tab.getScene().getWindow().hide();
    	Stage stage = new Stage();
    	try {
    		FXMLLoader fxmlloader = new FXMLLoader();
    		fxmlloader.setLocation(getClass().getResource("/FXML/Account.fxml"));
    		
    		Parent root = fxmlloader.load();
    		
    		//Load Controller and initialize sessionUser and MongoDB database
    		AccountController accCont = fxmlloader.getController();
    		accCont.start(sessionUser, database);
    		Scene scene = new Scene(root);
    		
    		stage.setScene(scene);
    		stage.show();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert account_tab != null : "fx:id=\"account_tab\" was not injected: check your FXML file 'Applications.fxml'.";
        assert applicationGrid != null : "fx:id=\"applicationGrid\" was not injected: check your FXML file 'Applications.fxml'.";
        assert applications_tab != null : "fx:id=\"applications_tab\" was not injected: check your FXML file 'Applications.fxml'.";
        assert documents_tab != null : "fx:id=\"documents_tab\" was not injected: check your FXML file 'Applications.fxml'.";
        assert schedules_tab != null : "fx:id=\"schedules_tab\" was not injected: check your FXML file 'Applications.fxml'.";
        assert searchBar != null : "fx:id=\"searchBar\" was not injected: check your FXML file 'Applications.fxml'.";
        assert searchBtn != null : "fx:id=\"searchBtn\" was not injected: check your FXML file 'Applications.fxml'.";
        assert sortComboBox != null : "fx:id=\"sortComboBox\" was not injected: check your FXML file 'Applications.fxml'.";
        assert user_display_field != null : "fx:id=\"user_display_field\" was not injected: check your FXML file 'Applications.fxml'.";
        sortComboBox.setItems(FXCollections.observableArrayList("appliedDate", "Company Name","Job Title"));
    }
    
 // Custom comparator to compare schedules based on their dates
    private static class ScheduleComparator implements Comparator<Schedule> {
        @Override
        public int compare(Schedule s1, Schedule s2) {
            return s1.getScheduleDate().compareTo(s2.getScheduleDate());
        }
    }
}
