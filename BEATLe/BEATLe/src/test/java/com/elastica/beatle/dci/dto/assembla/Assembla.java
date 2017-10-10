package com.elastica.beatle.dci.dto.assembla;

import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;


public class Assembla {

	@JsonProperty("id")
	private Integer id;
	@JsonProperty("number")
	private Integer number;
	@JsonProperty("summary")
	private String summary;
	@JsonProperty("description")
	private String description;
	@JsonProperty("priority")
	private Integer priority;
	@JsonProperty("completed_date")
	private Object completedDate;
	@JsonProperty("component_id")
	private Object componentId;
	@JsonProperty("created_on")
	private String createdOn;
	@JsonProperty("permission_type")
	private Integer permissionType;
	@JsonProperty("importance")
	private Double importance;
	@JsonProperty("is_story")
	private Boolean isStory;
	@JsonProperty("milestone_id")
	private Integer milestoneId;
	@JsonProperty("notification_list")
	private String notificationList;
	@JsonProperty("space_id")
	private String spaceId;
	@JsonProperty("state")
	private Integer state;
	@JsonProperty("status")
	private String status;
	@JsonProperty("story_importance")
	private Integer storyImportance;
	@JsonProperty("updated_at")
	private String updatedAt;
	@JsonProperty("working_hours")
	private Double workingHours;
	@JsonProperty("estimate")
	private Double estimate;
	@JsonProperty("total_estimate")
	private Double totalEstimate;
	@JsonProperty("total_invested_hours")
	private Double totalInvestedHours;
	@JsonProperty("total_working_hours")
	private Double totalWorkingHours;
	@JsonProperty("assigned_to_id")
	private String assignedToId;
	@JsonProperty("reporter_id")
	private String reporterId;
	@JsonProperty("custom_fields")
	private CustomFields customFields;
	@JsonProperty("hierarchy_type")
	private Integer hierarchyType;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The id
	 */
	@JsonProperty("id")
	public Integer getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 * The id
	 */
	@JsonProperty("id")
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 
	 * @return
	 * The number
	 */
	@JsonProperty("number")
	public Integer getNumber() {
		return number;
	}

	/**
	 * 
	 * @param number
	 * The number
	 */
	@JsonProperty("number")
	public void setNumber(Integer number) {
		this.number = number;
	}

	/**
	 * 
	 * @return
	 * The summary
	 */
	@JsonProperty("summary")
	public String getSummary() {
		return summary;
	}

