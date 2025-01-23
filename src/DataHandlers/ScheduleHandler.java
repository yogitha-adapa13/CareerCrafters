package DataHandlers;

import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import Models.Schedule;
import Models.User;

public class ScheduleHandler {
	private MongoDatabase database;
	private MongoCollection<Document> collection;
	
	public ScheduleHandler(MongoDatabase databaseToken) {
		this.database = databaseToken;
		System.out.println("Got Schedules Collection from database");
		this.collection = database.getCollection("schedules");
	}
	
	/**
	 * Get method that retrieves schedule from database
	 * @param ScheduleID in string representing schedule
	 * @return Schedule model object representing the schedule
	 * **/
	public Schedule getSchedule(int scheduleID) {
		Document schedDoc = collection.find(eq("scheduleID", scheduleID)).first();
		if(schedDoc== null) {
			return null;
		}
		Schedule resultSchedule = docToSchedule(schedDoc);
	  	return resultSchedule;
	}
	
	public String deleteSchedule(int id) {
    	String message;
    	try {
    		Document result = collection.findOneAndDelete(eq("scheduleID",id));
    		if(result == null) {
    			message = "No such schedule found to delete";
    		}
    		else {
    			message = "Delete Successfull";
    		}
    	}catch(Exception e){
    		message = e.getMessage();
    	}
    	return message;
	}
	
	/**
	 * Post method that inserts schedule into database
	 * @param Schedule model object representing schedule
	 * @return String representing result of insertion
	 * **/
	public String postSchedule(Schedule schedule) {
		String message = "";
		try {
			if(collection.find(eq("scheduleID", schedule.getScheduleID())).first()!=null) {
				return "A Schedule with same ID already exists";
			}else {
				Document scheduleDocument = scheduleToDoc(schedule);
				collection.insertOne(scheduleDocument);
				return "Schedule Added Successfully";
			}
		}catch(Exception e) {
			return e.getMessage();
		}
	}
	
	/**
	 * Put method that updates schedule in database
	 * @param String representing ScheduleID 
	 * @param Schedule model object representing schedule data
	 * @return String representing result of updating
	 * **/
	public String updateUser(String scheduleID, Schedule schedule) {
		Document schedDoc = scheduleToDoc(schedule);
    	Document filter = new Document("scheduleID", scheduleID);
    	try{
    		collection.findOneAndUpdate(filter, new Document("$set",schedDoc));
    		return "Schedule Update Successfull";
    	}catch(Exception e) {
    		return e.getMessage();
    	}
	}
	
	// Helper funcitons
	
	private static Document scheduleToDoc(Schedule sched) {
		Document schedDoc = new Document()
				.append("scheduleID", sched.getScheduleID())
				.append("scheduleName", sched.getScheduleName())
				.append("scheduleDesc", sched.getScheduleDescription())
				.append("scheduleType", sched.getScheduleType())
				.append("scheduleDate", sched.getScheduleDate());
		return schedDoc;
	}
	
	private static Schedule docToSchedule(Document schedDoc) {
		Schedule sched = new Schedule();
		if(schedDoc!=null) {
			sched.setScheduleID(schedDoc.getInteger("scheduleID"));
			sched.setScheduleName(schedDoc.getString("scheduleName"));
			sched.setScheduleType(schedDoc.getString("scheduleType"));
			sched.setScheduleDescription(schedDoc.getString("scheduleDesc"));
			sched.setScheduleDate(schedDoc.getString("scheduleDate"));
		}
		return sched;
	}
}
