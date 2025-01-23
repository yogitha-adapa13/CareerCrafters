package DataHandlers;
import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

@SuppressWarnings("unused")
public class DbConnection {
//	URI of the mangodb cluster of our database
	private static String uri;
	private static MongoClient mongoClient;
	
	public DbConnection() {
		uri = "mongodb+srv://eerantia:eerantia@testcluster.pu9tsrc.mongodb.net/?retryWrites=true&w=majority";
	}
	
//	Function to start connection to our database and returns database object, on which we can make our calls
    private static MongoClient startConnection() {
    	if(mongoClient==null) {
    		try {
    			System.out.println("Client instance is getting created");
        		mongoClient = MongoClients.create(uri);
                return mongoClient;
        	}catch(Exception e) {return null;}
    	}else {
    		System.out.println("Client instance is already created");
    		return mongoClient;
    	}
    }
    
//	
    public static MongoDatabase getDatabase() {
    	mongoClient = startConnection();
    	MongoDatabase database = mongoClient.getDatabase("career-craft");
    	System.out.println("Connected to database:\n" + database);
    	return database;
    }
   
//  call this function when user is logged out
    public static void endConnection() {
    	if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
