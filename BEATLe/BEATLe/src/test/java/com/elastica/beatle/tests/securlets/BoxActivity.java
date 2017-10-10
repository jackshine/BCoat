package com.elastica.beatle.tests.securlets;

import java.lang.reflect.Field;
import java.util.HashMap;

import com.elastica.beatle.securlets.dto.IpInfo;

public class BoxActivity {
	
	private String message;
	private String severity;
	private String objectType;
	private String activityType;
	private String createdTime;
	private String latitude;
	private String longitude;
	private String boxUser;
	private String boxUsername;
	private String resourceId;
	private String city;
	private String country;
	private String location;
	private String hostname;
	private String name;
	private String parent;
	private String parentId;
	private String documentType;
	private long fileSize;
	
	
	public BoxActivity(String message, String createdTime, String severity, String objectType, String activityType, 
												String boxUser, String boxUsername, IpInfo ipinfo, String resourceId) {
		this.message = message;
		this.createdTime = createdTime;
		this.severity = severity;
		this.objectType = objectType;
		this.activityType = activityType;
		this.boxUser = boxUser;
		this.boxUsername = boxUsername;
		String latlong[] = ipinfo.getLoc().split(",");
		this.latitude = latlong[0];
		this.longitude = latlong[1];
		this.resourceId = resourceId;
		this.city = ipinfo.getCity().equals("Bengaluru") ? "Bangalore": ipinfo.getCity();
		this.country = ipinfo.getCountry().equals("IN") ?  "India" : "United States";
		this.location = this.city + " (" + this.country + ")" ;
		this.setHostname(ipinfo.getIp());
		
	}
	
	public BoxActivity(String message, String createdTime, String severity, String objectType, String activityType, 
			String boxUser, String boxUsername, IpInfo ipinfo, String resourceId, String name, String folderName) {
		
		this(message, createdTime, severity, objectType, activityType, boxUser, boxUsername, ipinfo, resourceId);
		this.name = name;
		this.parent = folderName;

	}
	
	public BoxActivity(String message, String createdTime, String severity, String objectType, String activityType, 
			String boxUser, String boxUsername, IpInfo ipinfo, String resourceId, String name, String parent, String parentId, String documentType, long fileSize) {
		
		this(message, createdTime, severity, objectType, activityType, boxUser, boxUsername, ipinfo, resourceId, name, parent);
		this.parentId = parentId;
		this.documentType = documentType;
		this.setFileSize(fileSize);

	}
	
	public BoxActivity(HashMap<String, String> parameters) {
		this.message = parameters.get("message");
		this.severity = parameters.get("severity");
		this.objectType = parameters.get("objectType");
		this.activityType = parameters.get("activityType");
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
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	
	/**
	 * @return the lattitude
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * @param lattitude the lattitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the boxUser
	 */
	public String getBoxUser() {
		return boxUser;
	}

	/**
	 * @param boxUser the boxUser to set
	 */
	public void setBoxUser(String boxUser) {
		this.boxUser = boxUser;
	}

	/**
	 * @return the boxUsername
	 */
	public String getBoxUsername() {
		return boxUsername;
	}

	/**
	 * @param boxUsername the boxUsername to set
	 */
	public void setBoxUsername(String boxUsername) {
		this.boxUsername = boxUsername;
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
		            value="";
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

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
}
