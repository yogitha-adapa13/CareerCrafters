package DataHandlers;

import static com.mongodb.client.model.Filters.eq;

import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import Models.JobApplication;
import Models.JobDocument;

public class DocumentHandler {
	private MongoDatabase database;
	private MongoCollection<Document> collection;
	
	public DocumentHandler(MongoDatabase databaseToken) {
		this.database = databaseToken;
		System.out.println("Got Document Collection from database");
		this.collection = database.getCollection("jobDocuments");
	}
	
	/**
	 * Get method that retrieves documentData from database
	 * @param documentID in string representing documentData
	 * @return Job Document model object representing documentData
	 * **/
	public JobDocument getDocument(int documentID) {
		Document jobDoc    = collection.find(eq("documentId", documentID)).first();
		if(jobDoc== null) {
			return null;
		}
		JobDocument resultDoc = docToJobDoc(jobDoc);
	  	return resultDoc;
	}
	
	/**
	 * Post method that inserts DocumentData into database
	 * @param Job Document model object representing documentData
	 * @return String representing result of insertion
	 * **/
	public String postDocumentData(JobDocument jobDocument) {
		String message = "";
		try {
			if(collection.find(eq("documentId", jobDocument.getDocumentID())).first()!=null) {
				return "A DocumentData with same ID already exists"+jobDocument.getDocumentID();
			}else {
				Document document = jobDocToDoc(jobDocument);
				collection.insertOne(document);
				return "DocumentData Added Successfully";
			}
		}catch(Exception e) {
			return e.getMessage();
		}
	}
	
	/**
	 * Put method that updates document data in database
	 * @param documentID in string representing documentData
	 * @param Job Document model object representing documentData
	 * @return String representing result of updating
	 * **/
	public String updateDocumentData(String documentId, JobDocument jobDocument) {
		Document jobDoc = jobDocToDoc(jobDocument);
    	Document filter = new Document("documentId", documentId);
    	try{
    		collection.findOneAndUpdate(filter, new Document("$set",jobDoc));
    		return "Job Document Update Successfull";
    	}catch(Exception e) {
    		return e.getMessage();
    	}
	}
	
	/**
	 * Delete method that deletes documentDatain database
	 * @param documentID in string representing documentData
	 * @return String representing result of deletion
	 * **/
	public String deleteDocumentData(int documentId) {
    	String message;
    	try {
    		Document result = collection.findOneAndDelete(eq("documentId",documentId));
    		if(result == null) {
    			message = "No such DocumentData found to delete";
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
	private static Document jobDocToDoc(JobDocument jobDocument) {
	    Document document = new Document()
	    		.append("documentId", jobDocument.getDocumentID())
	            .append("documentName", jobDocument.getDocumentName())
	            .append("bucketId", jobDocument.getBucketId())
	            .append("documentDesc", jobDocument.getDocumentDesc());
	
	    return document;
	}

	@SuppressWarnings("unchecked")
	private static JobDocument docToJobDoc(Document document) {
		JobDocument job = new JobDocument(); // Assuming User is your model class for User
	
	    if (document != null) {
	    	job.setDocumentID(document.getInteger("documentId"));
	    	job.setDocumentName(document.getString("documentName"));
	    	job.setDocumentDesc(document.getString("documentDesc"));
	    	job.setBucketId(document.getString("bucketId"));
	        // invoke function to create object data from list of IDs - Applications, Schedules, Resumes, CoverLetters
	    }
	    return job;
	}
}
