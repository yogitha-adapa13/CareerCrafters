
package Controller;

import java.net.URL;
import java.util.ResourceBundle;

import Models.JobApplication;
import Controller.MyListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class JobCompController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="applicationDate"
    private Label applicationDate; // Value injected by FXMLLoader

    @FXML // fx:id="applicationStatusLabel"
    private Label applicationStatusLabel; // Value injected by FXMLLoader

    @FXML // fx:id="companyNameLabel"
    private Label companyNameLabel; // Value injected by FXMLLoader

    @FXML // fx:id="deleteApplicationBtn"
    private Button deleteApplicationBtn; // Value injected by FXMLLoader

    @FXML // fx:id="jobTitleLabel"
    private Label jobTitleLabel; // Value injected by FXMLLoader

    @FXML // fx:id="openApplicationBtn"
    private Button openApplicationBtn; // Value injected by FXMLLoader

    private JobApplication job;
    
    private MyListener deleteApplication;

    private MyListener openJobApplication;
    @FXML
    void deleteApplication(MouseEvent event) {
    	deleteApplication.onClickListener(job);
    }

    @FXML
    void openJobData(MouseEvent event) {
    	openJobApplication.onClickListener(job);
    }
    
    public void setData(JobApplication job, MyListener deleteListener, MyListener openApplication) {
    	this.job = job;
    	this.deleteApplication = deleteListener;
    	this.openJobApplication = openApplication;
    	
    	jobTitleLabel.setText(job.getJobTitle());
    	companyNameLabel.setText(job.getCompanyName());
    	applicationStatusLabel.setText(job.getStatus());
    	applicationDate.setText(job.getAppliedDate());
    }
    

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert applicationDate != null : "fx:id=\"applicationDate\" was not injected: check your FXML file 'ApplicationComponent.fxml'.";
        assert applicationStatusLabel != null : "fx:id=\"applicationStatusLabel\" was not injected: check your FXML file 'ApplicationComponent.fxml'.";
        assert companyNameLabel != null : "fx:id=\"companyNameLabel\" was not injected: check your FXML file 'ApplicationComponent.fxml'.";
        assert deleteApplicationBtn != null : "fx:id=\"deleteApplicationBtn\" was not injected: check your FXML file 'ApplicationComponent.fxml'.";
        assert jobTitleLabel != null : "fx:id=\"jobTitleLabel\" was not injected: check your FXML file 'ApplicationComponent.fxml'.";
        assert openApplicationBtn != null : "fx:id=\"openApplicationBtn\" was not injected: check your FXML file 'ApplicationComponent.fxml'.";
    }

}
