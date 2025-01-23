package Models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import MyCollections.AppArray;
import MyCollections.AppBag;

public class Schedule implements Comparable<Schedule>{
	private int scheduleID;
	private String scheduleName;
	private String scheduleType;
	private AppBag<String> schTypes;
	
	private String scheduleDescription;
	private Date scheduleDate;
	
	public Schedule() {
		//Function returns random scheduleID
		schTypes = new AppArray<>();
		schTypes.add("Interview");
		schTypes.add("Follow-up");
		schTypes.add("Email");
	}
	public Schedule(String scheduleName, String scheduleType, Date scheduleDate) {
		this.scheduleName = scheduleName;
		this.scheduleType = scheduleType;
		this.scheduleDate = scheduleDate;
	}
	
	public int getScheduleID() {
		return scheduleID;
	}
	public void setScheduleID(int scheduleID) {
		this.scheduleID = scheduleID;
	}
    
	
	/**
     * Get the schedule name.
     *
     * @return The schedule name.
     */
    public String getScheduleName() {
        return scheduleName;
    }

    /**
     * Set the schedule name.
     *
     * @param scheduleName The schedule name to set.
     */
    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    /**
     * Get the schedule type.
     *
     * @return The schedule type.
     */
    public String getScheduleType() {
        return scheduleType;
    }

    /**
     * Set the schedule type.
     *
     * @param scheduleType The schedule type to set.
     */
    public void setScheduleType(String scheduleType) {
    	this.scheduleType = scheduleType;
    }

    /**
     * Get the schedule description.
     *
     * @return The schedule description.
     */
    public String getScheduleDescription() {
        return scheduleDescription;
    }

    /**
     * Set the schedule description.
     * @param scheduleDescription The schedule description to set.
     */
    public void setScheduleDescription(String scheduleDescription) {
        this.scheduleDescription = scheduleDescription;
    }

    /**
     * Get the schedule date.
     *
     * @return The schedule date.
     */
    public String getScheduleDate() {
        return dateToString(scheduleDate);
    }

    /**
     * Set the schedule date.
     *
     * @param scheduleDate The schedule date to set.
     */
    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = stringToDate(scheduleDate);
    }
    
 // Method to convert String to Date
    private static Date stringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to convert Date to String
    private static String dateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
	
    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(scheduleDate);

        return "Schedule Name: " + scheduleName + "\n" +
               "Schedule Type: " + scheduleType + "\n" +
               "Schedule Description: " + scheduleDescription + "\n" +
               "Schedule Date: " + formattedDate;
    }
    
    @Override
    public int compareTo(Schedule other) {
        return this.scheduleDate.compareTo(other.scheduleDate);
    }
}