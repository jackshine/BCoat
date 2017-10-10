package com.elastica.beatle.securlets.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.elastica.beatle.ciq.dto.ContentChecks;
import com.elastica.beatle.ciq.dto.Pii;

@JsonIgnoreProperties(ignoreUnknown = true)

public class SalesforceSource {

	@JsonProperty("Object Name")
	private String ObjectName;
	@JsonProperty("facility")
	private String facility;
	@JsonProperty("file_type")
	private String fileType;
	@JsonProperty("file_name")
	private String fileName;
	@JsonProperty("file_extension")
	private String fileExtension;
	@JsonProperty("Object_type")
	private String ObjectType;
	@JsonProperty("severity")
	private String severity;
	@JsonProperty("__source")
	private String Source;
	@JsonProperty("user")
	private String user;
	@JsonProperty("OldValue")
	private Object OldValue;
	@JsonProperty("File_Size")
	private Integer FileSize;
	@JsonProperty("created_timestamp")
	private String createdTimestamp;
	@JsonProperty("Object Id")
	private String ObjectId;
	@JsonProperty("message")
	private String message;
	@JsonProperty("inserted_timestamp")
	private String insertedTimestamp;
	@JsonProperty("user_name")
	private String userName;
	@JsonProperty("NewValue")
	private Object NewValue;
	@JsonProperty("object_url")
	private String objectUrl;
	@JsonProperty("Activity_type")
	private String ActivityType;

	/**
	 * 
	 * @return
	 * The ObjectName
	 */
	@JsonProperty("Object Name")
	public String getObjectName() {
		return ObjectName;
	}

	/**
	 * 
	 * @param ObjectName
	 * The Object Name
	 */
	@JsonProperty("Object Name")
	public void setObjectName(String ObjectName) {
		this.ObjectName = ObjectName;
	}

	/**
	 * 
	 * @return
	 * The facility
	 */
	@JsonProperty("facility")
	public String getFacility() {
		return facility;
	}

	/**
	 * 
	 * @param facility
	 * The facility
	 */
	@JsonProperty("facility")
	public void setFacility(String facility) {
		this.facility = facility;
	}

	/**
	 * 
	 * @return
	 * The fileType
	 */
	@JsonProperty("file_type")
	public String getFileType() {
		return fileType;
	}

	/**
	 * 
	 * @param fileType
	 * The file_type
	 */
	@JsonProperty("file_type")
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 * 
	 * @return
	 * The fileName
	 */
	@JsonProperty("file_name")
	public String getFileName() {
		return fileName;
	}

	/**
	 * 
	 * @param fileName
	 * The file_name
	 */
	@JsonProperty("file_name")
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * 
	 * @return
	 * The fileExtension
	 */
	@JsonProperty("file_extension")
	public String getFileExtension() {
		return fileExtension;
	}

	/**
	 * 
	 * @param fileExtension
	 * The file_extension
	 */
	@JsonProperty("file_extension")
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	/**
	 * 
	 * @return
	 * The ObjectType
	 */
	@JsonProperty("Object_type")
	public String getObjectType() {
		return ObjectType;
	}

	/**
	 * 
	 * @param ObjectType
	 * The Object_type
	 */
	@JsonProperty("Object_type")
	public void setObjectType(String ObjectType) {
		this.ObjectType = ObjectType;
	}

	/**
	 * 
	 * @return
	 * The severity
	 */
	@JsonProperty("severity")
	public String getSeverity() {
		return severity;
	}

	/**
	 * 
	 * @param severity
	 * The severity
	 */
	@JsonProperty("severity")
	public void setSeverity(String severity) {
		this.severity = severity;
	}

	/**
	 * 
	 * @return
	 * The Source
	 */
	@JsonProperty("__source")
	public String getSource() {
		return Source;
	}

	/**
	 * 
	 * @param Source
	 * The __source
	 */
	@JsonProperty("__source")
	public void setSource(String Source) {
		this.Source = Source;
	}

