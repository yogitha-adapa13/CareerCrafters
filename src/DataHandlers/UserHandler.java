package DataHandlers;
import static com.mongodb.client.model.Filters.eq;

import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import Models.User;
public class UserHandler {
	private MongoDatabase database;
	private MongoCollection<Document> collection;
	
	public UserHandler(MongoDatabase databaseToken) {
		this.database = databaseToken;
		System.out.println("Got Users Collection from database");
		this.collection = database.getCollection("users");
	}
	
//  Method to get a user from database, it take userId as parameter, returns a Recipe object
	public User getUser(String userName) {
		Document userDoc = collection.find(eq("userName", userName)).first();
		if(userDoc== null) {
			return null;
		}
	  	User resultUser = docToUser(userDoc);
	  	return resultUser;
	}
	
//  create new user
	public String postUser(User user) {
		String message = "";
		try {
			if(collection.find(eq("userName",user.getUsername())).first()!=null){
				return "A User With Same UserName is already present, please change the userId and try again";
			}
			else {
				Document userDocument = userToDoc(user);
				collection.insertOne(userDocument);
				return "Account Created Successfully";
			}
		}catch(Exception e) {
    		return e.getMessage();
    	}
	}
	
//	Method to check if user exists, takes user name as input and return boolean
	public boolean checkIfUserExists(String userName) {
    	if( collection.find(eq("userName",userName)).first() != null ) {
    		return true;
    	}
    	return false;
	}
	
// 	check password match
	public boolean checkPasswordMatch(String userName, String password) {
		Document userDoc = collection.find(eq("userName",userName)).first();
		User user  = docToUser(userDoc);
		if(user.getPassword().matches(password)) {
			return true;
		}
		return false;
	}
	
//  Method to update User, Takes user name and updated user object and returns message
	public String updateUser(String userName, User user) {
		Document userDoc = userToDoc(user);
    	Document filter = new Document("userName", userName);
    	try{
    		collection.findOneAndUpdate(filter, new Document("$set",userDoc));
    		return "User Update Successfull";
    	}catch(Exception e) {
    		return e.getMessage();
    	}
	}
	
//	Method to delete a user from DB, takes user name as input and return
	public String deleteUser(String userName) {
    	String message;
    	try {
    		Document result = collection.findOneAndDelete(eq("userName",userName));
    		if(result == null) {
    			message = "No such recipe found to delete";
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
	private static Document userToDoc(User user) {
		    Document userDocument = new Document()
		    		.append("userID", user.getUserID())
		            .append("firstName", user.getFirstName())
		            .append("lastName", user.getLastName())
		            .append("email", user.getEmail())
		            .append("phoneNo", user.getPhone())
		            .append("userName", user.getUsername())
		            .append("password", user.getPassword())
		            .append("seqQuestion", user.getSecurityQuestion())
		            .append("seqAnswer", user.getAnswer())
		            .append("JobApplicationIDs", user.getApplicationIds())
		            .append("scheduleIDs", user.getScheduleIDs())
		            .append("resumeIDs", user.getResumeIDs())
		            .append("CoverLetterIDs", user.getCoverLetterIDs());
		
		    return userDocument;
	}

	@SuppressWarnings("unchecked")
		private static User docToUser(Document userDocument) {
		    User user = new User(); // Assuming User is your model class for User
		
		    if (userDocument != null) {
		        user.setFirstName(userDocument.getString("firstName"));
		        user.setLastName(userDocument.getString("lastName"));
		        user.setEmail(userDocument.getString("email"));
		        user.setPhone(userDocument.getString("phoneNo"));
		        user.setUsername(userDocument.getString("userName"));
		        user.setPassword(userDocument.getString("password"));
		        user.setSecurityQuestion(userDocument.getString("seqQuestion"));
		        user.setAnswer(userDocument.getString("seqAnswer"));
		        user.setApplicationIds((List<Integer>) userDocument.get("JobApplicationIDs"));
		        user.setResumeDocIds((List<Integer>) userDocument.get("resumeIDs"));
		        user.setCoverLetterIds((List<Integer>) userDocument.get("CoverLetterIDs"));
		        user.setScheduleIds((List<Integer>) userDocument.get("scheduleIDs"));
		        // invoke function to create object data from list of IDs - Applications, Schedules, Resumes, CoverLetters
		    }
		    return user;
		}
}