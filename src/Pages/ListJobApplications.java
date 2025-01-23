package Pages;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import com.mongodb.client.MongoDatabase;

import Alogrithms.AppQuickSort;
import DataHandlers.DocumentHandler;
import DataHandlers.JobApplicationHandler;
import DataHandlers.ScheduleHandler;
import DataHandlers.UserHandler;
import Models.JobApplication;
import Models.JobDocument;
import Models.Schedule;
import Models.User;
import MyCollections.AppArray;
import MyCollections.AppBag;
import MyCollections.AppPriorityQueue;
import MyCollections.JobApplicationBinaryTree;
import MyCollections.MyQueue;

public class ListJobApplications {
	private static User sessionUser;
	private static MongoDatabase sessionDb;
	
	private static DocumentHandler docHandler;
	private static UserHandler userHandler;
	private static JobApplicationHandler jobHandler;
	private static ScheduleHandler schedHandler;
	static Scanner inp = new Scanner(System.in);
	
	private static AppBag<JobApplication> appliedJobs;
	private static AppBag<JobDocument> userDocs;
	
	//Constructor
	
	public ListJobApplications(User user, MongoDatabase sessionDb) {
		this.sessionDb = sessionDb;
		this.sessionUser = user;
		
		userHandler = new UserHandler(sessionDb);
		jobHandler = new JobApplicationHandler(sessionDb);
		docHandler = new DocumentHandler(sessionDb);
		schedHandler = new ScheduleHandler(sessionDb);
		
		appliedJobs = new AppArray<>();
		userDocs = new AppArray<>();
		
		System.out.println("------------------------ Job Applications Page -------------------------");
		System.out.println(user.getFullName()+"'s Applciations");
	}
	
	/**
     * Starts the job application listing process.
     */
	public static void start() {
		setObjectDataToJobs();
		System.out.println("Select One:\n"
				+ "1) List Applications\n"
				+ "2) Add an application\n"
				+ "3) Select Application\n"
				+ "4) Search For an Application");
		
		int userChoice = inp.nextInt();
		while(userChoice<1 || userChoice >4) {
			System.out.println("invalid choice, try again:");
			userChoice = inp.nextInt();
		}
		switch(userChoice) {
		case 1:
			listAllApplications();
			break;
		case 2:
			addJobApplication();
			break;
		case 3:
			selectApplication();
			break;
		case 4:
			searchApplication();
			break;
		}
		
	}
	
	/**
	 * searches for a job using a search parameter
	 * */
	private static void searchApplication() {
		// TODO Auto-generated method stub
		System.out.println("Search Parameters:");
		String searchParams = inp.next();
		
		JobApplicationBinaryTree jobs = new JobApplicationBinaryTree();
		for(int i =0; i< appliedJobs.getCurrentSize(); i++) {
			jobs.insert(appliedJobs.get(i));
		}
		
		listApplications(jobs.search(searchParams));
		
	}

	/**
	 * Adds a new Job Application 
	 * */
	private static void addJobApplication() {
		System.out.print("\n-------------------New Application");
		JobApplication newJob = new JobApplication();
		newJob.setApplicationID(appliedJobs.getCurrentSize()+2001);
		 
		System.out.println("Enter Company name");
		newJob.setCompanyName(inp.nextLine());
		
		System.out.println("Enter Job Title");
		newJob.setJobTitle(inp.nextLine());
		
		System.out.println("Enter applied Date (yyyy-MM-dd HH:mm):");
		newJob.setAppliedDate(inp.nextLine());
		
		System.out.println("Enter applicaiton Status");
		newJob.setApplicationStatus(inp.nextLine());
		
		if(!userDocs.isEmpty()) {
			System.out.println("Enter a doc id from following docs");
			System.out.println(userDocs.toString());
			newJob.addDocument(inp.nextInt());
		}
		
		String insertStatus = jobHandler.postJobApplication(newJob);
		System.out.println(insertStatus);
		if(insertStatus == "Job Application Added Successfully") {
			sessionUser.addJobApplication(newJob.getApplicationID());
			String updateStatus = userHandler.updateUser(sessionUser.getUsername(), sessionUser);
			System.out.println(updateStatus);
		}
	}