	/**
	 * 
	 * @return
	 * The user
	 */
	@JsonProperty("user")
	public String getUser() {
		return user;
	}

	/**
	 * 
	 * @param user
	 * The user
	 */
	@JsonProperty("user")
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * 
	 * @return
	 * The OldValue
	 */
	@JsonProperty("OldValue")
	public Object getOldValue() {
		return OldValue;
	}

	/**
	 * 
	 * @param OldValue
	 * The OldValue
	 */
	@JsonProperty("OldValue")
	public void setOldValue(Object OldValue) {
		this.OldValue = OldValue;
	}

	/**
	 * 
	 * @return
	 * The FileSize
	 */
	@JsonProperty("File_Size")
	public Integer getFileSize() {
		return FileSize;
	}

	/**
	 * 
	 * @param FileSize
	 * The File_Size
	 */
	@JsonProperty("File_Size")
	public void setFileSize(Integer FileSize) {
		this.FileSize = FileSize;
	}

	/**
	 * 
	 * @return
	 * The createdTimestamp
	 */
	@JsonProperty("created_timestamp")
	public String getCreatedTimestamp() {
		return createdTimestamp;
	}

	/**
	 * 
	 * @param createdTimestamp
	 * The created_timestamp
	 */
	@JsonProperty("created_timestamp")
	public void setCreatedTimestamp(String createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	/**
	 * 
	 * @return
	 * The ObjectId
	 */
	@JsonProperty("Object Id")
	public String getObjectId() {
		return ObjectId;
	}

	/**
	 * 
	 * @param ObjectId
	 * The Object Id
	 */
	@JsonProperty("Object Id")
	public void setObjectId(String ObjectId) {
		this.ObjectId = ObjectId;
	}

	/**
	 * 
	 * @return
	 * The message
	 */
	@JsonProperty("message")
	public String getMessage() {
		return message;
	}

	/**
	 * 
	 * @param message
	 * The message
	 */
	@JsonProperty("message")
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 
	 * @return
	 * The insertedTimestamp
	 */
	@JsonProperty("inserted_timestamp")
	public String getInsertedTimestamp() {
		return insertedTimestamp;
	}

	/**
	 * 
	 * @param insertedTimestamp
	 * The inserted_timestamp
	 */
	@JsonProperty("inserted_timestamp")
	public void setInsertedTimestamp(String insertedTimestamp) {
		this.insertedTimestamp = insertedTimestamp;
	}

	/**
	 * 
	 * @return
	 * The userName
	 */
	@JsonProperty("user_name")
	public String getUserName() {
		return userName;
	}

	/**
	 * 
	 * @param userName
	 * The user_name
	 */
	@JsonProperty("user_name")
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 
	 * @return
	 * The NewValue
	 */
	@JsonProperty("NewValue")
	public Object getNewValue() {
		return NewValue;
	}

	/**
	 * 
	 * @param NewValue
	 * The NewValue
	 */
	@JsonProperty("NewValue")
	public void setNewValue(Object NewValue) {
		this.NewValue = NewValue;
	}

	/**
	 * 
	 * @return
	 * The objectUrl
	 */
	@JsonProperty("object_url")
	public String getObjectUrl() {
		return objectUrl;
	}

	/**
	 * 
	 * @param objectUrl
	 * The object_url
	 */
	@JsonProperty("object_url")
	public void setObjectUrl(String objectUrl) {
		this.objectUrl = objectUrl;
	}

	/**
	 * 
	 * @return
	 * The ActivityType
	 */
	@JsonProperty("Activity_type")
	public String getActivityType() {
		return ActivityType;
	}

	/**
	 * 
	 * @param ActivityType
	 * The Activity_type
	 */
	@JsonProperty("Activity_type")
	public void setActivityType(String ActivityType) {
		this.ActivityType = ActivityType;
	}

}