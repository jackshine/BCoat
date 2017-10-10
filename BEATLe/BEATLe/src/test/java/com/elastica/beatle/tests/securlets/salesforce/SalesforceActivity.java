package com.elastica.beatle.tests.securlets.salesforce;

import java.lang.reflect.Field;

import org.testng.Reporter;

public class SalesforceActivity {
	
	private String message;
	private String severity;
	private String objectType;
	private String activityType;
	private String createdTime;
	private String objectUrl;
	
	private String objectId;
	private String objectName;
	private String fileName;
	private String instance;
	private String service = "Salesforce";
	private String username; 
	private String user; //email is the user
	
	private String fileSize;
	private String fileExtension;
	private String fileType;
	
	private String ownedBy;
	private String ownedByName;
	private String userType;
	private String risks;
	
	
	public SalesforceActivity(String message, String createdTime, String severity, String objectType, String activityType, 
								String objectUrl, String objectId, String objectName, String fileName, String instance, String user, String username) {
		this.message = message;
		this.createdTime = createdTime;
		this.severity = severity;
		this.objectType = objectType;
		this.activityType = activityType;
		this.objectUrl = objectUrl;
		this.objectId = objectId;
		this.objectName = objectName;
		this.fileName = fileName;
		this.instance = instance;
		this.user = user;
		this.username = username;
		
	}
	
	public SalesforceActivity(String message, String createdTime, String severity, String objectType, String activityType, 
			String objectUrl, String objectId, String objectName, String fileName, String instance, String user, String username, 
								String fileSize, String fileExtension, String fileType, String ownedBy, String ownedByName, String userType) {
		
			this(message, createdTime, severity, objectType, activityType, objectUrl, objectId, objectName, fileName, instance, user, username);
			
			this.fileSize = fileSize;
			this.fileExtension = fileExtension;
			this.fileType = fileType;
			this.ownedBy 	= ownedBy;
			this.ownedByName = ownedByName;
			this.userType 	= userType;
			

	}
	
	//For ContentInspection
	public SalesforceActivity(String message, String createdTime, String severity, String activityType, 
								String name, String user, String risks, String resourceId) {
			
			this.message = message;
			this.createdTime = createdTime;
			this.severity = severity;
			this.activityType = activityType;
			this.fileName = name;
			this.user = user;
			this.objectId = resourceId;
			this.risks = risks;

}

	/**
	 * @return the fileSize
	 */
	public String getFileSize() {
		return fileSize;
	}

	/**
	 * @param fileSize the fileSize to set
	 */
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * @return the fileExtension
	 */
	public String getFileExtension() {
		return fileExtension;
	}

	/**
	 * @param fileExtension the fileExtension to set
	 */
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	/**
	 * @return the fileType
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * @param fileType the fileType to set
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
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

	/**
	 * @return the objectUrl
	 */
	public String getObjectUrl() {
		return objectUrl;
	}

	/**
	 * @param objectUrl the objectUrl to set
	 */
	public void setObjectUrl(String objectUrl) {
		this.objectUrl = objectUrl;
	}

	/**
	 * @return the objectId
	 */
	public String getObjectId() {
		return objectId;
	}

	/**
	 * @param objectId the objectId to set
	 */
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	/**
	 * @return the objectName
	 */
	public String getObjectName() {
		return objectName;
	}

	/**
	 * @param objectName the objectName to set
	 */
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
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
	 * @return the service
	 */
	public String getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(String service) {
		this.service = service;
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
	 * @return the ownedBy
	 */
	public String getOwnedBy() {
		return ownedBy;
	}

	/**
	 * @param ownedBy the ownedBy to set
	 */
	public void setOwnedBy(String ownedBy) {
		this.ownedBy = ownedBy;
	}

	/**
	 * @return the ownedByName
	 */
	public String getOwnedByName() {
		return ownedByName;
	}

	/**
	 * @param ownedByName the ownedByName to set
	 */
	public void setOwnedByName(String ownedByName) {
		this.ownedByName = ownedByName;
	}

	/**
	 * @return the userType
	 */
	public String getUserType() {
		return userType;
	}

	/**
	 * @param userType the userType to set
	 */
	public void setUserType(String userType) {
		this.userType = userType;
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

	public String getExpectedValues() {
		StringBuffer sb = new StringBuffer();

		Field[] fields = getClass().getDeclaredFields(); //Get all fields incl. private ones

		for (Field field : fields){

		    try {

		        field.setAccessible(true);
		        String key=field.getName();
		        String value;

		        try{
		            value = (String) field.get(this);
		        } catch (ClassCastException e){
		            value=field.get(this).toString();
		        }
		        Reporter.log(key + "::" + value, true);
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

