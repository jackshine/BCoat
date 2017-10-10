package com.elastica.beatle.tests.gmail;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class GmailActivity {
	
	private String message;
	private String severity;
	private String objectType;
	private String activityType;
	private String createdTime;
	private ArrayList<String> internalRecipients;
	
	private ArrayList<String> externalRecipients;
	private ArrayList<String> inFolder;
	private String name;
	private String sender;
	private String subject;
	private String user;
	private String service;
	private String filesize;
	private String resourceId;
	private String risks;
	
		
	//User(user email), severity, happenedat, message, object type, activity type, external recipient, in folder, internal recipient, 
			// name , sender, subject
	
	public GmailActivity(String message, String createdTime, String severity, String objectType, String activityType, 
					ArrayList<String> internalRecipients, ArrayList<String> externalRecipients, ArrayList<String> inFolder, 
						String sender, String subject, String user) {
		this.message = message;
		this.createdTime = createdTime;
		this.severity = severity;
		this.objectType = objectType;
		this.activityType = activityType;
		this.internalRecipients = internalRecipients;
		this.externalRecipients = externalRecipients;
		this.inFolder = inFolder;
		this.name = subject;// for gmail name is subject
		this.sender = sender;
		this.subject = subject;
		this.user = user;
	}
	
	
	public GmailActivity(String service, String message, String createdTime, String severity, String activityType, 
							String filesize, String name, String user, String risks, String resourceId) {
		this.service = service;
		this.message = message;
		this.createdTime = createdTime;
		this.severity = severity;
		this.activityType = activityType;
		this.filesize = filesize;
		this.name = name;
		this.user = user;
		this.resourceId = resourceId;
		this.risks = risks;
		
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
	 * @return the filesize
	 */
	public String getFilesize() {
		return filesize;
	}


	/**
	 * @param filesize the filesize to set
	 */
	public void setFilesize(String filesize) {
		this.filesize = filesize;
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
	 * @return the internalRecipients
	 */
	public ArrayList<String> getInternalRecipients() {
		return internalRecipients;
	}

	/**
	 * @param internalRecipients the internalRecipients to set
	 */
	public void setInternalRecipients(ArrayList<String> internalRecipients) {
		this.internalRecipients = internalRecipients;
	}

	/**
	 * @return the externalRecipients
	 */
	public ArrayList<String> getExternalRecipients() {
		return externalRecipients;
	}


	/**
	 * @param externalRecipients the externalRecipients to set
	 */
	public void setExternalRecipients(ArrayList<String> externalRecipients) {
		this.externalRecipients = externalRecipients;
	}
	
	/**
	 * @return the inFolder
	 */
	public ArrayList<String> getInFolder() {
		return inFolder;
	}


	/**
	 * @param inFolder the inFolder to set
	 */
	public void setInFolder(ArrayList<String> inFolder) {
		this.inFolder = inFolder;
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
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * @param sender the sender to set
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}


	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
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
