package com.elastica.beatle.tests.securlets;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.testng.Reporter;

import com.elastica.beatle.securlets.dto.IpInfo;

public class OneDriveBusinessActivity {
	
	private String message;
	private String severity;
	private String objectType;
	private String activityType;
	private String createdTime;
	private String resourceId;
	private String hostname;
	private String name;
	private String parent;
	private String parentId;
	private String documentType;
	private long fileSize;
	private String facility;
	private String user;
	private String username;
	private String risks;
	private String currentlySharedWith;
	private String unsharedWith;
    private String instance;
    private String site;
    private String title;
    private String objectnames;
	
	

	
	public OneDriveBusinessActivity(String message, String createdTime, String severity, String objectType, String activityType, 
			String user, String username, String resourceId, long fileSize, String parent, String facility, String currentlySharedWith, String unsharedWith, String instance) {
		
		this.message = message;
		this.createdTime = createdTime;
		this.severity = severity;
		this.objectType = objectType;
		this.activityType = activityType;
		this.user = user;
		this.username = username;
		this.resourceId = resourceId;
		this.parent = parent;
		this.facility = facility;
		this.fileSize = fileSize;
		this.currentlySharedWith = currentlySharedWith;
		this.unsharedWith = unsharedWith;
		this.instance = instance;
		

	}
	

	public OneDriveBusinessActivity(String message, String createdTime, String severity, String objectType, String activityType, 
			String user, String username, String resourceId, long fileSize, String parent, String facility ) {
		
		this.message = message;
		this.createdTime = createdTime;
		this.severity = severity;
		this.objectType = objectType;
		this.activityType = activityType;
		this.user = user;
		this.username = username;
		this.resourceId = resourceId;
		this.parent = parent;
		this.facility = facility;
		this.fileSize = fileSize;
		

	}
	
	public OneDriveBusinessActivity(String message, String createdTime, String severity, String activityType, 
			String user, String resourceId, String name, String risks, String facility ) {
		
		this.message = message;
		this.createdTime = createdTime;
		this.severity = severity;
		this.activityType = activityType;
		this.user = user;
		this.resourceId = resourceId;
		this.facility = facility;
		this.risks = risks;
		this.name = name;
		

	}
	
	//For site logs
	public OneDriveBusinessActivity(String message, String createdTime, String severity, String objectType, String activityType, 
			String site, String title, String objectnames, String parent, String sharedWith, String instance, String facility) {
		
		this.message = message;
		this.createdTime = createdTime;
		this.severity = severity;
		this.objectType = objectType;
		this.activityType = activityType;
		this.site = site;
		this.title = title;
		this.objectnames = objectnames;
		this.parent = parent;
		this.currentlySharedWith = sharedWith;
		this.instance = instance;
		this.facility = facility;
		

	}
	
	
	public OneDriveBusinessActivity(HashMap<String, String> parameters) {
		this.message = parameters.get("message");
		this.severity = parameters.get("severity");
		this.objectType = parameters.get("objectType");
		this.activityType = parameters.get("activityType");
	}
	
	
	
	/**
	 * @return the site
	 */
	public String getSite() {
		return site;
	}


	/**
	 * @param site the site to set
	 */
	public void setSite(String site) {
		this.site = site;
	}


	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}


	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}


	/**
	 * @return the objectnames
	 */
	public String getObjectnames() {
		return objectnames;
	}


	/**
	 * @param objectnames the objectnames to set
	 */
	public void setObjectnames(String objectnames) {
		this.objectnames = objectnames;
	}
	
	/**
	 * @return the unsharedWith
	 */
	public String getUnsharedWith() {
		return unsharedWith;
	}


	/**
	 * @param unsharedWith the unsharedWith to set
	 */
	public void setUnsharedWith(String unsharedWith) {
		this.unsharedWith = unsharedWith;
	}
	
	/**
	 * @return the currentlySharedWith
	 */
	public String getCurrentlySharedWith() {
		return currentlySharedWith;
	}

	/**
	 * @param currentlySharedWith the currentlySharedWith to set
	 */
	public void setCurrentlySharedWith(String currentlySharedWith) {
		this.currentlySharedWith = currentlySharedWith;
	}

	/**
	 * @return the instance
	 */
	public String getInstance() {
		return instance;
	}

	/**
	 * @param instance the instance to set
	 */
	public void setInstance(String instance) {
		this.instance = instance;
	}
	
	/**
	 * @return the risks
	 */
	public String getRisks() {
		return risks;
	}

	/**
	 * @param risks the risks to set
	 */
	public void setRisks(String risks) {
		this.risks = risks;
	}


	
	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}



	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}



	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}



	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	
	/**
	 * @return the facility
	 */
	public String getFacility() {
		return facility;
	}

	/**
	 * @param facility the facility to set
	 */
	public void setFacility(String facility) {
		this.facility = facility;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the folderName
	 */
	public String getParent() {
		return parent;
	}

	/**
	 * @param folderName the folderName to set
	 */
	public void setParent(String parent) {
		this.parent = parent;
	}
	

	/**
	 * @return the resourceId
	 */
	public String getResourceId() {
		return resourceId;
	}

	/**
	 * @param resourceId the resourceId to set
	 */
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}


	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * @return the severity
	 */
	public String getSeverity() {
		return severity;
	}


	/**
	 * @param severity the severity to set
	 */
	public void setSeverity(String severity) {
		this.severity = severity;
	}


	/**
	 * @return the objectType
	 */
	public String getObjectType() {
		return objectType;
	}


	/**
	 * @param objectType the objectType to set
	 */
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}


	/**
	 * @return the activityType
	 */
	public String getActivityType() {
		return activityType;
	}


	/**
	 * @param activityType the activityType to set
	 */
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}


	/**
	 * @return the createdTime
	 */
	public String getCreatedTime() {
		return createdTime;
	}


	/**
	 * @param createdTime the createdTime to set
	 */
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	/**
	 * @return the parentId
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the documentType
	 */
	public String getDocumentType() {
		return documentType;
	}

	/**
	 * @param documentType the documentType to set
	 */
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	
	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getExpectedValues() {
		StringBuffer sb = new StringBuffer();

		Field[] fields = getClass().getDeclaredFields(); //Get all fields incl. private ones

		for (Field field : fields){

		    try {

		        field.setAccessible(true);
		        String key=field.getName();
		        String value;

		        try{
		            value = String.valueOf(field.get(this));
		        } catch (ClassCastException e){
		            value="";
		        }
		        Reporter.log(key +":"+ value, true);
		        sb.append(key).append(": ").append(value).append(System.getProperty("line.separator"));

		    } catch (IllegalArgumentException e) {
		        e.printStackTrace();
		    } catch (SecurityException e) {
		        e.printStackTrace();
		    } catch (IllegalAccessException e) {
		        e.printStackTrace();
		    }
		}
		return sb.toString(); 
		
	}

	
}
