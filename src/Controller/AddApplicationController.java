package Controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import com.mongodb.client.MongoDatabase;

import DataHandlers.DocumentHandler;
import DataHandlers.JobApplicationHandler;
import DataHandlers.UserHandler;
import Models.JobApplication;
import Models.JobDocument;
import Models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class AddApplicationController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="addFileDoc"
    private Button addFileDoc; // Value injected by FXMLLoader

    @FXML // fx:id="appliedDateDP"
    private DatePicker appliedDateDP; // Value injected by FXMLLoader

    @FXML // fx:id="companyNameTF"
    private TextField companyNameTF; // Value injected by FXMLLoader

    @FXML // fx:id="jobTitleTF"
    private TextField jobTitleTF; // Value injected by FXMLLoader

    @FXML // fx:id="statusComboBox"
    private ComboBox<String> statusComboBox; // Value injected by FXMLLoader

    @FXML // fx:id="attachedDocCB"
    private ComboBox<String> attachedDocCB; // Value injected by FXMLLoader
    @FXML // fx:id="submitBtn"
    private Button submitBtn; // Value injected by FXMLLoader
    
    private HashMap<String, Integer> docDetails = new HashMap<>();
    
    private User sessionUser;
    private MongoDatabase sessionDb;
    private JobApplicationHandler jobHandler;
    private DocumentHandler docHandler;
    private UserHandler userHandler;
    
    
    public void start(User user, MongoDatabase database) {
    	this.sessionUser = user;
    	this.sessionDb = database;	
    	
    	jobHandler = new JobApplicationHandler(sessionDb);
    	userHandler = new UserHandler(sessionDb);
    	docHandler = new DocumentHandler(sessionDb);
    	
    	
    }

    @FXML
    void handleAttachDoc(ActionEvent event) {
    	Stage addFile = new Stage();
    	try {
    		FXMLLoader fxmlloader = new FXMLLoader();
    		fxmlloader.setLocation(getClass().getResource("/FXML/Document_dialogbox.fxml"));
    		Parent root = fxmlloader.load();
    		
    		AddDocController addDocCont = fxmlloader.getController();
    		addDocCont.start(sessionUser, sessionDb);
    		
    		// Set a listener to handle changes to sessionUser
            addDocCont.setOnUserUpdate(user -> {
                // Update sessionUser in AddApplicationController
                this.sessionUser = user;
                
                // Perform any additional actions if needed
                // For example, update UI elements with new user data
            });
    		
    		Scene scene = new Scene(root);
    		addFile.setScene(scene);
    		addFile.show();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }

    @FXML
    void handleSubmit(ActionEvent event) {
    	if(checkFields()) {
    		JobApplication job = getJobData();
    		CommonMethods.showAlert(job.toString());
    		
    		String insertStatus = jobHandler.postJobApplication(job);
    		if(insertStatus == "Job Application Added Successfully") {
    			sessionUser.addJobApplication(job.getApplicationID());
    			System.out.println(userHandler.updateUser(sessionUser.getUsername(), sessionUser));
    			Stage stage = (Stage) submitBtn.getScene().getWindow();
    			stage.close();
    		}
    		
    	}else {
    		CommonMethods.showAlert("Enter all fields before submit");
    	}
    }
    
    private JobApplication getJobData() {
		JobApplication job = new JobApplication();
		Random rand = new Random();
		job.setApplicationID(rand.nextInt(100)+sessionUser.getApplicationIds().size()+1001);
		job.setCompanyName(companyNameTF.getText());
		job.setJobTitle(jobTitleTF.getText());
		job.setAppliedDate(getFormattedDate());
		job.setApplicationStatus(statusComboBox.getSelectionModel().getSelectedItem().toString());
		job.addDocument(docDetails.get(attachedDocCB.getSelectionModel().getSelectedItem().toString()));
		return job;
	}

	private boolean checkFields() {
		if(jobTitleTF.getText().length() == 0 || companyNameTF.getText().length() == 0 || appliedDateDP.getValue()== null || statusComboBox.getSelectionModel().getSelectedItem() == null) {
			return false;
		}else {
			return true; 
		}
	}

	@FXML
    void updateUserDocs(MouseEvent event) {
		attachedDocCB.getItems().clear();
		docDetails.clear();
		List<Integer> userDocs = sessionUser.getResumeIDs();
		userDocs.addAll(sessionUser.getCoverLetterIDs());
		for(int i =0; i<userDocs.size(); i++) {
			JobDocument doc = docHandler.getDocument(userDocs.get(i));
			attachedDocCB.getItems().addAll(doc.getDocumentName());
			docDetails.put(doc.getDocumentName(), doc.getDocumentID());
		}
    }
	
	private String getFormattedDate() {
		LocalDate selectedDate = appliedDateDP.getValue();

        // Format the selected date using a DateTimeFormatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return selectedDate.format(formatter);
	}

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert addFileDoc != null : "fx:id=\"addFileDoc\" was not injected: check your FXML file 'Applications_dialogbox.fxml'.";
        assert appliedDateDP != null : "fx:id=\"appliedDateDP\" was not injected: check your FXML file 'Applications_dialogbox.fxml'.";
        assert companyNameTF != null : "fx:id=\"companyNameTF\" was not injected: check your FXML file 'Applications_dialogbox.fxml'.";
        assert jobTitleTF != null : "fx:id=\"jobTitleTF\" was not injected: check your FXML file 'Applications_dialogbox.fxml'.";
        assert submitBtn != null : "fx:id=\"submitBtn\" was not injected: check your FXML file 'Applications_dialogbox.fxml'.";
        
        statusComboBox.setItems(FXCollections.observableArrayList("Applied", "Under Review","Interview Phase", "Rejected"));

    }

}
