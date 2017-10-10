package com.elastica.beatle.ciq.dto;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
public class Source {

	@JsonProperty("severity")
	private String severity;
	@JsonProperty("facility")
	private String facility;
	@JsonProperty("_ObjectName")
	private String ObjectName;
	@JsonProperty("Resource_Id")
	private String ResourceId;
	@JsonProperty("Risks")
	private String Risks;
	@JsonProperty("__source")
	private String Source;
	@JsonProperty("content_checks")
	private ContentChecks contentChecks;
	@JsonProperty("user")
	private String user;
	@JsonProperty("created_timestamp")
	private String createdTimestamp;
	@JsonProperty("message")
	private String message;
	@JsonProperty("Activity_type")
	private String ActivityType;
	@JsonProperty("inserted_timestamp")
	private String insertedTimestamp;
	@JsonProperty("name")
	private String name;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
	 * The Risks
	 */
	@JsonProperty("Risks")
	public String getRisks() {
		return Risks;
	}

	/**
	 * 
	 * @param Risks
	 * The Risks
	 */
	@JsonProperty("Risks")
	public void setRisks(String Risks) {
		this.Risks = Risks;
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
	 * The contentChecks
	 */
	@JsonProperty("content_checks")
	public ContentChecks getContentChecks() {
		return contentChecks;
	}

	/**
	 * 
	 * @param contentChecks
	 * The content_checks
	 */
	@JsonProperty("content_checks")
	public void setContentChecks(ContentChecks contentChecks) {
		this.contentChecks = contentChecks;
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

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}