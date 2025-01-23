package DataHandlers;

import java.io.FileInputStream;
import java.io.IOException;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.mongodb.client.model.Filters;

import java.awt.Desktop;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
public class MongoGridHandler {
	private static MongoDatabase sessionDatabase;
	private static GridFSBucket gridFSBucket;
	public MongoGridHandler(MongoDatabase databaseName) {
        // Connect to MongoDB database
        this.sessionDatabase = databaseName;
        // Create GridFSBucket
        this.gridFSBucket = GridFSBuckets.create(sessionDatabase,"careerBucket");
    }
	
	// Method to upload a file to GridFS
    public ObjectId uploadFile(String filePath, String fileName) {
        FileInputStream fileInputStream = null;
        try {
            // Open FileInputStream for the file
            fileInputStream = new FileInputStream(new File(filePath));

            // Create GridFSUploadOptions (optional)
            GridFSUploadOptions options = new GridFSUploadOptions()
                    .chunkSizeBytes(1024)
                    .metadata(new Document("type", "document"));

            // Upload the file to MongoDB GridFS
            ObjectId fileId = gridFSBucket.uploadFromStream(fileName, fileInputStream, options);

            System.out.println("File uploaded successfully. File ID: " + fileId.toHexString());
            return fileId;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            // Close the FileInputStream in the finally block to ensure it's closed even if an exception occurs
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Method to retrieve a file from GridFS by name
    public void retrieveFile( String fileId, String destinationPath) {
        try {
            // Open download stream for the file
            InputStream inputStream = gridFSBucket.openDownloadStream(new ObjectId(fileId));

            // Create FileOutputStream to write the file to disk
            FileOutputStream fileOutputStream = new FileOutputStream(destinationPath);

            // Read from the input stream and write to the output stream
            byte[] buffer = new byte[1024*1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }

            // Close the streams
            inputStream.close();
            fileOutputStream.close();
            System.out.println("File retrieved successfully: " + destinationPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
 // Method to delete a file from GridFS by its ObjectID
    public void deleteFile(String fileId) {
        try {
            // Convert the fileId String to ObjectId
            ObjectId objectId = new ObjectId(fileId);

            // Find the file metadata in GridFS
            GridFSFile file = gridFSBucket.find(new Document("_id", objectId)).first();

            if (file != null) {
                // Delete the file from GridFS
                gridFSBucket.delete(objectId);
                System.out.println("File deleted successfully: " + fileId);
            } else {
                System.out.println("File not found in GridFS: " + fileId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

 // Method to open a file from GridFS
    public void openFile(String fileId) {
        try {
        	// Create a query to find the file by its ObjectId
            Bson filter = Filters.eq("_id", new ObjectId(fileId));
            
            // Find the file in GridFS
            GridFSFile file = gridFSBucket.find(filter).first();
            if (file != null) {
                // Open download stream for the file
                InputStream inputStream = gridFSBucket.openDownloadStream(file.getObjectId());

                // Create a temporary file path
                Path tempFilePath = Files.createTempFile("gridfs_", file.getFilename());

                // Write the file content to the temporary file
                Files.copy(inputStream, tempFilePath, StandardCopyOption.REPLACE_EXISTING);
                inputStream.close();

                // Open the temporary file using the default application
                Desktop.getDesktop().open(tempFilePath.toFile());
            } else {
                System.out.println("File not found in GridFS: " + fileId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    
}
