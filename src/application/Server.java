package application;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

import org.bson.types.ObjectId;

import com.mongodb.client.MongoDatabase;

import DataHandlers.DbConnection;
import DataHandlers.MongoGridHandler;
import Models.JobApplication;
import Models.User;
import MyCollections.AppArray;
import MyCollections.AppBag;
import Pages.DashboardPage;
import Pages.LoginPage;
import Pages.SchedulesPage;
import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Server extends Application{
	private DbConnection appToken;
	private MongoDatabase sessionDatabaseToken;
	private User sessionUser;
	
	private void handleFiles() {
		// TODO Auto-generated method stub
		MongoGridHandler fileHandle = new MongoGridHandler(sessionDatabaseToken);
	
	}

	private void authenticateUser() {
		LoginPage lp = new LoginPage(sessionDatabaseToken);
		Object getSession = LoginPage.start();
		if(getSession instanceof User) {
			sessionUser = (User) getSession;
			goToDashboard();
		}
	}
	
	private void goToDashboard() {
		System.out.println("\nYou've been redirected to dashboard");
		
		DashboardPage dp = new DashboardPage(sessionUser, sessionDatabaseToken);
		dp.start();
	}
	
	private void methodActor() {
		Scanner inp = new Scanner(System.in);
		System.out.println("Enter file url:");
		
		String url = inp.next();
		System.out.println("Enter file Name:");
		String name = inp.next();
		
		MongoGridHandler asd = new MongoGridHandler(sessionDatabaseToken);
		ObjectId obj = asd.uploadFile(url, name);
		System.out.println(obj.toString());
		System.out.println("\nUploaded!!");
	}
	
	private static void fileChooser() {
		FileChooser fc = new FileChooser();
		fc.setTitle("Select a file");
		FileChooser.ExtensionFilter allDocumentsFilter = new FileChooser.ExtensionFilter(
	            "All Documents (*.docx, *.doc, *.pdf, *.png, *.jpg, *.jpeg, *.gif)",
	            "*.docx", "*.doc", "*.pdf", "*.png", "*.jpg", "*.jpeg", "*.gif"
	        );
		fc.getExtensionFilters().add(allDocumentsFilter);
		File file = fc.showOpenDialog(null);
		String filePath = file.getAbsolutePath();
		System.out.println(filePath);
	}
	
	public static void main(String[] args) {
//		Server sr = new Server();
//		sr.startApp();
		launch(args);
	
	}

	@Override
	public void start(Stage arg0) throws Exception {
		appToken = new DbConnection();
		sessionDatabaseToken = appToken.getDatabase();
		
		handleFiles();
		
		appToken.endConnection();
		
	}
}
