package com.universal.dtos.onedrive;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class UserResult {

	@JsonProperty("__metadata")
	private com.universal.dtos.onedrive.Metadata Metadata;
	@JsonProperty("Groups")
	private com.universal.dtos.onedrive.Groups Groups;
	@JsonProperty("Id")
	private long Id;
	@JsonProperty("IsHiddenInUI")
	private boolean IsHiddenInUI;
	@JsonProperty("LoginName")
	private String LoginName;
	@JsonProperty("Title")
	private String Title;
	@JsonProperty("PrincipalType")
	private long PrincipalType;
	@JsonProperty("Email")
	private String Email;
	@JsonProperty("IsShareByEmailGuestUser")
	private boolean IsShareByEmailGuestUser;
	@JsonProperty("IsSiteAdmin")
	private boolean IsSiteAdmin;
	@JsonProperty("UserId")
	private Object UserId;
	@JsonProperty("Name")
	private String Name;
	
	
	/**
	 * @return the name
	 */
	public String getName() {
		return Name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		Name = name;
	}

	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The Metadata
	 */
	@JsonProperty("__metadata")
	public com.universal.dtos.onedrive.Metadata getMetadata() {
		return Metadata;
	}

	/**
	 * 
	 * @param Metadata
	 * The __metadata
	 */
	@JsonProperty("__metadata")
	public void setMetadata(com.universal.dtos.onedrive.Metadata Metadata) {
		this.Metadata = Metadata;
	}

	/**
	 * 
	 * @return
	 * The Groups
	 */
	@JsonProperty("Groups")
	public com.universal.dtos.onedrive.Groups getGroups() {
		return Groups;
	}

	/**
	 * 
	 * @param Groups
	 * The Groups
	 */
	@JsonProperty("Groups")
	public void setGroups(com.universal.dtos.onedrive.Groups Groups) {
		this.Groups = Groups;
	}

	/**
	 * 
	 * @return
	 * The Id
	 */
	@JsonProperty("Id")
	public long getId() {
		return Id;
	}

	/**
	 * 
	 * @param Id
	 * The Id
	 */
	@JsonProperty("Id")
	public void setId(long Id) {
		this.Id = Id;
	}

	/**
	 * 
	 * @return
	 * The IsHiddenInUI
	 */
	@JsonProperty("IsHiddenInUI")
	public boolean isIsHiddenInUI() {
		return IsHiddenInUI;
	}

	/**
	 * 
	 * @param IsHiddenInUI
	 * The IsHiddenInUI
	 */
	@JsonProperty("IsHiddenInUI")
	public void setIsHiddenInUI(boolean IsHiddenInUI) {
		this.IsHiddenInUI = IsHiddenInUI;
	}

	/**
	 * 
	 * @return
	 * The LoginName
	 */
	@JsonProperty("LoginName")
	public String getLoginName() {
		return LoginName;
	}

	/**
	 * 
	 * @param LoginName
	 * The LoginName
	 */
	@JsonProperty("LoginName")
	public void setLoginName(String LoginName) {
		this.LoginName = LoginName;
	}

	/**
	 * 
	 * @return
	 * The Title
	 */
	@JsonProperty("Title")
	public String getTitle() {
		return Title;
	}

	/**
	 * 
	 * @param Title
	 * The Title
	 */
	@JsonProperty("Title")
	public void setTitle(String Title) {
		this.Title = Title;
	}

	/**
	 * 
	 * @return
	 * The PrincipalType
	 */
	@JsonProperty("PrincipalType")
	public long getPrincipalType() {
		return PrincipalType;
	}

	/**
	 * 
	 * @param PrincipalType
	 * The PrincipalType
	 */
	@JsonProperty("PrincipalType")
	public void setPrincipalType(long PrincipalType) {
		this.PrincipalType = PrincipalType;
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
	 * The IsShareByEmailGuestUser
	 */
	@JsonProperty("IsShareByEmailGuestUser")
	public boolean isIsShareByEmailGuestUser() {
		return IsShareByEmailGuestUser;
	}

	/**
	 * 
	 * @param IsShareByEmailGuestUser
	 * The IsShareByEmailGuestUser
	 */
	@JsonProperty("IsShareByEmailGuestUser")
	public void setIsShareByEmailGuestUser(boolean IsShareByEmailGuestUser) {
		this.IsShareByEmailGuestUser = IsShareByEmailGuestUser;
	}

	/**
	 * 
	 * @return
	 * The IsSiteAdmin
	 */
	@JsonProperty("IsSiteAdmin")
	public boolean isIsSiteAdmin() {
		return IsSiteAdmin;
	}

	/**
	 * 
	 * @param IsSiteAdmin
	 * The IsSiteAdmin
	 */
	@JsonProperty("IsSiteAdmin")
	public void setIsSiteAdmin(boolean IsSiteAdmin) {
		this.IsSiteAdmin = IsSiteAdmin;
	}

	/**
	 * 
	 * @return
	 * The UserId
	 */
	@JsonProperty("UserId")
	public Object getUserId() {
		return UserId;
	}

	/**
	 * 
	 * @param UserId
	 * The UserId
	 */
	@JsonProperty("UserId")
	public void setUserId(Object UserId) {
		this.UserId = UserId;
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