	/**
     * Selects a job application.
     */
	private static void selectApplication() {
		listApplications(appliedJobs);
		System.out.println("----------------Select Application");
		System.out.println("Enter Application Id:");
		int selectedJobId = inp.nextInt();
		JobApplication jobSelected = jobHandler.getJobApplication(selectedJobId);
		System.out.println("Selected Application:\n"+ jobSelected);
		
		System.out.println("What do you wanna do with this application?\n"
				+ "1) Get upcoming schedule\n"
				+ "2) Add a schedule\n"
				+ "3) Add another Document\n"
				+ "4) Change status of the application");
		int userChoice = inp.nextInt();
		while(userChoice <1 || userChoice >4) {
			System.out.println("Invalid choice choose again:");
			userChoice = inp.nextInt();
		}
		switch(userChoice) {
		case 1:
			listSchedules(jobSelected);
			break;
		case 2:
			addScheduleToApplication(jobSelected);
			break;
		case 3:
			addDocToSchedule(jobSelected);
			break;
		case 4:
			changeJobStatus(jobSelected);
			break;
		}
	
	}

	private static void listSchedules(JobApplication jobSelected) {
		List<Integer> scheduleIds = jobSelected.getScheduleIDs();
		MyQueue<Schedule> scheds = new AppPriorityQueue(new ScheduleComparator());
		for(int i=0; i<scheduleIds.size(); i++) {
			Schedule sched = schedHandler.getSchedule(scheduleIds.get(i));
			scheds.enqueue(sched);
		}
		scheds.traverse();
		
	}

	private static void addScheduleToApplication(JobApplication jobSelected) {
		Schedule newSched = SchedulesPage.createScheduleData(sessionUser);
		newSched.setScheduleID(jobSelected.getScheduleIDs().size()+3011);
		String schedInsertStatus = schedHandler.postSchedule(newSched);
		System.out.println(schedInsertStatus);
		
		jobSelected.addSchedule(newSched.getScheduleID());
		String jobUpdateStatus = jobHandler.updateApplication(jobSelected.getApplicationID(), jobSelected);
		System.out.println(jobUpdateStatus);
		
	}

	private static void addDocToSchedule(JobApplication jobSelected) {
		// TODO Auto-generated method stub
		
	}

	private static void changeJobStatus(JobApplication jobSelected) {
		// TODO Auto-generated method stub
		
	}

	/**
     * Lists all job applications.
     */
	private static void listAllApplications() {
		// TODO Auto-generated method stub
		System.out.println("Sort them Base on?\n"
				+ "1) Company Name\n"
				+ "2) Job role\n"
				+ "3) Applied Date");
		int userChoice = inp.nextInt();
		switch(userChoice) {
		case 1:
			AppQuickSort.quickSort(appliedJobs, "companyName");
			break;
		case 2:
			AppQuickSort.quickSort(appliedJobs, "jobTitle");
			break;
		case 3:
			AppQuickSort.quickSort(appliedJobs, "appliedDate");
			break;
		}
		
		if(!appliedJobs.isEmpty()) {
			for(int i =0; i<appliedJobs.getCurrentSize(); i++) {
				System.out.println(appliedJobs.get(i)+"\n");
			}
		}else {
			System.out.println(sessionUser.getFirstName()+" didn't apply to any jobs");
		}
	}
	
	/**
     * Lists job applications based on specified parameters.
     * 
     * @param jobs The job applications to list.
     */
	private static void listApplications(AppBag<JobApplication> jobs) {
		if(!jobs.isEmpty()) {
			for(int i =0; i<jobs.getCurrentSize(); i++) {
				System.out.println(jobs.get(i)+"\n");
			}
		}else {
			System.out.println(sessionUser.getFirstName()+" didn't apply to searched jobs");
		}
	}
	
	/**
     * Sets object data to jobs.
     */
	private static void setObjectDataToJobs() {
		// TODO Auto-generated method stub
		List<Integer> jobIds = sessionUser.getApplicationIds();
		for(int i=0; i<jobIds.size(); i++) {
			JobApplication job = jobHandler.getJobApplication(jobIds.get(i));
			appliedJobs.add(job);
		}
		
		List<Integer> resumeIds = sessionUser.getResumeIDs();
		for(int i=0; i<resumeIds.size();i++) {
			JobDocument doc = docHandler.getDocument(resumeIds.get(i));
			userDocs.add(doc);
		}
		
		List<Integer> coverLetterIds = sessionUser.getCoverLetterIDs();
		for(int i=0; i<coverLetterIds.size(); i++) {
			JobDocument doc = docHandler.getDocument(coverLetterIds.get(i));
			userDocs.add(doc);
		}
	}
	
	// Custom comparator to compare schedules based on their dates
    private static class ScheduleComparator implements Comparator<Schedule> {
        @Override
        public int compare(Schedule s1, Schedule s2) {
            return s1.getScheduleDate().compareTo(s2.getScheduleDate());
        }
    }
}