package Controller;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mongodb.client.MongoDatabase;

import DataHandlers.DbConnection;
import DataHandlers.UserHandler;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import Models.User;

@SuppressWarnings("unused")
public class LoginController implements Initializable {
	private DbConnection appToken;
	private MongoDatabase sessionDatabaseToken;
	private UserHandler userHandler;
	
	
    @FXML
    private Hyperlink si_forgotPassword;

    @FXML
    private Button si_loginButton;

    @FXML
    private AnchorPane si_loginForm;

    @FXML
    private PasswordField si_password;

    @FXML
    private TextField si_username;

    @FXML
    private Button side_createButton;

    @FXML
    private AnchorPane side_form;

    @FXML
    private TextField su_answer;

    @FXML
    private PasswordField su_password;
    
    @FXML
    private TextField su_firstName;
    
    @FXML
    private TextField su_lastName;
    
    @FXML
    private TextField su_email;

    @FXML
    private TextField su_phno;
    @FXML
    private ComboBox<String> su_question;

    @FXML
    private Button su_signupButton;

    @FXML
    private AnchorPane su_signupForm;
    
    @FXML
    private AnchorPane su_signupForm1;

    @FXML
    private TextField su_username;
    
    @FXML
    private Button side_allHave;
    
    private User loggedInUser;
    
    public LoginController() {
    	appToken = new DbConnection();
		sessionDatabaseToken = appToken.getDatabase();
		userHandler = new UserHandler(sessionDatabaseToken);
    }
    
    
    //method for slide animation
    @SuppressWarnings("exports")
	public void slideForm(ActionEvent event) {
    	
    	TranslateTransition sliderform = new TranslateTransition();
    	
    	if(event.getSource() == side_createButton){
    		sliderform.setNode(side_form);
    		sliderform.setToX(300);
    		sliderform.setDuration(Duration.seconds(.5));
    		
    		sliderform.setOnFinished((ActionEvent e) -> {
    			side_allHave.setVisible(true);
    			side_createButton.setVisible(false);
    		});
    		sliderform.play();
    	}else if(event.getSource() == side_allHave){
    		sliderform.setNode(side_form);
    		sliderform.setToX(0);
    		sliderform.setDuration(Duration.seconds(.5));
    		sliderform.setOnFinished((ActionEvent e) -> {
    			side_allHave.setVisible(false);
    			side_createButton.setVisible(true);
    		});
    		sliderform.play();
    	}
    }
    
    
//    SignUp Helper Functions
    
//    Method to check if all fields are entered
    private boolean checkFields() {
    	if(su_firstName.getText().isEmpty() ||
                su_lastName.getText().isEmpty() ||
                su_email.getText().isEmpty() ||
                su_phno.getText().isEmpty() ||
                su_username.getText().isEmpty() ||
                su_password.getText().isEmpty() ||
                su_answer.getText().isEmpty()) { 
    		CommonMethods.showAlert("Enter All The Fields");
    		return false;
    	} else if(su_question.getValue()==null) {
    		CommonMethods.showAlert("Please Select A Question Before Setting An Answer");
    		return false;
    	}
    	return true;
    }
    
//    Method to check if email is valid
    private boolean isValidEmail(String email) {
        // Define the pattern for a valid email address
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        // Create a Pattern object
        Pattern pattern = Pattern.compile(emailRegex);
        // Create a Matcher object
        Matcher matcher = pattern.matcher(email);
        // Return true if the email matches the pattern
        return matcher.matches();
    }
    
//    Method to perform form validation
    private boolean performFormValidation() {
    	String email = su_email.getText();
    	String userName = su_username.getText();
    	String password = su_password.getText();
    	if(!isValidEmail(email)) {
    		CommonMethods.showAlert("Enter a valid email address");
    		return false;
    	}else if(userName.length()<5) {
    		CommonMethods.showAlert("Username should by greater than 5 characters");
    		return false;
    	}else if(password.length()<6) {
    		CommonMethods.showAlert("Password length should be greater than 6");
    		return false;
    	}
    	return true;
    	
    }
    
//    Method to check if userName already exists
    private boolean checkUserNameExists() {
    	if(userHandler.checkIfUserExists(su_username.getText())) {
    		CommonMethods.showAlert("User Name is Already Taken");
    		return true;
    	}
    	return false;
    }
    
    private User getUserDetails() {
    	User user = new User();
    	user.setFirstName(su_firstName.getText());
    	user.setLastName(su_lastName.getText());
    	user.setEmail(su_email.getText());
    	user.setPhone(su_phno.getText());
    	user.setUsername(su_username.getText());
    	user.setPassword(su_password.getText());
    	user.setSecurityQuestion(su_question.getValue());
    	user.setAnswer(su_answer.getText());
    	
    	
    	return user;
    }
    
    
//    Login Helper Functions
//    Methods which checks entered login details and returns a boolean if details are valid
    public boolean performLoginChecks(String userName, String password) {
    	if(!userHandler.checkIfUserExists(userName)) {
    		CommonMethods.showAlert("User Name Does Not Exists");
    		return false;
    	}else if(!userHandler.checkPasswordMatch(userName, password)){
    		CommonMethods.showAlert("Invalid Password");
    		return false;
    	}
    	return true;
    }
    
//    Method to redirect to HomePage after successful login
    private void redirectToHomePage() {
    	si_loginButton.getScene().getWindow().hide();
    	Stage stage = new Stage();
    	try {
			FXMLLoader fxmlloader = new FXMLLoader();
			fxmlloader.setLocation(getClass().getResource("/fxml/Dashboard.fxml"));
			
			Parent root = fxmlloader.load();
			
			DashboardController dashboardController = fxmlloader.getController();
			dashboardController.initialize(loggedInUser, sessionDatabaseToken);
			
    		Scene scene = new Scene(root);
    		
    		stage.setScene(scene);
    		stage.show();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    
    @FXML
    void loginSubmit(ActionEvent event) {
    	String enteredUserName = si_username.getText();
    	String enteredPassword = si_password.getText();
    	if(performLoginChecks(enteredUserName,enteredPassword)) {
    		loggedInUser =  userHandler.getUser(enteredUserName);
    		CommonMethods.showAlert("Login Successful");
    		redirectToHomePage();
    	}
    }
    @FXML
    void signUpSubmit(ActionEvent event) {
    	CommonMethods.showAlert("Done");
    	if(checkFields() && performFormValidation() && !checkUserNameExists()) {
    		String message = userHandler.postUser(getUserDetails());
    		CommonMethods.showAlert(message);
    	}
    	
    }
    
    //set visibility of the anchorpane 1
    @FXML
    void nextHandler(ActionEvent event) {
    	su_signupForm1.setVisible(true);
    }
    
    //turn off the visibility of the second anchor pane to accommodate the next slide for logging in
    @FXML
    void goBack(ActionEvent event) {
    	su_signupForm1.setVisible(false);
    }
   
    
//    Text Formatter for phonenumber;
    TextFormatter<String> phoneNo = new TextFormatter<>(change -> {
        if (!change.getText().matches("[\\d+]*")) {
            change.setText(""); // Remove non-numeric characters
        }
        return change;
    });

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		su_phno.setTextFormatter(phoneNo);
		
		String[] comboquestions = {
				"What is your first pet?",
				"Who is your your first ex?",
				"What is the name of your Elementary School?",
				"What is your Mother's maiden name?",
				"What is your favourite Color?"
		};
		su_question.getItems().addAll(comboquestions);
		
		
	}
    
    

}
