package Models;

import java.util.ArrayList;
import java.util.List;

import MyCollections.AppArray;
import MyCollections.AppBag;

public class User {
	//Authentication level
	private int userID;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String username;
	private String password;
	private String securityQuestion;
	private String answer;
	//Ids
	private List<Integer> JobApplicationIds;
	private List<Integer> ScheduleIds;
	private List<Integer> ResumesIds;
	private List<Integer> CoverLetterIds;
	
	
	public User() {
		//Invoke a function where it generates a random userID and sets it
		JobApplicationIds = new ArrayList<>();
		ScheduleIds= new ArrayList<>();
		ResumesIds= new ArrayList<>();
		CoverLetterIds= new ArrayList<>();
	}
	
	public User(int userID, String firstName, String lastName, String email, String username, String password, String SecurityQuestion, String answer) {
		this.setUserID(userID);
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.username = username;
		this.password = password;
		this.securityQuestion = SecurityQuestion;
		this.answer = answer;
	}
	
	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	// Getter and setter for firstName
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name.
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // Getter and setter for lastName
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name.
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getFullName() {
    	return this.firstName +" "+ this.lastName;
    }

    // Getter and setter for email
    public String getEmail() {
        return email;
    }
    
    /**
     * Sets the email.
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and setter for username
    public String getUsername() {
        return username;
    }
    
    /**
     * Sets the username.
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter and setter for password
    public String getPassword() {
        return password;
    }
    
    /**
     * Sets the password.
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    
    /**
     * Sets the security question.
     * @param securityQuestion the security question to set
     */
    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }
    
    
    
 // Getter and setter for securityQuestion
    public String getSecurityQuestion() {
        return securityQuestion;
    }
    
    /**
     * Sets the answer.
     * @param answer the answer to set
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    // Getter and setter for answer
    public String getAnswer() {
        return answer;
    }
    
    public void setApplicationIds(List<Integer> Ids) {
    	this.JobApplicationIds = Ids;
    }
    
    public List<Integer> getApplicationIds(){
		return this.JobApplicationIds;
	}	
    
    /**
     * Add a job applicationID to user.
     * @param Job application object representing Job application ID
     */
    public void addJobApplication(int jobApplicationId) {
    	System.out.println("Called me!!");
    	JobApplicationIds.add(jobApplicationId);
    }
    
    /**
     * remove a job applicationID from user.
     * @return returns TRUE if removed, or false if failed.
     */
    public boolean removeJobApplication(Integer jobApplicationId) {
    	return JobApplicationIds.remove(jobApplicationId);
    }
    
    public String getAllUsersApplications() {
    	if(JobApplicationIds.isEmpty()) {
    		return "\n"+this.getFirstName()+" didn't upload any Job Applications";
    	}
    	return JobApplicationIds.toString();
    }
    
    public void setResumeDocIds(List<Integer> Ids) {
    	this.ResumesIds = Ids;
    }
    
    /**
     * Add a resume  to user.
     * @param Document object representing resume document
     */
    public void addResume(int resumeId) {
    	ResumesIds.add(resumeId);
    }
    

	public List<Integer> getResumeIDs(){
		return this.ResumesIds;
	}
    
    /**
     * remove a resume from user.
     * @return returns TRUE if removed, or false if failed.
     */
    public boolean removeResume(Integer resumeId) {
    	return ResumesIds.remove(resumeId);
    }
    
    public String getAllUsersResumes() {
    	if(ResumesIds.isEmpty()) {
    		return "\n"+this.getFirstName()+" didn't upload any resumes";
    	}
    	return ResumesIds.toString();
    }
    
    public void setCoverLetterIds(List<Integer> Ids) {
    	this.CoverLetterIds = Ids;
    }
    
    /**
     * Adds a cover letter to user.
     * @param JobDocument object representing cover letter
     */
    public void addCoverLetter(int coverLetterId) {
    	CoverLetterIds.add(coverLetterId);
    }
    
    public List<Integer> getCoverLetterIDs(){
		return this.CoverLetterIds;
	}
    
    /**
     * remove a cover letter from user.
     * @return returns TRUE if removed, or false if failed.
     */
    public boolean removeCoverLetter(Integer coverLetterId) {
    	return CoverLetterIds.remove(coverLetterId);
    }
    
    public String getAllCoverLetters() {
    	if(CoverLetterIds.isEmpty()) {
    		return "\n"+this.getFirstName()+" didn't upload any cover letters"; 
    	}
    	return CoverLetterIds.toString();
    }
    
    public void setScheduleIds(List<Integer> Ids) {
    	this.ScheduleIds = Ids;
    }
    
    /**
     * Adds a schedules to user.
     * @param JobDocument object representing cover letter
     */
    public void addSchedule(int scheduleId) {
    	ScheduleIds.add(scheduleId);
    }
    
    public List<Integer> getScheduleIDs(){
		return this.ScheduleIds;
	}
    
    /**
     * remove a schedules from user.
     * @return returns TRUE if removed, or false if failed.
     */
    public boolean removeSchedule(Integer scheduleId) {
    	return ScheduleIds.remove(scheduleId);
    }
    
    public String getAllSchedules() {
    	if(ScheduleIds.isEmpty()) {
    		return "\n"+this.getFirstName()+" didn't upload any schedules"; 
    	}
    	return ScheduleIds.toString();
    }

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	 @Override
	    public String toString() {
	        StringBuilder sb = new StringBuilder("\n");
	        sb.append("Full Name: ").append(firstName).append(" ").append(lastName).append("\n");
	        sb.append("Email: ").append(email).append("\n");
	        sb.append("Phone: ").append(phone).append("\n");
	        sb.append("Username: ").append(username).append("\n");
	        sb.append("Job Applications: ").append(JobApplicationIds).append("\n");
	        sb.append("Schedules: ").append(ScheduleIds).append("\n");
	        sb.append("Resumes: ").append(ResumesIds).append("\n");
	        sb.append("Cover Letters: ").append(CoverLetterIds).append("\n");

	        return sb.toString();
	    }
}