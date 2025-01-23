package Pages;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.bson.types.ObjectId;

import com.mongodb.client.MongoDatabase;
import javafx.stage.FileChooser;

import DataHandlers.DocumentHandler;
import DataHandlers.MongoGridHandler;
import DataHandlers.UserHandler;
import Models.JobDocument;
import Models.User;
import MyCollections.AppArray;
import MyCollections.AppBag;

public class YourDocumentsPage {
	private static User sessionUser;
	private static MongoDatabase sessionDb;
	private static AppBag<JobDocument> Resumes;
	private static AppBag<JobDocument> CoverLetters;
	private static JobDocument Resume;
	private static DocumentHandler docHandler;
	private static UserHandler userHandler;
	private static MongoGridHandler gridHandler;
	
	static Scanner inp =  new Scanner(System.in);
	public YourDocumentsPage(User user, MongoDatabase sessionDb) {
		this.sessionUser = user;
		this.sessionDb = sessionDb;
		Resumes = new AppArray<JobDocument>();
		CoverLetters = new AppArray<JobDocument>();
		
		docHandler = new DocumentHandler(sessionDb);
		userHandler = new UserHandler(sessionDb);
		gridHandler = new MongoGridHandler(sessionDb);
		
		System.out.println("------------------------ Documents Page -------------------------");
		System.out.println(user.getFullName()+"'s Documents");
	}
	
	public static void start() {
		listAddTheDocuments();
		System.out.println("\nSelect One:\n"
				+ "1) Add a document\n"
				+ "2) Delete a document\n"
				+ "3) Update a document\n");
		
		int userChoice = inp.nextInt();
		while(userChoice > 3 || userChoice <1) {
			System.out.println("Invalid Choice, try again:\n");
			userChoice = inp.nextInt();
		}
		switch(userChoice) {
			case 1:
				addDocument();
				break;
			case 2:
				deleteDocument();
				break;
			case 3:
				updateDocument();
				break;
		}
	}

	private static void listAddTheDocuments() {
		System.out.println("----Document List:");
		setObjectDataToUser();
		if(Resumes.isEmpty()) {
			System.out.println("User didn't upload any resumes");
		}else {
			System.out.println(Resumes.toString());
		}
		if(CoverLetters.isEmpty()) {
			System.out.println("User didn't upload any cover letters");
		}else {
			System.out.println(CoverLetters.toString());
		}
	}

	private static void addDocument() {
		JobDocument createDoc = new JobDocument();
		System.out.println("Is this a (1) resume or (2)cover letter:");
		int choiceInt = inp.nextInt();
		 
		createDoc.setDocumentID(Resumes.getCurrentSize()+CoverLetters.getCurrentSize()+1001);
		System.out.println(createDoc.getDocumentID());
		System.out.println("Enter Document Name:");
		String docName = inp.nextLine();
		createDoc.setDocumentName(docName);
		
		String url = fileChooser();
		
		ObjectId buckObj = gridHandler.uploadFile(url, createDoc.getDocumentName());
		String bucketId = buckObj.toString();
		System.out.println("Added file to database");
		
		createDoc.setBucketId(bucketId);
		
		System.out.println("Enter Document Description:");
		createDoc.setDocumentDesc(inp.nextLine());
		
		if(choiceInt ==1) {
			sessionUser.addResume(createDoc.getDocumentID());
		}else {
			sessionUser.addCoverLetter(createDoc.getDocumentID());
		}
		
		//Update User Data in database;
		userHandler.updateUser(sessionUser.getUsername(), sessionUser);
		
		String postStatus = docHandler.postDocumentData(createDoc);
		System.out.println(postStatus);
		
	}	
	
	private static String fileChooser() {
		FileChooser fc = new FileChooser();
		fc.setTitle("Select a file");
		FileChooser.ExtensionFilter allDocumentsFilter = new FileChooser.ExtensionFilter(
	            "All Documents (*.docx, *.doc, *.pdf, *.png, *.jpg, *.jpeg, *.gif)",
	            "*.docx", "*.doc", "*.pdf", "*.png", "*.jpg", "*.jpeg", "*.gif"
	        );
		fc.getExtensionFilters().add(allDocumentsFilter);
		File file = fc.showOpenDialog(null);
		String filePath = file.getAbsolutePath();
		return filePath;
	}
	
	public static void setObjectDataToUser() {
		List<Integer> resumeIds = sessionUser.getResumeIDs();
		for(int i=0; i<resumeIds.size();i++) {
			JobDocument doc = docHandler.getDocument(resumeIds.get(i));
			Resumes.add(doc);
		}
		
		List<Integer> coverLetterIds = sessionUser.getCoverLetterIDs();
		for(int i=0; i<coverLetterIds.size(); i++) {
			JobDocument doc = docHandler.getDocument(coverLetterIds.get(i));
			CoverLetters.add(doc);
		}
	}

	private static void deleteDocument() {
        System.out.println("---------------------------------Delete Document");
        
        System.out.println("Do you want to delete a 1)Resume or 2) CoverLetter");
        int resOrCV = inp.nextInt();
        Object[] objDocs;
        if(resOrCV==1) {
        	 objDocs = Resumes.toArray();
        }else {
        	 objDocs = CoverLetters.toArray();
        }
        // Assuming Resumes is an AppArray<JobDocument> object
       
        if (objDocs.length == 0) {
            System.out.println("No documents to delete.");
            return;
        }

        // Convert Object[] to JobDocument[]
        JobDocument[] jobDocs = new JobDocument[objDocs.length];
        for (int i = 0; i < objDocs.length; i++) {
            jobDocs[i] = (JobDocument) objDocs[i];
        }

        // Display the list of documents for selection
        for (int i = 0; i < jobDocs.length; i++) {
            System.out.println((i + 1) + ") " + jobDocs[i]);
        }

        System.out.println("\nSelect Document to delete (Enter the corresponding number):");
        int userChoice = inp.nextInt();

        // Validate user input
        while (userChoice < 1 || userChoice > jobDocs.length) {
            System.out.println("Invalid document, please try again:");
            userChoice = inp.nextInt();
        }

        // Get the selected document ID

        // Attempt to delete the selected document
        //Delete document details from document table
        String deletionStatus = docHandler.deleteDocumentData(jobDocs[userChoice - 1].getDocumentID());
        System.out.println(deletionStatus);
        //Delete file form mongoGrid
        gridHandler.deleteFile(jobDocs[userChoice - 1].getBucketId());
        //Delete doc reference details from user data
        if(resOrCV == 1) {
            sessionUser.removeResume(jobDocs[userChoice - 1].getDocumentID());
        }else {
        	sessionUser.removeCoverLetter(jobDocs[userChoice - 1].getDocumentID());
        }
        String updationStatus = userHandler.updateUser(sessionUser.getUsername(), sessionUser);
        System.out.println(updationStatus);
        
        // Print deletion status
        
    }

	private static void updateDocument() {
		// TODO Auto-generated method stub
		
	}
}
