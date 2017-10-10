package com.elastica.beatle.es;

import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.elastica.beatle.ciq.dto.ContentChecks;
import com.elastica.beatle.ciq.dto.Pii;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Source {

	@JsonProperty("facility")
	private String facility;
	@JsonProperty("version")
	private String version;
	@JsonProperty("File_Size")
	private Long FileSize;
	@JsonProperty("created_timestamp")
	private String createdTimestamp;
	@JsonProperty("previous_path")
	private String previous_path;
	@JsonProperty("current_path")
	private String currentPath;
	@JsonProperty("message")
	private String message;
	@JsonProperty("Activity_type")
	private String ActivityType;
	@JsonProperty("city")
	private String city;
	@JsonProperty("severity")
	private String severity;
	@JsonProperty("Object_type")
	private String ObjectType;
	@JsonProperty("location")
	private String location;
	@JsonProperty("latitude")
	private String latitude;
	@JsonProperty("user_name")
	private String userName;
	@JsonProperty("parent")
	private String parent;
	@JsonProperty("Risks")
	private String Risks;
	@JsonProperty("Parent")
	private String OneDriveParent;
	@JsonProperty("Resource_Id")
	private String ResourceId;
	@JsonProperty("host")
	private String host;
	@JsonProperty("user")
	private String user;
	@JsonProperty("inserted_timestamp")
	private String insertedTimestamp;
	@JsonProperty("name")
	private String name;
	@JsonProperty("_ObjectName")
	private String ObjectName;
	@JsonProperty("country")
	private String country;
	@JsonProperty("longitude")
	private String longitude;
	@JsonProperty("__source")
	private String Source;
	@JsonProperty("Document_type")
	private String DocumentType;
	@JsonProperty("content_checks")
	private ContentChecks contentChecks;

	@JsonProperty("Object")
	private String Object;
	@JsonProperty("Collaborator")
	private String Collaborator;



	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();


	@JsonProperty("Risks")
	public String getRisks() {
		return Risks;
	}
	@JsonProperty("Risks")
	public void setRisks(String risks) {
		Risks = risks;
	}


	/**
	 *
	 * @return
	 * The Object
	 */
	@JsonProperty("Object")
	public String getObject() {
		return Object;
	}

	/**
	 *
	 * @param Object
	 * The Object
	 */
	@JsonProperty("Object")
	public void setObject(String Object) {
		this.Object = Object;
	}

	/**
	 *
	 * @return
	 * The Collaborator
	 */
	@JsonProperty("Collaborator")
	public String getCollaborator() {
		return Collaborator;
	}

	/**
	 *
	 * @param Collaborator
	 * The Collaborator
	 */
	@JsonProperty("Collaborator")
	public void setCollaborator(String Collaborator) {
		this.Collaborator = Collaborator;
	}

	/**
	 * 
	 * @return
	 * The version
	 */
	@JsonProperty("version")
	public String getVersion() {
		return version;
	}

	/**
	 * 
	 * @param version
	 * The version
	 */
	@JsonProperty("version")
	public void setVersion(String version) {
		this.version = version;
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
	 * The FileSize
	 */
	@JsonProperty("File_Size")
	public Long getFileSize() {
		return FileSize;
	}

	/**
	 * 
	 * @param FileSize
	 * The File_Size
	 */
	@JsonProperty("File_Size")
	public void setFileSize(Long FileSize) {
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

	/**
	 * 
	 * @return
	 * The city
	 */
	@JsonProperty("city")
	public String getCity() {
		return city;
	}

	/**
	 * 
	 * @param city
	 * The city
	 */
	@JsonProperty("city")
	public void setCity(String city) {
		this.city = city;
	}


	/**
	 * @return the previous_path
	 */
	@JsonProperty("previous_path")
	public String getPreviousPath() {
		return previous_path;
	}


	/**
	 * @param previous_path the previous_path to set
	 */
	@JsonProperty("previous_path")
	public void setPreviousPath(String previousPath) {
		this.previous_path = previousPath;
	}


	/**
	 * @return the currentPath
	 */
	@JsonProperty("current_path")
	public String getCurrentPath() {
		return currentPath;
	}

	/**
	 * @param currentPath the currentPath to set
	 */
	@JsonProperty("current_path")
	public void setCurrentPath(String currentPath) {
		this.currentPath = currentPath;
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
	 * The location
	 */
	@JsonProperty("location")
	public String getLocation() {
		return location;
	}

	/**
	 * 
	 * @param location
	 * The location
	 */
	@JsonProperty("location")
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * 
	 * @return
	 * The latitude
	 */
	@JsonProperty("latitude")
	public String getLatitude() {
		return latitude;
	}

	/**
	 * 
	 * @param latitude
	 * The latitude
	 */
	@JsonProperty("latitude")
	public void setLatitude(String latitude) {
		this.latitude = latitude;
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
	 * The parent
	 */
	@JsonProperty("parent")
	public String getParent() {
		return parent;
	}

	/**
	 * 
	 * @param parent
	 * The parent
	 */
	@JsonProperty("parent")
	public void setParent(String parent) {
		this.parent = parent;
	}


	@JsonProperty("Parent")
	public String getOneDriveParent() {
		return OneDriveParent;
	}
	@JsonProperty("Parent")
	public void setOneDriveParent(String oneDriveParent) {
		OneDriveParent = oneDriveParent;
	}


	/**
	 * 
	 * @return
	 * The ResourceId
	 */
	@JsonProperty("Resource_Id")
	public String getResourceId() {
		return ResourceId;
	}

	/**
	 * 
	 * @param ResourceId
	 * The Resource_Id
	 */
	@JsonProperty("Resource_Id")
	public void setResourceId(String ResourceId) {
		this.ResourceId = ResourceId;
	}

	/**
	 * 
	 * @return
	 * The host
	 */
	@JsonProperty("host")
	public String getHost() {
		return host;
	}

	/**
	 * 
	 * @param host
	 * The host
	 */
	@JsonProperty("host")
	public void setHost(String host) {
		this.host = host;
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
	 * The name
	 */
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 * The name
	 */
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return
	 * The ObjectName
	 */
	@JsonProperty("_ObjectName")
	public String getObjectName() {
		return ObjectName;
	}

	/**
	 * 
	 * @param ObjectName
	 * The _ObjectName
	 */
	@JsonProperty("_ObjectName")
	public void setObjectName(String ObjectName) {
		this.ObjectName = ObjectName;
	}

	/**
	 * 
	 * @return
	 * The country
	 */
	@JsonProperty("country")
	public String getCountry() {
		return country;
	}

	/**
	 * 
	 * @param country
	 * The country
	 */
	@JsonProperty("country")
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * 
	 * @return
	 * The longitude
	 */
	@JsonProperty("longitude")
	public String getLongitude() {
		return longitude;
	}

	/**
	 * 
	 * @param longitude
	 * The longitude
	 */
	@JsonProperty("longitude")
	public void setLongitude(String longitude) {
		this.longitude = longitude;
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
	 * The DocumentType
	 */
	@JsonProperty("Document_type")
	public String getDocumentType() {
		return DocumentType;
	}

	/**
	 * 
	 * @param DocumentType
	 * The Document_type
	 */
	@JsonProperty("Document_type")
	public void setDocumentType(String DocumentType) {
		this.DocumentType = DocumentType;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	/**
	 * 
	 * @return
	 * The pii
	 */
	@JsonProperty("content_checks")
	public ContentChecks getContentChecks() {
		return contentChecks;
	}

	/**
	 * 
	 * @param pii
	 * The pii
	 */
	@JsonProperty("content_checks")
	public void setContentChecks(ContentChecks contentChecks) {
		this.contentChecks = contentChecks;
	}

}