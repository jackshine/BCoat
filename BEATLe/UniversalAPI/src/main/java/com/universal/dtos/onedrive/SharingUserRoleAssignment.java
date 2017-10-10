package com.universal.dtos.onedrive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;



public class SharingUserRoleAssignment {

	@JsonProperty("resourceAddress")
	private String resourceAddress;
	@JsonProperty("userRoleAssignments")
	private List<UserRoleAssignment> userRoleAssignments = new ArrayList<UserRoleAssignment>();
	@JsonProperty("validateExistingPermissions")
	private boolean validateExistingPermissions;
	@JsonProperty("additiveMode")
	private boolean additiveMode;
	@JsonProperty("sendServerManagedNotification")
	private boolean sendServerManagedNotification;
	@JsonProperty("customMessage")
	private String customMessage;
	@JsonProperty("includeAnonymousLinksInNotification")
	private boolean includeAnonymousLinksInNotification;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The resourceAddress
	 */
	@JsonProperty("resourceAddress")
	public String getResourceAddress() {
		return resourceAddress;
	}

	/**
	 * 
	 * @param resourceAddress
	 * The resourceAddress
	 */
	@JsonProperty("resourceAddress")
	public void setResourceAddress(String resourceAddress) {
		this.resourceAddress = resourceAddress;
	}

	/**
	 * 
	 * @return
	 * The userRoleAssignments
	 */
	@JsonProperty("userRoleAssignments")
	public List<UserRoleAssignment> getUserRoleAssignments() {
		return userRoleAssignments;
	}

	/**
	 * 
	 * @param userRoleAssignments
	 * The userRoleAssignments
	 */
	@JsonProperty("userRoleAssignments")
	public void setUserRoleAssignments(List<UserRoleAssignment> userRoleAssignments) {
		this.userRoleAssignments = userRoleAssignments;
	}

	/**
	 * 
	 * @return
	 * The validateExistingPermissions
	 */
	@JsonProperty("validateExistingPermissions")
	public boolean isValidateExistingPermissions() {
		return validateExistingPermissions;
	}

	/**
	 * 
	 * @param validateExistingPermissions
	 * The validateExistingPermissions
	 */
	@JsonProperty("validateExistingPermissions")
	public void setValidateExistingPermissions(boolean validateExistingPermissions) {
		this.validateExistingPermissions = validateExistingPermissions;
	}

	/**
	 * 
	 * @return
	 * The additiveMode
	 */
	@JsonProperty("additiveMode")
	public boolean isAdditiveMode() {
		return additiveMode;
	}

	/**
	 * 
	 * @param additiveMode
	 * The additiveMode
	 */
	@JsonProperty("additiveMode")
	public void setAdditiveMode(boolean additiveMode) {
		this.additiveMode = additiveMode;
	}

	/**
	 * 
	 * @return
	 * The sendServerManagedNotification
	 */
	@JsonProperty("sendServerManagedNotification")
	public boolean isSendServerManagedNotification() {
		return sendServerManagedNotification;
	}

	/**
	 * 
	 * @param sendServerManagedNotification
	 * The sendServerManagedNotification
	 */
	@JsonProperty("sendServerManagedNotification")
	public void setSendServerManagedNotification(boolean sendServerManagedNotification) {
		this.sendServerManagedNotification = sendServerManagedNotification;
	}

	/**
	 * 
	 * @return
	 * The customMessage
	 */
	@JsonProperty("customMessage")
	public String getCustomMessage() {
		return customMessage;
	}

	/**
	 * 
	 * @param customMessage
	 * The customMessage
	 */
	@JsonProperty("customMessage")
	public void setCustomMessage(String customMessage) {
		this.customMessage = customMessage;
	}

	/**
	 * 
	 * @return
	 * The includeAnonymousLinksInNotification
	 */
	@JsonProperty("includeAnonymousLinksInNotification")
	public boolean isIncludeAnonymousLinksInNotification() {
		return includeAnonymousLinksInNotification;
	}

	/**
	 * 
	 * @param includeAnonymousLinksInNotification
	 * The includeAnonymousLinksInNotification
	 */
	@JsonProperty("includeAnonymousLinksInNotification")
	public void setIncludeAnonymousLinksInNotification(boolean includeAnonymousLinksInNotification) {
		this.includeAnonymousLinksInNotification = includeAnonymousLinksInNotification;
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
