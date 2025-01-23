package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.mongodb.client.MongoDatabase;

import DataHandlers.DocumentHandler;
import DataHandlers.MongoGridHandler;
import Models.JobDocument;
import Models.User;
import MyCollections.AppArray;
import MyCollections.AppBag;
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

public class DocumentController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="CoverLettersGrid"
    private GridPane CoverLettersGrid; // Value injected by FXMLLoader

    @FXML // fx:id="account_tab"
    private Label account_tab; // Value injected by FXMLLoader

    @FXML // fx:id="applications_tab"
    private Label applications_tab; // Value injected by FXMLLoader

    @FXML // fx:id="document_addfile_btn"
    private Button document_addfile_btn; // Value injected by FXMLLoader


    @FXML
    private Label dashboardTab;

    @FXML // fx:id="resumesGrid"
    private GridPane resumesGrid; // Value injected by FXMLLoader

    @FXML // fx:id="schedules_tab"
    private Label schedules_tab; // Value injected by FXMLLoader

    @FXML // fx:id="userTextField"
    private Label userTextField; // Value injected by FXMLLoader
    private User sessionUser;
    private MongoDatabase sessionDb;
    private DocumentHandler docHandler;
    private MongoGridHandler gridHandler;
    private openFIleListener opener;
    
    private AppBag<JobDocument> userResumes;
    private AppBag<JobDocument> userCVs;
    
    @FXML
    void handleAddFileButtonAction(ActionEvent event) {
    	Stage addFile = new Stage();
    	try {
    		FXMLLoader fxmlloader = new FXMLLoader();
    		fxmlloader.setLocation(getClass().getResource("/FXML/Document_dialogbox.fxml"));
    		Parent root = fxmlloader.load();
    		
    		AddDocController addDocCont = fxmlloader.getController();
    		addDocCont.start(sessionUser, sessionDb);
    		
    		Scene scene = new Scene(root);
    		addFile.setScene(scene);
    		addFile.show();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }

    @FXML
    void openAccount(MouseEvent event) {
    	account_tab.getScene().getWindow().hide();
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
    	applications_tab.getScene().getWindow().hide();
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
    void openSchedules(MouseEvent event) {
    	schedules_tab.getScene().getWindow().hide();
    	Stage stage = new Stage();
    	try {
    		FXMLLoader fxmlloader = new FXMLLoader();
    		fxmlloader.setLocation(getClass().getResource("/FXML/Schedule.fxml"));
    		
    		Parent root = fxmlloader.load();
    		
    		//Load Controller and initialize sessionUser and MongoDB database
    		SchedulesController schedCont = fxmlloader.getController();
    		schedCont.start(sessionUser, sessionDb);
    		Scene scene = new Scene(root);
    		
    		stage.setScene(scene);
    		stage.show();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    public void start(User user, MongoDatabase database) {
    	this.sessionUser = user;
    	this.sessionDb = database;
    	
    	userResumes = new AppArray<>();
    	userCVs = new AppArray<>();
    	userTextField.setText(sessionUser.getFirstName());
    	docHandler = new DocumentHandler(sessionDb);
    	gridHandler = new MongoGridHandler(sessionDb);
    	listResumes();
    	listCoverLetters();
    }

    private void listCoverLetters() {
		// TODO Auto-generated method stub
    	int cvRow = 1;
    	int cvCol = 0;
    	
    	List<Integer> cvIds = sessionUser.getCoverLetterIDs();
    	for(int i=0; i<cvIds.size(); i++) {
    		JobDocument doc = docHandler.getDocument(cvIds.get(i));
    		userCVs.add(doc);
    	}
    	
    	
    	if(userCVs.getCurrentSize()>0) {
    		opener = new openFIleListener() {
				@Override
				public void onClickListener(JobDocument doc) {
					try {
						gridHandler.openFile(doc.getBucketId());
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			};
    	}
    	
    	CoverLettersGrid.getChildren().clear();
    	CoverLettersGrid.getRowConstraints().clear();
    	CoverLettersGrid.getColumnConstraints().clear();
    	
    	try {
    		for(int i =0; i<userCVs.getCurrentSize(); i++) {
    			FXMLLoader fxmlloader = new FXMLLoader();
    			fxmlloader.setLocation(getClass().getResource("/FXML/fileComponent.fxml"));
				AnchorPane anchorPane = fxmlloader.load();
				
				FileCompController fileController = fxmlloader.getController();
				fileController.setData(userCVs.get(i), opener);
				if(cvRow == 2) {
					cvRow = 0;
					cvCol++;
				}
				CoverLettersGrid.add(anchorPane, cvCol, ++cvRow);
    		}
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
	}

	private void listResumes() {
    	int resRow = 1;
    	int resCol = 0;
    	
    	List<Integer> resumeIds = sessionUser.getResumeIDs();
    	for(int i=0; i<resumeIds.size(); i++) {
    		JobDocument doc = docHandler.getDocument(resumeIds.get(i));
    		userResumes.add(doc);
    	}
    	if(userResumes.getCurrentSize()>0) {
    		opener = new openFIleListener() {
				@Override
				public void onClickListener(JobDocument doc) {
					try {
						gridHandler.openFile(doc.getBucketId());
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			};
    	}
    	
    	resumesGrid.getChildren().clear();
    	resumesGrid.getRowConstraints().clear();
    	resumesGrid.getColumnConstraints().clear();
    	
    	try {
    		for(int i =0; i<userResumes.getCurrentSize(); i++) {
    			FXMLLoader fxmlloader = new FXMLLoader();
    			fxmlloader.setLocation(getClass().getResource("/FXML/fileComponent.fxml"));
				AnchorPane anchorPane = fxmlloader.load();
				
				FileCompController fileController = fxmlloader.getController();
				fileController.setData(userResumes.get(i), opener);
				if(resRow == 2) {
					resRow = 0;
					resCol++;
				}
				resumesGrid.add(anchorPane, resCol, resRow++);
    		}
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert CoverLettersGrid != null : "fx:id=\"CoverLettersGrid\" was not injected: check your FXML file 'Documents.fxml'.";
        assert account_tab != null : "fx:id=\"account_tab\" was not injected: check your FXML file 'Documents.fxml'.";
        assert applications_tab != null : "fx:id=\"applications_tab\" was not injected: check your FXML file 'Documents.fxml'.";
        assert document_addfile_btn != null : "fx:id=\"document_addfile_btn\" was not injected: check your FXML file 'Documents.fxml'.";
        assert dashboardTab != null : "fx:id=\"documents_tab\" was not injected: check your FXML file 'Documents.fxml'.";
        assert resumesGrid != null : "fx:id=\"resumesGrid\" was not injected: check your FXML file 'Documents.fxml'.";
        assert schedules_tab != null : "fx:id=\"schedules_tab\" was not injected: check your FXML file 'Documents.fxml'.";
        assert userTextField != null : "fx:id=\"userTextField\" was not injected: check your FXML file 'Documents.fxml'.";

    }

}
