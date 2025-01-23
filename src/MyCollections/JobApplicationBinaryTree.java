package MyCollections;

import Models.JobApplication;

public class JobApplicationBinaryTree implements JobApplicationTree {
    private JobApplicationTreeNode root;

    public JobApplicationBinaryTree() {
        this.root = null;
    }

    @Override
    public void insert(JobApplication job) {
        root = insertRecursive(root, job);
    }

    private JobApplicationTreeNode insertRecursive(JobApplicationTreeNode root, JobApplication job) {
        if (root == null) {
            return new JobApplicationTreeNode(job);
        }
        if (job.getJobTitle().compareTo(root.job.getJobTitle()) < 0) {
            root.left = insertRecursive(root.left, job);
        } else if (job.getJobTitle().compareTo(root.job.getJobTitle()) > 0) {
            root.right = insertRecursive(root.right, job);
        }
        return root;
    }

    @Override
    public AppBag<JobApplication> search(String searchString) {
    	AppBag<JobApplication> results = new AppArray<>();
        searchRecursive(root, searchString.toLowerCase(), results);
        return results;
    }

    private void searchRecursive(JobApplicationTreeNode root, String searchString, AppBag<JobApplication> results) {
        if (root != null) {
            if (root.job.getJobTitle().toLowerCase().contains(searchString) ||
                root.job.getCompanyName().toLowerCase().contains(searchString)) {
                results.add(root.job);
            }
            searchRecursive(root.left, searchString, results);
            searchRecursive(root.right, searchString, results);
        }
    }

    @Override
    public void traverse() {
    }
    // Other methods can be implemented as needed
}
