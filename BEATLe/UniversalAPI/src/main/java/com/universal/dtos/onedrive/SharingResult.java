package com.universal.dtos.onedrive;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class SharingResult {

	@JsonProperty("AllowedRoles")
	private com.universal.dtos.onedrive.AllowedRoles AllowedRoles;
	@JsonProperty("CurrentRole")
	private long CurrentRole;
	@JsonProperty("DisplayName")
	private String DisplayName;
	@JsonProperty("Email")
	private String Email;
	@JsonProperty("InvitationLink")
	private Object InvitationLink;
	@JsonProperty("IsUserKnown")
	private boolean IsUserKnown;
	@JsonProperty("Message")
	private Object Message;
	@JsonProperty("Status")
	private boolean Status;
	@JsonProperty("User")
	private String User;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The AllowedRoles
	 */
	@JsonProperty("AllowedRoles")
	public com.universal.dtos.onedrive.AllowedRoles getAllowedRoles() {
		return AllowedRoles;
	}

	/**
	 * 
	 * @param AllowedRoles
	 * The AllowedRoles
	 */
	@JsonProperty("AllowedRoles")
	public void setAllowedRoles(com.universal.dtos.onedrive.AllowedRoles AllowedRoles) {
		this.AllowedRoles = AllowedRoles;
	}

	/**
	 * 
	 * @return
	 * The CurrentRole
	 */
	@JsonProperty("CurrentRole")
	public long getCurrentRole() {
		return CurrentRole;
	}

	/**
	 * 
	 * @param CurrentRole
	 * The CurrentRole
	 */
	@JsonProperty("CurrentRole")
	public void setCurrentRole(long CurrentRole) {
		this.CurrentRole = CurrentRole;
	}

	/**
	 * 
	 * @return
	 * The DisplayName
	 */
	@JsonProperty("DisplayName")
	public String getDisplayName() {
		return DisplayName;
	}

	/**
	 * 
	 * @param DisplayName
	 * The DisplayName
	 */
	@JsonProperty("DisplayName")
	public void setDisplayName(String DisplayName) {
		this.DisplayName = DisplayName;
	}

	/**
	 * 
	 * @return
	 * The Email
	 */
	@JsonProperty("Email")
	public String getEmail() {
		return Email;
	}

	/**
	 * 
	 * @param Email
	 * The Email
	 */
	@JsonProperty("Email")
	public void setEmail(String Email) {
		this.Email = Email;
	}

	/**
	 * 
	 * @return
	 * The InvitationLink
	 */
	@JsonProperty("InvitationLink")
	public Object getInvitationLink() {
		return InvitationLink;
	}

	/**
	 * 
	 * @param InvitationLink
	 * The InvitationLink
	 */
	@JsonProperty("InvitationLink")
	public void setInvitationLink(Object InvitationLink) {
		this.InvitationLink = InvitationLink;
	}

	/**
	 * 
	 * @return
	 * The IsUserKnown
	 */
	@JsonProperty("IsUserKnown")
	public boolean isIsUserKnown() {
		return IsUserKnown;
	}

	/**
	 * 
	 * @param IsUserKnown
	 * The IsUserKnown
	 */
	@JsonProperty("IsUserKnown")
	public void setIsUserKnown(boolean IsUserKnown) {
		this.IsUserKnown = IsUserKnown;
	}

	/**
	 * 
	 * @return
	 * The Message
	 */
	@JsonProperty("Message")
	public Object getMessage() {
		return Message;
	}

	/**
	 * 
	 * @param Message
	 * The Message
	 */
	@JsonProperty("Message")
	public void setMessage(Object Message) {
		this.Message = Message;
	}

	/**
	 * 
	 * @return
	 * The Status
	 */
	@JsonProperty("Status")
	public boolean isStatus() {
		return Status;
	}

	/**
	 * 
	 * @param Status
	 * The Status
	 */
	@JsonProperty("Status")
	public void setStatus(boolean Status) {
		this.Status = Status;
	}

	/**
	 * 
	 * @return
	 * The User
	 */
	@JsonProperty("User")
	public String getUser() {
		return User;
	}

	/**
	 * 
	 * @param User
	 * The User
	 */
	@JsonProperty("User")
	public void setUser(String User) {
		this.User = User;
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

