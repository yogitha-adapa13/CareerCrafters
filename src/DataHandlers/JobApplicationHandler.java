package DataHandlers;

import static com.mongodb.client.model.Filters.eq;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import Models.JobApplication;
import Models.Schedule;
import Models.User;

public class JobApplicationHandler {
	private MongoDatabase database;
	private MongoCollection<Document> collection;
	
	public JobApplicationHandler(MongoDatabase databaseToken) {
		this.database = databaseToken;
		this.collection = database.getCollection("jobApplications");
	}
	
	/**
	 * Get method that retrieves job application from database
	 * @param job application ID in string representing job applicaiton
	 * @return job Application model object representing the job applicaiton
	 * **/
	public JobApplication getJobApplication(int jobId) {
		Document jobDoc = collection.find(eq("jobApplicationID", jobId)).first();
		if(jobDoc== null) {
			return null;
		}
		JobApplication resultJob = docToJob(jobDoc);
	  	return resultJob;
	}
	
	/**
	 * Post method that inserts job application into database
	 * @param Job Application model object representing job applicaiton
	 * @return String representing result of insertion
	 * **/
	public String postJobApplication(JobApplication job) {
		String message = "";
		try {
			if(collection.find(eq("jobApplicationID", job.getApplicationID())).first()!=null) {
				return "A Job Application with same ID already exists";
			}else {
				Document jobDocument = jobToDoc(job);
				collection.insertOne(jobDocument);
				return "Job Application Added Successfully";
			}
		}catch(Exception e) {
			return e.getMessage();
		}
	}
	
	/**
	 * Put method that updates job application in database
	 * @param job application ID in string representing job applicaiton
	 * @param job Application model object representing job applicaiton
	 * @return String representing result of updating
	 * **/
	public String updateApplication(int jobId, JobApplication job) {
		Document jobDoc = jobToDoc(job);
    	Document filter = new Document("jobApplicationID", jobId);
    	try{
    		collection.findOneAndUpdate(filter, new Document("$set",jobDoc));
    		return "Job application Update Successfull";
    	}catch(Exception e) {
    		return e.getMessage();
    	}
	}
	
	/**
	 * Delete method that deletes job application in database
	 * @param job application ID in string representing job applicaiton
	 * @return String representing result of updating
	 * **/
	public String deleteJobApplication(int jobId) {
    	String message;
    	try {
    		Document result = collection.findOneAndDelete(eq("jobApplicationID",jobId));
    		if(result == null) {
    			message = "No such Job Application found to delete";
    		}
    		else {
    			message = "Delete Successfull";
    		}
    	}catch(Exception e){
    		message = e.getMessage();
    	}
    	return message;
	}
	//Helper functions
		private static Document jobToDoc(JobApplication job) {
			    Document jobDocument = new Document()
			    		.append("jobApplicationID", job.getApplicationID())
			            .append("jobTitle", job.getJobTitle())
			            .append("companyName", job.getCompanyName())
			            .append("appliedDate", job.getAppliedDate())
			            .append("applicationStatus", job.getStatus())
			            .append("scheduleIds", job.getScheduleIDs())
			            .append("attachedDocs", job.getAttachedDocsIDs());
			
			    return jobDocument;
		}

		@SuppressWarnings("unchecked")
			private static JobApplication docToJob(Document jobDocument) {
			JobApplication job = new JobApplication(); // Assuming User is your model class for User
			
			    if (jobDocument != null) {
			    	job.setApplicationID(jobDocument.getInteger("jobApplicationID"));
			    	job.setCompanyName(jobDocument.getString("companyName"));
			    	job.setJobTitle(jobDocument.getString("jobTitle"));
			    	job.setAppliedDate(jobDocument.getString("appliedDate"));
			    	job.setApplicationStatus(jobDocument.getString("applicationStatus"));
			    	job.setScheduleIds((List<Integer>)jobDocument.get("scheduleIds"));
			    	job.setAttachedDocsIds((List<Integer>) jobDocument.get("attachedDocs"));
			        // invoke function to create object data from list of IDs - Applications, Schedules, Resumes, CoverLetters
			    }
			    return job;
			}
		
		
}
