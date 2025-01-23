package Controller;

import java.io.File;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import org.bson.types.ObjectId;

import com.mongodb.client.MongoDatabase;

import DataHandlers.DocumentHandler;
import DataHandlers.MongoGridHandler;
import DataHandlers.UserHandler;
import Models.JobDocument;
import Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AddDocController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="CVradio"
    private RadioButton CVradio; // Value injected by FXMLLoader

    @FXML // fx:id="addFileBtn"
    private Button addFileBtn; // Value injected by FXMLLoader

    @FXML // fx:id="docDescTF"
    private TextField docDescTF; // Value injected by FXMLLoader

    @FXML // fx:id="documentNameTF"
    private TextField documentNameTF; // Value injected by FXMLLoader

    @FXML // fx:id="resumeRadio"
    private RadioButton resumeRadio; // Value injected by FXMLLoader

    @FXML // fx:id="submitBtn"
    private Button submitBtn; // Value injected by FXMLLoader
    
    @FXML // fx:id="uploadingLabel"
    private Label uploadingLabel; // Value injected by FXMLLoader

    private JobDocument doc;
    private User sessionUser;
    private MongoDatabase sessionDb;
    private DocumentHandler docHandler;
    private String bucketId = "";
    private MongoGridHandler gridHandler;
    @FXML // fx:id="resumeOrCVToggle"
    private ToggleButton resumeOrCVToggle; // Value injected by FXMLLoader
    private UserHandler uh;
    
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
    
    @FXML
    void hadnleSubmitBtn(ActionEvent event) {
    	checkFields();
		String insertStatus = docHandler.postDocumentData(doc);
		CommonMethods.showAlert(insertStatus);
		if(insertStatus == "DocumentData Added Successfully") {
			if(resumeOrCVToggle.isSelected()) {
				sessionUser.addCoverLetter(doc.getDocumentID());
			}else {
				sessionUser.addResume(doc.getDocumentID());
			}
			
			uh.updateUser(sessionUser.getUsername(), sessionUser);
			notifyUserUpdate(sessionUser);
			
			Stage thisStage = (Stage)submitBtn.getScene().getWindow();
			
			thisStage.close();
    	}
		
		
    }
    
    @FXML
    void changeToggle(MouseEvent event) {
    	if(resumeOrCVToggle.isSelected()) {
    		resumeOrCVToggle.setText("Cover Letter");
    	}else {
    		resumeOrCVToggle.setText("Resume");
    	}
    }
    
    public void start(User user, MongoDatabase database) {
    	sessionUser = user;
    	sessionDb = database;
    	
    	gridHandler = new MongoGridHandler(database);
    	uh = new UserHandler(database);
    	docHandler = new DocumentHandler(database);
    	doc = new JobDocument();
    }

	private void checkFields() {
		// TODO Auto-generated method stub
		boolean checkRet = true;
		
		Random rand = new Random();
		int docId = rand.nextInt(100)+sessionUser.getCoverLetterIDs().size()*sessionUser.getResumeIDs().size() + 401;
		doc.setDocumentID(docId);
		if(documentNameTF.getText() == "") {
			CommonMethods.showAlert("Enter all the fields");
			checkRet = false;
			
		}else {
			doc.setDocumentName(documentNameTF.getText());
		}
		if(docDescTF.getText() == "") {
			
			CommonMethods.showAlert("Enter all the fields");
			checkRet = false;
		}else {
			doc.setDocumentDesc(docDescTF.getText());
		}
		
		if(bucketId == "") {
			CommonMethods.showAlert("Please add a file before submitting");
			checkRet = false;
		}else {
			doc.setBucketId(bucketId);
			
		}
	}


	@FXML
    void handleAddFile(ActionEvent event) {
		uploadingLabel.setText("Adding File...");
    	String url = fileChooser();
    	if(documentNameTF.getText() == "") {
    		CommonMethods.showAlert("Enter Document Name before adding file");
    	}else {
    		
    		
    		ObjectId buckObj = gridHandler.uploadFile(url, documentNameTF.getText());
    		bucketId = buckObj.toString();
    		uploadingLabel.setText("File Uploaded");
    	}
    	
    }
	
	private static String fileChooser() {
		FileChooser fc = new FileChooser();
		fc.setTitle("Select a file");
		FileChooser.ExtensionFilter allDocumentsFilter = new FileChooser.ExtensionFilter(
	            "All Documents (*.docx, *.doc, *.pdf, *.png, *.jpg, *.jpeg, *.gif)",
	            "*.docx", "*.doc", "*.pdf", "*.png", "*.jpg", "*.jpeg", "*.gif"
	        );
		fc.getExtensionFilters().add(allDocumentsFilter);
		File file = fc.showOpenDialog(null);
		String filePath = file.getAbsolutePath();
		return filePath;
	}

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert CVradio != null : "fx:id=\"CVradio\" was not injected: check your FXML file 'Document_dialogbox.fxml'.";
        assert addFileBtn != null : "fx:id=\"addFileBtn\" was not injected: check your FXML file 'Document_dialogbox.fxml'.";
        assert docDescTF != null : "fx:id=\"docDescTF\" was not injected: check your FXML file 'Document_dialogbox.fxml'.";
        assert documentNameTF != null : "fx:id=\"documentNameTF\" was not injected: check your FXML file 'Document_dialogbox.fxml'.";
        assert resumeRadio != null : "fx:id=\"resumeRadio\" was not injected: check your FXML file 'Document_dialogbox.fxml'.";
        assert submitBtn != null : "fx:id=\"submitBtn\" was not injected: check your FXML file 'Document_dialogbox.fxml'.";
        uploadingLabel.setText("");
    }

}