	/**
	 * 
	 * @param summary
	 * The summary
	 */
	@JsonProperty("summary")
	public void setSummary(String summary) {
		this.summary = summary;
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
	 * The priority
	 */
	@JsonProperty("priority")
	public Integer getPriority() {
		return priority;
	}

	/**
	 * 
	 * @param priority
	 * The priority
	 */
	@JsonProperty("priority")
	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	/**
	 * 
	 * @return
	 * The completedDate
	 */
	@JsonProperty("completed_date")
	public Object getCompletedDate() {
		return completedDate;
	}

	/**
	 * 
	 * @param completedDate
	 * The completed_date
	 */
	@JsonProperty("completed_date")
	public void setCompletedDate(Object completedDate) {
		this.completedDate = completedDate;
	}

	/**
	 * 
	 * @return
	 * The componentId
	 */
	@JsonProperty("component_id")
	public Object getComponentId() {
		return componentId;
	}

	/**
	 * 
	 * @param componentId
	 * The component_id
	 */
	@JsonProperty("component_id")
	public void setComponentId(Object componentId) {
		this.componentId = componentId;
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
	 * The permissionType
	 */
	@JsonProperty("permission_type")
	public Integer getPermissionType() {
		return permissionType;
	}

	/**
	 * 
	 * @param permissionType
	 * The permission_type
	 */
	@JsonProperty("permission_type")
	public void setPermissionType(Integer permissionType) {
		this.permissionType = permissionType;
	}

	/**
	 * 
	 * @return
	 * The importance
	 */
	@JsonProperty("importance")
	public Double getImportance() {
		return importance;
	}

	/**
	 * 
	 * @param importance
	 * The importance
	 */
	@JsonProperty("importance")
	public void setImportance(Double importance) {
		this.importance = importance;
	}

	/**
	 * 
	 * @return
	 * The isStory
	 */
	@JsonProperty("is_story")
	public Boolean getIsStory() {
		return isStory;
	}

	/**
	 * 
	 * @param isStory
	 * The is_story
	 */
	@JsonProperty("is_story")
	public void setIsStory(Boolean isStory) {
		this.isStory = isStory;
	}

	/**
	 * 
	 * @return
	 * The milestoneId
	 */
	@JsonProperty("milestone_id")
	public Integer getMilestoneId() {
		return milestoneId;
	}

	/**
	 * 
	 * @param milestoneId
	 * The milestone_id
	 */
	@JsonProperty("milestone_id")
	public void setMilestoneId(Integer milestoneId) {
		this.milestoneId = milestoneId;
	}

	/**
	 * 
	 * @return
	 * The notificationList
	 */
	@JsonProperty("notification_list")
	public String getNotificationList() {
		return notificationList;
	}

	/**
	 * 
	 * @param notificationList
	 * The notification_list
	 */
	@JsonProperty("notification_list")
	public void setNotificationList(String notificationList) {
		this.notificationList = notificationList;
	}

	/**
	 * 
	 * @return
	 * The spaceId
	 */
	@JsonProperty("space_id")
	public String getSpaceId() {
		return spaceId;
	}

	/**
	 * 
	 * @param spaceId
	 * The space_id
	 */
	@JsonProperty("space_id")
	public void setSpaceId(String spaceId) {
		this.spaceId = spaceId;
	}

	/**
	 * 
	 * @return
	 * The state
	 */
	@JsonProperty("state")
	public Integer getState() {
		return state;
	}

	/**
	 * 
	 * @param state
	 * The state
	 */
	@JsonProperty("state")
	public void setState(Integer state) {
		this.state = state;
	}

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
	 * The storyImportance
	 */
	@JsonProperty("story_importance")
	public Integer getStoryImportance() {
		return storyImportance;
	}

	/**
	 * 
	 * @param storyImportance
	 * The story_importance
	 */
	@JsonProperty("story_importance")
	public void setStoryImportance(Integer storyImportance) {
		this.storyImportance = storyImportance;
	}

	/**
	 * 
	 * @return
	 * The updatedAt
	 */
	@JsonProperty("updated_at")
	public String getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * 
	 * @param updatedAt
	 * The updated_at
	 */
	@JsonProperty("updated_at")
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	/**
	 * 
	 * @return
	 * The workingHours
	 */
	@JsonProperty("working_hours")
	public Double getWorkingHours() {
		return workingHours;
	}

	/**
	 * 
	 * @param workingHours
	 * The working_hours
	 */
	@JsonProperty("working_hours")
	public void setWorkingHours(Double workingHours) {
		this.workingHours = workingHours;
	}

	/**
	 * 
	 * @return
	 * The estimate
	 */
	@JsonProperty("estimate")
	public Double getEstimate() {
		return estimate;
	}

	/**
	 * 
	 * @param estimate
	 * The estimate
	 */
	@JsonProperty("estimate")
	public void setEstimate(Double estimate) {
		this.estimate = estimate;
	}

	/**
	 * 
	 * @return
	 * The totalEstimate
	 */
	@JsonProperty("total_estimate")
	public Double getTotalEstimate() {
		return totalEstimate;
	}

	/**
	 * 
	 * @param totalEstimate
	 * The total_estimate
	 */
	@JsonProperty("total_estimate")
	public void setTotalEstimate(Double totalEstimate) {
		this.totalEstimate = totalEstimate;
	}

	/**
	 * 
	 * @return
	 * The totalInvestedHours
	 */
	@JsonProperty("total_invested_hours")
	public Double getTotalInvestedHours() {
		return totalInvestedHours;
	}

	/**
	 * 
	 * @param totalInvestedHours
	 * The total_invested_hours
	 */
	@JsonProperty("total_invested_hours")
	public void setTotalInvestedHours(Double totalInvestedHours) {
		this.totalInvestedHours = totalInvestedHours;
	}

	/**
	 * 
	 * @return
	 * The totalWorkingHours
	 */
	@JsonProperty("total_working_hours")
	public Double getTotalWorkingHours() {
		return totalWorkingHours;
	}

	/**
	 * 
	 * @param totalWorkingHours
	 * The total_working_hours
	 */
	@JsonProperty("total_working_hours")
	public void setTotalWorkingHours(Double totalWorkingHours) {
		this.totalWorkingHours = totalWorkingHours;
	}

	/**
	 * 
	 * @return
	 * The assignedToId
	 */
	@JsonProperty("assigned_to_id")
	public String getAssignedToId() {
		return assignedToId;
	}

	/**
	 * 
	 * @param assignedToId
	 * The assigned_to_id
	 */
	@JsonProperty("assigned_to_id")
	public void setAssignedToId(String assignedToId) {
		this.assignedToId = assignedToId;
	}

	/**
	 * 
	 * @return
	 * The reporterId
	 */
	@JsonProperty("reporter_id")
	public String getReporterId() {
		return reporterId;
	}

	/**
	 * 
	 * @param reporterId
	 * The reporter_id
	 */
	@JsonProperty("reporter_id")
	public void setReporterId(String reporterId) {
		this.reporterId = reporterId;
	}

	/**
	 * 
	 * @return
	 * The customFields
	 */
	@JsonProperty("custom_fields")
	public CustomFields getCustomFields() {
		return customFields;
	}

	/**
	 * 
	 * @param customFields
	 * The custom_fields
	 */
	@JsonProperty("custom_fields")
	public void setCustomFields(CustomFields customFields) {
		this.customFields = customFields;
	}

	/**
	 * 
	 * @return
	 * The hierarchyType
	 */
	@JsonProperty("hierarchy_type")
	public Integer getHierarchyType() {
		return hierarchyType;
	}

	/**
	 * 
	 * @param hierarchyType
	 * The hierarchy_type
	 */
	@JsonProperty("hierarchy_type")
	public void setHierarchyType(Integer hierarchyType) {
		this.hierarchyType = hierarchyType;
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
