package MyCollections;

import Models.JobApplication;

public interface JobApplicationTree {
	
	
	 /**
     * Inserts a new node containing the specified data into the tree.
     *
     * @param data the data to be inserted into the tree
     */
	public void insert(JobApplication job);
	
	/**
     * Searches for a jobApplication containing the specified data in the tree.
     *
     * @param data the data to search for in the tree
     * @return true if the data is found in the tree, false otherwise
     */
	public AppBag<JobApplication> search(String searchString);
	
	/**
     * Traverses the tree and prints or processes each Job Application.
     */
    public void traverse();
	
}
