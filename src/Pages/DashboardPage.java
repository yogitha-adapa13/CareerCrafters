package Pages;

import java.util.Scanner;

import com.mongodb.client.MongoDatabase;

import Models.User;

public class DashboardPage {
	private static User sessionUser;
	private static MongoDatabase sessionDb;
	private static Scanner inp = new Scanner(System.in);
	
	public DashboardPage(User user, MongoDatabase db){
		System.out.println("-------------------------------Dashboard Page -------------------------------\n");
		sessionUser = user;
		sessionDb = db;
		System.out.println("Welcome "+sessionUser.getFullName()+"\n");
	}
	
	public static Object start() {
		System.out.println("Select one: \n"
				+ "1) List Job Applications\n"
				+ "2) List Schedules\n"
				+ "3) Your Documents\n"
				+ "4) Account");
		int userAction = inp.nextInt();
		
		while(userAction<1 || userAction>4) {
			System.out.println("\nInvalid choice, Try again (1 - 4):");
			userAction = inp.nextInt();
		}
		
		switch(userAction) {
			case 1:{
				goToListApplciationsPage();
				break;
			}
			case 2:{
				goToSchedulesPage();
				break;
			}
			case 3:{
				goToYourDocsPage();
				break;
			}
			case 4:{
				goToAccountPage();
				break;
			}
		}
		
		return null;
	}
	
	

	private static void goToAccountPage() {
		System.out.println("\nYou've been redirected to Accounts");
	
	}

	private static void goToYourDocsPage() {
		System.out.println("\nYou've been redirected to Documents");
		
		YourDocumentsPage docPage = new YourDocumentsPage(sessionUser, sessionDb);
		YourDocumentsPage.start();
	}

	private static void goToSchedulesPage() {
		System.out.println("\nYou've been redirected to Schedules");
		
	}

	private static void goToListApplciationsPage() {
		System.out.println("\nYou've been redirected to Applications");
		ListJobApplications jobsPage = new ListJobApplications(sessionUser, sessionDb);
		ListJobApplications.start();
	}
}
