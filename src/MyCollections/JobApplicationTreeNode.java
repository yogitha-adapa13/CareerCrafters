package MyCollections;

import Models.JobApplication;

public class JobApplicationTreeNode {
	JobApplication job;
    JobApplicationTreeNode left;
    JobApplicationTreeNode right;

    public JobApplicationTreeNode(JobApplication job) {
        this.job = job;
        this.left = null;
        this.right = null;
    }
}
