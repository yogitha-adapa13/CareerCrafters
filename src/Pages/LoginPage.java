package Pages;

import java.util.Scanner;

import com.mongodb.client.MongoDatabase;

import DataHandlers.UserHandler;
import Models.User;

public class LoginPage {
	
	
	static Scanner inp = new Scanner(System.in);
	private static MongoDatabase database;
	public LoginPage(MongoDatabase database){
		LoginPage.database = database;
	}
	public static Object start(){
		Object returnToken;
		System.out.println("\nSelect one: \n"
				+ "1) Login\n"
				+ "2) Register\n"
				+ "3) Close Application");
		int userCmd = inp.nextInt();
		while(userCmd !=1 && userCmd !=2 && userCmd !=3){
			System.out.println("\nThe number should be either 1 or 2, choose again :):");
			userCmd = inp.nextInt();
		}
		if(userCmd == 1) {
			returnToken = loginPrompt();
			if(returnToken == null) {
				throw new Error("Error while fetching user");
			}else if(returnToken instanceof User) {
				User loggedInUser = (User) returnToken;
				System.out.println("\nLoggedIn User: "+ loggedInUser.getUsername());
				return returnToken;
			}else if(returnToken instanceof String){
				System.out.println(returnToken);
				return start();
			}
		}
		else if(userCmd ==2) {
			returnToken = registerPrompt();
			System.out.println(returnToken);
			if(returnToken instanceof User) {
				User regesteredUser = (User) returnToken;
				return loginUser(regesteredUser.getUsername(), regesteredUser.getPassword());
				
			}else if(returnToken instanceof String) {
				start();
			}
		}
		else if(userCmd ==3) {
			System.exit(0);
		}
		return false;
	}
	
	public static Object loginPrompt() {
		String userName;
		String password;
		System.out.println("\nEnter user name:");
		userName = inp.next();
		System.out.println("\nEnter password:");
		password = inp.next();
		System.out.println("\nUsername: "+userName+"\nPassword: "+password);
		return loginUser(userName, password);
	}

	
	private static Object loginUser(String userName, String password) {
		UserHandler uh = new UserHandler(database);
		if(uh.checkIfUserExists(userName) && uh.checkPasswordMatch(userName, password)) {
			User sessionUser = uh.getUser(userName);
			return sessionUser;
		}else {
			return "Wrong Password or User Doesn't Exist";
		}
	}
	
	
	public static Object registerPrompt() {
		User createUser = new User();
		String[] questions = {"Name of your first pet", "Name of your first love", "Your Apt Number"};
		
		System.out.println("\nEnter your first name:");
		createUser.setFirstName(inp.next());
		
		System.out.println("\nEnter your last name:");
		createUser.setLastName(inp.next());
		
		System.out.println("\nEnter your email:");
		createUser.setEmail(inp.next());
		
		System.out.println("\nEnter your Phone number:");
		createUser.setPhone(inp.next());
		
		System.out.println("\nEnter your username:");
		createUser.setUsername(inp.next());
		
		System.out.println("\nEnter your password:");
		createUser.setPassword(inp.next());
		
		System.out.println("\nChoose a Security question:");
		for(int i=0; i<questions.length; i++) {
			System.out.println("\n"+i+"."+questions[i]);
		}
		createUser.setSecurityQuestion(questions[inp.nextInt()]);
		System.out.println("\nEnter an answer for your question");
		createUser.setAnswer(inp.next());
		
		UserHandler uh = new UserHandler(database);
		System.out.println("Given user information:"+createUser);
		String postMessage = uh.postUser(createUser);
		
		
		if(postMessage == "Account Created Successfully"){
			return createUser;
		}else {
			return postMessage;
		}
	}
}
