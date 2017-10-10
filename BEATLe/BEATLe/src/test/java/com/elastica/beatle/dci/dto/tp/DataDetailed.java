package com.elastica.beatle.dci.dto.tp;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class DataDetailed{

	@JsonProperty("status")
	private String status;
	@JsonProperty("training_files")
	private TrainingFiles trainingFiles;
	@JsonProperty("modified_by")
	private String modifiedBy;
	@JsonProperty("description")
	private String description;
	@JsonProperty("is_active")
	private Boolean isActive;
	@JsonProperty("created_by")
	private String createdBy;
	@JsonProperty("confidence_level_threshold")
	private Double confidenceLevelThreshold;
	@JsonProperty("created_on")
	private String createdOn;
	@JsonProperty("__audit_done__")
	private Boolean AuditDone;
	@JsonProperty("modified_on")
	private String modifiedOn;
	@JsonProperty("id")
	private String id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("components")
	private String components;
	@JsonProperty("resource_uri")
	private String resourceUri;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The status
	 */
	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	/**
	 * 
	 * @param status
	 * The status
	 */
	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 
	 * @return
	 * The trainingFiles
	 */
	@JsonProperty("training_files")
	public TrainingFiles getTrainingFiles() {
		return trainingFiles;
	}

	/**
	 * 
	 * @param trainingFiles
	 * The training_files
	 */
	@JsonProperty("training_files")
	public void setTrainingFiles(TrainingFiles trainingFiles) {
		this.trainingFiles = trainingFiles;
	}

	/**
	 * 
	 * @return
	 * The modifiedBy
	 */
	@JsonProperty("modified_by")
	public String getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * 
	 * @param modifiedBy
	 * The modified_by
	 */
	@JsonProperty("modified_by")
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * 
	 * @return
	 * The description
	 */
	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 * The description
	 */
	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return
	 * The isActive
	 */
	@JsonProperty("is_active")
	public Boolean getIsActive() {
		return isActive;
	}

	/**
	 * 
	 * @param isActive
	 * The is_active
	 */
	@JsonProperty("is_active")
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * 
	 * @return
	 * The createdBy
	 */
	@JsonProperty("created_by")
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * 
	 * @param createdBy
	 * The created_by
	 */
	@JsonProperty("created_by")
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * 
	 * @return
	 * The confidenceLevelThreshold
	 */
	@JsonProperty("confidence_level_threshold")
	public Double getConfidenceLevelThreshold() {
		return confidenceLevelThreshold;
	}

	/**
	 * 
	 * @param confidenceLevelThreshold
	 * The confidence_level_threshold
	 */
	@JsonProperty("confidence_level_threshold")
	public void setConfidenceLevelThreshold(Double confidenceLevelThreshold) {
		this.confidenceLevelThreshold = confidenceLevelThreshold;
	}

	/**
	 * 
	 * @return
	 * The createdOn
	 */
	@JsonProperty("created_on")
	public String getCreatedOn() {
		return createdOn;
	}

	/**
	 * 
	 * @param createdOn
	 * The created_on
	 */
	@JsonProperty("created_on")
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * 
	 * @return
	 * The AuditDone
	 */
	@JsonProperty("__audit_done__")
	public Boolean getAuditDone() {
		return AuditDone;
	}

	/**
	 * 
	 * @param AuditDone
	 * The __audit_done__
	 */
	@JsonProperty("__audit_done__")
	public void setAuditDone(Boolean AuditDone) {
		this.AuditDone = AuditDone;
	}

	/**
	 * 
	 * @return
	 * The modifiedOn
	 */
	@JsonProperty("modified_on")
	public String getModifiedOn() {
		return modifiedOn;
	}

	/**
	 * 
	 * @param modifiedOn
	 * The modified_on
	 */
	@JsonProperty("modified_on")
	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	/**
	 * 
	 * @return
	 * The id
	 */
	@JsonProperty("id")
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 * The id
	 */
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
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
	 * The status
	 */
	@JsonProperty("components")
	public String getComponents() {
		return components;
	}

	/**
	 * 
	 * @param status
	 * The status
	 */
	@JsonProperty("components")
	public void setComponents(String components) {
		this.components = components;
	}
	
	/**
	 * 
	 * @return
	 * The status
	 */
	@JsonProperty("resource_uri")
	public String getResourceUri() {
		return resourceUri;
	}

	/**
	 * 
	 * @param status
	 * The status
	 */
	@JsonProperty("resource_uri")
	public void setResourceUri(String resourceUri) {
		this.resourceUri = resourceUri;
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