package Pages;

import java.util.Comparator;
import java.util.Scanner;

import com.mongodb.client.MongoDatabase;

import DataHandlers.JobApplicationHandler;
import DataHandlers.ScheduleHandler;
import DataHandlers.UserHandler;
import Models.JobApplication;
import Models.Schedule;
import Models.User;
import MyCollections.AppPriorityQueue;
import MyCollections.MyQueue;

public class SchedulesPage {
	private static User sessionUser;
	private static MongoDatabase sessionDb;
	
	private static UserHandler userHandler;
	private static JobApplicationHandler jobHandler;
	private static ScheduleHandler schedHandler;
	static Scanner inp = new Scanner(System.in);
	
	private static MyQueue<Schedule> userSchedules;
	
	public SchedulesPage(User user, MongoDatabase sessionDb) {
		this.sessionDb = sessionDb;
		this.sessionUser = user;
		
		userHandler = new UserHandler(sessionDb);
		jobHandler = new JobApplicationHandler(sessionDb);
		schedHandler = new ScheduleHandler(sessionDb);
		
		userSchedules = new AppPriorityQueue(new ScheduleComparator());
	}
	
	
	public static void start() {
//		System.out.println()
	}
	private static void listSchedules(JobApplication jobSelected) {
		// TODO Auto-generated method stub
		schedHandler = new ScheduleHandler(sessionDb);
		
	}

	private static void addScheduleToApplication(JobApplication jobSelected) {
		// TODO Auto-generated method stub
		
	}

	private static void addDocToSchedule(JobApplication jobSelected) {
		// TODO Auto-generated method stub
		
	}

	private static void changeJobStatus(JobApplication jobSelected) {
		// TODO Auto-generated method stub
		
	}
	
	public static Schedule createScheduleData(User sessionUser) {
		Schedule sched = new Schedule();
		
		sched.setScheduleID(sessionUser.getScheduleIDs().size()+3001);
		System.out.println("Enter Schedule Name");
		sched.setScheduleName(inp.nextLine());
		System.out.println("Enter Schedule Type");
		sched.setScheduleType(inp.nextLine());
		System.out.println("Enter Schedule Date in format(yyyy-mm-dd hh:mm):");
		sched.setScheduleDate(inp.nextLine());
		System.out.println("Enter schdule Description:");
		sched.setScheduleDescription(inp.nextLine());
		
		return sched;
	}
	// Custom comparator to compare schedules based on their dates
    private static class ScheduleComparator implements Comparator<Schedule> {
        @Override
        public int compare(Schedule s1, Schedule s2) {
            return s1.getScheduleDate().compareTo(s2.getScheduleDate());
        }
    }
}
