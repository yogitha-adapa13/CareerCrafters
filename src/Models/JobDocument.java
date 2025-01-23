package Models;

public class JobDocument {
	private int documentId;
	private String documentName;
	private String bucketId; // Id of the file stored in gridFSBucket;
	private String documentDesc;
	
	public JobDocument() {
		// Gen random DocumentID number;
	}
	public void setDocumentID(int id) {
		this.documentId = id;
	}
	
	public int getDocumentID() {
		return this.documentId;
	}
	
	public String getDocumentName() {
	    return documentName;
	}

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentDesc() {
        return documentDesc;
    }

    public void setDocumentDesc(String fileName) {
        this.documentDesc = fileName;
    }
    
    public String getBucketId() {
    	return this.bucketId;
    }
    
    public void setBucketId(String id) {
    	this.bucketId = id;
    }
    
    @Override
    public String toString() {
        return
               "Document ID:" + documentId +"\n"
               +"Document Name: " + documentName + "\n" +
               "Document Description: " + documentDesc+"\n";
    }
}
